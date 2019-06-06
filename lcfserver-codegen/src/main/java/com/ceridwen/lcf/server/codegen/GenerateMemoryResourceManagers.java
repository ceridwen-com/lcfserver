/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.codegen;

import com.ceridwen.lcf.model.enumerations.EntityTypes;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Matthew
 */
public class GenerateMemoryResourceManagers extends Generator {
    @Override
    Map getEntityMap(EntityTypes.Type entity) {
        Map<String, Object> map = new HashMap<>();
        
        map.put("Entity", entity.name());      
        
        return map;
    }
    
    public static void main(String [] args)
    {
            String templatedir = args[0];
            String targetdir = args[1] + "\\com\\ceridwen\\lcf\\server\\resources\\memory";
            
            GenerateMemoryResourceManagers generator = new GenerateMemoryResourceManagers();
            
            for (EntityTypes.Type entity: EntityTypes.Type.values()) {
                for (String template: new String[]{"ResourceManager"}) {
                    generator.generateTemplate(templatedir, template, targetdir, "", ".java", entity);
                }
            }

            targetdir = args[1] + "\\META-INF\\services";

            for (EntityTypes.Type entity: EntityTypes.Type.values()) {
                for (String template: new String[]{"ResourceManagerInterface"}) {
                    generator.generateTemplate(templatedir, template, targetdir, "com.ceridwen.lcf.server.resources.", "", entity);
                }
            }
    }
  
}
