/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.waveprotocol.box.server.persistence.mongodb;

import org.waveprotocol.box.server.persistence.AttachmentStore;
import org.waveprotocol.box.server.persistence.AttachmentStoreTestBase;

/**
 * A wrapper around AttachmentStoreBase which tests the mongo-based attachment
 * store.
 */
public class AttachmentStoreTest extends AttachmentStoreTestBase {
  private static final String TEST_DATABASE = "AttachmentTest";

  private static MongoDbProvider mongoDbProvider = new MongoDbProvider();
  private final MongoDbAttachmentStore store;

  /**
   * Initializes the MongoDB version of a {@link AttachmentStoreTestBase}.
   */
  public AttachmentStoreTest() throws Exception {
    mongoDbProvider.setDatabaseName(TEST_DATABASE);
    mongoDbProvider.getMongo();
    store = new MongoDbAttachmentStore(mongoDbProvider.getDB());
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    mongoDbProvider.getDB().dropDatabase();
  }
  
  @Override
  protected AttachmentStore newAttachmentStore() {
    mongoDbProvider.getDB().dropDatabase();
    return store;
  }
}
