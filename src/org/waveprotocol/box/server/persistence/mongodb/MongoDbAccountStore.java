package org.waveprotocol.box.server.persistence.mongodb;

import com.google.inject.Inject;
import com.google.wave.api.Context;
import com.google.wave.api.ProtocolVersion;
import com.google.wave.api.event.EventType;
import com.google.wave.api.robot.Capability;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import org.bson.types.BasicBSONList;
import org.waveprotocol.box.server.account.AccountData;
import org.waveprotocol.box.server.account.HumanAccountData;
import org.waveprotocol.box.server.account.HumanAccountDataImpl;
import org.waveprotocol.box.server.account.RobotAccountData;
import org.waveprotocol.box.server.account.RobotAccountDataImpl;
import org.waveprotocol.box.server.authentication.PasswordDigest;
import org.waveprotocol.box.server.persistence.AccountStore;
import org.waveprotocol.box.server.persistence.PersistenceException;
import org.waveprotocol.box.server.robots.RobotCapabilities;
import org.waveprotocol.wave.model.util.CollectionUtils;
import org.waveprotocol.wave.model.wave.ParticipantId;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MongoDbAccountStore implements AccountStore {

  private static final String ACCOUNT_COLLECTION = "account";
  private static final String ACCOUNT_HUMAN_DATA_FIELD = "human";
  private static final String ACCOUNT_ROBOT_DATA_FIELD = "robot";

  private static final String HUMAN_PASSWORD_FIELD = "passwordDigest";

  private static final String PASSWORD_DIGEST_FIELD = "digest";
  private static final String PASSWORD_SALT_FIELD = "salt";

  private static final String ROBOT_URL_FIELD = "url";
  private static final String ROBOT_SECRET_FIELD = "secret";
  private static final String ROBOT_CAPABILITIES_FIELD = "capabilities";
  private static final String ROBOT_VERIFIED_FIELD = "verified";
  
  private static final String CAPABILITIES_VERSION_FIELD = "version";
  private static final String CAPABILITIES_HASH_FIELD = "capabilitiesHash";
  private static final String CAPABILITIES_CAPABILITIES_FIELD = "capabilities";
  private static final String CAPABILITY_CONTEXTS_FIELD = "contexts";
  private static final String CAPABILITY_FILTER_FIELD = "filter";
  
  private final DB database;

  @Inject
  public MongoDbAccountStore(DB database) {
    this.database = database;
  }
  
  @Override
  public void initializeAccountStore() throws PersistenceException {
    // TODO: Sanity checks not handled by MongoDBProvider???
  }

  @Override
  public AccountData getAccount(ParticipantId id) {
    DBObject query = getDBObjectForParticipant(id);
    DBObject result = getAccountCollection().findOne(query);

    if (result == null) {
      return null;
    }

    DBObject human = (DBObject) result.get(ACCOUNT_HUMAN_DATA_FIELD);
    if (human != null) {
      return objectToHuman(id, human);
    }

    DBObject robot = (DBObject) result.get(ACCOUNT_ROBOT_DATA_FIELD);
    if (robot != null) {
      return objectToRobot(id, robot);
    }

    throw new IllegalStateException("DB object contains neither a human nor a robot");
  }

  @Override
  public void putAccount(AccountData account) {
    DBObject object = getDBObjectForParticipant(account.getId());

    if (account.isHuman()) {
      object.put(ACCOUNT_HUMAN_DATA_FIELD, humanToObject(account.asHuman()));
    } else if (account.isRobot()) {
      object.put(ACCOUNT_ROBOT_DATA_FIELD, robotToObject(account.asRobot()));
    } else {
      throw new IllegalStateException("Account is neither a human nor a robot");
    }

    getAccountCollection().save(object);
  }

  @Override
  public void removeAccount(ParticipantId id) {
    DBObject object = getDBObjectForParticipant(id);
    getAccountCollection().remove(object);
  }

  private DBObject getDBObjectForParticipant(ParticipantId id) {
    DBObject query = new BasicDBObject();
    query.put("_id", id.getAddress());
    return query;
  }

  private DBCollection getAccountCollection() {
    return database.getCollection(ACCOUNT_COLLECTION);
  }
  
  // ****** HumanAccountData serialization

  private DBObject humanToObject(HumanAccountData account) {
    DBObject object = new BasicDBObject();

    PasswordDigest digest = account.getPasswordDigest();
    if (digest != null) {
      DBObject digestObj = new BasicDBObject();
      digestObj.put(PASSWORD_SALT_FIELD, digest.getSalt());
      digestObj.put(PASSWORD_DIGEST_FIELD, digest.getDigest());

      object.put(HUMAN_PASSWORD_FIELD, digestObj);
    }

    return object;
  }

  private HumanAccountData objectToHuman(ParticipantId id, DBObject object) {
    PasswordDigest passwordDigest = null;

    DBObject digestObj = (DBObject) object.get(HUMAN_PASSWORD_FIELD);
    if (digestObj != null) {
      byte[] salt = (byte[]) digestObj.get(PASSWORD_SALT_FIELD);
      byte[] digest = (byte[]) digestObj.get(PASSWORD_DIGEST_FIELD);
      passwordDigest = PasswordDigest.from(salt, digest);
    }

    return new HumanAccountDataImpl(id, passwordDigest);
  }

  // ****** RobotAccountData serialization

  private DBObject robotToObject(RobotAccountData account) {
    return new BasicDBObject()
        .append(ROBOT_URL_FIELD, account.getUrl())
        .append(ROBOT_SECRET_FIELD, account.getConsumerSecret())
        .append(ROBOT_CAPABILITIES_FIELD, capabilitiesToObject(account.getCapabilities()))
        .append(ROBOT_VERIFIED_FIELD, account.isVerified());
  }

  private DBObject capabilitiesToObject(RobotCapabilities capabilities) {
    if (capabilities == null) {
      return null;
    }

    BasicDBObject capabilitiesObj = new BasicDBObject();
    for (Capability capability : capabilities.getCapabilitiesMap().values()) {
      BasicBSONList contexts = new BasicBSONList();
      for (Context c : capability.getContexts()) {
        contexts.add(c.name());
      }
      capabilitiesObj.put(capability.getEventType().name(),
          new BasicDBObject()
              .append(CAPABILITY_CONTEXTS_FIELD, contexts)
              .append(CAPABILITY_FILTER_FIELD, capability.getFilter()));
    }

    BasicDBObject object =
        new BasicDBObject()
            .append(CAPABILITIES_CAPABILITIES_FIELD, capabilitiesObj)
            .append(CAPABILITIES_HASH_FIELD, capabilities.getCapabilitiesHash())
            .append(CAPABILITIES_VERSION_FIELD, capabilities.getProtocolVersion().name());

    return object;
  }

  private AccountData objectToRobot(ParticipantId id, DBObject robot) {
    String url = (String) robot.get(ROBOT_URL_FIELD);
    String secret = (String) robot.get(ROBOT_SECRET_FIELD);
    RobotCapabilities capabilities =
        objectToCapabilities((DBObject) robot.get(ROBOT_CAPABILITIES_FIELD));
    boolean verified = (Boolean) robot.get(ROBOT_VERIFIED_FIELD);
    return new RobotAccountDataImpl(id, url, secret, capabilities, verified);
  }
  
  @SuppressWarnings("unchecked")
  private RobotCapabilities objectToCapabilities(DBObject object) {
    if (object == null) {
      return null;
    }

    Map<String, Object> capabilitiesObj =
        (Map<String, Object>) object.get(CAPABILITIES_CAPABILITIES_FIELD);
    Map<EventType, Capability> capabilities = CollectionUtils.newHashMap();

    for (Entry<String, Object> capability : capabilitiesObj.entrySet()) {
      EventType eventType = EventType.valueOf(capability.getKey());
      List<Context> contexts = CollectionUtils.newArrayList();
      DBObject capabilityObj = (DBObject) capability.getValue();
      DBObject contextsObj = (DBObject) capabilityObj.get(CAPABILITY_CONTEXTS_FIELD);
      for (String contextId : contextsObj.keySet()) {
        contexts.add(Context.valueOf((String) contextsObj.get(contextId)));
      }
      String filter = (String) capabilityObj.get(CAPABILITY_FILTER_FIELD);

      capabilities.put(eventType, new Capability(eventType, contexts, filter));
    }

    String capabilitiesHash = (String) object.get(CAPABILITIES_HASH_FIELD);
    ProtocolVersion version =
        ProtocolVersion.valueOf((String) object.get(CAPABILITIES_VERSION_FIELD));

    return new RobotCapabilities(capabilities, capabilitiesHash, version);
  }
}
