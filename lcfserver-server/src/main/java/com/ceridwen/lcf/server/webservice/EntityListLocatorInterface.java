/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.webservice;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;

/**
 *
 * @author Matthew.Dovey
 */
public interface EntityListLocatorInterface<WS> {
//    @Path("")
    public EntityListInterface<WS> Locate(@HeaderParam("Authorization") String authorization, @HeaderParam("lcf-patron-credential") String lcfPatronCredential);
}
