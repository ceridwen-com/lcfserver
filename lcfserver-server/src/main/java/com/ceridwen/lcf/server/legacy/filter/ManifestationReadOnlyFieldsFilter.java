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
import com.ceridwen.lcf.server.resources.QueryResults;
import com.ceridwen.lcf.server.legacy.EntitySourcesInterface;
import java.util.TreeSet;

import org.bic.ns.lcf.v1_0.Item;
import org.bic.ns.lcf.v1_0.Manifestation;
import org.bic.ns.lcf.v1_0.Reservation;

/**
 *
 * @author Ceridwen Limited
 */
public class ManifestationReadOnlyFieldsFilter extends AbstractReadOnlyFieldsFilter<Manifestation> {
 
	@Override
	Class<Manifestation> getHandledClass() {
		return Manifestation.class;
	}

	@Override
	Manifestation updateReadOnlyFields(EntitySourcesInterface entitySources, Manifestation entity) {
		int stock = 0;
		QueryResults<Item> items = entitySources.getEntitySource(EntityTypes.Type.Item, Item.class).Query(entity, 0, Integer.MAX_VALUE, null);
		for (Item i: items.getResults()) {
			switch (i.getCirculationStatus()) {			
				case VALUE_1: //Other unknown
					break;
				case VALUE_2: // On order
					break;
				case VALUE_3: // Available
					stock++;
					break;
				case VALUE_4: // On loan
					stock++;
					break;
				case VALUE_5: // On loan - not to be recalled until earliest recal date
					stock++;
					break;
				case VALUE_6: // In process
					stock++;
					break;
				case VALUE_7: // Recalled
					stock++;
					break;
				case VALUE_8: // On hold shelf
					stock++;
					break;
				case VALUE_9: // To be reshelved
					stock++;
					break;
				case VALUE_10: // In transit
					stock++;
					break;
				case VALUE_11: // Claimed returned
					stock++;
					break;
				case VALUE_12: // Lost
					break;
				case VALUE_13: // Missing
					break;
//				case VALUE_14: // All copies withdrawn 
//					break
//				case VALUE_15: // Withdrawn from circulation for repair
//					break
//				case VALUE_16: // Withdrawn from circuation
//					break
				default:
					break;
			}
		}

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
				
		entity.setItemsInStock(stock);
		entity.setPatronsInHoldQueue(holds.size());
		
		return entity;
	}

}
