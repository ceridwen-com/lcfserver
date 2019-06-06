/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.webservice;

import com.ceridwen.lcf.lcfserver.model.CreationQualifier;
import com.ceridwen.lcf.lcfserver.model.EntityTypes;
import com.ceridwen.lcf.lcfserver.model.AddReferenceHandler;
import com.ceridwen.lcf.lcfserver.model.RemoveReferenceHandler;
import com.ceridwen.lcf.lcfserver.model.VirtualUpdatePath;
import com.ceridwen.lcf.lcfserver.model.authentication.AbstractAuthenticationToken;
import com.ceridwen.lcf.lcfserver.model.authentication.AuthenticationCategory;
import com.ceridwen.lcf.lcfserver.model.authentication.BasicAuthenticationToken;
import com.ceridwen.lcf.lcfserver.model.exceptions.EXC04_UnableToProcessRequest;
import com.ceridwen.lcf.lcfserver.model.exceptions.EXC05_InvalidEntityReference;
import com.ceridwen.lcf.server.resources.AbstractResourceManagerInterface;
import com.ceridwen.lcf.server.resources.QueryResults;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
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
    public AbstractResourceManagerInterface<E> getResourceManager() {
        return rm;
    }
    
    Response Create(Object parent, E entity, List<CreationQualifier> qualifiers, Map<AuthenticationCategory, AbstractAuthenticationToken> tokens, String baseUri) {
        new RemoveReferenceHandler().removeReferences(entity, baseUri + EntityTypes.LCF_PREFIX + "/");
        
        String identifier = rm.Create(tokens, parent, entity, qualifiers);
        
        new AddReferenceHandler().addReferences(entity, baseUri + EntityTypes.LCF_PREFIX + "/");
        
        return Response.created(URI.create(baseUri + EntityTypes.LCF_PREFIX + "/" + EntityTypes.lookUpByClass(rm.getEntityClass()).getEntityTypeCodeValue() + "/" + identifier)).entity(entity).header("lcf-version", EntityTypes.getLCFSpecVersion()).build();
    }
    
    Response Retrieve(String identifier, Map<AuthenticationCategory, AbstractAuthenticationToken> tokens, String baseUri) {
        E entity = rm.Retrieve(tokens, identifier);
        
        if (entity == null) {
            throw new EXC05_InvalidEntityReference("Entity not found", "Entity not found", "", null);
        }
        
        new AddReferenceHandler().addReferences(entity, baseUri + EntityTypes.LCF_PREFIX + "/");
        
        return Response.ok(entity).header("lcf-version", EntityTypes.getLCFSpecVersion()).build();
    }
    
    Response Modify(String identifier, E entity, Map<AuthenticationCategory, AbstractAuthenticationToken> tokens, String baseUri) {
        new RemoveReferenceHandler().removeReferences(entity, baseUri + EntityTypes.LCF_PREFIX + "/");
        
        E modified = rm.Modify(tokens, identifier, entity);
        
        new AddReferenceHandler().addReferences(modified, baseUri + EntityTypes.LCF_PREFIX + "/");
        
        return Response.ok(modified).header("lcf-version", EntityTypes.getLCFSpecVersion()).build();        
    }
    
    Response Delete(String identifier, Map<AuthenticationCategory, AbstractAuthenticationToken> tokens) {
        rm.Delete(tokens, identifier);
        return Response.ok().header("lcf-version", EntityTypes.getLCFSpecVersion()).build();
    }
    
    Response Query(Object parent, int startIndex, int count, List<SelectionCriterion> selectionCriterion, Map<AuthenticationCategory, AbstractAuthenticationToken> tokens, String baseUri) {
        LcfEntityListResponse response = new LcfEntityListResponse();
        
        EntityTypes.Type et = EntityTypes.lookUpByClass(clazz);
        if (et != null) {
            response.setEntityType(et.getEntityTypeCode());
        }
        
        for (SelectionCriterion criterion : selectionCriterion) {
            response.getSelectionCriterion().add(criterion);            
        }
        
        QueryResults<E> queryResults = rm.Query(tokens, parent, startIndex, count, selectionCriterion);
        
        for (E e : queryResults.getResults()) {
            try {
                Field field = e.getClass().getDeclaredField("identifier");
                field.setAccessible(true);
                String ref = (String) field.get(e);
                Entity entity = new Entity();
                entity.setHref(ref);
                response.getEntity().add(entity);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(WebserviceHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        response.setTotalResults(queryResults.getTotalResults());
        response.setStartIndex(queryResults.getSkippedResults());
        response.setItemsPerPage(queryResults.getResults().size());
        
        new AddReferenceHandler().addReferences(response, baseUri + EntityTypes.LCF_PREFIX + "/");
        
        return Response.ok(response).header("lcf-version", EntityTypes.getLCFSpecVersion()).build();        
    }
    
    public Response updateVirtualValue(String identifier, VirtualUpdatePath path, String value, Map<AuthenticationCategory, AbstractAuthenticationToken> tokens, String baseUri) {
        
        this.Retrieve(identifier, tokens, baseUri);
        
        if (value != null) {
            rm.UpdateValue(tokens, identifier, path, value);
        } else {
            throw new EXC04_UnableToProcessRequest("Invalid value", "Value cannot be empty", "", null);
        }
        
        return Response.ok().header("lcf-version", EntityTypes.getLCFSpecVersion()).build();        
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
    
    public void addAuthenthicationTokens(Map<AuthenticationCategory, AbstractAuthenticationToken> tokens, AuthenticationCategory category, String rawvalue) {
        if (rawvalue != null && !rawvalue.isEmpty()) {
            if (rawvalue.toUpperCase().startsWith("BASIC ")) {
                BasicAuthenticationToken token = new BasicAuthenticationToken(category, rawvalue.replace("BASIC ", ""));
                tokens.put(token.getAuthenticationCategory(), token);
            }
        }
    }
    
    public void addCreationQualifier(List<CreationQualifier> qualifiers, CreationQualifier qualifier, String value) {
        if (value != null && !value.isEmpty()) {
            if (value.equalsIgnoreCase("y") || value.equalsIgnoreCase("1")) {
                qualifiers.add(qualifier);
            }
        }
    }
}
