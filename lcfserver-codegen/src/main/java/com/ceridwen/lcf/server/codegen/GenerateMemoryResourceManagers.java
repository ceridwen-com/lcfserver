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
package com.ceridwen.lcf.server.codegen;

import com.ceridwen.lcf.model.EntityCodeListClassMapping;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.bic.ns.lcf.v1_0.EntityType;

/**
 *
 * @author Ceridwen Limited
 */
public class GenerateMemoryResourceManagers extends Generator {
    @Override
    Map getEntityMap(EntityType entity) {
        Map<String, Object> map = new HashMap<>();
        
        map.put("Entity", EntityCodeListClassMapping.getEntityClass(entity).getSimpleName());      
        
        return map;
    }
    
    /**
     *
     * @param args
     */
    public static void main(String [] args)
    {
            String templatedir = args[0];
            String targetdir = args[1] + File.separator + "com" + File.separator + "ceridwen" + File.separator + "lcf" + File.separator + "server" + File.separator + "resources" + File.separator + "memory";
            
            GenerateMemoryResourceManagers generator = new GenerateMemoryResourceManagers();
            
            for (EntityType entity: EntityType.values()) {
                if (!Arrays.asList(EntityType.LOANS, EntityType.PATRONS).contains(entity)) {
                    for (String template: new String[]{"ResourceManager"}) {
                        generator.generateTemplate(templatedir, template, targetdir, "", ".java", entity);
                    }
                }
            }

            targetdir = args[1] + File.separator + "META-INF" + File.separator + "services";

            for (EntityType entity: EntityType.values()) {
                for (String template: new String[]{"ResourceManagerInterface"}) {
                    generator.generateTemplate(templatedir, template, targetdir, "com.ceridwen.lcf.server.resources.", "", entity);
                }
            }
    }
  
}
