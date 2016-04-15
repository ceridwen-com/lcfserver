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
package com.ceridwen.lcf.server.frontend.restlet.modules;

import org.restlet.Application;
import org.restlet.ext.raml.RamlSpecificationRestlet;
import org.restlet.routing.Router;

import com.ceridwen.lcf.server.frontend.restlet.core.OptionalModuleConfiguration;

public class RamlModule implements OptionalModuleConfiguration {

	@Override
	public void initialise(Application application, Router router) {
		RamlSpecificationRestlet ramlSpecificationRestlet = new RamlSpecificationRestlet(application.getContext());
        
        ramlSpecificationRestlet.setApiInboundRoot(application);
        ramlSpecificationRestlet.setBasePath("http://lcf.ceridwen.com/restlet");
        ramlSpecificationRestlet.setAuthor("test");
        ramlSpecificationRestlet.setDescription("test");
        ramlSpecificationRestlet.setName("test");
        ramlSpecificationRestlet.setOwner("test");
        
        ramlSpecificationRestlet.attach(router, "/api-docs/raml");
	}
}
