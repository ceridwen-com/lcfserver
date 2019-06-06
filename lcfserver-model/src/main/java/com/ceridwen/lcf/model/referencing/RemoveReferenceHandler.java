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

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matthew.Dovey
 */
public class RemoveReferenceHandler extends AbstractReferenceHandler {
    public boolean removeReferences(Object entity, String urlPrefix) {
        if (entity != null) {
            return iterateProperties(entity.getClass(), entity, null, null, urlPrefix, "");
        } else {
            return true;
        }      
    }

    @Override
    protected void handleReference(Class clazz, Object instance, String propertyReference, String refprefix) {
    {
        try {
            if (instance != null) {
                Object result = clazz.getMethod(propertyReference).invoke(instance);
                if (result != null && result.getClass().equals(String.class)) {
                    String newresult;
                    String setter = propertyReference.replaceFirst("get", "set");
                    newresult = ((String)result).replaceFirst(refprefix, ""); 
                    clazz.getMethod(setter, String.class).invoke(instance, newresult);
                }        
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(RemoveReferenceHandler.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    }

    
}
