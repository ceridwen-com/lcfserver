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

import org.bic.ns.lcf.v1_0.Charge;
import org.bic.ns.lcf.v1_0.LcfEntityListResponse;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.ceridwen.lcf.server.core.EntityTypes;

public class ChargeList extends ServerResource {

	ResourceHandler<Charge> handler;
	
	public ChargeList() {
		handler = new ResourceHandler<>(this, EntityTypes.Type.Charge);
	}

	@Post(ResourceHandler.CONSUME_PRODUCES_TYPES)
	public Charge Create(Charge data) {
		return handler.Create(data);
	}

	@Get(ResourceHandler.CONSUME_PRODUCES_TYPES)
	public LcfEntityListResponse List() {
		return handler.List();
	}

}
