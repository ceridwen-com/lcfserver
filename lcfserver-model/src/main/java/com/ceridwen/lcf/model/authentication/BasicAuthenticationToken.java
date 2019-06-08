/* 
 * Copyright 2019 Ceridwen Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ceridwen.lcf.model.authentication;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ceridwen Limited
 */
public class BasicAuthenticationToken extends AuthenticationToken {
    private String username;
    private String password;
 
    /**
     *
     * @param authenticationCategory
     * @param Encoded
     */
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
    
    /**
     *
     * @param authenticationCategory
     * @param username
     * @param password
     */
    public BasicAuthenticationToken(AuthenticationCategory authenticationCategory, String username, String password)
    {
        super(authenticationCategory);
        this.username = username;
        this.password = password;
    }    

    /**
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
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
