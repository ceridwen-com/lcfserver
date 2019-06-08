EntityResourceManagerInterface(Entity) ::= <<
package com.ceridwen.lcf.server.resources;

import com.ceridwen.lcf.model.authentication.AuthenticationToken;
import com.ceridwen.lcf.model.enumerations.CreationQualifier;
import com.ceridwen.lcf.model.enumerations.DirectUpdatePath;
import java.util.List;
import org.bic.ns.lcf.v1_0.<Entity>;
import org.bic.ns.lcf.v1_0.SelectionCriterion;

/**
 * Interface for handling <Entity> LCF Entity requests
 * Register specific implementation in 
 * META-INF/Servers/com.ceridwen.lcf.server.resources.<Entity>ResourceManagerInterface
 * 
 */
public interface <Entity>ResourceManagerInterface extends AbstractResourceManagerInterface\<<Entity>\> {
    /**
     *
     * @return  LCF <Entity> class 
     */
    @Override
    default public Class getEntityClass() {
        return <Entity>.class;
    }    

    /**
     * Handle request to create a new LCF <Entity>
     * 
     * @param authTokens    Authentication tokens submitted via REST request
     * @param parent        Parent LCF Entity if use REST operation of form https://server/lcf/1.0/entity/{id}/subentity (null if direct access)
     * @param entity        <Entity> being created
     * @param qualifiers    Any query paraemters passed during REST operation
     * @return              id of <Entity> created
     */
    @Override
    String Create(List\<AuthenticationToken\> authTokens, Object parent, <Entity> entity, List\<CreationQualifier\> qualifiers);

    /**
     * Handle request to retrieve an existing LCF <Entity>
     * 
     * @param authTokens    Authentication tokens submitted via REST request
     * @param identifier    Identifier of <Entity> being requested
     * @return              <Entity> for corresponding identifier, or null if no <Entity> exists for that identifier
     */
    @Override
    <Entity> Retrieve(List\<AuthenticationToken\> authTokens, String identifier);

    /**
     * Handle request to update an existing LCF <Entity>
     * 
     * @param authTokens    Authentication tokens submitted via REST request
     * @param identifier    Identifier of <Entity> being modified
     * @param entity        Updated data for entity
     * @return              <Entity> data as stored (may include server-side modifications), or null if no <Entity> exists for that identifier
     */
    @Override
    <Entity> Modify(List\<AuthenticationToken\> authTokens, String identifier, <Entity> entity);

    /**
     * Handle direct update to value of LCF <Entity> (e.g. Patron password)
     * 
     * @param authTokens    Authentication tokens submitted via REST request
     * @param identifier    Identifier of <Entity> being updated
     * @param path          path identifying value to update
     * @param value         value 
     * @return              true if successful, false if no <Entity> exists for that identifier
     */
    @Override
    boolean DirectValueUpdate(List\<AuthenticationToken\> authTokens, String identifier, DirectUpdatePath path, String value);

    /**
     * Handle deletion of existing LCF <Entity>
     * 
     * @param authTokens    Authentication tokens submitted via REST request
     * @param identifier    Identifier of <Entity> being deleted
     * @return             true if successful, false if no <Entity> exists for that identifier
     */
    @Override
    boolean Delete(List\<AuthenticationToken\> authTokens, String identifier);

    /**
     * Handle a request to list existing LCF <Entity>
     * 
     * @param authTokens    Authentication tokens submitted via REST request
     * @param parent        Parent LCF Entity if use REST operation of form https://server/lcf/1.0/entity/{id}/subentity (null if direct access)
     * @param startIndex    start index for returned values
     * @param count         maximum number of entities to return
     * @param selection     selection query parameters passed in REST request
     * @return              Result list
     */
    @Override
    QueryResults\<<Entity>\> Query(List\<AuthenticationToken\> authTokens, Object parent, int startIndex, int count, List\<SelectionCriterion\> selection);

}
>>
