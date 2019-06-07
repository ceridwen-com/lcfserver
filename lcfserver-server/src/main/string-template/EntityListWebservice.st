EntityListWebservice(LCFPath, Entity, EntityPath, SubEntity, SelectionCriteria, AlternativePosts, AlternativePuts, AlternativeGets, AlternativeDeletes, CreationQualifiers, VirtualUpdatePaths, AuthenticationSchemes) ::= <<
package com.ceridwen.lcf.server.webservice;

import com.ceridwen.lcf.model.enumerations.CreationQualifier;
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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.bic.ns.lcf.v1_0.LcfEntityListResponse;
import org.bic.ns.lcf.v1_0.SelectionCriterion;
import org.bic.ns.lcf.v1_0.<Entity>;


public class <Entity>ListWebservice {
    
    private final Object parent;
    WebserviceHelper\<<Entity>\> helper = new WebserviceHelper\<\>(<Entity>.class, <Entity>ResourceManagerInterface.class);

    @Context
    UriInfo uri;

    public <Entity>ListWebservice() {
        this.parent = null;
    }
    
    public <Entity>ListWebservice(Object parent, UriInfo uri) {
        this.parent = parent;
        this.uri = uri;
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
<AuthenticationSchemes: {authenticationscheme |
    @SecurityRequirement(name = "<authenticationscheme.key>")}>
    @ApiResponse(
        responseCode = "200",
        description = "Retrieved",
        content = @Content(schema = @Schema(implementation = LcfEntityListResponse.class)))
    public Response List(  
<AuthenticationSchemes: {authenticationscheme |
            @Parameter(hidden=true) @HeaderParam("<authenticationscheme.value>") String <authenticationscheme.key>,}>
<SelectionCriteria: {criterion | 
                                        @QueryParam("<criterion.Parameter>") String <criterion.Variable>,}>
            @DefaultValue("0") @QueryParam("os:startIndex") int startIndex, 
            @DefaultValue("-1") @QueryParam("os:count") int count 
        ) {

        List\<AuthenticationToken\> tokens = new ArrayList\<\>();
        
<AuthenticationSchemes: {authenticationscheme |
        helper.addAuthenthicationTokens(tokens, AuthenticationCategory.<authenticationscheme.key>, <authenticationscheme.key>);}>

        List\<SelectionCriterion\> selectionCriterion = new ArrayList\<\>();

<SelectionCriteria: {criterion | 
        helper.addSelectionCriterion(selectionCriterion, "<criterion.Parameter>", <criterion.Variable>);}>

        return helper.Query(parent, startIndex, count, selectionCriterion, tokens, uri.getBaseUri().toString());
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
<AuthenticationSchemes: {authenticationscheme |
    @SecurityRequirement(name = "<authenticationscheme.key>")}>
    @ApiResponse(
        responseCode = "201",
        description = "Without content")    
    @ApiResponse(
        responseCode = "201",
        description = "With echoed content",
        content = @Content(schema = @Schema(implementation = <Entity>.class))) 
<AlternativePosts: {alternative |
    @ApiResponse(
        responseCode = "201",
        description = "<alternative>",
        content = @Content(schema = @Schema(implementation = <alternative>.class)))}>
    public Response Create(
<AuthenticationSchemes: {authenticationscheme |
            @Parameter(hidden=true) @HeaderParam("<authenticationscheme.value>") String <authenticationscheme.key>,}>
<CreationQualifiers: {qualifier |
            @HeaderParam("<qualifier.value>") String <qualifier.key>,}>
            <Entity> data
        ) {
        List\<AuthenticationToken\> tokens = new ArrayList\<\>();
        
<AuthenticationSchemes: {authenticationscheme |
        helper.addAuthenthicationTokens(tokens, AuthenticationCategory.<authenticationscheme.key>, <authenticationscheme.key>);}>

        List\<CreationQualifier\> qualifiers = new ArrayList\<\>();

<CreationQualifiers: {qualifier |
        helper.addCreationQualifier(qualifiers, CreationQualifier.<qualifier.key>, <qualifier.key>);}>

        return helper.Create(parent, data, qualifiers, tokens, uri.getBaseUri().toString());
    }
}
>>