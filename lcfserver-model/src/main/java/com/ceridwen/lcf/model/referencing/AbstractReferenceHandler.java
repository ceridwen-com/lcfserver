/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.model.referencing;

import com.ceridwen.lcf.model.enumerations.EntityTypes;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Matthew
 */
public abstract class AbstractReferenceHandler {

//    static EnumMap<EntityTypes.Type, AbstractResourceManagerInterface> resourceHandlers = new EnumMap<>(EntityTypes.Type.class);
    
//    public static void registerResourceHandler(EntityTypes.Type type, AbstractResourceManagerInterface rm) {
//        resourceHandlers.put(type, rm);
//    }
    
//    
//    
//    public boolean checkReferences(Object entity) {
//        if (entity != null) {
//            return iterateProperties(entity.getClass(), entity, null, null, "", "");
//        } else {
//            return true;
//        }      
//    }
//    
//

    
    protected boolean iterateProperties(Class clazz, Object instance, Class parentClazz, Object parent, String urlPrefix, String logPrefix) {
        boolean success = true;
        try {
            for (Method method: clazz.getDeclaredMethods()) {
                if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                    if (method.getReturnType().equals(String.class)) {
                        String property = propertyFromPropertyReferenceGetter(method.getName());
                        if (property != null) {
                            if (!canHandleReference(clazz, instance, parentClazz, parent, method.getName(), property, urlPrefix, logPrefix)) {
                                success = false;
                            }                            
                        }
                    } else {
                        Type returnType = method.getGenericReturnType();
                        Class returnClazz = method.getReturnType();
                        if (!returnType.getTypeName().startsWith("java.lang")) {
                            if (returnClazz.equals(List.class)){
                                if ((Type)returnType instanceof ParameterizedType) {
                                    Type elementType = ((ParameterizedType)returnType).getActualTypeArguments()[0];
                                    if (instance == null) {
                                        iterateProperties(Class.forName(elementType.getTypeName()), null, clazz, instance, urlPrefix, logPrefix + clazz.getName() + ".");
                                    } else {
                                        for (Object i : (List)method.invoke(instance)) {
                                            iterateProperties(Class.forName(elementType.getTypeName()), i, clazz, instance, urlPrefix, logPrefix + clazz.getName() + ".");
                                        }
                                    }
                                }
                            } else {
                                if (instance == null) {
                                    iterateProperties(returnClazz, null, clazz, instance, urlPrefix, logPrefix + clazz.getName() + ".");
                                } else {
                                    iterateProperties(returnClazz, method.invoke(instance), clazz, instance, urlPrefix, logPrefix + clazz.getName() + ".");
                                }
                            }
                        }
                    }
                }
            }
        } catch (SecurityException | IllegalArgumentException | ClassNotFoundException | IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(AbstractReferenceHandler.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }
        
        return success;
    }
    
    protected String propertyFromPropertyReferenceGetter(String propertyReference) {
        for (String suffix: new String[]{"Ref", "Href"}) {
            if (propertyReference.endsWith(suffix)) {
                return propertyReference.replaceAll(suffix + "$", "").replaceFirst("get", "");
            }
        }
        return null;
    }
        
    protected EntityTypes.Type getPropertyType(Class clazz, String propertyReference, String property, Class parentClazz, Object parent) {
        for (EntityTypes.Type type: EntityTypes.Type.values()) {
            if (property.endsWith(type.name())) {
                return type;
            }
        }
        return new SpecialReferenceCases().handle(clazz, propertyReference, property, parentClazz, parent);
    }

    protected boolean canHandleReference(Class clazz, Object instance, Class parentClazz, Object parent, String propertyReference, String property, String urlPrefix, String logPrefix) {
        EntityTypes.Type propertyType = getPropertyType(clazz, propertyReference, property, parentClazz, parent);
        if (propertyType == null) {
            Logger.getLogger(AbstractReferenceHandler.class.getName()).log(Level.WARNING, "{0}{1}: {2} -> UNKNOWN", new Object[]{logPrefix, clazz.getName(), propertyReference});
            return false;
        } else {
            handleReference(clazz, instance, propertyReference, urlPrefix + propertyType.getEntityTypeCodeValue() + "/");
            Logger.getLogger(AbstractReferenceHandler.class.getName()).log(Level.FINE, "{0}{1}: {2} -> {3}", new Object[]{logPrefix, clazz.getName(), propertyReference, propertyType.name()});
            return true;
            
        }        
    }
    
    abstract protected void handleReference(Class clazz, Object instance, String propertyReference, String refprefix);
        
}
