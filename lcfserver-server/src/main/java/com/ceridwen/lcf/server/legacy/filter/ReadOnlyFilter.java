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

/**
 *
 * @author Ceridwen Limited
 */
@SuppressWarnings("rawtypes")
public class ReadOnlyFilter {//implements EntitySourcesFilter {
//
//	private Class managedClass;
//
//    /**
//     *
//     * @param managedClass
//     */
//    public ReadOnlyFilter(Class managedClass) {
//		this.managedClass = managedClass;
//	}
//	
//    /**
//     *
//     * @param entitySources
//     * @return
//     */
//    @Override
//	public EntitySourcesInterface filters(EntitySourcesInterface entitySources) {
//		
//		return new EntitySourcesInterface() {
//			@SuppressWarnings({ "unchecked" })
//			@Override
//			public <T> EntitySourceInterface<T> getEntitySource(Type type, Class<T> clazz) {
//				if (clazz == managedClass) {
//					return (EntitySourceInterface<T>) new ReadOnlyManagedtEntitySource(entitySources.getEntitySource(type, clazz));
//				} else {
//					return entitySources.getEntitySource(type, clazz);
//				}
//			}
//		};
//	}
//
//	class ReadOnlyManagedtEntitySource<E> implements EntitySourceInterface<E> {
//		private EntitySourceInterface<E> wrapped;
//	
//		public ReadOnlyManagedtEntitySource(EntitySourceInterface<E> wrapped) {
//			this.wrapped = wrapped;
//		}
//	
//		@Override
//		public String Create(Object parent, E entity) {
//			throw new EXC04_UnableToProcessRequest(EntityTypes.lookUpByClass(entity.getClass()).getEntityTypeCodeValue() + " are read only", EntityTypes.lookUpByClass(entity.getClass()).getEntityTypeCodeValue() + " are read only", null, null);
//		}
//
//	
//		@Override
//		public String Create(E entity) {
//			throw new EXC04_UnableToProcessRequest(EntityTypes.lookUpByClass(entity.getClass()).getEntityTypeCodeValue() + " are read only", EntityTypes.lookUpByClass(entity.getClass()).getEntityTypeCodeValue() + " are read only", null, null);
//		}
//	
//		@Override
//		public E Retrieve(String identifier) {
//			return wrapped.Retrieve(identifier);
//		}
//	
//		@Override
//		public E Modify(String identifier, E entity) {
//			throw new EXC04_UnableToProcessRequest(EntityTypes.lookUpByClass(entity.getClass()).getEntityTypeCodeValue() + " are read only", EntityTypes.lookUpByClass(entity.getClass()).getEntityTypeCodeValue() + " are read only", null, null);
//		}
//	
//		@Override
//		public void Delete(String identifier) {
//			throw new EXC04_UnableToProcessRequest("Read only", "Read only", null, null);
//		}
//	
//		@Override
//		public QueryResults<E> Query(Object parent, int start, int max, String query) {
//			return wrapped.Query(parent, start, max, query);
//		}
//	
//		@Override
//		public QueryResults<E> Query(String query, int start, int max) {
//			return wrapped.Query(query, start, max);
//		}
//	}
}

