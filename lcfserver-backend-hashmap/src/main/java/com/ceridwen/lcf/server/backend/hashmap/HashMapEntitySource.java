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
package com.ceridwen.lcf.server.backend.hashmap;

import java.util.Enumeration;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;

import com.ceridwen.lcf.server.core.EntityTypes;
import com.ceridwen.lcf.server.core.QueryResults;
import com.ceridwen.lcf.server.core.exceptions.EXC01_ServiceUnavailable;
import com.ceridwen.lcf.server.core.exceptions.EXC05_InvalidEntityReference;
import com.ceridwen.lcf.server.core.persistence.EntitySourceInterface;


public class HashMapEntitySource<E> implements EntitySourceInterface<E> {
	
	private ConcurrentHashMap<String, E> datasource;

	public HashMapEntitySource(ConcurrentHashMap<String,E> datasource) {
		this.datasource = datasource;
	}

	@Override
	public String Create(Object parent, E entity) {
		return this.Create(entity);
	}

	@Override
	public String Create(E entity) {
		EntityTypes.Type type = EntityTypes.lookUpByClass(entity.getClass());
		if (type == null) {
			// Something has gone very wrong if we get here!
			throw new EXC05_InvalidEntityReference("Entity type " + entity.getClass().getName() + " unknown", "Entity type " + entity.getClass().getName() + " unknown", null, null);
		}
		String id = type.getIdentifier(entity);
		if (StringUtils.isEmpty(id)) {
			id = UUID.randomUUID().toString();			
		}
		if (this.Retrieve(id) != null) {
			throw new EXC05_InvalidEntityReference(type.getEntityTypeCodeValue() + "/" + id + " already exists", type.getEntityTypeCodeValue() + "/" + id + " already exists", null, null);
		}		
		type.setIdentifier(entity, id);
		if (this.datasource.putIfAbsent(id, entity) != null) {
			throw new EXC01_ServiceUnavailable("UUID clash!", "UUID clash!", null, null);
		}
		return id;
	}

	@Override
	public E Retrieve(String identifier) {
		return this.datasource.get(identifier);
	}

	@Override
	public E Modify(String identifier, E entity) {
		EntityTypes.Type type = EntityTypes.lookUpByClass(entity.getClass());
		if (type == null) {
			// Something has gone very wrong if we get here!
			throw new EXC05_InvalidEntityReference("Entity type " + entity.getClass().getName() + " unknown", "Entity type " + entity.getClass().getName() + " unknown", null, null);
		}
		type.setIdentifier(entity, identifier);
		this.datasource.replace(identifier, entity);
		return this.datasource.get(identifier);
	}

	@Override
	public void Delete(String identifier) {
		this.datasource.remove(identifier);
	}

	@Override
	public QueryResults<E> Query(Object parent, int start, int max, String query) {
		// This class does not know about referential relationships
		// Could return nothing or everything (configured here)
		// If referential wrapper is used this method will never be called
				
		QueryResults<E> results = new QueryResults<>();
		results.setTotalResults(0);
		results.setSkippedResults(0);
		results.setResults(new Vector<E>());

		return this.Query(query, start, max);
//			return results;
	}

	@Override
	public QueryResults<E> Query(String query, int start, int max) {
		
		QueryResults<E> results = new QueryResults<>();
		results.setTotalResults(this.datasource.size());
		
		results.setResults(new Vector<E>());
		
		Enumeration<E> values = this.datasource.elements();
		
		int actualStart = 0;
		int startCounter = start;
		int maxCounter = max;
		
		while (startCounter > 0 && values.hasMoreElements()) {
			startCounter--;
			actualStart++;
			values.nextElement();
		}

		while (maxCounter > 0 && values.hasMoreElements()) {
			maxCounter--;
			results.getResults().add(values.nextElement());
		}
		
		results.setSkippedResults(actualStart);
		
		return results;
	}

}
