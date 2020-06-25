package org.waveprotocol.box.server.persistence.mongodb;

import com.google.inject.Inject;
import com.google.protobuf.InvalidProtocolBufferException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import org.waveprotocol.box.server.persistence.PersistenceException;
import org.waveprotocol.box.server.persistence.SignerInfoStore;
import org.waveprotocol.wave.crypto.SignatureException;
import org.waveprotocol.wave.crypto.SignerInfo;
import org.waveprotocol.wave.federation.Proto.ProtocolSignerInfo;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MongoDbSignerInfoStore implements SignerInfoStore {

  private final DB database;
  
  private static final Logger LOG = Logger.getLogger(MongoDbSignerInfoStore.class.getName());
  
  @Inject
  public MongoDbSignerInfoStore(DB database) {
    this.database = database;
  }

  @Override
  public void initializeSignerInfoStore() throws PersistenceException {
    // Nothing to initialize
  }

  @Override
  public SignerInfo getSignerInfo(byte[] signerId) {
    DBObject query = getDBObjectForSignerId(signerId);
    DBCollection signerInfoCollection = getSignerInfoCollection();
    DBObject signerInfoDBObject = signerInfoCollection.findOne(query);

    // Sub-class contract specifies return null when not found
    SignerInfo signerInfo = null;

    if (signerInfoDBObject != null) {
      byte[] protobuff = (byte[]) signerInfoDBObject.get("protoBuff");
      try {
        signerInfo = new SignerInfo(ProtocolSignerInfo.parseFrom(protobuff));
      } catch (InvalidProtocolBufferException e) {
        LOG.log(Level.SEVERE, "Couldn't parse the protobuff stored in MongoDB: " + protobuff, e);
      } catch (SignatureException e) {
        LOG.log(Level.SEVERE, "Couldn't parse the certificate chain or domain properly", e);
      }
    }
    return signerInfo;
  }

  @Override
  public void putSignerInfo(ProtocolSignerInfo protocolSignerInfo) throws SignatureException {
    SignerInfo signerInfo = new SignerInfo(protocolSignerInfo);
    byte[] signerId = signerInfo.getSignerId();

    // Not using a modifier here because rebuilding the object is not a lot of
    // work. Doing implicit upsert by using save with a DBOBject that has an _id
    // set.
    DBObject signerInfoDBObject = getDBObjectForSignerId(signerId);
    signerInfoDBObject.put("protoBuff", protocolSignerInfo.toByteArray());
    getSignerInfoCollection().save(signerInfoDBObject);
  }

  /**
   * Returns an instance of {@link DBCollection} for storing SignerInfo.
   */
  private DBCollection getSignerInfoCollection() {
    return database.getCollection("signerInfo");
  }

  /**
   * Returns a {@link DBObject} which contains the key-value pair used to
   * signify the signerId.
   * 
   * @param signerId the signerId value to set.
   * @return a new {@link DBObject} with the (_id,signerId) entry.
   */
  private DBObject getDBObjectForSignerId(byte[] signerId) {
    DBObject query = new BasicDBObject();
    query.put("_id", signerId);
    return query;
  }
}
