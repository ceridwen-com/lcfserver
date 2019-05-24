EntityListWebservice(LCFPath, Entity, EntityPath, SubEntity) ::= <<
package com.ceridwen.lcf.server.webservice;

import com.ceridwen.lcf.server.resources.<Entity>ResourceManagerInterface;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import org.bic.ns.lcf.v1_0.<Entity>;

public class <Entity>ListWebservice implements EntityListInterface\<<Entity>\> {
    
    private final Object parent;
    WebserviceHelper\<<Entity>\> helper = new WebserviceHelper\<\>(<Entity>.class, <Entity>ResourceManagerInterface.class);

    public <Entity>ListWebservice() {
        this.parent = null;
    }
    
    public <Entity>ListWebservice(Object parent) {
        this.parent = parent;
    }
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Override
    public LcfEntityListResponse List(@DefaultValue("0") @QueryParam("os:startIndex") int startIndex, @DefaultValue("0") @QueryParam("os:count") int count, @Context UriInfo uriInfo, @HeaderParam("Authorization") String authorization, @HeaderParam("lcf-patron-credential") String lcfPatronCredential) {
        return helper.Query(parent, startIndex, count, uriInfo.getQueryParameters(true), authorization, lcfPatronCredential);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @ApiResponse(
        responseCode = "201",
        description = "Without content")    
    @ApiResponse(
        responseCode = "201",
        description = "With echoed content",
        content = @Content(schema = @Schema(implementation = <Entity>.class)))    
    @Override
    public Response Create(<Entity> data, @Context UriInfo uriInfo, @HeaderParam("Authorization") String authorization, @HeaderParam("lcf-patron-credential") String lcfPatronCredential) {
        return helper.Create(parent, data, uriInfo, authorization, lcfPatronCredential);
    }
}
>>
