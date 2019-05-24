/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.webservice;

import com.ceridwen.lcf.server.resources.LoanResourceManagerInterface;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.bic.ns.lcf.v1_0.Loan;

/**
 *
 * @author Matthew.Dovey
 */
public class LoanWebservice implements EntityInterface<Loan> {

    WebserviceHelper<Loan> helper = new WebserviceHelper<Loan>(Loan.class, LoanResourceManagerInterface.class);

    private final String identifier;
    private final String authorization;
    private final String lcfPatronCredential;

    LoanWebservice(String identifier, String authorization, String lcfPatronCredential) {
        this.authorization = authorization;
        this.lcfPatronCredential = lcfPatronCredential;
        this.identifier = identifier;
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Loan Retrieve(@HeaderParam("Authorization") String authorization, @HeaderParam("lcf-patron-credential") String lcfPatronCredential) {
        return helper.Retrieve(identifier, authorization, lcfPatronCredential);
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Loan Modify(Loan data, @HeaderParam("Authorization") String authorization, @HeaderParam("lcf-patron-credential") String lcfPatronCredential) {
        return helper.Modify(identifier, data, authorization, lcfPatronCredential);
    }

    @DELETE
    @Override
    public void Delete(@HeaderParam("Authorization") String authorization, @HeaderParam("lcf-patron-credential") String lcfPatronCredential) {
        helper.Delete(identifier, authorization, lcfPatronCredential);
    }
    
}