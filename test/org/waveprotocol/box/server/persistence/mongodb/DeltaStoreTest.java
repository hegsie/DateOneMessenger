package org.waveprotocol.box.server.persistence.mongodb;

import org.waveprotocol.box.server.persistence.DeltaStoreTestBase;
import org.waveprotocol.box.server.waveserver.DeltaStore;

public class DeltaStoreTest extends DeltaStoreTestBase {

  private static final String TEST_DATABASE = "DeltaStoreTest";
  private MongoDbDeltaStore deltaStore;
  private static MongoDbProvider mongoDbProvider = new MongoDbProvider();
  
  /**
   * Initializes the MongoDB version of a {@link MongoDbDeltaStore}.
   */
  public DeltaStoreTest() throws Exception { 
    mongoDbProvider.setDatabaseName(TEST_DATABASE);
    mongoDbProvider.getMongo();
    deltaStore = new MongoDbDeltaStore(mongoDbProvider.getDB());
  }
  
  @Override
  protected DeltaStore newDeltaStore() throws Exception {
    mongoDbProvider.getDB().dropDatabase();
    return deltaStore;
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    mongoDbProvider.getDB().dropDatabase();
  }
}
