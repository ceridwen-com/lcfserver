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
package com.ceridwen.lcf.server.legacy.filter;

import com.ceridwen.lcf.model.enumerations.EntityTypes;
import com.ceridwen.lcf.model.enumerations.EntityTypes.Type;
import com.ceridwen.lcf.model.exceptions.EXC05_InvalidEntityReference;
import com.ceridwen.lcf.server.resources.QueryResults;
import com.ceridwen.lcf.server.legacy.EntitySourceInterface;
import com.ceridwen.lcf.server.legacy.EntitySourcesInterface;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Ceridwen Limited
 */
@SuppressWarnings("rawtypes")
public class ClientGeneratedIdFilter implements EntitySourcesFilter {

	private Class managedClass;

    /**
     *
     * @param managedClass
     */
    public ClientGeneratedIdFilter(Class managedClass) {
		this.managedClass = managedClass;
	}
	
    /**
     *
     * @param entitySources
     * @return
     */
    @Override
	public EntitySourcesInterface filters(EntitySourcesInterface entitySources) {
		
		return new EntitySourcesInterface() {
			@SuppressWarnings({ "unchecked" })
			@Override
			public <T> EntitySourceInterface<T> getEntitySource(Type type, Class<T> clazz) {
				if (clazz == managedClass) {
					return (EntitySourceInterface<T>) new IdManagedtEntitySource(entitySources.getEntitySource(type, clazz));
				} else {
					return entitySources.getEntitySource(type, clazz);
				}
			}
		};
	}

	class IdManagedtEntitySource<E> implements EntitySourceInterface<E> {
		private EntitySourceInterface<E> wrapped;
	
		public IdManagedtEntitySource(EntitySourceInterface<E> wrapped) {
			this.wrapped = wrapped;
		}

		private void checkId(E entity) {
			EntityTypes.Type type = EntityTypes.lookUpByClass(entity.getClass());
			if (type == null) {
				// Something has gone very wrong if we get here!
				throw new EXC05_InvalidEntityReference("Entity type " + entity.getClass().getName() + " unknown", "Entity type " + entity.getClass().getName() + " unknown", null, null);
			}
			String id = type.getIdentifier(entity);
			if (StringUtils.isEmpty(id)) {
				throw new EXC05_InvalidEntityReference("Entity type " + type.getEntityTypeCodeValue() + " requires client generated id", "Entity type " + type.getEntityTypeCodeValue() + " requires client generated id", null, null);				
			}
			type.setIdentifier(entity, id);
		}
	
		@Override
		public String Create(Object parent, E entity) {
			checkId(entity);
			return wrapped.Create(parent, entity);
		}

	
		@Override
		public String Create(E entity) {
			checkId(entity);
			return wrapped.Create(entity);
		}
	
		@Override
		public E Retrieve(String identifier) {
			return wrapped.Retrieve(identifier);
		}
	
		@Override
		public E Modify(String identifier, E entity) {
			return wrapped.Modify(identifier, entity);
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
