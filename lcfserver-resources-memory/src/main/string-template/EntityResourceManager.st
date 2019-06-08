EntityResourceManager(Entity) ::= <<
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

import com.ceridwen.lcf.model.enumerations.EntityTypes;
import com.ceridwen.lcf.model.enumerations.CreationQualifier;
import com.ceridwen.lcf.model.enumerations.DirectUpdatePath;
import com.ceridwen.lcf.model.authentication.AuthenticationToken;
import com.ceridwen.lcf.server.resources.<Entity>ResourceManagerInterface;
import com.ceridwen.lcf.server.resources.QueryResults;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bic.ns.lcf.v1_0.SelectionCriterion;
import org.bic.ns.lcf.v1_0.<Entity>;


public class <Entity>ResourceManager implements <Entity>ResourceManagerInterface {

    @Override
    public String Create(List\<AuthenticationToken\> authTokens, Object parent, <Entity> entity, List\<CreationQualifier\> qualifiers) {
        MemoryResourceManager.getMemoryResourceManager().put(EntityTypes.Type.<Entity>, null, parent, entity, qualifiers, authTokens);
        return entity.getIdentifier();
    }

    @Override
    public <Entity> Retrieve(List\<AuthenticationToken\> authTokens, String identifier) {
        return (<Entity>) MemoryResourceManager.getMemoryResourceManager().get(EntityTypes.Type.<Entity>, identifier, authTokens);
    }

    @Override
    public <Entity> Modify(List\<AuthenticationToken\> authTokens, String identifier, <Entity> entity) {
        return (<Entity>) MemoryResourceManager.getMemoryResourceManager().put(EntityTypes.Type.<Entity>, identifier, null, entity, new ArrayList\<\>(), authTokens);
    }

    @Override
    public boolean Delete(List\<AuthenticationToken\> authTokens, String identifier) {
        return MemoryResourceManager.getMemoryResourceManager().delete(EntityTypes.Type.<Entity>, identifier, authTokens);
    }

    @Override
    public QueryResults\<<Entity>\> Query(List\<AuthenticationToken\> authTokens, Object parent, int startIndex, int count, List\<SelectionCriterion\> selection) {
        return (QueryResults\<<Entity>\>)MemoryResourceManager.getMemoryResourceManager().list(EntityTypes.Type.<Entity>, parent, startIndex, count, selection, authTokens);
    }

    @Override
    public boolean DirectValueUpdate(List\<AuthenticationToken\> authTokens, String identifier, DirectUpdatePath path, String value) {
        return MemoryResourceManager.getMemoryResourceManager().DirectValueUpdate(EntityTypes.Type.<Entity>, identifier, path, value, authTokens);
    }

}
>>
