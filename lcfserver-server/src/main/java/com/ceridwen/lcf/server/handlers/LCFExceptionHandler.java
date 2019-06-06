/*******************************************************************************
 * Copyright (c) 2016, Matthew J. Dovey (www.ceridwen.com).
 *   
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   
 *     http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *    
 *   
 * Contributors:
 *     Matthew J. Dovey (www.ceridwen.com) - initial API and implementation
 *
 *     
 *******************************************************************************/
package com.ceridwen.lcf.server.handlers;

import com.ceridwen.lcf.model.referencing.AddReferenceHandler;
import com.ceridwen.lcf.model.enumerations.EntityTypes;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.ceridwen.lcf.model.exceptions.EXC00_LCF_Exception;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import javax.ws.rs.core.Response.ResponseBuilder;

@Provider
public class LCFExceptionHandler implements ExceptionMapper<EXC00_LCF_Exception>{
    @Override
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT}, serializationDisable = {SerializationFeature.WRITE_DATES_AS_TIMESTAMPS})    
    public Response toResponse(final EXC00_LCF_Exception exception) {
	new AddReferenceHandler().addReferences(exception.getLcfException(), "<expandurl>");	// TODO need to check how data is marshalled
    	ResponseBuilder responseBuilder = Response.status(exception.getHTTPErrorCode());
        for (EXC00_LCF_Exception.CustomHeader customHeader: exception.getCustomHeaders()) {
            responseBuilder = responseBuilder.header(customHeader.header, customHeader.value);       
        }
        responseBuilder = responseBuilder.header("lcf-version", EntityTypes.getLCFSpecVersion());       
        return responseBuilder.entity(exception.getLcfException()).build(); //type(MediaType.APPLICATIOn_XML?
    }
}
