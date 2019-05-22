/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.webservice;

import com.ceridwen.lcf.server.resources.AbstractResourceManagerInterface;
import java.lang.reflect.Field;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bic.ns.lcf.v1_0.Entity;
import org.bic.ns.lcf.v1_0.LcfEntityListResponse;

/**
 *
 * @author Matthew.Dovey
 */
public class WebserviceHelper<E> {
    
        AbstractResourceManagerInterface<E> rm = null;
    
        public WebserviceHelper(Class<? extends AbstractResourceManagerInterface<E>> clazz) {
            ServiceLoader<? extends AbstractResourceManagerInterface<E>> configurationLoader = ServiceLoader.load(clazz);
			   
            while (rm == null && configurationLoader.iterator().hasNext()) {
                try{
                    rm = configurationLoader.iterator().next();
                } catch ( LinkageError | ServiceConfigurationError | Exception e) {
                    rm = null;
                }
             }

            if (rm == null) {
                System.out.println("Unable to load backend");
            }

            System.out.println("Loaded backend: " + rm.getClass().getName());       
        }
        
    
	String Create(Object parent, E entity) {
            return rm.Create(parent, entity);
        }
        
	E Retrieve(String identifier) {
            return rm.Retrieve(identifier);            
        }
        
	E Modify(String identifier, E entity) {
            return rm.Modify(identifier, entity);
        }
                
	void Delete(String identifier) {
            rm.Delete(identifier);
        }

        LcfEntityListResponse Query(Object parent, int startIndex, int count) {
            LcfEntityListResponse response = new LcfEntityListResponse();

            List<E> results = rm.Query(parent, startIndex, startIndex);
            for (E e: results) {
                try {
                    Field field = e.getClass().getDeclaredField("identifier");
                    field.setAccessible(true);
                    String ref = (String)field.get(e);
                    Entity entity = new Entity();
                    entity.setHref(ref);
                    response.getEntity().add(entity);
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(WebserviceHelper.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            response.setTotalResults(results.size());
            response.setStartIndex(startIndex);
            response.setItemsPerPage(count);

            return response;        
        }

}
