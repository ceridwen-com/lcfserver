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
package com.ceridwen.lcf.server.core;

import java.util.Iterator;

import org.bic.ns.lcf.v1_0.Entity;
import org.bic.ns.lcf.v1_0.LcfEntityListResponse;

import com.ceridwen.lcf.server.core.config.ConfigurationLoader;
import com.ceridwen.lcf.server.core.exceptions.EXC05_InvalidEntityReference;
import com.ceridwen.lcf.server.core.persistence.EntitySourceInterface;
import com.ceridwen.lcf.server.core.referencing.Referencer;
import com.ceridwen.lcf.server.core.referencing.editor.ReferenceEditor;

public abstract class AbstractResourceHandler<E> {

	public EntityTypes.Type entityType;
	
	public AbstractResourceHandler(EntityTypes.Type entityType) {
		this.entityType = entityType;
	}
	
	public abstract String getBaseUrl();
	public abstract void setLocation(String suffixUri);
	public abstract void setStatusCreated();
	
	/**
	 * 
	 * Utility method for removing base url from href entries
	 * 
	 */
	@SuppressWarnings("unchecked")
	public <T>T dereference(T entity) {
		ReferenceEditor referenceEditor = ConfigurationLoader.getConfiguration().getReferenceEditor();
				
		if (referenceEditor != null) {
			referenceEditor.init(this.getBaseUrl() + EntityTypes.LCF_PREFIX + "/");
			
			return (T)Referencer.factory(entity, referenceEditor).dereference();
		} else {
			return entity;
		}
	}

	/**
	 * Utility method for adding base url to href entries
	 */
	@SuppressWarnings("unchecked")
	public <T>T reference(T entity) {
		ReferenceEditor referenceEditor = ConfigurationLoader.getConfiguration().getReferenceEditor();
		
		if (referenceEditor != null) {
			referenceEditor.init(this.getBaseUrl() + EntityTypes.LCF_PREFIX + "/");
			
			return (T)Referencer.factory(entity, referenceEditor).reference();
		} else {
			return entity;
		}
	}
	
	/**
	 * get the EntityDataSources
	 */
	@SuppressWarnings("unchecked")
	private EntitySourceInterface<E> getEntitySource(EntityTypes.Type entityType) {
		return (EntitySourceInterface<E>)ConfigurationLoader.getConfiguration()
				.getEntityDataSources().getEntitySource(entityType, entityType.getTypeClass());
	}

	public LcfEntityListResponse List(String parentcode, String identifier, int startIndex, int count) {
		EntityTypes.Type parentType = EntityTypes.lookUpByEntityTypeCodeValue(parentcode);
		Object parent = null;

		if (parentType != null) {
			parent = this.getEntitySource(parentType).Retrieve(identifier);
			if (parent == null) {
				throw new EXC05_InvalidEntityReference(parentType.getEntityTypeCodeValue() + "/" + identifier + " not found.",
						parentType.getEntityTypeCodeValue() + "/" + identifier + " not found.", null, null);
			}
		} 
		
		//FIXME check parent\child and child\parent relationship exists
		
		if (count == 0) {
			count = Integer.MAX_VALUE;
		}		
		
		QueryResults<E> results;
		
		if (parent == null) {
			results = this.getEntitySource(this.entityType).Query("", startIndex, count);
		} else {
			results = this.getEntitySource(this.entityType).Query(parent, startIndex, count, "");	
		}
		
		LcfEntityListResponse entitylist = new LcfEntityListResponse();
		entitylist.setEntityType(this.entityType.getEntityTypeCode());
		
		entitylist.setTotalResults(results.getTotalResults());
		
		int actualCount = 0;
		int actualStart = results.getSkippedResults();
		startIndex -= actualStart;
		
		Iterator<E> resultsIterator = results.getResults().iterator();
		
		while (startIndex > 0 && resultsIterator.hasNext()) {
			startIndex--;
			actualStart++;
			resultsIterator.next();
		}

		while (count > 0 && resultsIterator.hasNext()) {
			count--;
			actualCount++;
			Entity entity = new Entity();
			entity.setHref(this.entityType.getIdentifier(resultsIterator.next()));
			entitylist.getEntity().add(entity);
		}
		
		entitylist.setStartIndex(actualStart);
		entitylist.setItemsPerPage(actualCount);
				
		return this.reference(entitylist);
	}

	public E Create(String parentcode, String identifier, E entity) {
		EntityTypes.Type parentType = EntityTypes.lookUpByEntityTypeCodeValue(parentcode);
		
		Object parent = null;

		if (parentType != null) {
			parent = this.getEntitySource(parentType).Retrieve(identifier);
			if (parent == null) {
				throw new EXC05_InvalidEntityReference(parentType.getEntityTypeCodeValue() + "/" + identifier + " not found.",
						parentType.getEntityTypeCodeValue() + "/" + identifier + " not found.", null, null);
			}
		} 

		entity = this.dereference(entity);
		
		String newidentifier;
		
		if (parent == null) {
			newidentifier = this.getEntitySource(this.entityType).Create(entity);
		} else {
			newidentifier = this.getEntitySource(this.entityType).Create(parent, entity);			
		}
		
		entity = this.getEntitySource(this.entityType).Retrieve(newidentifier);

		this.setLocation(this.entityType.getEntityTypeCodeValue() + "/" + this.entityType.getIdentifier(entity));
		this.setStatusCreated();
						
		return this.reference(entity);
	}

	public E Retrieve(String identifier) {
		E entity = this.getEntitySource(this.entityType).Retrieve(identifier);
		if (entity == null) {
			throw new EXC05_InvalidEntityReference(this.entityType.getEntityTypeCodeValue() + "/" + identifier + " not found.",
						this.entityType.getEntityTypeCodeValue() + "/" + identifier + " not found.", null, null);
		}
		return this.reference(entity);
	}

	public E Modify(String identifier, E entity) {
		E current = this.getEntitySource(this.entityType).Retrieve(identifier);
		if (current == null) {
			throw new EXC05_InvalidEntityReference(this.entityType.getEntityTypeCodeValue() + "/" + identifier + " not found.",
						this.entityType.getEntityTypeCodeValue() + "/" + identifier + " not found.", null, null);
		}		
		entity = this.dereference(entity);
		entity = this.getEntitySource(this.entityType).Modify(identifier, entity);		
		return this.reference(entity);
	}

	public void Delete(String identifier) {
		E current = this.getEntitySource(this.entityType).Retrieve(identifier);
		if (current == null) {
			throw new EXC05_InvalidEntityReference(this.entityType.getEntityTypeCodeValue() + "/" + identifier + " not found.",
						this.entityType.getEntityTypeCodeValue() + "/" + identifier + " not found.", null, null);
		}
		this.getEntitySource(this.entityType).Delete(identifier);
		return;
	}
	
}
