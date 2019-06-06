/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.codegen;

import com.ceridwen.lcf.model.enumerations.AlternativeResponseFormats;
import com.ceridwen.lcf.model.enumerations.CreationQualifier;
import com.ceridwen.lcf.model.enumerations.EntityTypes;
import com.ceridwen.lcf.model.enumerations.VirtualUpdatePath;
import com.ceridwen.lcf.model.authentication.AuthenticationCategory;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bic.ns.lcf.v1_0.SelectionCriteria;

/**
 *
 * @author Matthew
 */
public class GenerateWebservices extends Generator {
    
    String dashedToCamel(String dashed) {
        
        if (dashed.endsWith("-id")) {
            dashed = dashed.replaceAll("-id$", "-ref");
        }
        
        StringBuffer out = new StringBuffer();
        boolean shift = true;
        
        for (Character c: dashed.toCharArray()) {
            if (c == '-') {
                shift = true;
            } else {                
                out.append(shift?c.toString().toUpperCase():c);
                shift = false;
            }
        }
        return out.toString();
    }
    
    
    List<StringTemplateSelectionCriterion> getSelectionCriteria(EntityTypes.Type entity) {
            List<StringTemplateSelectionCriterion> list = new ArrayList<>();
                        
            for (SelectionCriteria criterion: SelectionCriteria.values()) {
                String property = dashedToCamel(criterion.value());
                try {
                    entity.getTypeClass().getMethod("get" + property);
                    StringTemplateSelectionCriterion criteria = new StringTemplateSelectionCriterion();
                    criteria.setParameter(criterion.value());
                    criteria.setVariable(criterion.name());
                    list.add(criteria);
                } catch (NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(GenerateWebservices.class.getName()).log(Level.INFO, "Selection Criteria {0} not applicable to {1}", new String[]{criterion.value(), entity.getEntityTypeCodeValue()});
                }
            }
            
            return list;
            
    }
    
    List<String> getAlternativePostResponses(EntityTypes.Type entity) {
        List<String> formats = new ArrayList<>();
        for (Class format: new AlternativeResponseFormats().getAlternativePostFormat(entity)) {
            formats.add(format.getName());
        }
        
        return formats;
    }

    List<String> getAlternativePutResponses(EntityTypes.Type entity) {
        List<String> formats = new ArrayList<>();
        for (Class format: new AlternativeResponseFormats().getAlternativePutFormat(entity)) {
            formats.add(format.getName());
        }
        
        return formats;
    }

    List<String> getAlternativeGetResponses(EntityTypes.Type entity) {
        List<String> formats = new ArrayList<>();
        for (Class format: new AlternativeResponseFormats().getAlternativeGetFormat(entity)) {
            formats.add(format.getName());
        }
        
        return formats;
    }

    List<String> getAlternativeDeleteResponses(EntityTypes.Type entity) {
        List<String> formats = new ArrayList<>();
        for (Class format: new AlternativeResponseFormats().getAlternativeDeleteFormat(entity)) {
            formats.add(format.getName());
        }
        
        return formats;
    }
    
    List<KeyValue<CreationQualifier>> getCreationQualifiers(EntityTypes.Type entity) {
        ArrayList<KeyValue<CreationQualifier>> list = new ArrayList<>();
        
        for (CreationQualifier qualifier: CreationQualifier.values()) {
            if (qualifier.isApplicable(entity)) {
                list.add(new KeyValue<>(qualifier, qualifier.getParameterText()));
            }
        }
        
        return list;
    }
    
    List<KeyValue<VirtualUpdatePath>> getVirtualPaths(EntityTypes.Type entity) {
        ArrayList<KeyValue<VirtualUpdatePath>> list = new ArrayList<>();
        
        for (VirtualUpdatePath path: VirtualUpdatePath.values()) {
            if (path.isApplicable(entity)) {
                list.add(new KeyValue<>(path, path.getPath()));
            }
        }
        
        return list;
    }
    
    List<KeyValue<AuthenticationCategory>> getAuthenticationSchemes(EntityTypes.Type entity) {
        ArrayList<KeyValue<AuthenticationCategory>> list = new ArrayList<>();
        
        for (AuthenticationCategory category: AuthenticationCategory.values()) {
            list.add(new KeyValue<>(category, category.getParameterText()));
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
        map.put("SelectionCriteria", getSelectionCriteria(entity));
        map.put("AlternativePosts", getAlternativePostResponses(entity));
        map.put("AlternativePuts", getAlternativePutResponses(entity));
        map.put("AlternativeGets", getAlternativeGetResponses(entity));
        map.put("AlternativeDeletes", getAlternativeDeleteResponses(entity));
        map.put("CreationQualifiers", getCreationQualifiers(entity));
        map.put("VirtualUpdatePaths", getVirtualPaths(entity));
        map.put("AuthenticationSchemes", getAuthenticationSchemes(entity));
        
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





