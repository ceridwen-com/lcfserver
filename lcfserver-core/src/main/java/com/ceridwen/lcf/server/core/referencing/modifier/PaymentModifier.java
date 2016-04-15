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
package com.ceridwen.lcf.server.core.referencing.modifier;

import org.bic.ns.lcf.v1_0.Payment;

import com.ceridwen.lcf.server.core.EntityTypes;
import com.ceridwen.lcf.server.core.referencing.Referencer;
import com.ceridwen.lcf.server.core.referencing.editor.ReferenceEditor;

public class PaymentModifier extends Referencer<Payment> {

	public PaymentModifier(Payment entity, ReferenceEditor modifier) {
		super(entity, modifier);
	}

	@Override
	protected Payment updateReferences(Payment entity, final boolean addUrlPrefix) {
		entity.getChargeRef().replaceAll(getUpdateOperator(EntityTypes.Type.Charge, addUrlPrefix));
		entity.setPatronRef(update(entity.getPatronRef(), EntityTypes.Type.Patron, addUrlPrefix));
//		entity.getTransactionRef()
		
		return entity;
	}

	
	
}
