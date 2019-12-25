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
package com.ceridwen.lcf.server.handlers;

import com.ceridwen.lcf.model.LcfConstants;
import com.ceridwen.lcf.model.exceptions.EXC00_LCF_Exception;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import com.ceridwen.lcf.model.responses.LCFResponse;
import java.net.URI;

/**
 *
 * @author Ceridwen Limited
 */
@Provider
public class LCFResponseHandler implements ExceptionMapper<LCFResponse>{
    /**
     *
     * @param exception
     * @return
     */
    @Override
    public Response toResponse(final LCFResponse exception) {
    	Response.ResponseBuilder responseBuilder;
        if (exception.getHTTPStatus() == 201) {
            responseBuilder = Response.created(URI.create(LcfConstants.LCF_PREFIX + "/" + exception.getEntityType().value() + "/" + exception.getIdentifier()));
        } else {
            responseBuilder= Response.status(exception.getHTTPStatus());
        }
        
        for (EXC00_LCF_Exception.CustomHeader customHeader: exception.getCustomHeaders()) {
            responseBuilder = responseBuilder.header(customHeader.header, customHeader.value);       
        }

        return responseBuilder.entity(exception.getLCFResponse()).build();
    }
}