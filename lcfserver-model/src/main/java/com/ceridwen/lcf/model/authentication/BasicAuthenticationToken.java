/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.model.authentication;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matthew.Dovey
 */
public class BasicAuthenticationToken extends AuthenticationToken {
    private String username;
    private String password;
 
    public BasicAuthenticationToken(AuthenticationCategory authenticationCategory, String Encoded)
    {
        super(authenticationCategory);
        this.username = "";
        this.password = "";
        
 //       if (!StringUtil.isNullOrEmpty(BASE64)) {
        try {  
            String payload = Encoded.replaceFirst("[bB][aA][sS][iI][cC] ", "");
            String token = new String(java.util.Base64.getDecoder().decode(payload));
            String[] tokens = token.split(":");
            this.username = tokens[0];
            this.password = tokens[1];
        } catch (Exception ex) {
            Logger.getLogger(BasicAuthenticationToken.class.getName()).log(Level.WARNING, "BASIC Authentication Error", ex);                    
        }
    }
    
    public BasicAuthenticationToken(AuthenticationCategory authenticationCategory, String username, String password)
    {
        super(authenticationCategory);
        this.username = username;
        this.password = password;
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
    
    public String toString() {
        String token = new String(java.util.Base64.getEncoder().encode((this.username + ":" + this.password).getBytes()));
        return "Basic " + token;
    }

    @Override
    public boolean equals(Object obj) {
        return (this.username.equals(((BasicAuthenticationToken)obj).username) && 
                this.password.equals(((BasicAuthenticationToken)obj).password) &&
                this.getAuthenticationCategory().equals(((BasicAuthenticationToken)obj).getAuthenticationCategory()));
    }

    @Override
    public int hashCode() {
        return this.username.hashCode() + this.password.hashCode() + this.getAuthenticationCategory().hashCode();
    }
    
    
}
