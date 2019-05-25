/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.webservice;

import com.ceridwen.lcf.lcfserver.model.EntityTypes;
import com.ceridwen.lcf.lcfserver.model.authentication.AbstractAuthenticationToken;
import com.ceridwen.lcf.lcfserver.model.authentication.BasicAuthenticationToken;
import com.ceridwen.lcf.server.resources.AbstractResourceManagerInterface;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import org.bic.ns.lcf.v1_0.Entity;
import org.bic.ns.lcf.v1_0.LcfEntityListResponse;
import org.bic.ns.lcf.v1_0.SelectionCriteria;
import org.bic.ns.lcf.v1_0.SelectionCriterion;

/**
 *
 * @author Matthew.Dovey
 */
public class WebserviceHelper<E> {
    
        AbstractResourceManagerInterface<E> rm = null;
        Class<E> clazz;
    
        public WebserviceHelper(Class<E> clazz, Class<? extends AbstractResourceManagerInterface<E>> interfaze) {
            this.clazz = clazz;
            
            ServiceLoader<? extends AbstractResourceManagerInterface<E>> configurationLoader = ServiceLoader.load(interfaze);
			   
            while (rm == null && configurationLoader.iterator().hasNext()) {
                try{
                    rm = configurationLoader.iterator().next();
                } catch ( LinkageError | ServiceConfigurationError | Exception e) {
                    rm = null;
                }
             }

            if (rm == null) {
                System.out.println("Unable to load resource manager for " + clazz.getName() );
            } else {
                System.out.println("Loaded resource manager " + rm.getClass().getName() + " for " + clazz.getName() );       
            }
        }
        
        private Map<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken> getAuthenticationTokens(String authorization, String lcfPatronCredential) {
            Map<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken> authenticationTokens = new HashMap<>();
            
            if (authorization != null) {
                if (authorization.startsWith("BASIC ")) {
                    BasicAuthenticationToken token = new BasicAuthenticationToken(BasicAuthenticationToken.AuthenticationCategory.TERMINAL, authorization.replace("BASIC ", ""));
                    authenticationTokens.put(token.getAuthenticationCategory(), token);
                }
            }

            if (lcfPatronCredential != null) {
                if (lcfPatronCredential.startsWith("BASIC ")) {
                    BasicAuthenticationToken token = new BasicAuthenticationToken(BasicAuthenticationToken.AuthenticationCategory.USER, lcfPatronCredential.replace("BASIC ", ""));
                    authenticationTokens.put(token.getAuthenticationCategory(), token);
                }
            }
            
            return authenticationTokens;
        } 
    
	Response Create(Object parent, E entity, UriInfo uriInfo, String authorization, String lcfPatronCredential) {
            String identifier = rm.Create(getAuthenticationTokens(authorization, lcfPatronCredential), parent, entity);
        
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(identifier);
            return Response.created(builder.build()).entity(entity).build();
        }
                    
	E Retrieve(String identifier, String authorization, String lcfPatronCredential) {
            return rm.Retrieve(getAuthenticationTokens(authorization, lcfPatronCredential), identifier);            
        }
        
	E Modify(String identifier, E entity, String authorization, String lcfPatronCredential) {
            return rm.Modify(getAuthenticationTokens(authorization, lcfPatronCredential), identifier, entity);
        }
                
	void Delete(String identifier, String authorization, String lcfPatronCredential) {
            rm.Delete(getAuthenticationTokens(authorization, lcfPatronCredential), identifier);
        }

        LcfEntityListResponse Query(Object parent, int startIndex, int count, List<SelectionCriterion> selectionCriterion, String authorization, String lcfPatronCredential) {
            LcfEntityListResponse response = new LcfEntityListResponse();

            EntityTypes.Type et = EntityTypes.lookUpByClass(clazz);
            if (et != null) {
                response.setEntityType(et.getEntityTypeCode());
            }
            
            for (SelectionCriterion criterion: selectionCriterion) {
               response.getSelectionCriterion().add(criterion); 
            }
            
            List<E> results = rm.Query(getAuthenticationTokens(authorization, lcfPatronCredential), parent, startIndex, count, selectionCriterion);
            
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
        
    public void addSelectionCriterion(List<SelectionCriterion> selectionCriterion, String parameter, String variable) {
        try {
            if (variable != null && !variable.isEmpty()) {
                SelectionCriteria criteria = SelectionCriteria.fromValue(parameter);
                SelectionCriterion criterion = new SelectionCriterion();
                criterion.setCode(criteria);
                criterion.setValue(variable);
                selectionCriterion.add(criterion);
            }
        } catch (IllegalArgumentException ex) {
        }
    }
}
