EntityWebservice(LCFPath, Entity, EntityPath, SubEntity) ::= <<
package com.ceridwen.lcf.server.webservice;

import com.ceridwen.lcf.server.resources.<Entity>ResourceManagerInterface;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.bic.ns.lcf.v1_0.<Entity>;

public class <Entity>Webservice implements EntityInterface\<<Entity>\>
 {
    WebserviceHelper\<<Entity>\> helper = new WebserviceHelper\<\>(<Entity>.class, <Entity>ResourceManagerInterface.class);

    private final String identifier;
    private final String authorization;
    private final String lcfPatronCredential;
    
    <Entity>Webservice(String identifier, String authorization, String lcfPatronCredential) {
        this.authorization = authorization;
        this.lcfPatronCredential = lcfPatronCredential;
        this.identifier = identifier;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Override
    public <Entity> Retrieve(@HeaderParam("Authorization") String authorization, @HeaderParam("lcf-patron-credential") String lcfPatronCredential) {
        return helper.Retrieve(identifier, authorization, lcfPatronCredential);
    }

    @PUT
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public <Entity> Modify(<Entity> data, @HeaderParam("Authorization") String authorization, @HeaderParam("lcf-patron-credential") String lcfPatronCredential) {
        return helper.Modify(identifier, data, authorization, lcfPatronCredential);
    }

    @DELETE
    @Override
    public void Delete(@HeaderParam("Authorization") String authorization, @HeaderParam("lcf-patron-credential") String lcfPatronCredential) {
        helper.Delete(identifier, authorization, lcfPatronCredential);
    }
<SubEntity: {subentity |


    @Path("<subentity.EntityPath>")
    public <subentity.Entity>ListWebservice Locate<subentity.Entity>(@HeaderParam("Authorization") String authorization, @HeaderParam("lcf-patron-credential") String lcfPatronCredential) {
        return new <subentity.Entity>ListWebservice(helper.Retrieve(identifier, authorization, lcfPatronCredential));
    \}
}>}
>>
