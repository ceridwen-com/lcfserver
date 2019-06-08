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
package com.ceridwen.lcf.server.legacy.filter;

import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.bic.ns.lcf.v1_0.AssociatedLocation;
import org.bic.ns.lcf.v1_0.CardStatus;
import org.bic.ns.lcf.v1_0.CardStatusInfo;
import org.bic.ns.lcf.v1_0.CirculationStatusCode;
import org.bic.ns.lcf.v1_0.CommunicationType;
import org.bic.ns.lcf.v1_0.Contact;
import org.bic.ns.lcf.v1_0.Item;
import org.bic.ns.lcf.v1_0.Iso639LanguageCode;
import org.bic.ns.lcf.v1_0.Location;
import org.bic.ns.lcf.v1_0.LocationAssociationType;
import org.bic.ns.lcf.v1_0.LocationType;
import org.bic.ns.lcf.v1_0.Manifestation;
import org.bic.ns.lcf.v1_0.ManifestationStatus;
import org.bic.ns.lcf.v1_0.Patron;
import org.bic.ns.lcf.v1_0.Title;
import org.bic.ns.lcf.v1_0.TitleType;

import com.ceridwen.lcf.model.enumerations.EntityTypes;
import com.ceridwen.lcf.server.legacy.EntitySourceInterface;
import com.ceridwen.lcf.server.legacy.EntitySourcesInterface;
import com.ceridwen.lcf.server.legacy.filter.EntitySourcesFilter;



/**
 * 
 * Pipeline overlaying default data
 * 
 */
public class DefaultDataLoaderFilter implements EntitySourcesFilter {
		
    /**
     *
     * @param entitySources
     * @return
     */
    @SuppressWarnings("unchecked")
	@Override
	public EntitySourcesInterface filters(EntitySourcesInterface entitySources) {

		int counter = 0;
		
		for (EntityTypes.Type type: EntityTypes.Type.values()) {
			counter += entitySources.getEntitySource(type, type.getTypeClass()).Query("", 0, 1).getTotalResults();
		}
		
		if (counter == 0) {
			System.out.println("Loading default data...");
			try {
				loadDefaultData(entitySources);
				System.out.println("Default data loaded");
			} catch (Exception e) {
				System.out.println("Default data load aborted due to error: " + e.getMessage());
			}
		}

		return entitySources;
	}

	private void loadDefaultData(EntitySourcesInterface database) {
		AssociatedLocation defaultLocation = populateLocations((EntitySourceInterface<Location>)database.getEntitySource(EntityTypes.Type.Location, Location.class));
		populatePatrons((EntitySourceInterface<Patron>)database.getEntitySource(EntityTypes.Type.Patron, Patron.class),
						(EntitySourceInterface<Contact>)database.getEntitySource(EntityTypes.Type.Contact, Contact.class),
						defaultLocation);		
		populateManifestations((EntitySourceInterface<Manifestation>)database.getEntitySource(EntityTypes.Type.Manifestation, Manifestation.class),
				(EntitySourceInterface<Item>)database.getEntitySource(EntityTypes.Type.Item, Item.class),
				defaultLocation);		
	}
	
	private AssociatedLocation populateLocations(EntitySourceInterface<Location> locationSource) {
		
		Location location = new Location();
		location.setIdentifier("default");
		location.setLocationType(LocationType.VALUE_1);
		location.setName("Default location");
		location.setDescription("Default location");

		locationSource.Create(location);
		
		AssociatedLocation defaultLocation = new AssociatedLocation();
		defaultLocation.setAssociationType(LocationAssociationType.VALUE_1);
		defaultLocation.setLocationRef(location.getIdentifier());
		return defaultLocation;
	}

	private void populateContacts(EntitySourceInterface<Contact> contactSource, Patron patron, String email, String house) {
		Contact contact = new Contact();
		contact.setPatronRef(patron.getIdentifier());
//		patron.getContactRef().add(contact.getIdentifier());
		contact.setCommunicationType(CommunicationType.VALUE_5);
		contact.getLocator().add(email);
		
		contactSource.Create(contact);
		
		contact = new Contact();
		contact.setPatronRef(patron.getIdentifier());
//		patron.getContactRef().add(contact.getIdentifier());
		contact.setCommunicationType(CommunicationType.VALUE_4);
		contact.getLocator().add(house + " Library Avenue");
		contact.getLocator().add("Library Town");
		contact.getLocator().add("Libraryshire");

		contactSource.Create(contact);
	}
	
	private void populatePatrons(EntitySourceInterface<Patron> patronSource, EntitySourceInterface<Contact> contactSource, AssociatedLocation defaultLocation) {
		Patron patron = new Patron();
		patron.setName("A Patron");	
		patron.setLanguage(Iso639LanguageCode.ENG);
		patron.getAssociatedLocation().add(defaultLocation);
		CardStatusInfo csi = new CardStatusInfo();
		csi.setCardStatus(CardStatus.VALUE_1);
		patron.setCardStatusInfo(csi);
		try {
			patron.setDateOfBirth(DatatypeFactory.newInstance().newXMLGregorianCalendarDate(1960, 1, 1, 0));
		} catch (DatatypeConfigurationException e) {
			// as this is demo data, not crucial if DOB not set.
		}
		
		patronSource.Create(patron);
		populateContacts(contactSource, patron, "a.patron@library", "1");

		patron = new Patron();
		patron.setIdentifier(UUID.randomUUID().toString());
		patron.setName("Boe Rower");
		patron.setLanguage(Iso639LanguageCode.ENG);
		patron.getAssociatedLocation().add(defaultLocation);
		csi = new CardStatusInfo();
		csi.setCardStatus(CardStatus.VALUE_1);
		patron.setCardStatusInfo(csi);
		try {
			patron.setDateOfBirth(DatatypeFactory.newInstance().newXMLGregorianCalendarDate(1980, 1, 1, 0));
		} catch (DatatypeConfigurationException e) {
			// as this is demo data, not crucial if DOB not set.
		}
		
		patronSource.Create(patron);
		
		populateContacts(contactSource, patron, "boe.rower@library", "2");
	}
	
	private void populateManifestations(EntitySourceInterface<Manifestation> manifestationSource, EntitySourceInterface<Item> itemSource, AssociatedLocation defaultLocation) {
		Manifestation man = new Manifestation();
		Title title = new Title();
		title.setTitleText("Test book");
		title.setTitleType(TitleType.VALUE_1);
		man.getTitle().add(title);
		man.setManifestationStatus(ManifestationStatus.VALUE_1);
		man.setPublisherName("Test");
		manifestationSource.Create(man);
		
		for (int i=0; i <10; i++) {
			populateItems(itemSource, man, defaultLocation);
		}
		man = new Manifestation();
		title = new Title();
		title.setTitleText("Text book");
		title.setTitleType(TitleType.VALUE_1);
		man.getTitle().add(title);
		man.setManifestationStatus(ManifestationStatus.VALUE_1);
		man.setPublisherName("Text");
		manifestationSource.Create(man);
		
		for (int i=0; i <5; i++) {
			populateItems(itemSource, man, defaultLocation);
		}
	}

	private void populateItems(EntitySourceInterface<Item> itemSource, Manifestation manifestation, AssociatedLocation defaultLocation) {
		Item item = new Item();
		item.setManifestationRef(manifestation.getIdentifier());
		item.setCirculationStatus(CirculationStatusCode.VALUE_3);
		item.getAssociatedLocation().add(defaultLocation);
		itemSource.Create(item);
	}
}
