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
package com.ceridwen.lcf.server.frontend.jaxrs;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.ceridwen.lcf.server.frontend.jaxrs.errors.LCFExceptionHandler;
import com.ceridwen.lcf.server.frontend.jaxrs.errors.LCFResponseHandler;
import com.ceridwen.lcf.server.frontend.jaxrs.resources.ChargeResource;
import com.ceridwen.lcf.server.frontend.jaxrs.resources.ClassSchemeResource;
import com.ceridwen.lcf.server.frontend.jaxrs.resources.ClassTermResource;
import com.ceridwen.lcf.server.frontend.jaxrs.resources.ContactResource;
import com.ceridwen.lcf.server.frontend.jaxrs.resources.ItemResource;
import com.ceridwen.lcf.server.frontend.jaxrs.resources.LoanResource;
import com.ceridwen.lcf.server.frontend.jaxrs.resources.LocationResource;
import com.ceridwen.lcf.server.frontend.jaxrs.resources.ManifestationResource;
import com.ceridwen.lcf.server.frontend.jaxrs.resources.PatronResource;
import com.ceridwen.lcf.server.frontend.jaxrs.resources.PaymentResource;
import com.ceridwen.lcf.server.frontend.jaxrs.resources.PropertyResource;
import com.ceridwen.lcf.server.frontend.jaxrs.resources.ReservationResource;

public class JaxRsServer extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		HashSet<Class<?>> classes = new HashSet<>();
		
		classes.add(ChargeResource.class);
		classes.add(ClassSchemeResource.class);
		classes.add(ClassTermResource.class);
		classes.add(ContactResource.class);
		classes.add(ItemResource.class);
		classes.add(LoanResource.class);
		classes.add(LocationResource.class);
		classes.add(ManifestationResource.class);
		classes.add(PatronResource.class);
		classes.add(PaymentResource.class);
		classes.add(PropertyResource.class);
		classes.add(ReservationResource.class);
		
		classes.add(LCFExceptionHandler.class);
		classes.add(LCFResponseHandler.class);
		
		return classes;
	}

}
