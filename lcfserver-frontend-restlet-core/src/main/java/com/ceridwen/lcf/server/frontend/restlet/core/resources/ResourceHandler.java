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
package com.ceridwen.lcf.server.frontend.restlet.core.resources;


import org.apache.commons.lang.StringUtils;
import org.bic.ns.lcf.v1_0.LcfEntityListResponse;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;

import com.ceridwen.lcf.server.core.AbstractResourceHandler;
import com.ceridwen.lcf.server.core.EntityTypes;
import com.ceridwen.util.collections.ListReverser;

/**
 * 
 * Main utility class for handling the REST operations defined in 
 * ResourceHandlerInterface
 * 
 * Uses wrapped approach rather than inheritence with Generics 
 * as this works with Eclipse compiler but generates runtime errors
 * with Oracle compiler
 * 
 */
public class ResourceHandler<E> extends AbstractResourceHandler<E> { 
	public static final String CONSUME_PRODUCES_TYPES = "xml|json:xml|json";

	ServerResource resource;
	
	public ResourceHandler(ServerResource resource, EntityTypes.Type entityType) {
		super(entityType);
		this.resource = resource;
	}

	@Override
	public String getBaseUrl() {
		return resource.getRootRef().toString();
	}

	@Override
	public void setLocation(String suffixUri) {		
		resource.setLocationRef(suffixUri);
	}

	@Override
	public void setStatusCreated() {
		resource.setStatus(Status.SUCCESS_CREATED);
	}
	

	/**
	 * Utility method for detemining parent type if url is /lcf/1.0/parent/id/child
	 */
	private String getParent(Reference ref) {
		Iterable<String> segments = new ListReverser<>(ref.getSegments());
		while(segments.iterator().hasNext() && !segments.iterator().next().equals(this.entityType.getEntityTypeCodeValue())) {}		
		if (segments.iterator().hasNext()) {
                    segments.iterator().next();
                }
		String parentCandidate = "";
		if (segments.iterator().hasNext()) {
                    parentCandidate = StringUtils.defaultIfEmpty(segments.iterator().next(),"");
                }
		return parentCandidate;
	}
	
	public E Create(E entity) {
		return this.Create(this.getParent(resource.getReference()), resource.getAttribute("identifier"), entity);
	}
	
	public E Retrieve() {
		return this.Retrieve(resource.getAttribute("identifier"));
	}
	
	public E Modify(E entity) {
		return this.Modify(resource.getAttribute("identifier"), entity);
	}
	
	public void Delete() {
		this.Delete(resource.getAttribute("identifier"));
	}
		
	public LcfEntityListResponse List() {
		int startIndex = 0;
		try { 
			startIndex = Integer.parseInt(resource.getQuery().getFirstValue("os:startIndex", "0"));
		} catch (NumberFormatException ex) {}
		int count = 0;
		try { 
			count = Integer.parseInt(resource.getQuery().getFirstValue("os:count", "0"));
		} catch (NumberFormatException ex) {}
		return this.List(this.getParent(resource.getReference()), resource.getAttribute("identifier"), startIndex, count);
	}

}
