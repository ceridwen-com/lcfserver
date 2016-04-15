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
package com.ceridwen.lcf.server.frontend.jaxrs.errors;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.ceridwen.lcf.server.core.exceptions.EXC00_LCF_Exception;

@Provider
public class LCFExceptionHandler implements ExceptionMapper<EXC00_LCF_Exception>{
    @Override
	public Response toResponse(final EXC00_LCF_Exception exception) {
		// TODO need to check how data is marshalled
    	return Response.status(exception.getHTTPErrorCode()).entity(exception.getLcfException()).build(); //type(MediaType.APPLICATIOn_XML?
    }
}
