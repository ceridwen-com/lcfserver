EntityResourceManager(Entity) ::= <<
package com.ceridwen.lcf.server.resources.memory;

import com.ceridwen.lcf.lcfserver.model.EntityTypes;
import com.ceridwen.lcf.lcfserver.model.CreationQualifier;
import com.ceridwen.lcf.lcfserver.model.VirtualUpdatePath;
import com.ceridwen.lcf.lcfserver.model.authentication.AuthenticationCategory;
import com.ceridwen.lcf.lcfserver.model.authentication.AbstractAuthenticationToken;
import com.ceridwen.lcf.server.resources.<Entity>ResourceManagerInterface;
import com.ceridwen.lcf.server.resources.QueryResults;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bic.ns.lcf.v1_0.SelectionCriterion;
import org.bic.ns.lcf.v1_0.<Entity>;


public class <Entity>ResourceManager implements <Entity>ResourceManagerInterface {

    @Override
    public Class getEntityClass() {
        return <Entity>.class;
    }

    @Override
    public String Create(Map\<AuthenticationCategory, AbstractAuthenticationToken\> authTokens, Object parent, <Entity> entity, List\<CreationQualifier\> qualifiers) {
        if (entity.getIdentifier() == null || entity.getIdentifier().isBlank()) {
            entity.setIdentifier(UUID.randomUUID().toString());
        }
        MemoryResourceManager.getMemoryResourceManager().put(EntityTypes.Type.<Entity>, entity.getIdentifier(), parent, entity, qualifiers, authTokens);
        return entity.getIdentifier();
    }

    @Override
    public <Entity> Retrieve(Map\<AuthenticationCategory, AbstractAuthenticationToken\> authTokens, String identifier) {
        return (<Entity>) MemoryResourceManager.getMemoryResourceManager().get(EntityTypes.Type.<Entity>, identifier, authTokens);
    }

    @Override
    public <Entity> Modify(Map\<AuthenticationCategory, AbstractAuthenticationToken\> authTokens, String identifier, <Entity> entity) {
        return (<Entity>) MemoryResourceManager.getMemoryResourceManager().put(EntityTypes.Type.<Entity>, identifier, null, entity, new ArrayList\<\>(), authTokens);
    }

    @Override
    public void Delete(Map\<AuthenticationCategory, AbstractAuthenticationToken\> authTokens, String identifier) {
        MemoryResourceManager.getMemoryResourceManager().delete(EntityTypes.Type.<Entity>, identifier, authTokens);
    }

    @Override
    public QueryResults\<<Entity>\> Query(Map\<AuthenticationCategory, AbstractAuthenticationToken\> authTokens, Object parent, int startIndex, int count, List\<SelectionCriterion\> selection) {
        return (QueryResults\<<Entity>\>)MemoryResourceManager.getMemoryResourceManager().list(EntityTypes.Type.<Entity>, parent, startIndex, count, selection, authTokens);
    }

    @Override
    public void UpdateValue(Map\<AuthenticationCategory, AbstractAuthenticationToken\> authTokens, String identifier, VirtualUpdatePath path, String value) {
        MemoryResourceManager.getMemoryResourceManager().UpdateValue(EntityTypes.Type.<Entity>, identifier, path, value, authTokens);
    }

}
>>
