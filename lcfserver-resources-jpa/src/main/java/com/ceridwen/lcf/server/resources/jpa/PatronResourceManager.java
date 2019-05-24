/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.resources.jpa;

import com.ceridwen.lcf.server.authentication.AbstractAuthenticationToken;
import com.ceridwen.lcf.server.resources.PatronResourceManagerInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import org.bic.ns.lcf.v1_0.AssociatedLocation;
import org.bic.ns.lcf.v1_0.CardStatus;
import org.bic.ns.lcf.v1_0.CardStatusInfo;
import org.bic.ns.lcf.v1_0.Iso639LanguageCode;
import org.bic.ns.lcf.v1_0.Location;
import org.bic.ns.lcf.v1_0.LocationAssociationType;
import org.bic.ns.lcf.v1_0.LocationType;
import org.bic.ns.lcf.v1_0.Patron;
import org.bic.ns.lcf.v1_0.SelectionCriterion;

/**
 *
 * @author Matthew.Dovey
 */
public class PatronResourceManager implements PatronResourceManagerInterface {

    @Override
    public String Create(Map<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken> authTokens, Object parent, Patron entity) {
        return "012345";
    }

    @Override
    public Patron Retrieve(Map<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken> authTokens, String identifier) {
        Location location = new Location();
        location.setIdentifier("default");
        location.setLocationType(LocationType.VALUE_1);
        location.setName("Default location");
        location.setDescription("Default location");

        AssociatedLocation defaultLocation = new AssociatedLocation();
        defaultLocation.setAssociationType(LocationAssociationType.VALUE_1);
        defaultLocation.setLocationRef(location.getIdentifier());
        
        Patron patron = new Patron();
        patron.setIdentifier(identifier);
	patron.setName("A Patron");	
	patron.setLanguage(Iso639LanguageCode.ENG);
	patron.getAssociatedLocation().add(defaultLocation);
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

    @Override
    public Patron Modify(Map<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken> authTokens, String identifier, Patron entity) {
        return entity;
    }

    @Override
    public void Delete(Map<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken> authTokens, String identifier) {
        return;
    }

    @Override
    public List<Patron> Query(Map<AbstractAuthenticationToken.AuthenticationCategory, AbstractAuthenticationToken> authTokens, Object parent, int startIndex, int count, List<SelectionCriterion> selection) {
        List<Patron> list = new ArrayList<Patron>();
        Patron p = new Patron();
        p.setIdentifier("00000");
        list.add(p);
        p = new Patron();
        p.setIdentifier("00001");
        list.add(p);
        p = new Patron();
        p.setIdentifier("00002");
        list.add(p);
        return list;
    }
    
}
