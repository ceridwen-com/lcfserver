/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.lcfserver.model.authentication;

import java.util.logging.Level;
import java.util.logging.Logger;

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
        this.username = "";
        this.password = "";
        
 //       if (!StringUtil.isNullOrEmpty(BASE64)) {
        try {  
            String payload = BASE64.replaceFirst("[bB][aA][sS][iI][cC] ", "");
            String token = new String(java.util.Base64.getDecoder().decode(payload));
            String[] tokens = token.split(":");
            this.username = tokens[0];
            this.password = tokens[1];
        } catch (Exception ex) {
            Logger.getLogger(BasicAuthenticationToken.class.getName()).log(Level.WARNING, "BASIC Authentication Error", ex);                    
        }
    }
    
    public BasicAuthenticationToken(BasicAuthenticationToken.AuthenticationCategory authenticationCategory, String username, String password)
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
}
