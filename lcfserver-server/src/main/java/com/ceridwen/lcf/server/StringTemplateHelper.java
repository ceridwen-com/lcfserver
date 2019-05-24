/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Matthew
 */
public class StringTemplateHelper {
    
    public Map<String, Object> getPatronProperties() {
        return getEntityMap(EntityTypes.Type.Patron);
    }
    
    public Map<String, Object> getLoanProperties() {
        return getEntityMap(EntityTypes.Type.Loan);
    }
    
    public Map<String, Object> getContactProperties() {
        return getEntityMap(EntityTypes.Type.Contact);
    }
    
    public Map<String, Object> getChargeProperties() {
        return getEntityMap(EntityTypes.Type.Charge);
    }
        
    public Map<String, Object> getReservationProperties() {
        return getEntityMap(EntityTypes.Type.Reservation);
    }
    
    public Map<String, Object> getPaymentProperties() {
        return getEntityMap(EntityTypes.Type.Payment);
    }
    
    Map getEntityMap(EntityTypes.Type entity) {
        Map<String, Object> map = new HashMap<>();
        
        map.put("LCFPath", EntityTypes.LCF_PREFIX);
        map.put("Entity", entity.name());        
        map.put("EntityPath", entity.getEntityTypeCodeValue());
        
        List<StringTemplateHelperSubEntity> subentities = new ArrayList<>();
        
        for (Method method: entity.getTypeClass().getMethods()) {
            try {
                if (method.getName().startsWith("get") && method.getName().endsWith("Ref") && method.getReturnType().isAssignableFrom(List.class)) {
                    StringTemplateHelperSubEntity subentity = new StringTemplateHelperSubEntity();
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
        
        return map;        
    }
    
}
