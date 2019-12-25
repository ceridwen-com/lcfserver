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

import com.ceridwen.lcf.server.resources.memory.database.Authenticator;
import com.ceridwen.lcf.server.resources.memory.database.Database;
import com.ceridwen.lcf.model.authentication.AuthenticationCategory;
import com.ceridwen.lcf.model.authentication.AuthenticationToken;
import com.ceridwen.lcf.model.enumerations.DirectUpdatePath;
import com.ceridwen.lcf.server.resources.PatronResourceManagerInterface;
import java.util.List;
import org.bic.ns.lcf.v1_0.Patron;

/**
 *
 * @author Ceridwen Limited
 */
public class PatronResourceManager extends AbstractResourceManager<Patron> implements PatronResourceManagerInterface {

    @Override
    public boolean DirectValueUpdate(List<AuthenticationToken> authTokens, String identifier, DirectUpdatePath path, String value) {
        Patron patron = (Patron)Database.getDatabase().get(getType(), identifier);
        if (patron == null) {
            return false;
        }
        Authenticator.getAuthenticator().authenticate(AuthenticationCategory.USER, authTokens, patron);
        Authenticator.getAuthenticator().updatePassword(AuthenticationCategory.USER, patron.getBarcodeId(), value);
        return true;
    }
}
