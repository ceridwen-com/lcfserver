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

import com.ceridwen.lcf.lcfserver.model.CreationQualifier;
import com.ceridwen.lcf.lcfserver.model.VirtualUpdatePath;
import com.ceridwen.lcf.lcfserver.model.authentication.AbstractAuthenticationToken;
import com.ceridwen.lcf.lcfserver.model.authentication.AuthenticationCategory;
import java.util.List;
import java.util.Map;
import org.bic.ns.lcf.v1_0.SelectionCriterion;


public abstract interface AbstractResourceManagerInterface<E> {
        Class getEntityClass();
	String Create(Map<AuthenticationCategory, AbstractAuthenticationToken> authTokens, Object parent, E entity, List<CreationQualifier> qualifiers);
	E Retrieve(Map<AuthenticationCategory, AbstractAuthenticationToken> authTokens, String identifier);
	E Modify(Map<AuthenticationCategory, AbstractAuthenticationToken> authTokens, String identifier, E entity);
        void UpdateValue(Map<AuthenticationCategory, AbstractAuthenticationToken> authTokens, String identifier, VirtualUpdatePath path, String value);
	void Delete(Map<AuthenticationCategory, AbstractAuthenticationToken> authTokens, String identifier);
	QueryResults<E> Query(Map<AuthenticationCategory, AbstractAuthenticationToken> authTokens, Object parent, int startIndex, int count, List<SelectionCriterion> selection);
}
