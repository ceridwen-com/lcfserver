/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.codegen;

import com.ceridwen.lcf.lcfserver.model.EntityTypes;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Matthew
 */
public class GenerateResourceManagers extends Generator {

    @Override
    Map getEntityMap(EntityTypes.Type entity) {
        Map<String, Object> map = new HashMap<>();
        
        map.put("Entity", entity.name());      
        
        return map;
    }
    
    public static void main(String [] args)
    {
            String templatedir = args[0];
            String targetdir = args[1] + "\\com\\ceridwen\\lcf\\server\\resources";
            
            GenerateResourceManagers generator = new GenerateResourceManagers();
            
            for (EntityTypes.Type entity: EntityTypes.Type.values()) {
                for (String template: new String[]{"ResourceManagerInterface"}) {
                    generator.generateTemplate(templatedir, template, targetdir, "", ".java", entity);
                }
            }
    }

    
}
