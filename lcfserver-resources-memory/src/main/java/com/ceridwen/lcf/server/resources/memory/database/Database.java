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

import com.ceridwen.lcf.model.enumerations.EntityTypes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

/**
 *
 * @author Ceridwen Limited
 */
public class Database {
    
    private static Database db = null;
    
    private Map<EntityTypes.Type, Map<String, Object>> database = new EnumMap<>(EntityTypes.Type.class);
    
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
    private void generateRandomContent(EntityTypes.Type type) {
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
            Object o = easyRandom.nextObject(type.getTypeClass());
            type.setIdentifier(o, UUID.randomUUID().toString());
            this.put(type, type.getIdentifier(o), o);
        }
    }
    
    public void Config() {
        Logger.getLogger(Database.class.getName()).info("Loading Default Data");
        for (EntityTypes.Type type: EntityTypes.Type.values()) {
            generateRandomContent(type);
        }
        Logger.getLogger(Database.class.getName()).info("Default Data Load Complete");
    }
    
    public boolean contains(EntityTypes.Type type, String identifier) {
        if (database.containsKey(type)) {
            Map<String, Object> map = database.get(type);
            return map.containsKey(identifier);
        }
        
        return false;                
    }

    public Object put(EntityTypes.Type type, String identifier, Object data) {
        if (!database.containsKey(type)) {
            database.put(type, new HashMap<>());
        }    
        Map<String, Object> map = database.get(type);
        if (map.containsKey(identifier)) {
            map.replace(identifier, data);
        }
        else {
            map.put(identifier, data);
        }
        
        return get(type, identifier);        
    }
   
    public Object get(EntityTypes.Type type, String identifier) {
        if (database.containsKey(type)) {
            Map<String, Object> map = database.get(type);
            if (map.containsKey(identifier)) {
                return map.get(identifier);
            }
        }
        return null;
    }

    public boolean delete(EntityTypes.Type type, String identifier) {
        if (database.containsKey(type)) {
            Map<String, Object> map = database.get(type);
            if (map.containsKey(identifier)) {
                map.remove(identifier);
                return true;
            }
        }
        
        return false;        
    }
    
    public Collection<Object> list(EntityTypes.Type type) {
        if (database.containsKey(type)) {
            return database.get(type).values();
        } else {
            return new ArrayList();
        }

    }
}
