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

import com.ceridwen.lcf.model.Constants;
import com.ceridwen.lcf.model.exceptions.EXC00_LCF_Exception;
import com.ceridwen.lcf.model.exceptions.EXC01_ServiceUnavailable;
import java.net.URI;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Ceridwen Limited
 */
@Provider
public class LCFErrorHandler implements ExceptionMapper<Throwable>{

    @Override
    public Response toResponse(Throwable e) {
    	EXC00_LCF_Exception exception = new EXC01_ServiceUnavailable("Unknoen error", null, null, e);
        
        Response.ResponseBuilder responseBuilder;

        responseBuilder= Response.status(exception.getHTTPErrorCode());
        
        for (EXC00_LCF_Exception.CustomHeader customHeader: exception.getCustomHeaders()) {
            responseBuilder = responseBuilder.header(customHeader.header, customHeader.value);       
        }

        return responseBuilder.entity(exception.getLcfException()).build();    }
}
