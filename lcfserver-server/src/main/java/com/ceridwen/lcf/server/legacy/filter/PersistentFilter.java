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

import com.ceridwen.lcf.lcfserver.model.EntityTypes;
import com.ceridwen.lcf.lcfserver.model.EntityTypes.Type;
import com.ceridwen.lcf.server.legacy.EntitySourceInterface;
import com.ceridwen.lcf.server.legacy.EntitySourcesInterface;
import com.ceridwen.lcf.server.resources.QueryResults;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;


import com.ceridwen.util.xml.XmlUtilities;

/**
 * 
 * Pipeline overlaying persistence to XML to MemoryEntitySources
 * 
 */
public class PersistentFilter implements EntitySourcesFilter {
	private String path;
	private EntitySourcesInterface sources;

	public PersistentFilter(String path) {
		this.path = path;
	}
	
	@Override
	public EntitySourcesInterface filters(EntitySourcesInterface entitySources) {
            synchronized(this) {
		
		sources = load(entitySources);
		
		return new EntitySourcesInterface() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public <T> EntitySourceInterface<T> getEntitySource(Type type, Class<T> clazz) {
				return (EntitySourceInterface<T>) new PersistentEntitySource(sources.getEntitySource(type, clazz));
			}
		};
            }
	}

	class PersistentEntitySource<E> implements EntitySourceInterface<E> {
		private EntitySourceInterface<E> wrapped;

		public PersistentEntitySource(EntitySourceInterface<E> wrapped) {
			this.wrapped = wrapped;
		}

		@Override
		public String Create(Object parent, E entity) {
			String s = wrapped.Create(parent, entity);
			save();
			return s;
		}

		@Override
		public String Create(E entity) {
			String s = wrapped.Create(entity);
			save();
			return s;
		}

		@Override
		public E Retrieve(String identifier) {
			return wrapped.Retrieve(identifier);
		}

		@Override
		public E Modify(String identifier, E entity) {
			E e = wrapped.Modify(identifier, entity);
			save();
			return e;
		}

		@Override
		public void Delete(String identifier) {
			wrapped.Delete(identifier);		
			save();
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

	EntitySourcesInterface load(EntitySourcesInterface def) {
            synchronized(this) {
		InputStream input;
		try {
			input = new FileInputStream(path);
			return (EntitySourcesInterface)XmlUtilities.processXML(input, def.getClass() );
		} catch (Exception e) {
			return def;
		}
            }
	}
	
	void save() {
            synchronized(this) {
		Writer output;
		try {
			output = new FileWriter(this.path);
			XmlUtilities.generateXML(output, sources);
		} catch (Exception e) {
			e.printStackTrace();
		}
            }
	}
	
}

