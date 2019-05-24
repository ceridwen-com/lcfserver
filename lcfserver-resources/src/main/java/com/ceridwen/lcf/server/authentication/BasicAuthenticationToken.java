/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.authentication;

/**
 *
 * @author Matthew.Dovey
 */
public class BasicAuthenticationToken extends AbstractAuthenticationToken {
    private String username;
    private String password;
    
    public BasicAuthenticationToken(BasicAuthenticationToken.AuthenticationCategory authenticationCategory, String BASE64)
    {
        super(authenticationCategory);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    
}
