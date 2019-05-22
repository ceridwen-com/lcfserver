/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.webservice;

import com.ceridwen.lcf.server.resources.LoanResourceManagerInterface;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import org.bic.ns.lcf.v1_0.LcfEntityListResponse;
import org.bic.ns.lcf.v1_0.Loan;

/**
 *
 * @author Matthew.Dovey
 */
public class LoansListWebservice  implements EntitiesWebserviceInterface<Loan> {

    private final Object parent;
    WebserviceHelper<Loan> helper = new WebserviceHelper<Loan>(LoanResourceManagerInterface.class);


    public LoansListWebservice() {
        this.parent = null;
    }
    
    public LoansListWebservice(Object parent) {
        this.parent = parent;
    }
    
    @GET
    @Override
    public LcfEntityListResponse List(@DefaultValue("0") @QueryParam("os:startIndex") int startIndex, @DefaultValue("0") @QueryParam("os:count") int count) {
        return helper.Query(parent, startIndex, count);
    }

    @POST
    @Override
    public Loan Create(Loan data) {
        String identifier = helper.Create(parent, data);
        return data;
    }
}