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
package com.ceridwen.lcf.server.frontend.servlet.resources;

import javax.servlet.http.HttpServletResponse;

import org.bic.ns.lcf.v1_0.Charge;
import org.bic.ns.lcf.v1_0.ClassScheme;
import org.bic.ns.lcf.v1_0.ClassTerm;
import org.bic.ns.lcf.v1_0.Contact;
import org.bic.ns.lcf.v1_0.Item;
import org.bic.ns.lcf.v1_0.Loan;
import org.bic.ns.lcf.v1_0.Location;
import org.bic.ns.lcf.v1_0.Manifestation;
import org.bic.ns.lcf.v1_0.Patron;
import org.bic.ns.lcf.v1_0.Payment;
import org.bic.ns.lcf.v1_0.Property;
import org.bic.ns.lcf.v1_0.Reservation;

import com.ceridwen.lcf.server.core.EntityTypes;

public class ServletResourceFactory {
	
	public static ServletResource<?> getServletResource(EntityTypes.Type type, String baseUrl, HttpServletResponse response) {
		switch (type) {
		case Charge: 
			return new ServletResource<>(EntityTypes.Type.Charge, baseUrl, response);
		case ClassTerm:
			return new ServletResource<>(EntityTypes.Type.ClassTerm, baseUrl, response);
		case ClassScheme:
			return new ServletResource<>(EntityTypes.Type.ClassScheme, baseUrl, response);
		case Contact:
			return new ServletResource<>(EntityTypes.Type.Contact, baseUrl, response);
		case Item:
			return new ServletResource<>(EntityTypes.Type.Item, baseUrl, response);
		case Loan:
			return new ServletResource<>(EntityTypes.Type.Loan, baseUrl, response);
		case Location:
			return new ServletResource<>(EntityTypes.Type.Location, baseUrl, response);
		case Manifestation:
			return new ServletResource<>(EntityTypes.Type.Manifestation, baseUrl, response);
		case Patron:
			return new ServletResource<>(EntityTypes.Type.Patron, baseUrl, response);
		case Payment:
			return new ServletResource<>(EntityTypes.Type.Payment, baseUrl, response);
		case Reservation:
			return new ServletResource<>(EntityTypes.Type.Reservation, baseUrl, response);
		default:
			return null;
		}
	}
}
