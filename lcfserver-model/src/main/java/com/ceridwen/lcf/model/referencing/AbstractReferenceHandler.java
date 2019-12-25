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
package com.ceridwen.lcf.model.referencing;

import com.ceridwen.lcf.model.EntityCodeListClassMapping;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bic.ns.lcf.v1_0.EntityType;


/**
 *
 * @author Ceridwen Limited
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

    /**
     *
     * @param clazz
     * @param instance
     * @param parentClazz
     * @param parent
     * @param urlPrefix
     * @param logPrefix
     * @return
     */

    
    protected boolean iterateProperties(Class clazz, Object instance, Class parentClazz, Object parent, String urlPrefix, String logPrefix) {
        boolean success = true;
        try {
            for (Method method: clazz.getDeclaredMethods()) {
                if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                    if (method.getReturnType().equals(String.class)) {
                        success = handleStringType(method, clazz, instance, parentClazz, parent, urlPrefix, logPrefix, success);
                    } else {
                        Type returnType = method.getGenericReturnType();
                        Class returnClazz = method.getReturnType();
                        if (!(returnType.getTypeName().startsWith("java.lang") || returnType.getTypeName().endsWith("XMLGregorianCalendar"))) {
                            if (returnClazz.equals(List.class)){
                                if ((Type)returnType instanceof ParameterizedType) {
                                    Type elementType = ((ParameterizedType)returnType).getActualTypeArguments()[0];
                                    if (elementType.equals(String.class)) {
                                        handleStringType(method, clazz, instance, parentClazz, parent, urlPrefix, logPrefix, success);                                        
                                    } else {
                                        if (instance == null) {
                                            iterateProperties(Class.forName(elementType.getTypeName()), null, clazz, instance, urlPrefix, logPrefix + clazz.getName() + ".");
                                        } else {
                                            for (Object i : (List)method.invoke(instance)) {
                                                iterateProperties(Class.forName(elementType.getTypeName()), i, clazz, instance, urlPrefix, logPrefix + clazz.getName() + ".");
                                            }
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

    /**
     *
     * @param method
     * @param clazz
     * @param instance
     * @param parentClazz
     * @param parent
     * @param urlPrefix
     * @param logPrefix
     * @param success
     * @return
     */
    protected boolean handleStringType(Method method, Class clazz, Object instance, Class parentClazz, Object parent, String urlPrefix, String logPrefix, boolean success) {
        String property = propertyFromPropertyReferenceGetter(method.getName());
        if (property != null) {
            if (!canHandleReference(clazz, instance, parentClazz, parent, method.getName(), property, urlPrefix, logPrefix)) {
                success = false;
            }
        }
        return success;
    }
    
    /**
     *
     * @param propertyReference
     * @return
     */
    protected String propertyFromPropertyReferenceGetter(String propertyReference) {
        for (String suffix: new String[]{"Ref", "Href"}) {
            if (propertyReference.endsWith(suffix)) {
                return propertyReference.replaceAll(suffix + "$", "").replaceFirst("get", "");
            }
        }
        return null;
    }
        
    /**
     *
     * @param clazz
     * @param propertyReference
     * @param property
     * @param parentClazz
     * @param parent
     * @return
     */
    protected EntityType getPropertyType(Class clazz, String propertyReference, String property, Class parentClazz, Object parent) {
        for (EntityType type: EntityType.values()) {
            if (property.endsWith(EntityCodeListClassMapping.getEntityClass(type).getSimpleName())) {
                return type;
            }
        }
        return new SpecialReferenceCases().handle(clazz, propertyReference, property, parentClazz, parent);
    }

    /**
     *
     * @param clazz
     * @param instance
     * @param parentClazz
     * @param parent
     * @param propertyReference
     * @param property
     * @param urlPrefix
     * @param logPrefix
     * @return
     */
    protected boolean canHandleReference(Class clazz, Object instance, Class parentClazz, Object parent, String propertyReference, String property, String urlPrefix, String logPrefix) {
        EntityType propertyType = getPropertyType(clazz, propertyReference, property, parentClazz, parent);
        if (propertyType == null) {
            Logger.getLogger(AbstractReferenceHandler.class.getName()).log(Level.WARNING, "{0}{1}: {2} -> UNKNOWN", new Object[]{logPrefix, clazz.getName(), propertyReference});
            return false;
        } else {
            handleReference(clazz, instance, propertyReference, urlPrefix + propertyType.value() + "/");
            Logger.getLogger(AbstractReferenceHandler.class.getName()).log(Level.FINE, "{0}{1}: {2} -> {3}", new Object[]{logPrefix, clazz.getName(), propertyReference, propertyType.name()});
            return true;
            
        }        
    }
    
    /**
     *
     * @param clazz
     * @param instance
     * @param propertyReference
     * @param refprefix
     */
    abstract protected void handleReference(Class clazz, Object instance, String propertyReference, String refprefix);
        
}
