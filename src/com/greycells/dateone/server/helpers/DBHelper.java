package com.greycells.dateone.server.helpers;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Singleton;

import org.apache.commons.fileupload.FileItem;
import org.bson.Document;

import com.google.inject.Inject;
import com.greycells.dateone.server.Consts;
import com.greycells.dateone.server.DocumentUserConverter;
import com.greycells.dateone.shared.User;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

@Singleton
public class DBHelper {
	private MongoClient mongo;
	private static GridFS gridfs;
	private UserHelper userHelper;
	private MongoDatabase database;
	private DocumentUserConverter converter;

	@Inject
	private DBHelper(UserHelper userHelper, DocumentUserConverter converter) {
		this.userHelper = userHelper;
		this.converter = converter;
		try {
			
			mongo = new MongoClient(new ServerAddress(Consts.DatabaseHost, Consts.DatabasePort));

			database = mongo.getDatabase(Consts.DatabaseName);
						
			DB db = new Mongo().getDB(Consts.DatabaseName);
			
			gridfs = new GridFS(db);

		} catch (Exception ex) {
			ex.printStackTrace(System.out);
		}
		System.out.println("datastore created");
	}

	public MongoDatabase getMongoDatabase() {
		return database;
	}

	public GridFS getGridFS() {
		return gridfs;
	}
	
	public void persist(User user){
		Document userDoc = converter.ToDocument(user);
		database.getCollection(Consts.UsersCollectionName).insertOne(userDoc);
	}
	
	public String saveProfilePicture(InputStream in, User user)
			throws IOException {
		// saves the file to "fs" GridFS bucket
		String filename = userHelper.createProfilePicFilename(user);
		GridFSInputFile gfs = gridfs.createFile(in, filename);
		
		gfs.setContentType("image/jpeg");
		
		gfs.save();
		return filename;
	}

	public String saveProfilePicture(FileItem item, User user)
			throws IOException {
		// saves the file to "fs" GridFS bucket
		String filename = userHelper.createProfilePicFilename(user);
		GridFSInputFile gfs = gridfs.createFile(item.getInputStream(), filename);
		
		gfs.setContentType(item.getContentType());
		
		gfs.save();
		return filename;
	}
}
