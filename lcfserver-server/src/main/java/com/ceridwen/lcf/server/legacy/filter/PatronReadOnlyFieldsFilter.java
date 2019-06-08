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


import org.bic.ns.lcf.v1_0.Charge;
import org.bic.ns.lcf.v1_0.ChargeStatus;
import org.bic.ns.lcf.v1_0.Loan;
import org.bic.ns.lcf.v1_0.LoanStatusCode;
import org.bic.ns.lcf.v1_0.Patron;
import org.bic.ns.lcf.v1_0.Reservation;

import com.ceridwen.lcf.server.resources.QueryResults;
import com.ceridwen.lcf.model.enumerations.EntityTypes;
import com.ceridwen.lcf.server.legacy.EntitySourcesInterface;

/**
 *
 * @author Ceridwen Limited
 */
public class PatronReadOnlyFieldsFilter extends AbstractReadOnlyFieldsFilter<Patron> {

	@Override
	Class<Patron> getHandledClass() {
		return Patron.class;
	}

	@Override
	Patron updateReadOnlyFields(EntitySourcesInterface entitySources, Patron entity) {
		if (entity != null) {
			int onloan = 0;
			int overdue = 0;
			int recalled = 0;
			int fees = 0;
			int fines = 0;
			int available = 0;
			int unavailable = 0;
			
			QueryResults<Loan> loans = entitySources.getEntitySource(EntityTypes.Type.Loan, Loan.class).Query(entity, 0, Integer.MAX_VALUE, null);
			for (Loan l: loans.getResults()) {
				for (LoanStatusCode lsc: l.getLoanStatus()) {
					switch (lsc) {
						case VALUE_1: // On loan to patron
							onloan++;
							break;
						case VALUE_2: // Overdue from patron
							overdue++;
							break;
						case VALUE_3: // Recalled
							recalled++;
							break;
						case VALUE_4: // Charge owed
							break;
						case VALUE_5: // In transit between library locations
							break;
						case VALUE_6: // Claimed returned by patron
							break;
						case VALUE_7: // Lost
							break;
						case VALUE_8: // Checked in
							break;
						case VALUE_9: // Superseded by renewal loan
							break;
						case VALUE_10:// Cancelled 
							break;
						case VALUE_11:// Renewal loan
							break;
						default:
							break;
					}
				}
			}

			QueryResults<Charge> charges = entitySources.getEntitySource(EntityTypes.Type.Charge, Charge.class).Query(entity, 0, Integer.MAX_VALUE, null);
			for (Charge c: charges.getResults()) {
				if (c.getChargeStatus() != ChargeStatus.VALUE_3) { // what is 4?
					switch (c.getChargeType()) {
						case VALUE_1: // Other Unknown
							fees++;
							break;
						case VALUE_2: // Administrative
							fees++;
							break;
						case VALUE_3: // Damage
							fines++;
							break;
						case VALUE_4: // Overdue
							fines++;
							break;
						case VALUE_5: // Processing
							fees++;
							break;
						case VALUE_6: // Rental
							fees++;
							break;
						case VALUE_7: // Replacement
							fines++;
							break;
						case VALUE_8: // Computer access charge
							fees++;
							break;
						case VALUE_9: // Reservation fee
							fees++;
							break;
						case VALUE_10: // Aggregate
							fees++;
							break;
//							case VALUE_11: // Membership fee
//								fees++;
//								break;
//							case VALUE_12: // Notice fee
//								fees++;
//								break;
//							case VALUE_13: // Debt collection referral fee
//								fees++;
//								break;
						default:
							break;
					}
				}
			}

			QueryResults<Reservation> reservations = entitySources.getEntitySource(EntityTypes.Type.Reservation, Reservation.class).Query(entity, 0, Integer.MAX_VALUE, null);
			for (Reservation r: reservations.getResults()) {
				switch (r.getReservationStatus()) {
					case VALUE_1: // Item available
						available++;
						break;
					case VALUE_2: // Item unavailable
						unavailable++;
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
			
			entity.setAvailableHoldItems(available);
			entity.setFeesDueItems(fees);
			entity.setFinesDueItems(fines);
			entity.setOnLoanItems(onloan);
			entity.setOverdueItems(overdue);
			entity.setRecalledItems(recalled);
			entity.setUnavailableHoldItems(unavailable);
		}
		return entity;
	}
}
