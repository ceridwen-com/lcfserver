/*
 * Copyright 2019 Ceridwen Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ceridwen.lcf.server.resources.memory.database;

import com.ceridwen.lcf.model.EntityCodeListClassMapping;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;
import org.bic.ns.lcf.v1_0.EntityType;
import org.bic.ns.lcf.v1_0.LcfEntity;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

/**
 *
 * @author Ceridwen Limited
 */
public class Database {
    
    private static Database db = null;
    private static final Logger LOG = Logger.getLogger(Database.class.getName());
    
    private Map<EntityType, Map<String, LcfEntity>> database = new EnumMap<>(EntityType.class);
    
    /**
     *
     * @return
     */
    public static Database getDatabase() {
        if (db == null) {
            db = new Database();
        }
        return db;
    }
    
    public Database() {
        Config();
    }
    
    /**
     *
     * @param type
     */
    private void generateRandomContent(EntityType type) {
        EasyRandomParameters parameters = new EasyRandomParameters()
            .seed(123L)
            .objectPoolSize(100)
            .randomizationDepth(3)
            .stringLengthRange(5, 12)
            .collectionSizeRange(1, 10)
            .scanClasspathForConcreteTypes(true)
            .overrideDefaultInitialization(false)
            .ignoreRandomizationErrors(true);
        EasyRandom easyRandom = new EasyRandom(parameters);

        int amount = (new Random()).nextInt(25)+5;
        for (int i=0; i < amount; i++) {
            LcfEntity o = easyRandom.nextObject(EntityCodeListClassMapping.getEntityClass(type));
            o.setIdentifier(UUID.randomUUID().toString());
            this.write(type, o.getIdentifier(), o);
        }
    }
    
    public void Config() {
        LOG.info("Loading Default Data");
        for (EntityType type: EntityType.values()) {
            generateRandomContent(type);
        }
        LOG.info("Default Data Load Complete");
    }

    public LcfEntity write(EntityType type, String identifier, LcfEntity data) {
        if (!database.containsKey(type)) {
            database.put(type, new HashMap<>());
        }    
        Map<String, LcfEntity> map = database.get(type);
        if (map.containsKey(identifier)) {
            map.replace(identifier, data);
            LOG.info(new TransactionalSupplier(Operation.WRITE, data));
        }
        else {
            map.put(identifier, data);
            LOG.info(new TransactionalSupplier(Operation.CREATE, data));
        }
        
        return read(type, identifier);        
    }
   
    public LcfEntity read(EntityType type, String identifier) {
        if (database.containsKey(type)) {
            Map<String, LcfEntity> map = database.get(type);
            if (map.containsKey(identifier)) {
                LOG.info(new TransactionalSupplier(Operation.READ, map.get(identifier)));
                return map.get(identifier);
            }
        }
        return null;
    }

    public boolean delete(EntityType type, String identifier) {
        if (database.containsKey(type)) {
            Map<String, LcfEntity> map = database.get(type);
            if (map.containsKey(identifier)) {
                map.remove(identifier);
                LOG.info(new TransactionalSupplier(Operation.DELETE, map.get(identifier)));
                return true;
            }
        }
        
        return false;        
    }
    
    public Collection<LcfEntity> list(EntityType type) {
        if (database.containsKey(type)) {
            LOG.info(new TransactionalSupplier(Operation.LIST, database.get(type).values()));
            return database.get(type).values();
        } else {
            return new ArrayList();
        }

    }
}
