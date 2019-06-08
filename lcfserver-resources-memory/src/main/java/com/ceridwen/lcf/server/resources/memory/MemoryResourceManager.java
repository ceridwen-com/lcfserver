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
package com.ceridwen.lcf.server.resources.memory;

import com.ceridwen.lcf.model.enumerations.CreationQualifier;
import com.ceridwen.lcf.model.enumerations.EntityTypes;
import com.ceridwen.lcf.model.enumerations.DirectUpdatePath;
import com.ceridwen.lcf.model.authentication.AuthenticationToken;
import com.ceridwen.lcf.model.authentication.AuthenticationCategory;
import com.ceridwen.lcf.model.authentication.BasicAuthenticationToken;
import com.ceridwen.lcf.model.exceptions.EXC02_InvalidUserCredentials;
import com.ceridwen.lcf.model.exceptions.EXC03_InvalidTerminalCredentials;
import com.ceridwen.lcf.model.exceptions.EXC04_UnableToProcessRequest;
import com.ceridwen.lcf.model.exceptions.EXC06_InvalidDataInElement;
import com.ceridwen.lcf.model.responses.LCFResponse_CheckIn;
import com.ceridwen.lcf.model.responses.LCFResponse_CheckOut;
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
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import org.bic.ns.lcf.v1_0.CardStatus;
import org.bic.ns.lcf.v1_0.CardStatusInfo;
import org.bic.ns.lcf.v1_0.Iso639LanguageCode;
import org.bic.ns.lcf.v1_0.LcfCheckInResponse;
import org.bic.ns.lcf.v1_0.LcfCheckOutResponse;
import org.bic.ns.lcf.v1_0.Loan;
import org.bic.ns.lcf.v1_0.LoanStatusCode;
import org.bic.ns.lcf.v1_0.Patron;
import org.bic.ns.lcf.v1_0.SelectionCriterion;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;


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
 * @author Ceridwen Limited
 */
public class MemoryResourceManager {
    
    private static MemoryResourceManager mgr = null;
    
    private Map<EntityTypes.Type, Map<String, Object>> database;
    
    /**
     *
     * @return
     */
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
    
    /**
     *
     * @param type
     */
    public void generateRandomContent(EntityTypes.Type type) {
        EasyRandomParameters parameters = new EasyRandomParameters()
            .seed(123L)
            .objectPoolSize(100)
            .randomizationDepth(3)
            .stringLengthRange(5, 12)
            .collectionSizeRange(1, 10)
            .scanClasspathForConcreteTypes(true)
            .overrideDefaultInitialization(false)
            .ignoreRandomizationErrors(true);
        EasyRandom easyRandom = new EasyRandom(parameters);

        int amount = (new Random()).nextInt(25)+5;
        for (int i=0; i < amount; i++) {
            Object o = easyRandom.nextObject(type.getTypeClass());
            type.setIdentifier(o, UUID.randomUUID().toString());
            this.directPut(type, type.getIdentifier(o), o);
        }
    }
    
    /**
     *
     */
    public MemoryResourceManager() {
        this.database = new HashMap<>();
        Logger.getLogger(MemoryResourceManager.class.getName()).info("Loading data");
        loadData();
        Logger.getLogger(MemoryResourceManager.class.getName()).info("Loading acls");
        loadACLS();
        Logger.getLogger(MemoryResourceManager.class.getName()).info("Initialisation complete");
    }

    private void loadACLS() {
        Authenticator.getAuthenticator().updatePassword(AuthenticationCategory.USER, "patron", "password");
        Authenticator.getAuthenticator().updatePassword(AuthenticationCategory.TERMINAL, "terminal", "password");
        
        Authenticator.getAuthenticator().addACL(EntityTypes.Type.Authorisation, AuthenticationCategory.USER);
        Authenticator.getAuthenticator().addACL(EntityTypes.Type.Authorisation, AuthenticationCategory.TERMINAL);
    }

    private void loadData() {
//        int amount = (new Random()).nextInt(25)+5;
//        for (int i=0; i < amount; i++) {
//            Patron patron = this.GeneratePatron();
//            this.put(EntityTypes.Type.Patron, patron.getIdentifier(), null, patron, new ArrayList<>(), new ArrayList<>());
//            Authenticator.getAuthenticator().updatePassword(AuthenticationCategory.USER, patron.getBarcodeId(), "password");
//        }
        for (EntityTypes.Type type: EntityTypes.Type.values()) {
            generateRandomContent(type);
        }
    }
  
    /**
     *
     * @param type
     * @param identifier
     * @param authTokens
     * @return
     */
    public Object get(EntityTypes.Type type, String identifier, List<AuthenticationToken> authTokens) {
        Authenticator.getAuthenticator().authenticate(type, Operation.GET, authTokens);

        Object response = directGet(type, identifier);
        
//        if (type.equals(EntityTypes.Type.Patron)) {
//            Authenticator.getAuthenticator().authenticate(((Patron)response).getBarcodeId(), authTokens);
//        }

        return response;
    }

    private Object directGet(EntityTypes.Type type, String identifier) {
        
        if (database.containsKey(type)) {
            Map<String, Object> map = database.get(type);
            if (map.containsKey(identifier)) {
                return map.get(identifier);
            }
        }
        return null;
    }
    
    /**
     *
     * @param type
     * @param identifier
     * @param parent
     * @param data
     * @param qualifiers
     * @param authTokens
     * @return
     */
    public Object put(EntityTypes.Type type, String identifier, Object parent, Object data, List<CreationQualifier> qualifiers, List<AuthenticationToken> authTokens) {
        Authenticator.getAuthenticator().authenticate(type, Operation.PUT, authTokens);
        
        if (identifier == null) {
            String test = type.getIdentifier(data);
            if (test != null) {
                if (directGet(type, test) != null) {
                    throw new EXC06_InvalidDataInElement("Entity already exists with that identifier", "", "", null);
                }
            } else {
                type.setIdentifier(data, UUID.randomUUID().toString());
            }
        } else {
            if (directGet(type, identifier) == null) {
                return null;            
            }
            if (type.getIdentifier(data) != null && !identifier.equals(type.getIdentifier(data))) {
                throw new EXC06_InvalidDataInElement("Change of identifier not permitted", "", "", null);
            } else {
                type.setIdentifier(data, identifier);
            }
        }
       
        if (type.equals(EntityTypes.Type.Loan)) {
            doLoan(identifier, (Loan)data);
        }
        
        return directPut(type, type.getIdentifier(data), data);
    }    

    private Object directPut(EntityTypes.Type type, String identifier, Object data) {
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
        
        return directGet(type, identifier);
    }    
    
    /**
     *
     * @param type
     * @param identifier
     * @param authTokens
     * @return
     */
    public boolean delete(EntityTypes.Type type, String identifier, List<AuthenticationToken> authTokens) {
        Authenticator.getAuthenticator().authenticate(type, Operation.DELETE, authTokens);

        if (database.containsKey(type)) {
            Map<String, Object> map = database.get(type);
            if (map.containsKey(identifier)) {
                map.remove(identifier);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     *
     * @param type
     * @param parent
     * @param startIndex
     * @param count
     * @param selection
     * @param authTokens
     * @return
     */
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
    
    /**
     *
     * @param type
     * @param identifier
     * @param path
     * @param value
     * @param authTokens
     * @return
     */
    public boolean DirectValueUpdate(EntityTypes.Type type, String identifier, DirectUpdatePath path, String value, List<AuthenticationToken> authTokens) {
        if (type.equals(EntityTypes.Type.Patron) && (path.equals(DirectUpdatePath.PASSWORD) || path.equals(DirectUpdatePath.PIN))) {
            Patron patron = (Patron)this.directGet(type, identifier);
            if (patron == null) {
                return false;
            }
            Authenticator.getAuthenticator().authenticate(patron.getBarcodeId(), authTokens);
            Authenticator.getAuthenticator().updatePassword(AuthenticationCategory.USER, patron.getBarcodeId(), value);
            return true;
        } else {
            throw new EXC04_UnableToProcessRequest("Invalid request", "Request invalid for " + type.getEntityTypeCodeValue(), path.getPath(), null );
        }        
    }

    private void doLoan(String identifier, Loan loan) {
        EasyRandomParameters parameters = new EasyRandomParameters()
            .seed(123L)
            .objectPoolSize(100)
            .randomizationDepth(3)
            .stringLengthRange(5, 12)
            .collectionSizeRange(1, 10)
            .scanClasspathForConcreteTypes(true)
            .overrideDefaultInitialization(false)
            .ignoreRandomizationErrors(true);
        EasyRandom easyRandom = new EasyRandom(parameters);

        if (identifier == null) {
            LcfCheckOutResponse response = easyRandom.nextObject(LcfCheckOutResponse.class);
            response.setLoan((Loan)directPut(EntityTypes.Type.Loan, loan.getIdentifier(), loan));
            response.setLoanRef(null);
            throw new LCFResponse_CheckOut(response);
        } else {
            if (loan.getLoanStatus().contains(LoanStatusCode.VALUE_8)) {
                LcfCheckInResponse response = easyRandom.nextObject(LcfCheckInResponse.class);
                response.setLoanRef(((Loan)directPut(EntityTypes.Type.Loan, identifier, loan)).getIdentifier());
                response.setReturnLocationRef(UUID.randomUUID().toString());
                throw new LCFResponse_CheckIn(response);
            }
        }
    }

}
