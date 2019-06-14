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
package com.ceridwen.lcf.server.resources.memory;

import com.ceridwen.lcf.model.authentication.AuthenticationToken;
import com.ceridwen.lcf.server.resources.AuthorisationResourceManagerInterface;
import com.ceridwen.lcf.server.resources.QueryResults;
import com.ceridwen.lcf.server.resources.memory.database.Authenticator;
import java.util.List;
import org.bic.ns.lcf.v1_0.Authorisation;
import org.bic.ns.lcf.v1_0.Patron;
import org.bic.ns.lcf.v1_0.SelectionCriterion;

/**
 *
 * @author Ceridwen Limited
 */
public class AuthorisationResourceManager  extends AbstractResourceManager<Authorisation> implements AuthorisationResourceManagerInterface {

    @Override
    public QueryResults<Authorisation> Query(List<AuthenticationToken> authTokens, Object parent, int startIndex, int count, List<SelectionCriterion> selection) {
        if (parent instanceof Patron) {
            Authenticator.getAuthenticator().authenticate(((Patron)parent).getBarcodeId(), authTokens);            
        }

        return super.Query(authTokens, parent, startIndex, count, selection);
    }


 
    
}
