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
package com.ceridwen.lcf.server.filters;

import com.ceridwen.lcf.model.enumerations.EntityTypes;
import com.ceridwen.lcf.model.referencing.AddReferenceHandler;
import com.ceridwen.lcf.model.referencing.RemoveReferenceHandler;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Ceridwen Limited
 */
@Provider
public class ReferenceHandlingFilter implements ContainerRequestFilter, ContainerResponseFilter {
    List<String> ReferencableHeaders = Arrays.asList("Location");

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String baseUrl = requestContext.getUriInfo().getBaseUri().toString() + EntityTypes.LCF_PREFIX;

        for (String header: ReferencableHeaders) {
            if (requestContext.getHeaders().containsKey(header)) {
                requestContext.getHeaders().get(header).replaceAll(s -> baseUrl + s);
            }        
        }
        
        if (requestContext.hasEntity()) {
            try {
                ObjectInputStream entityReader = new ObjectInputStream(requestContext.getEntityStream());
                new RemoveReferenceHandler().removeReferences(entityReader.readObject(), baseUrl);
                
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ReferenceHandlingFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        String baseUrl = requestContext.getUriInfo().getBaseUri().toString() + EntityTypes.LCF_PREFIX;
        new AddReferenceHandler().addReferences(responseContext.getEntity(), baseUrl);
        
        for (String header: ReferencableHeaders) {
            if (responseContext.getHeaders().containsKey(header)) {
                responseContext.getHeaders().get(header).replaceAll(s -> baseUrl + s);
            }        
        }
    }
    
}
