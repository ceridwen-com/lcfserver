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
package com.ceridwen.lcf.server.resources.memory.database;

import com.ceridwen.lcf.model.authentication.AuthenticationCategory;
import com.ceridwen.lcf.model.authentication.AuthenticationToken;
import com.ceridwen.lcf.model.authentication.BasicAuthenticationToken;
import com.ceridwen.lcf.model.exceptions.EXC02_InvalidUserCredentials;
import com.ceridwen.lcf.model.exceptions.EXC03_InvalidTerminalCredentials;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.bic.ns.lcf.v1_0.Authorisation;
import org.bic.ns.lcf.v1_0.EntityType;
import org.bic.ns.lcf.v1_0.LcfEntity;

/**
 *
 * @author Ceridwen Limited
 */
public class Authenticator {
    Map<EntityType, Map<Operation, List<AuthenticationCategory>>> acls = new EnumMap<>(EntityType.class);
    Map<String, String> terminalAccounts = new HashMap<>();
    Map<String, String> userAccounts =new HashMap<>();
    
    static Authenticator authenticator = new Authenticator();
    
    public static Authenticator getAuthenticator() {
        if (authenticator == null) {
            authenticator = new Authenticator();
        }
        return authenticator;
    }
    
    public Authenticator() {
        Config();
    }
    
    public void Config() {
        Logger.getLogger(Authenticator.class.getName()).info("Loading Default ACLS Data");
        updatePassword(AuthenticationCategory.USER, "patron", "password");
        updatePassword(AuthenticationCategory.TERMINAL, "terminal", "password");       
        addACL(EntityType.AUTHORISATIONS, AuthenticationCategory.USER);
        addACL(EntityType.AUTHORISATIONS, AuthenticationCategory.TERMINAL);
        Logger.getLogger(Authenticator.class.getName()).info("ACLS Data Load Complete");
    }


    
    public void updatePassword(AuthenticationCategory category, String id, String password) {
        switch (category) {
            case USER:
                userAccounts.put(id, password);
                break;
            case TERMINAL:
                terminalAccounts.put(id, password);
                break;
        }
    }
    
    private boolean authenticate(Map<String, String> accounts, List<AuthenticationToken> tokens) {
        for (AuthenticationToken token: tokens) {
            if (accounts.containsKey(((BasicAuthenticationToken)token).getUsername())) {
                if (
                        accounts.get(((BasicAuthenticationToken)token).getUsername()).equals(
                        ((BasicAuthenticationToken)token).getPassword())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void authenticate(EntityType type, Operation operation, List<AuthenticationToken> tokens) {
        if (acls.containsKey(type)) {
            Map<Operation, List<AuthenticationCategory>> ops = acls.get(type);
            if (ops.containsKey(operation)) {
                List<AuthenticationCategory> categories = ops.get(operation);
                categories.sort(Comparator.comparing(AuthenticationCategory::ordinal));
                for (AuthenticationCategory category: categories) {
                    authenticate(category, tokens);
                }
            }
        }
    }

    public void authenticate(AuthenticationCategory category, List<AuthenticationToken> tokens) {
        switch (category) {
            case TERMINAL:
                if (!authenticate(terminalAccounts, tokens.stream().filter(
                        token -> token.getAuthenticationCategory() == AuthenticationCategory.TERMINAL)
                        .collect(Collectors.toList()))) {
                    throw new EXC03_InvalidTerminalCredentials("", "", "", null);
                }
                break;
            case USER:
                if (!authenticate(userAccounts, tokens.stream().filter(
                        token -> token.getAuthenticationCategory() == AuthenticationCategory.USER)
                        .collect(Collectors.toList()))) {
                    throw new EXC02_InvalidUserCredentials("", "", "", null);
                }
                break;
        }
    }
    
    public void authenticate(String patronId, List<AuthenticationToken> authTokens) {
        try {
            Authenticator.getAuthenticator().authenticate(AuthenticationCategory.USER,  authTokens.stream().filter(
                    token -> (token.getAuthenticationCategory() == AuthenticationCategory.USER &&
                            ((BasicAuthenticationToken)token).getUsername().equals(patronId)))
                    .collect(Collectors.toList()));
        } catch (EXC02_InvalidUserCredentials ex1) {
            try {
                Authenticator.getAuthenticator().authenticate(AuthenticationCategory.TERMINAL, authTokens);
            } catch (EXC03_InvalidTerminalCredentials ex2) {
                throw ex1;
            }
        }
    }
    
    public void addACL(EntityType type, Operation operation, AuthenticationCategory category) {
        if (!acls.containsKey(type)) {
            acls.put(type, new EnumMap<>(Operation.class));
        }
        
        Map<Operation, List<AuthenticationCategory>> ops = acls.get(type);

        if (!ops.containsKey(operation)) {
            ops.put(operation, new ArrayList<>());
        }
        
        List<AuthenticationCategory> categories = ops.get(operation);
        
        if (!categories.contains(category)) {
            categories.add(category);
        }
    }

    public void addACL(EntityType type, AuthenticationCategory category) {
        for (Operation operation: Operation.values()) {
            addACL(type, operation, category);
        }
    }

    public void removeACL(EntityType type, Operation operation, AuthenticationCategory category) {
        if (!acls.containsKey(type)) {
            acls.put(type, new EnumMap<>(Operation.class));
        }
        
        Map<Operation, List<AuthenticationCategory>> ops = acls.get(type);

        if (!ops.containsKey(operation)) {
            ops.put(operation, new ArrayList<>());
        }
        
        List<AuthenticationCategory> categories = ops.get(operation);
        
        if (!categories.contains(category)) {
            categories.remove(category);
        }
    }

    public void removeACL(EntityType type, AuthenticationCategory category) {
        for (Operation operation: Operation.values()) {
            removeACL(type, operation, category);
        }
    }
}

