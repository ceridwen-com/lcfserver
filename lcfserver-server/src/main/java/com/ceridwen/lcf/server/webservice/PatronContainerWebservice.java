/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.webservice;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.bic.ns.lcf.v1_0.Patron;

/**
 *
 * @author Matthew.Dovey
 */
@Path("patrons")
public class PatronContainerWebservice extends PatronListWebservice implements EntityListInterface<Patron>, EntityLocatorInterface<Patron> {
    
    @Path("{identifier}")
    @Override
    public PatronWebservice Locate(@PathParam("identifier") String identifier, @HeaderParam("Authorization") String authorization, @HeaderParam("lcf-patron-credential") String lcfPatronCredential) {
        return new PatronWebservice(identifier, authorization, lcfPatronCredential);
    }
}    
