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
package com.ceridwen.lcf.model.referencing;

import java.util.HashMap;
import java.util.Map;
import org.bic.ns.lcf.v1_0.Entity;
import org.bic.ns.lcf.v1_0.EntityType;
import org.bic.ns.lcf.v1_0.Item;
import org.bic.ns.lcf.v1_0.LcfEntityListResponse;

/**
 *
 * @author Ceridwen Limited
 */


interface SpecialCase {
    EntityType handle(String propertyReference, String property, Class parentClazz, Object parent);
}

class EntityProperty {
    private final Class entity;
    private final String property;
    
    public EntityProperty(Class entity, String property) {
        this.entity = entity;
        this.property = property;
    }
    
    public boolean equals(Object test) {
        return (this.entity.equals(((EntityProperty)test).entity) && this.property.equals(((EntityProperty)test).property));
    }
    
    public int hashCode() {
        return entity.hashCode() + property.hashCode();
    }
}

/**
 *
 * @author Ceridwen Limited
 */
public class SpecialReferenceCases {
    private Map<String, EntityType> globalMap = new HashMap<>();
    private Map<EntityProperty, EntityType> localMap = new HashMap<>();
    private Map<Class, SpecialCase> cases = new HashMap<>();
    
    /**
     *
     */
    public SpecialReferenceCases() {
        
        globalMap.put("Institution", EntityType.AUTHORITIES);
        globalMap.put("Message", EntityType.MESSAGES);
        
        localMap.put(new EntityProperty(Item.class, "Owner"), EntityType.AUTHORITIES);
        
        cases.put(Entity.class, (propertyReference, property, parentClazz, parent) -> {
            if (parentClazz != null) {
                if (parentClazz.equals(LcfEntityListResponse.class)) {
                    if (propertyReference.equals("getHref")) {
                        if (parent == null) {
                            return EntityType.PATRONS;
                        } else {
                            return ((LcfEntityListResponse)parent).getEntityType();
                        }
                    }
                }
            }
            return null;            
        });
    }
    
    /**
     *
     * @param clazz
     * @param propertyReference
     * @param property
     * @param parentClazz
     * @param parent
     * @return
     */
    public EntityType handle(Class clazz, String propertyReference, String property, Class parentClazz, Object parent) {
        for (String mapping: globalMap.keySet()) {
            if (property.endsWith(mapping)) {
                return globalMap.get(mapping);
            }
        }
        
        if (localMap.containsKey(new EntityProperty(clazz, property))) {
            return localMap.get(new EntityProperty(clazz, property));
        }
        
        if (cases.containsKey(clazz)) {
            SpecialCase acase = cases.get(clazz);       
            return acase.handle(propertyReference, property, parentClazz, parent);
        } else {
            return null;
        }
    }
}
