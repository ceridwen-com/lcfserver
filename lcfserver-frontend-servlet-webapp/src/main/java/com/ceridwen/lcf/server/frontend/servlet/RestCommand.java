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
package com.ceridwen.lcf.server.frontend.servlet;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.ceridwen.lcf.server.core.EntityTypes;
import com.ceridwen.lcf.server.core.exceptions.EXC04_UnableToProcessRequest;
import com.ceridwen.lcf.server.frontend.servlet.resources.ServletResource;
import com.ceridwen.lcf.server.frontend.servlet.resources.ServletResourceFactory;

class RestCommand {
	private String id;
	private EntityTypes.Type parentType;
	private String parentId;
	@SuppressWarnings("rawtypes")
	private ServletResource resource;
	private int count;
	private int startIndex;

	public RestCommand(HttpServletRequest request, HttpServletResponse response, String baseUrl) {	
		
		Map<String, String> query = new HashMap<>();

		if (StringUtils.isNotEmpty(request.getQueryString())) {
	    	String[] params = request.getQueryString().split("&");  
	        for (String param : params)  
	        {  
	            String name = param.split("=")[0];  
	            String value = param.split("=")[1]; 
	            if (!query.containsKey(name)) {
	            	query.put(name, value);  
	            }
	        }  			
		}

		this.startIndex = 0;
		try { 
			startIndex = Integer.parseInt(query.get("os:startIndex"));
		} catch (NumberFormatException ex) {}
		this.count = 0;
		try { 
			count = Integer.parseInt(query.get("os:count"));
		} catch (NumberFormatException ex) {}

		
		StringTokenizer st = new StringTokenizer(request.getRequestURI(),"/");
    	while (!(st.nextToken().matches("lcf"))) {    		
    	}
		if (!st.hasMoreTokens()) {
			throw new EXC04_UnableToProcessRequest("Invalid URL: " + request.getRequestURI(), "Invalid URL: " + request.getRequestURI(), request.getRequestURI(), null);
		}
		if (!(st.nextToken().matches("1.0"))) {
			throw new EXC04_UnableToProcessRequest("Invalid URL: " + request.getRequestURI(), "Invalid URL: " + request.getRequestURI(), request.getRequestURI(), null);
		}
		if (!st.hasMoreTokens()) {
			throw new EXC04_UnableToProcessRequest("Invalid URL: " + request.getRequestURI(), "Invalid URL: " + request.getRequestURI(), request.getRequestURI(), null);
		}
		EntityTypes.Type type1 = EntityTypes.Type.Manifestation; //Force lookup table load!!
		type1 =	EntityTypes.lookUpByEntityTypeCodeValue(st.nextToken());
		if (type1 == null) {
			throw new EXC04_UnableToProcessRequest("71: Invalid URL: " + request.getRequestURI(), "Invalid URL: " + request.getRequestURI(), request.getRequestURI(), null);
		}
		if (!st.hasMoreTokens()) {
			this.resource = ServletResourceFactory.getServletResource(type1, baseUrl, response);
			return;
		}
		String id1 = st.nextToken();
		if (!st.hasMoreTokens()) {
			this.resource = ServletResourceFactory.getServletResource(type1, baseUrl, response);
			this.id = id1;
			return;
		}
		EntityTypes.Type type2 = EntityTypes.lookUpByEntityTypeCodeValue(st.nextToken());
		if (type2 == null) {
			throw new EXC04_UnableToProcessRequest("85: Invalid URL: " + request.getRequestURI(), "Invalid URL: " + request.getRequestURI(), request.getRequestURI(), null);
		}
		if (!st.hasMoreTokens()) {
			this.parentType = type1;
			this.parentId = id1;
			this.resource = ServletResourceFactory.getServletResource(type2, baseUrl, response);;
			return;
		}
		if (st.hasMoreTokens()) {
			throw new EXC04_UnableToProcessRequest("Invalid URL: " + request.getRequestURI(), "Invalid URL: " + request.getRequestURI(), request.getRequestURI(), null);
		}
		throw new EXC04_UnableToProcessRequest("Invalid URL: " + request.getRequestURI(), "Invalid URL: " + request.getRequestURI(), request.getRequestURI(), null);
	}
	
	public String getId() {
		return id;
	}
	public EntityTypes.Type getParentType() {
		return parentType;
	}
	public String getParentId() {
		return parentId;
	}
	@SuppressWarnings("rawtypes")
	public ServletResource getResource() {
		return resource;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public int getCount() {
		return count;
	}
	
}