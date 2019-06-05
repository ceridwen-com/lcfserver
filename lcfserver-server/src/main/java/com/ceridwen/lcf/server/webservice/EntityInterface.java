/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.webservice;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;

import javax.ws.rs.core.Response;

/**
 *
 * @author Matthew.Dovey
 */
public interface EntityInterface<E> {
//	@GET
	public Response Retrieve(@HeaderParam("Authorization") String authorization, @HeaderParam("lcf-patron-credential") String lcfPatronCredential); 

//	@PUT
	public Response Modify(E data, @HeaderParam("Authorization") String authorization, @HeaderParam("lcf-patron-credential") String lcfPatronCredential);

//	@DELETE
	public Response Delete(@HeaderParam("Authorization") String authorization, @HeaderParam("lcf-patron-credential") String lcfPatronCredential); 
}
