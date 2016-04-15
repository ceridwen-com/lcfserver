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

import java.io.File;

import org.bic.ns.lcf.v1_0.Location;
import org.bic.ns.lcf.v1_0.Manifestation;
import org.bic.ns.lcf.v1_0.Property;

import com.ceridwen.lcf.server.core.config.Configuration;
import com.ceridwen.lcf.server.core.integrity.ReferentialIntegrityFilter;
import com.ceridwen.lcf.server.core.persistence.EntitySourcesInterface;
import com.ceridwen.lcf.server.core.referencing.editor.BasicReferenceEditor;
import com.ceridwen.lcf.server.core.referencing.editor.ReferenceEditor;
import com.ceridwen.lcf.server.backend.hashmap.filter.CheckInOutFilter;
import com.ceridwen.lcf.server.backend.hashmap.filter.ClientGeneratedIdFilter;
import com.ceridwen.lcf.server.backend.hashmap.filter.DefaultDataLoaderFilter;
import com.ceridwen.lcf.server.backend.hashmap.filter.ItemReadOnlyFieldsFilter;
import com.ceridwen.lcf.server.backend.hashmap.filter.ManifestationReadOnlyFieldsFilter;
import com.ceridwen.lcf.server.backend.hashmap.filter.PatronReadOnlyFieldsFilter;
import com.ceridwen.lcf.server.backend.hashmap.filter.PersistentFilter;
import com.ceridwen.lcf.server.backend.hashmap.filter.ReadOnlyFilter;
import com.ceridwen.lcf.server.backend.hashmap.filter.ServerGeneratedIdFilter;

public class HashMapConfiguration implements Configuration {

	private EntitySourcesInterface datasources;
	
	public HashMapConfiguration() {
		datasources = 	   
						 new ReadOnlyFilter(Property.class)            		// Sets entity to be read only
				.filters(new ManifestationReadOnlyFieldsFilter()	            // Automatically updates patronsinholdqueue, iteminstock counts
				.filters(new ItemReadOnlyFieldsFilter()	               			// Automatically updates patronsinholdqueue counts
				.filters(new PatronReadOnlyFieldsFilter()	               		// Automatically updates loan, reservation, hold counts
				.filters(new CheckInOutFilter()                              			// generate LcfLoanResponses for Loan related requests
				.filters(new ClientGeneratedIdFilter(Location.class)     		// return error if id not set by client for Create (default is use client id if present else server generate)
				.filters(new ServerGeneratedIdFilter(Manifestation.class)    	// ignore client set id in Create (default is use client id if present else server generate)
				.filters(new DefaultDataLoaderFilter()				   			// load default data 
				.filters(new ReferentialIntegrityFilter()              			// maintain parent-child relations in database
 				.filters(new PersistentFilter(System.getProperty("java.io.tmpdir") + File.pathSeparatorChar + "database.xml")  // save to file
				.filters(new HashMapEntitySources()	                   			// use in memory database
				))))))))));                                               		// number of closing parenthesis need to match number of processes!		
	}

	@Override
	public ReferenceEditor getReferenceEditor() {
		return new BasicReferenceEditor();
	}

	@Override
	public EntitySourcesInterface getEntityDataSources() {
		return datasources;
	}

}
