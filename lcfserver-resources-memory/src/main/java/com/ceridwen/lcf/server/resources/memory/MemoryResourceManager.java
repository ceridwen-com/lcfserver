/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.resources.memory;

import com.ceridwen.lcf.model.enumerations.CreationQualifier;
import com.ceridwen.lcf.model.enumerations.EntityTypes;
import com.ceridwen.lcf.model.enumerations.VirtualUpdatePath;
import com.ceridwen.lcf.model.authentication.AuthenticationToken;
import com.ceridwen.lcf.model.authentication.AuthenticationCategory;
import com.ceridwen.lcf.model.authentication.BasicAuthenticationToken;
import com.ceridwen.lcf.model.exceptions.EXC02_InvalidUserCredentials;
import com.ceridwen.lcf.model.exceptions.EXC03_InvalidTerminalCredentials;
import com.ceridwen.lcf.model.exceptions.EXC04_UnableToProcessRequest;
import com.ceridwen.lcf.model.exceptions.EXC05_InvalidEntityReference;
import com.ceridwen.lcf.server.resources.QueryResults;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import org.bic.ns.lcf.v1_0.CardStatus;
import org.bic.ns.lcf.v1_0.CardStatusInfo;
import org.bic.ns.lcf.v1_0.Iso639LanguageCode;
import org.bic.ns.lcf.v1_0.Patron;
import org.bic.ns.lcf.v1_0.SelectionCriterion;


enum Operation {
    GET, PUT, DELETE, LIST, UPDATE
}

class Authenticator {
    Map<EntityTypes.Type, Map<Operation, List<AuthenticationCategory>>> acls;
    Map<String, String> terminalAccounts = new HashMap<>();
    Map<String, String> userAccounts =new HashMap<>();
    
    static Authenticator authenticator = new Authenticator();
    
    public static Authenticator getAuthenticator() {
        return authenticator;
    }
    
    public Authenticator() {
        acls = new EnumMap<>(EntityTypes.Type.class);
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

    public void authenticate(EntityTypes.Type type, Operation operation, List<AuthenticationToken> tokens) {
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
    
    public void addACL(EntityTypes.Type type, Operation operation, AuthenticationCategory category) {
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

    public void addACL(EntityTypes.Type type, AuthenticationCategory category) {
        for (Operation operation: Operation.values()) {
            addACL(type, operation, category);
        }
    }

    public void removeACL(EntityTypes.Type type, Operation operation, AuthenticationCategory category) {
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

    public void removeACL(EntityTypes.Type type, AuthenticationCategory category) {
        for (Operation operation: Operation.values()) {
            removeACL(type, operation, category);
        }
    }
}

class NameGenerator {

   private static String[] Beginning = { "Kr", "Ca", "Ra", "Mrok", "Cru",
         "Ray", "Bre", "Zed", "Drak", "Mor", "Jag", "Mer", "Jar", "Mjol",
         "Zork", "Mad", "Cry", "Zur", "Creo", "Azak", "Azur", "Rei", "Cro",
         "Mar", "Luk" };
   private static String[] Middle = { "air", "ir", "mi", "sor", "mee", "clo",
         "red", "cra", "ark", "arc", "miri", "lori", "cres", "mur", "zer",
         "marac", "zoir", "slamar", "salmar", "urak" };
   private static String[] End = { "d", "ed", "ark", "arc", "es", "er", "der",
         "tron", "med", "ure", "zur", "cred", "mur" };
   
   private static Random rand = new Random();

   public static String generateName() {

      return Beginning[rand.nextInt(Beginning.length)] + 
            Middle[rand.nextInt(Middle.length)]+
            End[rand.nextInt(End.length)];

   }

}

/**
 *
 * @author Matthew
 */
public class MemoryResourceManager {
    
    private static MemoryResourceManager mgr = null;
    
    private Map<EntityTypes.Type, Map<String, Object>> database;
    
    public static MemoryResourceManager getMemoryResourceManager() {
        if (mgr == null) {
            mgr = new MemoryResourceManager();
        }
        return mgr;
    }
    
    private Patron GeneratePatron() {
        Patron patron = new Patron();
        patron.setIdentifier(UUID.randomUUID().toString());
        patron.setBarcodeId(Integer.toUnsignedString(patron.getIdentifier().hashCode()));
	patron.setName(NameGenerator.generateName());	
	patron.setLanguage(Iso639LanguageCode.ENG);
	CardStatusInfo csi = new CardStatusInfo();
	csi.setCardStatus(CardStatus.VALUE_3);
	patron.setCardStatusInfo(csi);
	try {
            patron.setDateOfBirth(DatatypeFactory.newInstance().newXMLGregorianCalendarDate(1960, 1, 1, 0));
        } catch (DatatypeConfigurationException e) {
            // as this is demo data, not crucial if DOB not set.
	}
        
        return patron;

    }
    
    public MemoryResourceManager() {
        this.database = new HashMap<>();
        
        int amount = (new Random()).nextInt(25)+5;
        for (int i=0; i < amount; i++) {
            Patron patron = this.GeneratePatron();
            this.put(EntityTypes.Type.Patron, patron.getIdentifier(), null, patron, new ArrayList<>(), new ArrayList<>());
            Authenticator.getAuthenticator().updatePassword(AuthenticationCategory.USER, patron.getBarcodeId(), "password");
        }

        Authenticator.getAuthenticator().updatePassword(AuthenticationCategory.USER, "patron", "password");
        Authenticator.getAuthenticator().updatePassword(AuthenticationCategory.TERMINAL, "terminal", "password");
        
        Authenticator.getAuthenticator().addACL(EntityTypes.Type.Authorisation, AuthenticationCategory.USER);
        Authenticator.getAuthenticator().addACL(EntityTypes.Type.Authorisation, AuthenticationCategory.TERMINAL);
    }
   
    public Object get(EntityTypes.Type type, String identifier, List<AuthenticationToken> authTokens) {
        Authenticator.getAuthenticator().authenticate(type, Operation.GET, authTokens);
        
        Object response = null;
        
        if (database.containsKey(type)) {
            Map<String, Object> map = database.get(type);
            if (map.containsKey(identifier)) {
                response = map.get(identifier);
            }
        }
        
//        if (type.equals(EntityTypes.Type.Patron)) {
//            Authenticator.getAuthenticator().authenticate(((Patron)response).getBarcodeId(), authTokens);
//        }

        return response;
    }
  
    public Object put(EntityTypes.Type type, String identifier, Object parent, Object data, List<CreationQualifier> qualifiers, List<AuthenticationToken> authTokens) {
        Authenticator.getAuthenticator().authenticate(type, Operation.PUT, authTokens);

        if (!database.containsKey(type)) {
            database.put(type, new HashMap<>());
        }    
        Map<String, Object> map = database.get(type);
        if (map.containsKey(identifier)) {
            map.replace(identifier, data);
        }
        else {
            map.put(identifier, data);
        }
        
        return data;
    }    
    
    public void delete(EntityTypes.Type type, String identifier, List<AuthenticationToken> authTokens) {
        Authenticator.getAuthenticator().authenticate(type, Operation.DELETE, authTokens);

        if (database.containsKey(type)) {
            Map<String, Object> map = database.get(type);
            if (map.containsKey(identifier)) {
                map.remove(identifier);
            }
        }
    }
    
    public QueryResults<? extends Object> list(EntityTypes.Type type, Object parent, int startIndex, int count, List<SelectionCriterion> selection, List<AuthenticationToken> authTokens) {
        Authenticator.getAuthenticator().authenticate(type, Operation.LIST, authTokens);

        QueryResults queryResults = new QueryResults();
        Collection results;
        if (database.containsKey(type)) {
            results = database.get(type).values();
        } else {
            results = new ArrayList();
        }
       
        int counter = 0;
        queryResults.setTotalResults(results.size());
        queryResults.setSkippedResults(counter);
        
        for (Object o: results) {
            if (counter >= startIndex) {
                if (count == -1 || counter < startIndex + count) {
                    queryResults.getResults().add(o);
                }
            } else {
                queryResults.setSkippedResults(counter+1);
            }
            counter++;
        }
        
        return queryResults;
    }
    
    public void UpdateValue(EntityTypes.Type type, String identifier, VirtualUpdatePath path, String value, List<AuthenticationToken> authTokens) {
        if (type.equals(EntityTypes.Type.Patron) && (path.equals(VirtualUpdatePath.PASSWORD) || path.equals(VirtualUpdatePath.PIN))) {
            Patron patron = (Patron)this.get(type, identifier, authTokens);
            if (patron == null) {
                throw new EXC05_InvalidEntityReference("", "", "", null);
            }
            Authenticator.getAuthenticator().authenticate(patron.getBarcodeId(), authTokens);
            Authenticator.getAuthenticator().updatePassword(AuthenticationCategory.USER, patron.getBarcodeId(), value);
        } else {
            throw new EXC04_UnableToProcessRequest("Invalid request", "Request invalid for " + type.getEntityTypeCodeValue(), path.getPath(), null );
        }
        
    }

}