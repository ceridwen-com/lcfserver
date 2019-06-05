/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.resources.memory;

import com.ceridwen.lcf.lcfserver.model.EntityTypes;
import com.ceridwen.lcf.lcfserver.model.authentication.AbstractAuthenticationToken;
import com.ceridwen.lcf.lcfserver.model.authentication.BasicAuthenticationToken;
import com.ceridwen.lcf.lcfserver.model.exceptions.EXC02_InvalidUserCredentials;
import com.ceridwen.lcf.lcfserver.model.exceptions.EXC03_InvalidTerminalCredentials;
import com.ceridwen.lcf.server.resources.QueryResults;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import org.bic.ns.lcf.v1_0.CardStatus;
import org.bic.ns.lcf.v1_0.CardStatusInfo;
import org.bic.ns.lcf.v1_0.Iso639LanguageCode;
import org.bic.ns.lcf.v1_0.Patron;
import org.bic.ns.lcf.v1_0.SelectionCriterion;


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
    private Map<EntityTypes.Type, Map<AbstractAuthenticationToken.AuthenticationCategory, BasicAuthenticationToken>> authentications;
    
    public static MemoryResourceManager getMemoryResourceManager() {
        if (mgr == null) {
            mgr = new MemoryResourceManager();
        }
        return mgr;
    }
    
    private Patron GeneratePatron() {
        Patron patron = new Patron();
        patron.setIdentifier(UUID.randomUUID().toString());
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
        this.authentications = new HashMap<>();
        
        int amount = (new Random()).nextInt(25)+5;
        for (int i=0; i < amount; i++) {
            Patron patron = this.GeneratePatron();
            this.put(EntityTypes.Type.Patron, patron.getIdentifier(), null, patron, new HashMap<>());
        }
        
        Map<AbstractAuthenticationToken.AuthenticationCategory, BasicAuthenticationToken> authorisationsCredentials = new EnumMap<>(AbstractAuthenticationToken.AuthenticationCategory.class);
        authorisationsCredentials.put(AbstractAuthenticationToken.AuthenticationCategory.TERMINAL, new BasicAuthenticationToken(AbstractAuthenticationToken.AuthenticationCategory.TERMINAL, "terminal", "password"));
        authorisationsCredentials.put(AbstractAuthenticationToken.AuthenticationCategory.USER, new BasicAuthenticationToken(AbstractAuthenticationToken.AuthenticationCategory.USER, "user", "patron"));
        authentications.put(EntityTypes.Type.Authorisation, authorisationsCredentials);
    }
    
    public void Authenticate(EntityTypes.Type type, Map<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken> authTokens) {
        Map<AbstractAuthenticationToken.AuthenticationCategory, BasicAuthenticationToken> entityAuthentications = authentications.get(type);
        
        if (entityAuthentications == null) {
            return;
        }
        
        BasicAuthenticationToken token;
        BasicAuthenticationToken mytoken;
        
        token = entityAuthentications.get(AbstractAuthenticationToken.AuthenticationCategory.TERMINAL);
        if (token != null) {
            mytoken = (BasicAuthenticationToken)authTokens.get(AbstractAuthenticationToken.AuthenticationCategory.TERMINAL);
            if (mytoken == null) {
                throw new EXC03_InvalidTerminalCredentials();
            }
            if (!(mytoken.getUsername().equals(token.getUsername()) && mytoken.getPassword().equals(token.getPassword()))) {
                throw new EXC03_InvalidTerminalCredentials();
            }
        }

        token = entityAuthentications.get(AbstractAuthenticationToken.AuthenticationCategory.USER);
        if (token != null) {
            mytoken = (BasicAuthenticationToken)authTokens.get(AbstractAuthenticationToken.AuthenticationCategory.USER);
            if (mytoken == null) {
                throw new EXC02_InvalidUserCredentials();
            }
            if (!(mytoken.getUsername().equals(token.getUsername()) && mytoken.getPassword().equals(token.getPassword()))) {
                throw new EXC02_InvalidUserCredentials();
            }
        }
    }
    
    public Object get(EntityTypes.Type type, String identifier, Map<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken> authTokens) {
        Authenticate(type, authTokens);
        
        if (database.containsKey(type)) {
            Map<String, Object> map = database.get(type);
            if (map.containsKey(identifier)) {
                return map.get(identifier);
            }
        }
        return null;
    }
  
    public Object put(EntityTypes.Type type, String identifier, Object parent, Object data, Map<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken> authTokens) {
        Authenticate(type, authTokens);

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
    
    public void delete(EntityTypes.Type type, String identifier, Map<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken> authTokens) {
        Authenticate(type, authTokens);

        if (database.containsKey(type)) {
            Map<String, Object> map = database.get(type);
            if (map.containsKey(identifier)) {
                map.remove(identifier);
            }
        }
    }
    
    public QueryResults<? extends Object> list(EntityTypes.Type type, Object parent, int startIndex, int count, List<SelectionCriterion> selection, Map<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken> authTokens) {
        Authenticate(type, authTokens);

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
    
    
}
