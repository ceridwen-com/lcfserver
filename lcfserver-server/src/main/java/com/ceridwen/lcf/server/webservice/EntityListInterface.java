/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.webservice;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.bic.ns.lcf.v1_0.LcfEntityListResponse;

/**
 *
 * @author Matthew.Dovey
 */
@Path("")
public interface EntityListInterface<E> {
//	@GET
	public LcfEntityListResponse List(@DefaultValue("0") @QueryParam("os:startIndex") int startIndex, @DefaultValue("0") @QueryParam("os:count") int count, @Context UriInfo uriInfo, @HeaderParam("Authorization") String authorization, @HeaderParam("lcf-patron-credential") String lcfPatronCredential);
	
//	@POST
	public Response Create(E data, @Context UriInfo uriInfo, @HeaderParam("Authorization") String authorization, @HeaderParam("lcf-patron-credential") String lcfPatronCredential);
}
