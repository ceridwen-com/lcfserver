/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.codegen;

import com.ceridwen.lcf.lcfserver.model.EntityTypes;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bic.ns.lcf.v1_0.SelectionCriteria;

/**
 *
 * @author Matthew
 */
public class GenerateWebservices extends Generator {
    
    List<StringTemplateSelectionCriterion> getSelectionCriteria() {
            List<StringTemplateSelectionCriterion> list = new ArrayList<>();
            
            for (SelectionCriteria criterion: SelectionCriteria.values()) {
                StringTemplateSelectionCriterion criteria = new StringTemplateSelectionCriterion();
                criteria.setParameter(criterion.value());
                criteria.setVariable(criterion.name());
                list.add(criteria);
            }
            
            return list;
            
    }
    
    @Override
    Map getEntityMap(EntityTypes.Type entity) {
        Map<String, Object> map = new HashMap<>();
        
        map.put("LCFPath", EntityTypes.LCF_PREFIX);
        map.put("Entity", entity.name());        
        map.put("EntityPath", entity.getEntityTypeCodeValue());
        
        List<StringTemplateSubEntity> subentities = new ArrayList<>();
        
        for (Method method: entity.getTypeClass().getMethods()) {
            try {
                if (method.getName().startsWith("get") && method.getName().endsWith("Ref") && method.getReturnType().isAssignableFrom(List.class)) {
                    StringTemplateSubEntity subentity = new StringTemplateSubEntity();
                    String entname = method.getName().substring(3, method.getName().length()-3);
                    EntityTypes.Type enttype = EntityTypes.Type.valueOf(entname);
                    subentity.setEntity(enttype.name());
                    subentity.setEntityPath(enttype.getEntityTypeCodeValue());                
                    subentities.add(subentity);
                }
            } catch (IllegalArgumentException ex) {
            }
        }
        map.put("SubEntity", subentities);
        map.put("SelectionCriteria", getSelectionCriteria());
        
        return map;        
    }
    
    public static void main(String [] args)
    {
            String templatedir = args[0];
            String targetdir = args[1] + "\\com\\ceridwen\\lcf\\server\\webservice";
            
            GenerateWebservices generator = new GenerateWebservices();
            
            for (EntityTypes.Type entity: EntityTypes.Type.values()) {
                for (String template: new String[]{"Webservice", "ListWebservice", "ContainerWebservice"}) {
                    generator.generateTemplate(templatedir, template, targetdir, "", ".java", entity);
                }
            }
    }

    
}
