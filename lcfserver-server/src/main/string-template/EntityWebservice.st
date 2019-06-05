EntityWebservice(LCFPath, Entity, EntityPath, SubEntity, SelectionCriteria) ::= <<
package com.ceridwen.lcf.server.webservice;

import com.ceridwen.lcf.server.resources.<Entity>ResourceManagerInterface;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Parameter;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;
import org.bic.ns.lcf.v1_0.<Entity>;

public class <Entity>Webservice implements EntityInterface\<<Entity>\>
 {
    WebserviceHelper\<<Entity>\> helper = new WebserviceHelper\<\>(<Entity>.class, <Entity>ResourceManagerInterface.class);

    UriInfo uri;

    private final String identifier;
    private final String authorization;
    private final String lcfPatronCredential;
    
    <Entity>Webservice(String identifier, String authorization, String lcfPatronCredential, UriInfo uri) {
        this.authorization = authorization;
        this.lcfPatronCredential = lcfPatronCredential;
        this.identifier = identifier;
        this.uri = uri;
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @SecurityRequirement(name = "Terminal Authentication")
    @SecurityRequirement(name = "Patron Authentication")
    @ApiResponse(
        responseCode = "200",
        description = "Retrieved",
        content = @Content(schema = @Schema(implementation = <Entity>.class)))
    @Override
    public Response Retrieve(@Parameter(hidden=true) @HeaderParam("Authorization") String authorization, @Parameter(hidden=true) @HeaderParam("lcf-patron-credential") String lcfPatronCredential) {
        return helper.Retrieve(identifier, authorization, lcfPatronCredential, uri.getBaseUri().toString());
    }

    @PUT
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @SecurityRequirement(name = "Terminal Authentication")
    @SecurityRequirement(name = "Patron Authentication")
    @ApiResponse(
        responseCode = "200",
        description = "Modfied",
        content = @Content(schema = @Schema(implementation = <Entity>.class)))
    @Override
    public Response Modify(<Entity> data, @Parameter(hidden=true) @HeaderParam("Authorization") String authorization, @Parameter(hidden=true) @HeaderParam("lcf-patron-credential") String lcfPatronCredential) {
        return helper.Modify(identifier, data, authorization, lcfPatronCredential, uri.getBaseUri().toString());
    }

    @DELETE
    @SecurityRequirement(name = "Terminal Authentication")
    @SecurityRequirement(name = "Patron Authentication")
    @ApiResponse(
        responseCode = "200",
        description = "Deleted")
    @Override
    public Response Delete(@Parameter(hidden=true) @HeaderParam("Authorization") String authorization, @Parameter(hidden=true) @HeaderParam("lcf-patron-credential") String lcfPatronCredential) {
        return helper.Delete(identifier, authorization, lcfPatronCredential);
    }
<SubEntity: {subentity |


    @Path("<subentity.EntityPath>")
    public <subentity.Entity>ListWebservice Locate<subentity.Entity>(@Parameter(hidden=true) @HeaderParam("Authorization") String authorization, @Parameter(hidden=true) @HeaderParam("lcf-patron-credential") String lcfPatronCredential) {
        return new <subentity.Entity>ListWebservice(helper.Retrieve(identifier, authorization, lcfPatronCredential, uri.getBaseUri().toString()), uri);
    \}
}>}
>>
