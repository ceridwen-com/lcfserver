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

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.ceridwen.lcf.server.responses.LCFResponse;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;

@Provider
public class LCFResponseHandler implements ExceptionMapper<LCFResponse>{
    @Override
    @JacksonFeatures(serializationEnable = {SerializationFeature.INDENT_OUTPUT}, serializationDisable = {SerializationFeature.WRITE_DATES_AS_TIMESTAMPS})
    public Response toResponse(final LCFResponse exception) {
        // TODO need to ensure ids in LCFResponse are referenced
        // TODO need to check how data is marshalled
        return Response.status(exception.getHTTPStatus()).entity(exception.getLCFResponse()).build(); //type(MediaType.APPLICATIOn_XML?
    }
}
