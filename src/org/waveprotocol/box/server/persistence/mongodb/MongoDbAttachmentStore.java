package org.waveprotocol.box.server.persistence.mongodb;

import com.google.inject.Inject;

import com.mongodb.DB;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import org.waveprotocol.box.attachment.AttachmentMetadata;
import org.waveprotocol.box.server.persistence.AttachmentStore;
import org.waveprotocol.wave.media.model.AttachmentId;

import java.io.IOException;
import java.io.InputStream;

public class MongoDbAttachmentStore implements AttachmentStore {

  private final DB database;

  @Inject
  public MongoDbAttachmentStore(DB database) {
    this.database = database;
  }

  private GridFS attachmentGrid;

  private GridFS getAttachmentGrid() {
    if (attachmentGrid == null) {
      attachmentGrid = new GridFS(database, "attachments");
    }

    return attachmentGrid;
  }
  
    @Override
    public AttachmentData getAttachment(AttachmentId attachmentId) {

      final GridFSDBFile attachment = getAttachmentGrid().findOne(attachmentId.serialise());

      if (attachment == null) {
        return null;
      } else {
        return new AttachmentData() {

          @Override
          public InputStream getInputStream() throws IOException {
            return attachment.getInputStream();
          }

          @Override
          public long getSize() {
            return attachment.getLength();
          }
        };
      }
    }

    @Override
    public void storeAttachment(AttachmentId attachmentId, InputStream data)
        throws IOException {
      GridFSInputFile file = getAttachmentGrid().createFile(data, attachmentId.serialise());

      try {
        if (getAttachmentGrid().findOne(attachmentId.serialise()) != null)
          throw new IOException("A file by the name '" + attachmentId.serialise() + "' already exists in the system!");
        file.save();
      } catch (MongoException e) {
        // Unfortunately, file.save() wraps any IOException thrown in a
        // 'MongoException'. Since the interface explicitly throws IOExceptions,
        // we unwrap any IOExceptions thrown.
        Throwable innerException = e.getCause();
        if (innerException instanceof IOException) {
          throw (IOException) innerException;
        } else {
          throw e;
        }
      }
    }
    
  @Override
  public void deleteAttachment(AttachmentId attachmentId) {
    getAttachmentGrid().remove(attachmentId.serialise());
  }


  @Override
  public AttachmentMetadata getMetadata(AttachmentId attachmentId) throws IOException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public AttachmentData getThumbnail(AttachmentId attachmentId) throws IOException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void storeMetadata(AttachmentId attachmentId, AttachmentMetadata metaData)
      throws IOException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void storeThumbnail(AttachmentId attachmentId, InputStream dataData) throws IOException {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
