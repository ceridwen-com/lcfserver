/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.model.authentication;

/**
 *
 * @author Matthew.Dovey
 */
public abstract class AuthenticationToken {

    private AuthenticationCategory authenticationCategory;
    
    public AuthenticationToken(AuthenticationCategory authenticationCategory) {
        this.authenticationCategory = authenticationCategory;
    }
    

    public AuthenticationCategory getAuthenticationCategory() {
        return authenticationCategory;
    }

    public void setAuthenticationCategory(AuthenticationCategory authenticationCategory) {
        this.authenticationCategory = authenticationCategory;
    }
}
