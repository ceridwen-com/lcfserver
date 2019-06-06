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
package com.ceridwen.lcf.server.legacy.filter;

import com.ceridwen.lcf.lcfserver.model.EntityTypes.Type;
import com.ceridwen.lcf.server.resources.QueryResults;
import com.ceridwen.lcf.server.legacy.EntitySourceInterface;
import com.ceridwen.lcf.server.legacy.EntitySourcesInterface;

public abstract class AbstractReadOnlyFieldsFilter<E> implements EntitySourcesFilter {
	
	EntitySourcesInterface entitySources;
	
	abstract Class<E> getHandledClass();
	abstract E updateReadOnlyFields(EntitySourcesInterface entitySources, E entity);
	
	@Override
	public EntitySourcesInterface filters(EntitySourcesInterface entitySources) {
		
		this.entitySources = entitySources;
		
		return new EntitySourcesInterface() {
			@SuppressWarnings({ "unchecked" })
			@Override
			public <T> EntitySourceInterface<T> getEntitySource(Type type, Class<T> clazz) {
				if (clazz == getHandledClass()) {
					return (EntitySourceInterface<T>) new ReadOnlyFieldsManagedtEntitySource(entitySources.getEntitySource(type, getHandledClass()));
				} else {
					return entitySources.getEntitySource(type, clazz);
				}
			}
		};
	}

	class ReadOnlyFieldsManagedtEntitySource implements EntitySourceInterface<E> {
		private EntitySourceInterface<E> wrapped;
	
		public ReadOnlyFieldsManagedtEntitySource(EntitySourceInterface<E> wrapped) {
			this.wrapped = wrapped;
		}
	

		@Override
		public String Create(Object parent, E entity) {
			return wrapped.Create(parent, entity);
		}

	
		@Override
		public String Create(E entity) {
			return wrapped.Create(entity);
		}

		@Override
		public E Retrieve(String identifier) {
			return updateReadOnlyFields(entitySources, wrapped.Retrieve(identifier));
		}
	
		@Override
		public E Modify(String identifier, E entity) {
			return updateReadOnlyFields(entitySources, wrapped.Modify(identifier, entity));
		}
	
		@Override
		public void Delete(String identifier) {
			wrapped.Delete(identifier);		
		}
	
		@Override
		public QueryResults<E> Query(Object parent, int start, int max, String query) {
			return wrapped.Query(parent, start, max, query);
		}
	
		@Override
		public QueryResults<E> Query(String query, int start, int max) {
			return wrapped.Query(query, start, max);
		}
	}
}
