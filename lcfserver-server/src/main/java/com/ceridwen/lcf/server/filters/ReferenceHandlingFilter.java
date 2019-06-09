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

import com.ceridwen.lcf.model.Constants;
import com.ceridwen.lcf.model.enumerations.ReferenceableHTTPHeaders;
import com.ceridwen.lcf.model.referencing.AddReferenceHandler;
import com.ceridwen.lcf.model.referencing.RemoveReferenceHandler;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

/**
 *
 * @author Ceridwen Limited
 */
@Provider
public class ReferenceHandlingFilter implements ContainerRequestFilter, ReaderInterceptor, ContainerResponseFilter {
    @Context
    UriInfo uri;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String baseUrl = uri.getBaseUri().toString() + Constants.LCF_PREFIX + "/";

        for (ReferenceableHTTPHeaders header: ReferenceableHTTPHeaders.values()) {
            if (requestContext.getHeaders().containsKey(header.getParameter())) {
                requestContext.getHeaders().get(header.getParameter()).replaceAll(s -> s.replaceFirst(baseUrl, ""));
            }        
        }
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext ric) throws IOException, WebApplicationException {
        String baseUrl = uri.getBaseUri().toString() + Constants.LCF_PREFIX + "/";

        Object entity = ric.proceed();
        
        new RemoveReferenceHandler().removeReferences(entity, baseUrl);
        
        return entity;
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        String baseUrl = uri.getBaseUri().toString() + Constants.LCF_PREFIX + "/";
        
        new AddReferenceHandler().addReferences(responseContext.getEntity(), baseUrl);
        
        for (ReferenceableHTTPHeaders header: ReferenceableHTTPHeaders.values()) {
            if (requestContext.getHeaders().containsKey(header.getParameter())) {
                requestContext.getHeaders().get(header.getParameter()).replaceAll(s -> baseUrl + s);
            }        
        }
    }
}
