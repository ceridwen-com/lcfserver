EntityWebservice(LCFPath, Entity, EntityPath, SubEntity, SelectionCriteria, AlternativePosts, AlternativePuts, AlternativeGets, AlternativeDeletes, CreationQualifiers, DirectUpdatePaths, AuthenticationSchemes) ::= <<
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
package com.ceridwen.lcf.server.webservice;

import com.ceridwen.lcf.model.enumerations.DirectUpdatePath;
import com.ceridwen.lcf.model.authentication.AuthenticationToken;
import com.ceridwen.lcf.model.authentication.AuthenticationCategory;
import com.ceridwen.lcf.server.resources.<Entity>ResourceManagerInterface;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.bic.ns.lcf.v1_0.<Entity>;

public class <Entity>Webservice
 {
    WebserviceHelper\<<Entity>\> helper = new WebserviceHelper\<\>(<Entity>.class, <Entity>ResourceManagerInterface.class);

    private final String identifier;
    List\<AuthenticationToken\> tokens;
    
    <Entity>Webservice(String identifier, List\<AuthenticationToken\> tokens) {
        this.tokens = tokens;
        this.identifier = identifier;
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
<AuthenticationSchemes: {authenticationscheme |
    @SecurityRequirement(name = "<authenticationscheme.key>")}>
    @ApiResponse(
        headers = @Header(name="lcf-version", description="LCF Specification Version", required=true, schema=@Schema(type="string")),
        responseCode = "200",
        description = "Retrieved",
        content = @Content(schema = @Schema(implementation = <Entity>.class)))
<AlternativeGets: {alternative |
    @ApiResponse(
        headers = @Header(name="lcf-version", description="LCF Specification Version", required=true, schema=@Schema(type="string")),
        responseCode = "200",
        description = "<alternative>",
        content = @Content(schema = @Schema(implementation = <alternative>.class)))}>
    public <Entity> Retrieve(
<AuthenticationSchemes: {authenticationscheme |
            @Parameter(hidden=true) @HeaderParam("<authenticationscheme.value>") String <authenticationscheme.key>,}>
            @Parameter(hidden=true) @HeaderParam("dummy") String dummy    
        ) {
        List\<AuthenticationToken\> tokens = new ArrayList\<\>();
        
<AuthenticationSchemes: {authenticationscheme |
        helper.addAuthenthicationTokens(tokens, AuthenticationCategory.<authenticationscheme.key>, <authenticationscheme.key>);}>

        return helper.Retrieve(identifier, tokens);
    }

    @PUT
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
<AuthenticationSchemes: {authenticationscheme |
    @SecurityRequirement(name = "<authenticationscheme.key>")}>
    @ApiResponse(
        headers = @Header(name="lcf-version", description="LCF Specification Version", required=true, schema=@Schema(type="string")),
        responseCode = "200",
        description = "Modified",
        content = @Content(schema = @Schema(implementation = <Entity>.class)))
<AlternativePuts: {alternative |
    @ApiResponse(
        headers = @Header(name="lcf-version", description="LCF Specification Version", required=true, schema=@Schema(type="string")),
        responseCode = "200",
        description = "<alternative>",
        content = @Content(schema = @Schema(implementation = <alternative>.class)))}>
    public <Entity> Modify(
<AuthenticationSchemes: {authenticationscheme |
            @Parameter(hidden=true) @HeaderParam("<authenticationscheme.value>") String <authenticationscheme.key>,}>
            <Entity> data
        ) {
        List\<AuthenticationToken\> tokens = new ArrayList\<\>();
        
<AuthenticationSchemes: {authenticationscheme |
        helper.addAuthenthicationTokens(tokens, AuthenticationCategory.<authenticationscheme.key>, <authenticationscheme.key>);}>

        return helper.Modify(identifier, data, tokens);
    }

    @DELETE
<AuthenticationSchemes: {authenticationscheme |
    @SecurityRequirement(name = "<authenticationscheme.key>")}>
    @ApiResponse(
        headers = @Header(name="lcf-version", description="LCF Specification Version", required=true, schema=@Schema(type="string")),
        responseCode = "200",
        description = "Deleted")
<AlternativeDeletes: {alternative |
    @ApiResponse(
        headers = @Header(name="lcf-version", description="LCF Specification Version", required=true, schema=@Schema(type="string")),
        responseCode = "200",
        description = "<alternative>",
        content = @Content(schema = @Schema(implementation = <alternative>.class)))}>
    public void Delete(
<AuthenticationSchemes: {authenticationscheme |
            @Parameter(hidden=true) @HeaderParam("<authenticationscheme.value>") String <authenticationscheme.key>,}>
            @Parameter(hidden=true) @HeaderParam("dummy") String dummy    
        ) {
        List\<AuthenticationToken\> tokens = new ArrayList\<\>();
        
<AuthenticationSchemes: {authenticationscheme |
        helper.addAuthenthicationTokens(tokens, AuthenticationCategory.<authenticationscheme.key>, <authenticationscheme.key>);}>
        
        helper.Delete(identifier, tokens);
    }

<DirectUpdatePaths: {path |


    @Path("<path.value>")
    @PUT
<AuthenticationSchemes: {authenticationscheme |
    @SecurityRequirement(name = "<authenticationscheme.key>")}>
    @ApiResponse(
        headers = @Header(name="lcf-version", description="LCF Specification Version", required=true, schema=@Schema(type="string")),
        responseCode = "200",
        description = "Updated")
    public void DirectUpdate<path.key>(
<AuthenticationSchemes: {authenticationscheme |
            @Parameter(hidden=true) @HeaderParam("<authenticationscheme.value>") String <authenticationscheme.key>,}>
            String data
        ) {
        List\<AuthenticationToken\> tokens = new ArrayList\<\>();
        
<AuthenticationSchemes: {authenticationscheme |
        helper.addAuthenthicationTokens(tokens, AuthenticationCategory.<authenticationscheme.key>, <authenticationscheme.key>);}>

        helper.directValueUpdate(identifier, DirectUpdatePath.<path.key>, data, tokens);
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

        return new <subentity.Entity>ListWebservice(helper.Retrieve(identifier, tokens));
    \}
}>}
>>
