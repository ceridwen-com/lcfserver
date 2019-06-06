EntityWebservice(LCFPath, Entity, EntityPath, SubEntity, SelectionCriteria, AlternativePosts, AlternativePuts, AlternativeGets, AlternativeDeletes, CreationQualifiers, VirtualUpdatePaths, AuthenticationSchemes) ::= <<
package com.ceridwen.lcf.server.webservice;

import com.ceridwen.lcf.model.enumerations.VirtualUpdatePath;
import com.ceridwen.lcf.model.authentication.AuthenticationToken;
import com.ceridwen.lcf.model.authentication.AuthenticationCategory;
import com.ceridwen.lcf.server.resources.<Entity>ResourceManagerInterface;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;
import org.bic.ns.lcf.v1_0.<Entity>;

public class <Entity>Webservice
 {
    WebserviceHelper\<<Entity>\> helper = new WebserviceHelper\<\>(<Entity>.class, <Entity>ResourceManagerInterface.class);

    UriInfo uri;

    private final String identifier;
    List\<AuthenticationToken\> tokens;
    
    <Entity>Webservice(String identifier, List\<AuthenticationToken\> tokens, UriInfo uri) {
        this.tokens = tokens;
        this.identifier = identifier;
        this.uri = uri;
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
<AuthenticationSchemes: {authenticationscheme |
    @SecurityRequirement(name = "<authenticationscheme.key>")}>
    @ApiResponse(
        responseCode = "200",
        description = "Retrieved",
        content = @Content(schema = @Schema(implementation = <Entity>.class)))
<AlternativeGets: {alternative |
    @ApiResponse(
        responseCode = "200",
        description = "<alternative>",
        content = @Content(schema = @Schema(implementation = <alternative>.class)))}>
    public Response Retrieve(
<AuthenticationSchemes: {authenticationscheme |
            @Parameter(hidden=true) @HeaderParam("<authenticationscheme.value>") String <authenticationscheme.key>,}>
            @Parameter(hidden=true) @HeaderParam("dummy") String dummy    
        ) {
        List\<AuthenticationToken\> tokens = new ArrayList\<\>();
        
<AuthenticationSchemes: {authenticationscheme |
        helper.addAuthenthicationTokens(tokens, AuthenticationCategory.<authenticationscheme.key>, <authenticationscheme.key>);}>

        return helper.Retrieve(identifier, tokens, uri.getBaseUri().toString());
    }

    @PUT
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
<AuthenticationSchemes: {authenticationscheme |
    @SecurityRequirement(name = "<authenticationscheme.key>")}>
    @ApiResponse(
        responseCode = "200",
        description = "Modified",
        content = @Content(schema = @Schema(implementation = <Entity>.class)))
<AlternativePuts: {alternative |
    @ApiResponse(
        responseCode = "200",
        description = "<alternative>",
        content = @Content(schema = @Schema(implementation = <alternative>.class)))}>
    public Response Modify(
<AuthenticationSchemes: {authenticationscheme |
            @Parameter(hidden=true) @HeaderParam("<authenticationscheme.value>") String <authenticationscheme.key>,}>
            <Entity> data
        ) {
        List\<AuthenticationToken\> tokens = new ArrayList\<\>();
        
<AuthenticationSchemes: {authenticationscheme |
        helper.addAuthenthicationTokens(tokens, AuthenticationCategory.<authenticationscheme.key>, <authenticationscheme.key>);}>

        return helper.Modify(identifier, data, tokens, uri.getBaseUri().toString());
    }

    @DELETE
<AuthenticationSchemes: {authenticationscheme |
    @SecurityRequirement(name = "<authenticationscheme.key>")}>
    @ApiResponse(
        responseCode = "200",
        description = "Deleted")
<AlternativeDeletes: {alternative |
    @ApiResponse(
        responseCode = "200",
        description = "<alternative>",
        content = @Content(schema = @Schema(implementation = <alternative>.class)))}>
    public Response Delete(
<AuthenticationSchemes: {authenticationscheme |
            @Parameter(hidden=true) @HeaderParam("<authenticationscheme.value>") String <authenticationscheme.key>,}>
            @Parameter(hidden=true) @HeaderParam("dummy") String dummy    
        ) {
        List\<AuthenticationToken\> tokens = new ArrayList\<\>();
        
<AuthenticationSchemes: {authenticationscheme |
        helper.addAuthenthicationTokens(tokens, AuthenticationCategory.<authenticationscheme.key>, <authenticationscheme.key>);}>
        
        return helper.Delete(identifier, tokens);
    }

<VirtualUpdatePaths: {path |


    @Path("<path.value>")
    @PUT
<AuthenticationSchemes: {authenticationscheme |
    @SecurityRequirement(name = "<authenticationscheme.key>")}>
    @ApiResponse(
        responseCode = "200",
        description = "Updated")
    public Response Update<path.key>(
<AuthenticationSchemes: {authenticationscheme |
            @Parameter(hidden=true) @HeaderParam("<authenticationscheme.value>") String <authenticationscheme.key>,}>
            String data
        ) {
        List\<AuthenticationToken\> tokens = new ArrayList\<\>();
        
<AuthenticationSchemes: {authenticationscheme |
        helper.addAuthenthicationTokens(tokens, AuthenticationCategory.<authenticationscheme.key>, <authenticationscheme.key>);}>

        return helper.updateVirtualValue(identifier, VirtualUpdatePath.<path.key>, data, tokens, uri.getBaseUri().toString());
    \}
}>

<SubEntity: {subentity |


    @Path("<subentity.EntityPath>")
<AuthenticationSchemes: {authenticationscheme |
    @SecurityRequirement(name = "<authenticationscheme.key>")}>
    public <subentity.Entity>ListWebservice Locate<subentity.Entity>
        (
<AuthenticationSchemes: {authenticationscheme |
            @Parameter(hidden=true) @HeaderParam("<authenticationscheme.value>") String <authenticationscheme.key>,}>
            @Parameter(hidden=true) @HeaderParam("dummy") String dummy    
        ) {
        List\<AuthenticationToken\> tokens = new ArrayList\<\>();
        
<AuthenticationSchemes: {authenticationscheme |
        helper.addAuthenthicationTokens(tokens, AuthenticationCategory.<authenticationscheme.key>, <authenticationscheme.key>);}>

        return new <subentity.Entity>ListWebservice(helper.Retrieve(identifier, tokens, uri.getBaseUri().toString()), uri);
    \}
}>}
>>
