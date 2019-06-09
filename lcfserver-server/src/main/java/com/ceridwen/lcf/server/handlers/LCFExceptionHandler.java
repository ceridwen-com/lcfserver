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

import com.ceridwen.lcf.model.referencing.AddReferenceHandler;
import com.ceridwen.lcf.model.enumerations.EntityTypes;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.ceridwen.lcf.model.exceptions.EXC00_LCF_Exception;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Ceridwen Limited
 */
@Provider
public class LCFExceptionHandler implements ExceptionMapper<EXC00_LCF_Exception>{
    /**
     *
     * @param exception
     * @return
     */
    @Override
    public Response toResponse(final EXC00_LCF_Exception exception) {
    	ResponseBuilder responseBuilder = Response.status(exception.getHTTPErrorCode());
        for (EXC00_LCF_Exception.CustomHeader customHeader: exception.getCustomHeaders()) {
            responseBuilder = responseBuilder.header(customHeader.header, customHeader.value);       
        }
        return responseBuilder.entity(exception.getLcfException()).build(); //type(MediaType.APPLICATIOn_XML?
    }
}
