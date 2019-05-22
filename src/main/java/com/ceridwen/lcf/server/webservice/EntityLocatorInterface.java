/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.webservice;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 *
 * @author Matthew.Dovey
 */
public interface EntityLocatorInterface<E> {
//    @Path("{identifier}")
    public EntityWebserviceInterface<E> Locate(@PathParam("identifier") String identifier);
}
