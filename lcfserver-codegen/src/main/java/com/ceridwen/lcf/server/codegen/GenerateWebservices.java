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

import com.ceridwen.lcf.model.LcfConstants;
import com.ceridwen.lcf.model.EntityCodeListClassMapping;
import com.ceridwen.lcf.model.enumerations.AlternativeResponseFormats;
import com.ceridwen.lcf.model.enumerations.CreationQualifier;
import com.ceridwen.lcf.model.enumerations.DirectUpdatePath;
import com.ceridwen.lcf.model.authentication.AuthenticationCategory;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bic.ns.lcf.v1_0.EntityType;
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
    
    
    List<StringTemplateSelectionCriterion> getSelectionCriteria(EntityType entity) {
            List<StringTemplateSelectionCriterion> list = new ArrayList<>();
                        
            for (SelectionCriteria criterion: SelectionCriteria.values()) {
                String property;
                if (criterion.value().startsWith("alt-")) {
                    property = dashedToCamel(criterion.value().replace("^alt-", "additional-").replace("-type$", ""));                    
                } else {
                    property = dashedToCamel(criterion.value());
                }
                try {
                    EntityCodeListClassMapping.getEntityClass(entity).getMethod("get" + property);
                    StringTemplateSelectionCriterion criteria = new StringTemplateSelectionCriterion();
                    criteria.setParameter(criterion.value());
                    criteria.setVariable(criterion.name());
                    list.add(criteria);
                } catch (NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(GenerateWebservices.class.getName()).log(Level.INFO, "Selection Criteria {0} not applicable to {1}", new String[]{criterion.value(), EntityCodeListClassMapping.getEntityClass(entity).getSimpleName()});
                }
            }
            
            return list;
            
    }
    
    List<String> getAlternativePostResponses(EntityType entity) {
        List<String> formats = new ArrayList<>();
        for (Class format: new AlternativeResponseFormats().getAlternativePostFormat(EntityCodeListClassMapping.getEntityClass(entity))) {
            formats.add(format.getName());
        }
        
        return formats;
    }

    List<String> getAlternativePutResponses(EntityType entity) {
        List<String> formats = new ArrayList<>();
        for (Class format: new AlternativeResponseFormats().getAlternativePutFormat(EntityCodeListClassMapping.getEntityClass(entity))) {
            formats.add(format.getName());
        }
        
        return formats;
    }

    List<String> getAlternativeGetResponses(EntityType entity) {
        List<String> formats = new ArrayList<>();
        for (Class format: new AlternativeResponseFormats().getAlternativeGetFormat(EntityCodeListClassMapping.getEntityClass(entity))) {
            formats.add(format.getName());
        }
        
        return formats;
    }

    List<String> getAlternativeDeleteResponses(EntityType entity) {
        List<String> formats = new ArrayList<>();
        for (Class format: new AlternativeResponseFormats().getAlternativeDeleteFormat(EntityCodeListClassMapping.getEntityClass(entity))) {
            formats.add(format.getName());
        }
        
        return formats;
    }
    
    List<KeyValue<CreationQualifier>> getCreationQualifiers(EntityType entity) {
        ArrayList<KeyValue<CreationQualifier>> list = new ArrayList<>();
        
        for (CreationQualifier qualifier: CreationQualifier.values()) {
            if (qualifier.isApplicable(EntityCodeListClassMapping.getEntityClass(entity))) {
                list.add(new KeyValue<>(qualifier, qualifier.getParameterText()));
            }
        }
        
        return list;
    }
    
    List<KeyValue<DirectUpdatePath>> getDirectUpdatePaths(EntityType entity) {
        ArrayList<KeyValue<DirectUpdatePath>> list = new ArrayList<>();
        
        for (DirectUpdatePath path: DirectUpdatePath.values()) {
            if (path.isApplicable(EntityCodeListClassMapping.getEntityClass(entity))) {
                list.add(new KeyValue<>(path, path.getPath()));
            }
        }
        
        return list;
    }
    
    List<KeyValue<AuthenticationCategory>> getAuthenticationSchemes() {
        ArrayList<KeyValue<AuthenticationCategory>> list = new ArrayList<>();
        
        for (AuthenticationCategory category: AuthenticationCategory.values()) {
            list.add(new KeyValue<>(category, category.getParameterText()));
        }
        
        return list;
    }
    
    
    @Override
    Map getEntityMap(EntityType entity) {
        Map<String, Object> map = new HashMap<>();
        
        map.put("LCFPath", LcfConstants.LCF_PREFIX);
        map.put("Entity", EntityCodeListClassMapping.getEntityClass(entity).getSimpleName());        
        map.put("EntityPath", entity.value());
        
        List<StringTemplateSubEntity> subentities = new ArrayList<>();
        
        for (Method method: EntityCodeListClassMapping.getEntityClass(entity).getMethods()) {
            try {
                if (method.getName().startsWith("get") && method.getName().endsWith("Ref") && method.getReturnType().isAssignableFrom(List.class)) {
                    StringTemplateSubEntity subentity = new StringTemplateSubEntity();
                    String entname = method.getName().substring(3, method.getName().length()-3);
                    subentity.setEntity(entname);
                    subentity.setEntityPath(EntityCodeListClassMapping.getEntityType(entname).value());                
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
        map.put("AuthenticationSchemes", getAuthenticationSchemes());
        
        return map;        
    }
    
    /**
     *
     * @param args
     */
    public static void main(String [] args)
    {
            String templatedir = args[0];
            String targetdir = args[1] + File.separator + "com" + File.separator + "ceridwen" + File.separator + "lcf" + File.separator + "server" + File.separator + "webservice";
            
            GenerateWebservices generator = new GenerateWebservices();
            
            for (EntityType entity: EntityType.values()) {
                for (String template: new String[]{"Webservice", "ListWebservice", "ContainerWebservice"}) {
                    generator.generateTemplate(templatedir, template, targetdir, "", ".java", entity);
                }
            }
    }

    
}





