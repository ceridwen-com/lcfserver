/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.webservice;

import com.ceridwen.lcf.server.resources.PatronResourceManagerInterface;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.bic.ns.lcf.v1_0.Patron;

/**
 *
 * @author Matthew.Dovey
 */
@Path("patrons")
public class PatronsWebservice extends PatronsListWebservice implements EntitiesWebserviceInterface<Patron>, EntityLocatorInterface<Patron> {
    
    @Path("{identifier}")
    @Override
    public PatronWebservice Locate(String identifier) {
        return new PatronWebservice(identifier);
    }
}    
