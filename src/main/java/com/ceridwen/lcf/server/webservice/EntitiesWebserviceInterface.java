/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.webservice;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.bic.ns.lcf.v1_0.LcfEntityListResponse;

/**
 *
 * @author Matthew.Dovey
 */
@Path("")
public interface EntitiesWebserviceInterface<E> {
//	@GET
	public LcfEntityListResponse List(@DefaultValue("0") @QueryParam("os:startIndex") int startIndex, @DefaultValue("0") @QueryParam("os:count") int count);
	
//	@POST
	public E Create(E data);
}
