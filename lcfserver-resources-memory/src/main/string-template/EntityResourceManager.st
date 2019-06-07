EntityResourceManager(Entity) ::= <<
package com.ceridwen.lcf.server.resources.memory;

import com.ceridwen.lcf.model.enumerations.EntityTypes;
import com.ceridwen.lcf.model.enumerations.CreationQualifier;
import com.ceridwen.lcf.model.enumerations.VirtualUpdatePath;
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
    public Class getEntityClass() {
        return <Entity>.class;
    }

    @Override
    public String Create(List\<AuthenticationToken\> authTokens, Object parent, <Entity> entity, List\<CreationQualifier\> qualifiers) {
        if (entity.getIdentifier() == null || entity.getIdentifier().isBlank()) {
            entity.setIdentifier(UUID.randomUUID().toString());
        }
        MemoryResourceManager.getMemoryResourceManager().put(EntityTypes.Type.<Entity>, entity.getIdentifier(), parent, entity, qualifiers, authTokens);
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
    public void Delete(List\<AuthenticationToken\> authTokens, String identifier) {
        MemoryResourceManager.getMemoryResourceManager().delete(EntityTypes.Type.<Entity>, identifier, authTokens);
    }

    @Override
    public QueryResults\<<Entity>\> Query(List\<AuthenticationToken\> authTokens, Object parent, int startIndex, int count, List\<SelectionCriterion\> selection) {
        return (QueryResults\<<Entity>\>)MemoryResourceManager.getMemoryResourceManager().list(EntityTypes.Type.<Entity>, parent, startIndex, count, selection, authTokens);
    }

    @Override
    public void UpdateValue(List\<AuthenticationToken\> authTokens, String identifier, VirtualUpdatePath path, String value) {
        MemoryResourceManager.getMemoryResourceManager().UpdateValue(EntityTypes.Type.<Entity>, identifier, path, value, authTokens);
    }

}
>>