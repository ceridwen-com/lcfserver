/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.resources.jpa;

import com.ceridwen.lcf.server.authentication.AbstractAuthenticationToken;
import com.ceridwen.lcf.server.resources.LoanResourceManagerInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bic.ns.lcf.v1_0.Loan;
import org.bic.ns.lcf.v1_0.SelectionCriterion;

/**
 *
 * @author Matthew.Dovey
 */
public class LoanResourceManager implements LoanResourceManagerInterface {

    @Override
    public String Create(Map<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken> authTokens, Object parent, Loan entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Loan Retrieve(Map<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken> authTokens, String identifier) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Loan Modify(Map<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken> authTokens, String identifier, Loan entity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void Delete(Map<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken> authTokens, String identifier) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Loan> Query(Map<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken> authTokens, Object parent, int startIndex, int count, List<SelectionCriterion> selection) {
        List<Loan> list = new ArrayList<Loan>();
        Loan l = new Loan();
        l.setIdentifier("00000");
        list.add(l);
        l = new Loan();
        l.setIdentifier("00001");
        list.add(l);
        l = new Loan();
        l.setIdentifier("00002");
        list.add(l);
        return list;    
    }
    
}
