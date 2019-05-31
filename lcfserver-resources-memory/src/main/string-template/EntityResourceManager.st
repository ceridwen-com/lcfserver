EntityResourceManager(Entity) ::= <<
package com.ceridwen.lcf.server.resources.memory;

import com.ceridwen.lcf.lcfserver.model.EntityTypes;
import com.ceridwen.lcf.lcfserver.model.authentication.AbstractAuthenticationToken;
import com.ceridwen.lcf.server.resources.<Entity>ResourceManagerInterface;
import com.ceridwen.lcf.server.resources.QueryResults;
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
    public String Create(Map\<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken\> authTokens, Object parent, <Entity> entity) {
        if (entity.getIdentifier() == null || entity.getIdentifier().isBlank()) {
            entity.setIdentifier(UUID.randomUUID().toString());
        }
        MemoryResourceManager.getMemoryResourceManager().put(EntityTypes.Type.<Entity>, entity.getIdentifier(), entity);
        return entity.getIdentifier();
    }

    @Override
    public <Entity> Retrieve(Map\<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken\> authTokens, String identifier) {
        return (<Entity>) MemoryResourceManager.getMemoryResourceManager().get(EntityTypes.Type.<Entity>, identifier);
    }

    @Override
    public <Entity> Modify(Map\<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken\> authTokens, String identifier, <Entity> entity) {
        return (<Entity>) MemoryResourceManager.getMemoryResourceManager().put(EntityTypes.Type.<Entity>, identifier, entity);
    }

    @Override
    public void Delete(Map\<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken\> authTokens, String identifier) {
        MemoryResourceManager.getMemoryResourceManager().delete(EntityTypes.Type.<Entity>, identifier);
    }

    @Override
    public QueryResults\<<Entity>\> Query(Map\<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken\> authTokens, Object parent, int startIndex, int count, List\<SelectionCriterion\> selection) {
        return (QueryResults\<<Entity>\>)MemoryResourceManager.getMemoryResourceManager().list(EntityTypes.Type.<Entity>, startIndex, count);
    }
    
}
>>
