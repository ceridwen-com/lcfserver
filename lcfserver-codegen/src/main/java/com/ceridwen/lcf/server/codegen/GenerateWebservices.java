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

import com.ceridwen.lcf.model.Constants;
import com.ceridwen.lcf.model.enumerations.AlternativeResponseFormats;
import com.ceridwen.lcf.model.enumerations.CreationQualifier;
import com.ceridwen.lcf.model.enumerations.EntityTypes;
import com.ceridwen.lcf.model.enumerations.DirectUpdatePath;
import com.ceridwen.lcf.model.authentication.AuthenticationCategory;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bic.ns.lcf.v1_0.SelectionCriteria;

/**
 *
 * @author Ceridwen Limited
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
                String property;
                if (criterion.value().startsWith("alt-")) {
                    property = dashedToCamel(criterion.value().replace("^alt-", "additional-").replace("-type$", ""));                    
                } else {
                    property = dashedToCamel(criterion.value());
                }
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
    
    List<KeyValue<DirectUpdatePath>> getDirectUpdatePaths(EntityTypes.Type entity) {
        ArrayList<KeyValue<DirectUpdatePath>> list = new ArrayList<>();
        
        for (DirectUpdatePath path: DirectUpdatePath.values()) {
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
        
        map.put("LCFPath", Constants.LCF_PREFIX);
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
        map.put("DirectUpdatePaths", getDirectUpdatePaths(entity));
        map.put("AuthenticationSchemes", getAuthenticationSchemes(entity));
        
        return map;        
    }
    
    /**
     *
     * @param args
     */
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





