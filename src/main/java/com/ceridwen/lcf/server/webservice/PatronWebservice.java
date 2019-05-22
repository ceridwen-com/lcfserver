/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.webservice;

import com.ceridwen.lcf.server.resources.PatronResourceManagerInterface;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import org.bic.ns.lcf.v1_0.Loan;
import org.bic.ns.lcf.v1_0.Patron;

/**
 *
 * @author Matthew.Dovey
 */
public class PatronWebservice implements EntityWebserviceInterface<Patron>, EntitiesLocatorInterface<Loan> {

    WebserviceHelper<Patron> helper = new WebserviceHelper<Patron>(PatronResourceManagerInterface.class);

    private final String identifier;
    
    PatronWebservice(String identifier) {
        this.identifier = identifier;
    }

    @GET
    @Override
    public Patron Retrieve() {
        return helper.Retrieve(identifier);
    }

    @PUT
    @Override
    public Patron Modify(Patron data) {
        return helper.Modify(identifier, data);
    }

    @DELETE
    @Override
    public void Delete() {
        helper.Delete(identifier);
    }

    @Path("loans")
    @Override
    public LoansListWebservice Locate() {
        return new LoansListWebservice(helper.Retrieve(identifier));
    }
    
}
