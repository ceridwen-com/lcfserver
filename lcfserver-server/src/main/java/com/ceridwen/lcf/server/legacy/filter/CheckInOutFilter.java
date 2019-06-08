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
import com.ceridwen.lcf.model.enumerations.EntityTypes.Type;
import com.ceridwen.lcf.server.resources.QueryResults;
import com.ceridwen.lcf.server.legacy.EntitySourceInterface;
import com.ceridwen.lcf.server.legacy.EntitySourcesInterface;
import com.ceridwen.lcf.model.responses.LCFResponse_CheckIn;
import com.ceridwen.lcf.model.responses.LCFResponse_CheckOut;
import org.bic.ns.lcf.v1_0.LcfCheckInResponse;
import org.bic.ns.lcf.v1_0.LcfCheckOutResponse;
import org.bic.ns.lcf.v1_0.Loan;
import org.bic.ns.lcf.v1_0.LoanStatusCode;
import org.bic.ns.lcf.v1_0.MediaWarningFlag;
import org.bic.ns.lcf.v1_0.SecurityDesensitize;
import org.bic.ns.lcf.v1_0.SpecialAttention;

/**
 * 
 * Pipeline overlaying loan handling
 * 
 */

// FIXME need to add checkin\out logic
public class CheckInOutFilter implements EntitySourcesFilter {

    /**
     *
     * @param entitySources
     * @return
     */
    @Override
	public EntitySourcesInterface filters(EntitySourcesInterface entitySources) {
		return new EntitySourcesInterface() {

			@SuppressWarnings("unchecked")
			@Override
			public <T> EntitySourceInterface<T> getEntitySource(Type type, Class<T> clazz) {
				if (type == EntityTypes.Type.Loan) {
					return (EntitySourceInterface<T>)new LoanEntitySource((EntitySourceInterface<Loan>)entitySources.getEntitySource(type, clazz));						
				} else {
					return entitySources.getEntitySource(type, clazz);
				}					
			}
		};	 
	}

	class LoanEntitySource implements EntitySourceInterface<Loan> {
		private EntitySourceInterface<Loan> wrapped;

		public LoanEntitySource(EntitySourceInterface<Loan> wrapped) {
			this.wrapped = wrapped;
		}

		@Override
		public String Create(Object parent, Loan entity) {
			String id = wrapped.Create(parent, entity);
			CheckOut(entity);
			return id;
		}

		@Override
		public String Create(Loan entity) {
			String id = wrapped.Create(entity);
			CheckOut(entity);
			return id;
		}

		public void CheckOut(Loan data) {
			if (data.getLoanStatus().contains(LoanStatusCode.VALUE_1)) {
				LcfCheckOutResponse response = new LcfCheckOutResponse();
				response.setLoanRef(data.getIdentifier());
				response.setMediaWarning(MediaWarningFlag.VALUE_1);
				response.setSecurityDesensitize(SecurityDesensitize.VALUE_1);

				throw new LCFResponse_CheckOut(response);
			}
		}
		
		
		@Override
		public Loan Retrieve(String identifier) {
			return wrapped.Retrieve(identifier);
		}

		@Override
		public Loan Modify(String identifier, Loan entity) {
			Loan loan = wrapped.Modify(identifier, entity);
			CheckIn(loan);
			return loan;
		}

		public void CheckIn(Loan data) {
			if (!data.getLoanStatus().contains(LoanStatusCode.VALUE_1)) {
				LcfCheckInResponse response = new LcfCheckInResponse();
				response.setLoanRef(data.getIdentifier());
				response.setMediaWarning(MediaWarningFlag.VALUE_1);
				response.setReturnLocationRef("GA");
				response.setSpecialAttention(SpecialAttention.VALUE_1);
				response.setSpecialAttentionNote("No special attention");
				
				response.getChargeRef().addAll(data.getChargeRef());
				throw new LCFResponse_CheckIn(response);
			}
		}	

		
		@Override
		public void Delete(String identifier) {
			wrapped.Delete(identifier);			
		}

		@Override
		public QueryResults<Loan> Query(Object parent, int start, int max, String query) {
			return wrapped.Query(parent, start, max, query);
		}

		@Override
		public QueryResults<Loan> Query(String query, int start, int max) {
			return wrapped.Query(query, start, max);
		}
	}
		
}
