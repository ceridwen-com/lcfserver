/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.webservice;

import javax.ws.rs.Path;
import org.bic.ns.lcf.v1_0.Loan;

/**
 *
 * @author Matthew.Dovey
 */
@Path("loans")
public class LoansWebservice extends LoansListWebservice implements EntitiesWebserviceInterface<Loan>, EntityLocatorInterface<Loan> {
    @Path("{identifier}")
    @Override
    public LoanWebservice Locate(String identifier) {
        return new LoanWebservice(identifier);
    }
}    
