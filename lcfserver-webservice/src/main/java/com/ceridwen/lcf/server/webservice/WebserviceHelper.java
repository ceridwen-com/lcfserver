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
package com.ceridwen.lcf.server.webservice;

import com.ceridwen.lcf.model.LcfConstants;
import com.ceridwen.lcf.model.EntityCodeListClassMapping;
import com.ceridwen.lcf.model.enumerations.CreationQualifier;
import com.ceridwen.lcf.model.enumerations.DirectUpdatePath;
import com.ceridwen.lcf.model.authentication.AuthenticationToken;
import com.ceridwen.lcf.model.authentication.AuthenticationCategory;
import com.ceridwen.lcf.model.authentication.BasicAuthenticationToken;
import com.ceridwen.lcf.model.exceptions.EXC04_UnableToProcessRequest;
import com.ceridwen.lcf.model.exceptions.EXC05_InvalidEntityReference;
import com.ceridwen.lcf.server.resources.AbstractResourceManagerInterface;
import com.ceridwen.lcf.server.resources.QueryResults;
import java.net.URI;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import org.bic.ns.lcf.v1_0.Entity;
import org.bic.ns.lcf.v1_0.EntityType;
import org.bic.ns.lcf.v1_0.LcfEntity;
import org.bic.ns.lcf.v1_0.LcfEntityListResponse;
import org.bic.ns.lcf.v1_0.SelectionCriteria;
import org.bic.ns.lcf.v1_0.SelectionCriterion;

/**
 *
 * @author Ceridwen Limited
 * @param <E>
 */
public class WebserviceHelper<E extends LcfEntity> {
    
    AbstractResourceManagerInterface<E> rm = null;
    Class<E> clazz;
    
    /**
     *
     * @param clazz
     * @param interfaze
     */
    public WebserviceHelper(Class<E> clazz, Class<? extends AbstractResourceManagerInterface<E>> interfaze) {
        this.clazz = clazz;
        
        ServiceLoader<? extends AbstractResourceManagerInterface<E>> configurationLoader = ServiceLoader.load(interfaze);
        
        while (rm == null && configurationLoader.iterator().hasNext()) {
            try {
                rm = configurationLoader.iterator().next();
            } catch (LinkageError | ServiceConfigurationError | Exception e) {
                rm = null;
            }
        }
        
        if (rm == null) {
            Logger.getLogger(WebserviceHelper.class.getName()).log(Level.FINE, clazz.getName() + ": Resource Manager not available");
        } else {
            Logger.getLogger(WebserviceHelper.class.getName()).log(Level.FINE, clazz.getName() + ": Resource Manager loaded");
        }
    }

//        public boolean hasResourceManager() {
//            return (rm != null);
//        }

    /**
     *
     * @return
     */
    public AbstractResourceManagerInterface<E> getResourceManager() {
        return rm;
    }
    
    Response Create(LcfEntity parent, E entity, List<CreationQualifier> qualifiers, List<AuthenticationToken> tokens) {
        nullifyEmptyIdentifier(entity);
        
        String identifier = rm.Create(tokens, parent, entity, qualifiers);
        
        return Response.created(URI.create(LcfConstants.LCF_PREFIX + "/" + EntityCodeListClassMapping.getEntityType(rm.getEntityClass()).value() + "/" + identifier)).entity(entity).build();
    }
    
    E Retrieve(String identifier, List<AuthenticationToken> tokens) {
        E entity = rm.Retrieve(tokens, identifier);
        
        if (entity == null) {
            throw new EXC05_InvalidEntityReference("Entity not found", "Entity not found", "", null);
        }
        
        return entity;
    }
    
    E Modify(String identifier, E entity, List<AuthenticationToken> tokens) {
        nullifyEmptyIdentifier(entity);
        
        E modified = rm.Modify(tokens, identifier, entity);

        if (modified == null) {
            throw new EXC05_InvalidEntityReference("Entity not found", "Entity not found", "", null);
        }
        
        return modified;
    }
    
    void Delete(String identifier, List<AuthenticationToken> tokens) {
        if (!rm.Delete(tokens, identifier)) {
             throw new EXC05_InvalidEntityReference("Entity not found", "Entity not found", "", null);
        }
    }
    
    LcfEntityListResponse Query(LcfEntity parent, int startIndex, int count, List<SelectionCriterion> selectionCriterion, List<AuthenticationToken> tokens) {
        LcfEntityListResponse response = new LcfEntityListResponse();
        
        EntityType et = EntityCodeListClassMapping.getEntityType(clazz);
        if (et != null) {
            response.setEntityType(et);
        }
        
        for (SelectionCriterion criterion : selectionCriterion) {
            response.getSelectionCriterion().add(criterion);            
        }
        
        QueryResults<E> queryResults = rm.Query(tokens, parent, startIndex, count, selectionCriterion);
        
        for (E e : queryResults.getResults()) {
            Entity entity = new Entity();
            entity.setHref(e.getIdentifier());
            response.getEntity().add(entity);
        }
        
        response.setTotalResults(queryResults.getTotalResults());
        response.setStartIndex(queryResults.getSkippedResults());
        response.setItemsPerPage(queryResults.getResults().size());
        
        return response;        
    }
    
    /**
     *
     * @param identifier
     * @param path
     * @param value
     * @param tokens
     * @param baseUri
     */
    public void directValueUpdate(String identifier, DirectUpdatePath path, String value, List<AuthenticationToken> tokens) {
        if (value != null) {
            if (!rm.DirectValueUpdate(tokens, identifier, path, value)) {
                 throw new EXC05_InvalidEntityReference("Entity not found", "Entity not found", "", null);
            }            
        } else {
            throw new EXC04_UnableToProcessRequest("Invalid value", "Value cannot be empty", "", null);
        }
    }
    
    /**
     *
     * @param selectionCriterion
     * @param parameter
     * @param variable
     */
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
    
    /**
     *
     * @param tokens
     * @param category
     * @param rawvalue
     */
    public void addAuthenthicationTokens(List<AuthenticationToken> tokens, AuthenticationCategory category, String rawvalue) {
        if (rawvalue != null && !rawvalue.isEmpty()) {
            if (rawvalue.toUpperCase().startsWith("BASIC ")) {
                BasicAuthenticationToken token = new BasicAuthenticationToken(category, rawvalue.replace("BASIC ", ""));
                tokens.add(token);
            }
        }
    }
    
    /**
     *
     * @param qualifiers
     * @param qualifier
     * @param value
     */
    public void addCreationQualifier(List<CreationQualifier> qualifiers, CreationQualifier qualifier, String value) {
        if (value != null && !value.isEmpty()) {
            if (value.equalsIgnoreCase("y") || value.equalsIgnoreCase("1")) {
                qualifiers.add(qualifier);
            }
        }
    }
    
    private void nullifyEmptyIdentifier(E entity) {
        String id = entity.getIdentifier();
        if (id != null && (id.isBlank() || id.isEmpty())) {
            entity.setIdentifier(null);
        }
    }
}
