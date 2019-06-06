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
package com.ceridwen.lcf.server.resources;

import com.ceridwen.lcf.model.enumerations.CreationQualifier;
import com.ceridwen.lcf.model.enumerations.VirtualUpdatePath;
import com.ceridwen.lcf.model.authentication.AuthenticationToken;
import java.util.List;
import org.bic.ns.lcf.v1_0.SelectionCriterion;


public abstract interface AbstractResourceManagerInterface<E> {
        Class getEntityClass();
	String Create(List<AuthenticationToken> authTokens, Object parent, E entity, List<CreationQualifier> qualifiers);
	E Retrieve(List<AuthenticationToken> authTokens, String identifier);
	E Modify(List<AuthenticationToken> authTokens, String identifier, E entity);
        void UpdateValue(List<AuthenticationToken> authTokens, String identifier, VirtualUpdatePath path, String value);
	void Delete(List<AuthenticationToken> authTokens, String identifier);
	QueryResults<E> Query(List<AuthenticationToken> authTokens, Object parent, int startIndex, int count, List<SelectionCriterion> selection);
}
