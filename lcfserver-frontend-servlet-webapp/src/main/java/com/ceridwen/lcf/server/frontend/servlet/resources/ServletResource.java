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
package com.ceridwen.lcf.server.frontend.servlet.resources;

import javax.servlet.http.HttpServletResponse;

import com.ceridwen.lcf.server.core.AbstractResourceHandler;
import com.ceridwen.lcf.server.core.EntityTypes;
import com.ceridwen.lcf.server.core.EntityTypes.Type;

public class ServletResource<E> extends AbstractResourceHandler<E> {
	
	String baseUrl;
	HttpServletResponse response;

	public ServletResource(Type entityType, String baseUrl, HttpServletResponse response) {
		super(entityType);
		this.baseUrl = baseUrl;
		this.response = response;
	}

	@Override
	public String getBaseUrl() {
		return this.baseUrl;
	}

	@Override
	public void setLocation(String suffixUri) {
		response.addHeader("Location", this.baseUrl + EntityTypes.LCF_PREFIX + "/" + suffixUri);
	}

	@Override
	public void setStatusCreated() {
		response.setStatus(201);
		
	}

}
