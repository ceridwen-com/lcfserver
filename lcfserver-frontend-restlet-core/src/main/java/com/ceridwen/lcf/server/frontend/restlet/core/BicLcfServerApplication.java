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
package com.ceridwen.lcf.server.frontend.restlet.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import com.ceridwen.lcf.server.core.EntityTypes;
import com.ceridwen.lcf.server.core.config.ConfigurationLoader;
import com.ceridwen.lcf.server.core.integrity.Relationship;
import com.ceridwen.lcf.server.core.integrity.RelationshipFactory;
import com.ceridwen.lcf.server.frontend.restlet.core.errors.LcfStatusService;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.ChargeEditor;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.ChargeList;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.ClassSchemeEditor;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.ClassSchemeList;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.ClassTermEditor;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.ClassTermList;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.ContactEditor;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.ContactList;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.ItemEditor;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.ItemList;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.LoanEditor;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.LoanList;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.LocationEditor;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.LocationList;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.ManifestationEditor;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.ManifestationList;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.PatronEditor;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.PatronList;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.PaymentEditor;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.PaymentList;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.PropertyEditor;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.PropertyList;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.ReservationEditor;
import com.ceridwen.lcf.server.frontend.restlet.core.resources.ReservationList;

public class BicLcfServerApplication extends Application {
	
	public Map<EntityTypes.Type, Routes<?>> getRouteMapping() {
		Map<EntityTypes.Type, Routes<?>> map = new HashMap<>();

		map.put(EntityTypes.Type.Charge, new Routes<>(ChargeList.class, ChargeEditor.class) );
		map.put(EntityTypes.Type.ClassTerm, new Routes<>(ClassTermList.class, ClassTermEditor.class) );
		map.put(EntityTypes.Type.ClassScheme, new Routes<>(ClassSchemeList.class, ClassSchemeEditor.class) );
		map.put(EntityTypes.Type.Contact, new Routes<>(ContactList.class, ContactEditor.class) );
		map.put(EntityTypes.Type.Item, new Routes<>(ItemList.class, ItemEditor.class) );
		map.put(EntityTypes.Type.Loan, new Routes<>(LoanList.class, LoanEditor.class) );
		map.put(EntityTypes.Type.Location, new Routes<>(LocationList.class, LocationEditor.class) );
		map.put(EntityTypes.Type.Manifestation, new Routes<>(ManifestationList.class, ManifestationEditor.class) );
		map.put(EntityTypes.Type.Patron, new Routes<>(PatronList.class, PatronEditor.class) );
		map.put(EntityTypes.Type.Payment, new Routes<>(PaymentList.class, PaymentEditor.class) );
		map.put(EntityTypes.Type.Property, new Routes<>(PropertyList.class, PropertyEditor.class) );
		map.put(EntityTypes.Type.Reservation, new Routes<>(ReservationList.class, ReservationEditor.class) );
		
		return map;
	}
	
    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {
    	
    	this.setStatusService(new LcfStatusService());
    	
        // Create a router Restlet that routes each call to a new instance of HelloWorldResource.
        Router router = new Router(getContext());
        
        loadOptionalModulesConfig(router);
       
        ConfigurationLoader.getConfiguration(); // Force any startup config
       
        Map<EntityTypes.Type, Routes<?>> routeMap = this.getRouteMapping();

        for (EntityTypes.Type type: EntityTypes.Type.values()) {
        	// attach router for list\creation
        	router.attach(EntityTypes.LCF_PREFIX + "/" + type.getEntityTypeCodeValue(), routeMap.get(type).getList());
        	router.attach(EntityTypes.LCF_PREFIX + "/" + type.getEntityTypeCodeValue() + "/", routeMap.get(type).getList());
        	
        	// attach router for retrieval\modify\delete
        	router.attach(EntityTypes.LCF_PREFIX + "/" + type.getEntityTypeCodeValue() + "/{identifier}", routeMap.get(type).getEditor());
        	router.attach(EntityTypes.LCF_PREFIX + "/" + type.getEntityTypeCodeValue() + "/{identifier}/", routeMap.get(type).getEditor());
        	
        	// attach parent list\create routers
        	for (Relationship<?,?> rel: RelationshipFactory.getRelationshipsAsChild(type)) {
        		router.attach(EntityTypes.LCF_PREFIX + "/" + rel.getParentType().getEntityTypeCodeValue() + 
        					  "/{identifier}/" + type.getEntityTypeCodeValue(), routeMap.get(type).getList());
        		router.attach(EntityTypes.LCF_PREFIX + "/" + rel.getParentType().getEntityTypeCodeValue() + 
  					  "/{identifier}/" + type.getEntityTypeCodeValue() + "/", routeMap.get(type).getList());
        	}

        	for (Relationship<?,?> rel: RelationshipFactory.getRelationshipsAsParent(type)) {
        		router.attach(EntityTypes.LCF_PREFIX + "/" + rel.getChildType().getEntityTypeCodeValue() + 
        					  "/{identifier}/" + type.getEntityTypeCodeValue(), routeMap.get(type).getList());
        		router.attach(EntityTypes.LCF_PREFIX + "/" + rel.getChildType().getEntityTypeCodeValue() + 
  					  "/{identifier}/" + type.getEntityTypeCodeValue() + "/", routeMap.get(type).getList());
        	}

        }

        return router;
    }

	private void loadOptionalModulesConfig(Router router) {
		ServiceLoader<OptionalModuleConfiguration> bootstrapLoader = ServiceLoader.load(OptionalModuleConfiguration.class);
		
	   Iterator<OptionalModuleConfiguration> loaders = bootstrapLoader.iterator();
	
	   while (loaders.hasNext()) {
	    	try{
	    		OptionalModuleConfiguration bootstrap = loaders.next();
	        	bootstrap.initialise(this, router);
				System.out.println("Loaded restlet bootstrap: " + bootstrap.getClass().getName());

	        } catch ( LinkageError | ServiceConfigurationError | Exception e) {
	        	System.out.println(e.getMessage());
		    }
	    }
	}
}
