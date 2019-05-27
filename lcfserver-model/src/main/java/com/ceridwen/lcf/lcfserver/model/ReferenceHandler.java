/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.lcfserver.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bic.ns.lcf.v1_0.Entity;
import org.bic.ns.lcf.v1_0.LcfEntityListResponse;

/**
 *
 * @author Matthew
 */
public class ReferenceHandler {
    public static boolean processReferences(Object entity, String urlPrefix) {
        if (entity != null) {
            return iterateProperties(entity.getClass(), entity, null, null, urlPrefix, "");
        } else {
            return true;
        }      
    }

    public static boolean checkReferences(Class clazz) {
        return iterateProperties(clazz, null, null, null, "", "");
    }
    
    private static boolean iterateProperties(Class clazz, Object instance, Class parentClazz, Object parent, String urlPrefix, String logPrefix) {
        boolean success = true;
        try {
            for (Method method: clazz.getDeclaredMethods()) {
                if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                    if (method.getReturnType().equals(String.class)) {
                        String property = propertyFromPropertyReferenceGetter(method.getName());
                        if (property != null) {
                            if (!applyReference(clazz, instance, parentClazz, parent, method.getName(), property, urlPrefix, logPrefix)) {
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
            Logger.getLogger(ReferenceHandler.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }
        
        return success;
    }
    
    private static String propertyFromPropertyReferenceGetter(String propertyReference) {
        for (String suffix: new String[]{"Ref", "Href"}) {
            if (propertyReference.endsWith(suffix)) {
                return propertyReference.replaceAll(suffix + "$", "").replaceFirst("get", "");
            }
        }
        return null;
    }
        
    private static EntityTypes.Type getPropertyType(Class clazz, String propertyReference, String property, Class parentClazz, Object parent) {
        for (EntityTypes.Type type: EntityTypes.Type.values()) {
            if (property.endsWith(type.name())) {
                return type;
            }
        }
        return new SpecialReferenceCases().handle(clazz, propertyReference, property, parentClazz, parent);
    }

    private static boolean applyReference(Class clazz, Object instance, Class parentClazz, Object parent, String propertyReference, String property, String urlPrefix, String logPrefix) {
        EntityTypes.Type propertyType = getPropertyType(clazz, propertyReference, property, parentClazz, parent);
        if (propertyType == null) {
            Logger.getLogger(ReferenceHandler.class.getName()).log(Level.WARNING, "{0}{1}: {2} -> UNKNOWN", new Object[]{logPrefix, clazz.getName(), propertyReference});
            return false;
        } else {
            if (instance != null) {
                editReference(clazz, instance, propertyReference, true, urlPrefix + propertyType.getEntityTypeCodeValue() + "/");
            }
            Logger.getLogger(ReferenceHandler.class.getName()).log(Level.FINE, "{0}{1}: {2} -> {3}", new Object[]{logPrefix, clazz.getName(), propertyReference, propertyType.name()});
            return true;
            
        }        
    }
    
    private static void editReference(Class clazz, Object instance, String propertyReference, boolean addref, String refprefix) {
        try {
            Object result = clazz.getMethod(propertyReference).invoke(instance);
            if (result != null && result.getClass().equals(String.class)) {
                String newresult;
                if (addref) {
                    newresult = (String)refprefix + result;
                } else {
                    newresult = ((String)result).replaceFirst(refprefix, "");          
                }
                String setter = propertyReference.replaceFirst("get", "set");
                clazz.getMethod(setter, String.class).invoke(instance, newresult);
            }        
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ReferenceHandler.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
        
}
