/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.lcfserver.model.authentication;

/**
 *
 * @author Matthew.Dovey
 */
public abstract class AbstractAuthenticationToken {
    public enum AuthenticationCategory {
        TERMINAL,
        USER
    };

    private AuthenticationCategory authenticationCategory;
    
    public AbstractAuthenticationToken(AuthenticationCategory authenticationCategory) {
        this.authenticationCategory = authenticationCategory;
    }
    

    public AuthenticationCategory getAuthenticationCategory() {
        return authenticationCategory;
    }

    public void setAuthenticationCategory(AuthenticationCategory authenticationCategory) {
        this.authenticationCategory = authenticationCategory;
    }
}
