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
package com.ceridwen.lcf.server;

import com.ceridwen.lcf.model.EntityCodeListClassMapping;
import com.ceridwen.lcf.server.resources.AbstractResourceManagerInterface;
import com.ceridwen.lcf.server.webservice.WebserviceHelper;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bic.ns.lcf.v1_0.EntityType;

/**
 *
 * @author Matthew
 */
public class ImplementedOperations {
    
    private static Map<EntityType, Class> implemented = new HashMap<>();
    
    static public void init() {
        for (EntityType type: EntityType.values()) {
            try {
                Class entity = EntityCodeListClassMapping.getEntityClass(type);
                Class rmi = Class.forName("com.ceridwen.lcf.server.resources." + entity.getSimpleName() + "ResourceManagerInterface");
                WebserviceHelper helper = new WebserviceHelper(entity, rmi);
                AbstractResourceManagerInterface rm = helper.getResourceManager();    
                if (rm != null) {
                    Class webservice = Class.forName("com.ceridwen.lcf.server.webservice." + entity.getSimpleName() + "ContainerWebservice");
                    implemented.put(type, webservice);
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ApplicationConfig.class.getName()).log(Level.SEVERE, type.value() + "{0}: Error loading Resource Manager", ex);
            }
        }
                
    }
    
    static public Collection<Class> getResources() {
        return implemented.values();
    }
  
    static public Set<EntityType> getImplemented() {
        return implemented.keySet();
    }
    
    static public Set<EntityType> getUnimplemented() {
        Set<EntityType> unimplemented = new HashSet<>();
        Set<EntityType> impl = getImplemented();
        for (EntityType type: EntityType.values()) {
            if (!impl.contains(type)) {
                unimplemented.add(type);
            }
        }
        
        // These are deprecated in LCF 1.2.0 but are still present in the schema
        // The following lines will suppress the operations from the definition
        // whilst still permitting implementation
        unimplemented.add(EntityType.CLASS_SCHEMES);
        unimplemented.add(EntityType.CLASS_TERMS);
        
        return unimplemented;
    }
    
    
}
