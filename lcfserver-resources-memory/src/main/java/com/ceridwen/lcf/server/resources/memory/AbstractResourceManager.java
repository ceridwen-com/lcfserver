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
package com.ceridwen.lcf.server.resources.memory;

import com.ceridwen.lcf.model.EntityCodeListClassMapping;
import com.ceridwen.lcf.server.resources.memory.database.Operation;
import com.ceridwen.lcf.server.resources.memory.database.Authenticator;
import com.ceridwen.lcf.server.resources.memory.database.Database;
import com.ceridwen.lcf.model.authentication.AuthenticationToken;
import com.ceridwen.lcf.model.enumerations.CreationQualifier;
import com.ceridwen.lcf.model.enumerations.DirectUpdatePath;
import com.ceridwen.lcf.model.exceptions.EXC04_UnableToProcessRequest;
import com.ceridwen.lcf.model.exceptions.EXC06_InvalidDataInElement;
import com.ceridwen.lcf.server.resources.AbstractResourceManagerInterface;
import com.ceridwen.lcf.server.resources.QueryResults;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.bic.ns.lcf.v1_0.EntityType;
import org.bic.ns.lcf.v1_0.LcfEntity;
import org.bic.ns.lcf.v1_0.SelectionCriterion;

/**
 *
 * @author Ceridwen Limited
 * @param <E>
 */
public abstract class AbstractResourceManager<E extends LcfEntity> implements AbstractResourceManagerInterface<E> {

    protected EntityType getType() {
        return EntityCodeListClassMapping.getEntityType(this.getEntityClass());
    }
    
    @Override
    public String Create(List<AuthenticationToken> authTokens, LcfEntity parent, E entity, List<CreationQualifier> qualifiers) {
        Authenticator.getAuthenticator().authenticate(getType(), Operation.CREATE, authTokens, parent);
        
        String test = entity.getIdentifier();
        if (test != null) {
            if (Database.getDatabase().read(getType(), test) != null) {
                throw new EXC06_InvalidDataInElement("Entity already exists with that identifier", "", "", null);
            }
        } else {
            entity.setIdentifier(UUID.randomUUID().toString());
        }
       
        Database.getDatabase().write(getType(), entity.getIdentifier(), entity);
        return entity.getIdentifier();
    }

    @Override
    public E Retrieve(List<AuthenticationToken> authTokens, String identifier) {
        E response = (E)Database.getDatabase().read(getType(), identifier);

        Authenticator.getAuthenticator().authenticate(getType(), Operation.READ, authTokens, response);
        
        return response;
    }

    @Override
    public E Modify(List<AuthenticationToken> authTokens, String identifier, E entity) {
        Object original = Database.getDatabase().read(getType(), identifier);

        if (original == null) {
            return null; // This will trigger a 404 not found error         
        }

        Authenticator.getAuthenticator().authenticate(getType(), Operation.WRITE, authTokens, original);
        
        if (entity.getIdentifier() != null && !identifier.equals(entity.getIdentifier())) {
            throw new EXC06_InvalidDataInElement("Change of identifier not permitted", "", "", null);
        } else {
            entity.setIdentifier(identifier);
        }
       
        return (E)Database.getDatabase().write(getType(), entity.getIdentifier(), entity);
    }

    @Override
    public boolean DirectValueUpdate(List<AuthenticationToken> authTokens, String identifier, DirectUpdatePath path, String value) {
        throw new EXC04_UnableToProcessRequest("Invalid request", "Request invalid for " + getType().value(), path.getPath(), null );
    }

    @Override
    public boolean Delete(List<AuthenticationToken> authTokens, String identifier) {
        Object original = Database.getDatabase().read(getType(), identifier);
 
        Authenticator.getAuthenticator().authenticate(getType(), Operation.DELETE, authTokens, original);
        
        return Database.getDatabase().delete(getType(), identifier);
    }

    @Override
    public QueryResults<E> Query(List<AuthenticationToken> authTokens, LcfEntity parent, int startIndex, int count, List<SelectionCriterion> selection) {
        Authenticator.getAuthenticator().authenticate(getType(), Operation.LIST, authTokens, parent);

        QueryResults queryResults = new QueryResults();
        Collection results = Database.getDatabase().list(getType());
       
        int counter = 0;
        queryResults.setTotalResults(results.size());
        queryResults.setSkippedResults(counter);
        
        for (Object o: results) {
            if (counter >= startIndex) {
                if (count == -1 || counter < startIndex + count) {
                    queryResults.getResults().add(o);
                }
            } else {
                queryResults.setSkippedResults(counter+1);
            }
            counter++;
        }
        
        return queryResults;
    }
    
    
}
