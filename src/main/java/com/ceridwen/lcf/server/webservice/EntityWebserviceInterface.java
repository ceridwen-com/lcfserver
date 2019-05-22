/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.webservice;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;

/**
 *
 * @author Matthew.Dovey
 */
public interface EntityWebserviceInterface<E> {
//	@GET
	public E Retrieve(); 

//	@PUT
	public E Modify(E data);

//	@DELETE
	public void Delete(); 
}
