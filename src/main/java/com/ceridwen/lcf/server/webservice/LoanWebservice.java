/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.webservice;

import com.ceridwen.lcf.server.resources.LoanResourceManagerInterface;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import org.bic.ns.lcf.v1_0.Loan;

/**
 *
 * @author Matthew.Dovey
 */
public class LoanWebservice implements EntityWebserviceInterface<Loan> {

    WebserviceHelper<Loan> helper = new WebserviceHelper<Loan>(LoanResourceManagerInterface.class);

    private final String identifier;

    LoanWebservice(String identifier) {
        this.identifier = identifier;
    }

    @GET
    @Override
    public Loan Retrieve() {
        return helper.Retrieve(identifier);
    }

    @PUT
    @Override
    public Loan Modify(Loan data) {
        return helper.Modify(identifier, data);
    }

    @DELETE
    @Override
    public void Delete() {
        helper.Delete(identifier);
    }
    
}