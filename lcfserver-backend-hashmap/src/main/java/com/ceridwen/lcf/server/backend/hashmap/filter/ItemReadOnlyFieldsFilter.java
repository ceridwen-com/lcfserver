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
package com.ceridwen.lcf.server.backend.hashmap.filter;

import java.util.TreeSet;

import org.bic.ns.lcf.v1_0.Item;
import org.bic.ns.lcf.v1_0.Reservation;

import com.ceridwen.lcf.server.core.EntityTypes;
import com.ceridwen.lcf.server.core.QueryResults;
import com.ceridwen.lcf.server.core.persistence.EntitySourcesInterface;

public class ItemReadOnlyFieldsFilter extends AbstractReadOnlyFieldsFilter<Item> {

	@Override
	Class<Item> getHandledClass() {
		return Item.class;
	}

	@Override
	Item updateReadOnlyFields(EntitySourcesInterface entitySources, Item entity) {
		TreeSet<String> holds = new TreeSet<>();
		
		QueryResults<Reservation> reservations = entitySources.getEntitySource(EntityTypes.Type.Reservation, Reservation.class).Query(entity, 0, Integer.MAX_VALUE, null);
		for (Reservation r: reservations.getResults()) {
			switch (r.getReservationStatus()) {
				case VALUE_1: // Item available
					holds.add(r.getPatronRef());
					break;
				case VALUE_2: // Item unavailable
					holds.add(r.getPatronRef()); // TODO check?
					break;
				case VALUE_3: // Cancelled by patron
					break;
				case VALUE_4: // Cancelled by staff
					break;
				case VALUE_5: // Ended by check-out to patron
					break;
				case VALUE_6: // Expired
					break;
				default:
					break;
			}
		}
		
		entity.setPatronsInHoldQueue(holds.size());
		
		return entity;
	}

}
