/*******************************************************************************
 * Copyright (c) 2016, Matthew J. Dovey (www.ceridwen.com).
 *   
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   
 *     http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *    
 *   
 * Contributors:
 *     Matthew J. Dovey (www.ceridwen.com) - initial API and implementation
 *
 *     
 *******************************************************************************/
package com.ceridwen.lcf.server.backend.hashmap;

import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlRootElement;

import org.bic.ns.lcf.v1_0.Charge;
import org.bic.ns.lcf.v1_0.ClassTerm;
import org.bic.ns.lcf.v1_0.ClassScheme;
import org.bic.ns.lcf.v1_0.Contact;
import org.bic.ns.lcf.v1_0.Item;
import org.bic.ns.lcf.v1_0.Loan;
import org.bic.ns.lcf.v1_0.Location;
import org.bic.ns.lcf.v1_0.Manifestation;
import org.bic.ns.lcf.v1_0.Patron;
import org.bic.ns.lcf.v1_0.Payment;
import org.bic.ns.lcf.v1_0.Property;
import org.bic.ns.lcf.v1_0.Reservation;

import com.ceridwen.lcf.server.core.EntityTypes;
import com.ceridwen.lcf.server.core.persistence.EntitySourcesInterface;
import com.ceridwen.lcf.server.core.persistence.EntitySourceInterface;

@XmlRootElement (name="database")
@XmlAccessorType(XmlAccessType.FIELD)
public class HashMapEntitySources implements EntitySourcesInterface {
	ConcurrentHashMap<String,Charge> charges = new ConcurrentHashMap<>();
	ConcurrentHashMap<String,ClassTerm> classTerms = new ConcurrentHashMap<>();
	ConcurrentHashMap<String,ClassScheme> classSchemes = new ConcurrentHashMap<>();
	ConcurrentHashMap<String,Contact> contacts = new ConcurrentHashMap<>();
	ConcurrentHashMap<String,Item> items = new ConcurrentHashMap<>();
	ConcurrentHashMap<String,Loan> loans = new ConcurrentHashMap<>();
	ConcurrentHashMap<String,Location> locations = new ConcurrentHashMap<>();
	ConcurrentHashMap<String,Manifestation> manifestations = new ConcurrentHashMap<>();
	ConcurrentHashMap<String,Patron> patrons = new ConcurrentHashMap<>();
	ConcurrentHashMap<String,Payment> payments = new ConcurrentHashMap<>();
	ConcurrentHashMap<String,Property> properties = new ConcurrentHashMap<>();
	ConcurrentHashMap<String,Reservation> reservations = new ConcurrentHashMap<>();
	
	@Override
	@SuppressWarnings("unchecked")
	public <T>EntitySourceInterface<T> getEntitySource(EntityTypes.Type type, Class<T> clazz) {		
		switch (type) {
		case Charge:
			return (EntitySourceInterface<T>) new HashMapEntitySource<>(charges);
		case ClassTerm:
			return (EntitySourceInterface<T>) new HashMapEntitySource<>(classTerms);
		case ClassScheme:
			return (EntitySourceInterface<T>) new HashMapEntitySource<>(classSchemes);
		case Contact:
			return (EntitySourceInterface<T>) new HashMapEntitySource<>(contacts);
		case Item:
			return (EntitySourceInterface<T>) new HashMapEntitySource<>(items);
		case Loan:
			return (EntitySourceInterface<T>) new HashMapEntitySource<>(loans);
		case Location:
			return (EntitySourceInterface<T>) new HashMapEntitySource<>(locations);
		case Manifestation:
			return (EntitySourceInterface<T>) new HashMapEntitySource<>(manifestations);
		case Patron:
			return (EntitySourceInterface<T>) new HashMapEntitySource<>(patrons);
		case Payment:
			return (EntitySourceInterface<T>) new HashMapEntitySource<>(payments);
		case Reservation:
			return (EntitySourceInterface<T>) new HashMapEntitySource<>(reservations);
		default:
			return null;
		}		
	}

// Needed for persistence	
	
	public ConcurrentHashMap<String, Charge> getCharges() {
		return charges;
	}

	public void setCharges(ConcurrentHashMap<String, Charge> charges) {
		this.charges = charges;
	}

	public ConcurrentHashMap<String, ClassTerm> getClassTerms() {
		return classTerms;
	}

	public void setClassTerms(ConcurrentHashMap<String, ClassTerm> classTerms) {
		this.classTerms = classTerms;
	}

	public ConcurrentHashMap<String, ClassScheme> getClassSchemes() {
		return classSchemes;
	}

	public void setClassSchemes(ConcurrentHashMap<String, ClassScheme> classSchemes) {
		this.classSchemes = classSchemes;
	}

	public ConcurrentHashMap<String, Contact> getContacts() {
		return contacts;
	}

	public void setContacts(ConcurrentHashMap<String, Contact> contacts) {
		this.contacts = contacts;
	}

	public ConcurrentHashMap<String, Item> getItems() {
		return items;
	}

	public void setItems(ConcurrentHashMap<String, Item> items) {
		this.items = items;
	}

	public ConcurrentHashMap<String, Loan> getLoans() {
		return loans;
	}

	public void setLoans(ConcurrentHashMap<String, Loan> loans) {
		this.loans = loans;
	}

	public ConcurrentHashMap<String, Location> getLocations() {
		return locations;
	}

	public void setLocations(ConcurrentHashMap<String, Location> locations) {
		this.locations = locations;
	}

	public ConcurrentHashMap<String, Manifestation> getManifestations() {
		return manifestations;
	}

	public void setManifestations(ConcurrentHashMap<String, Manifestation> manifestations) {
		this.manifestations = manifestations;
	}

	public ConcurrentHashMap<String, Patron> getPatrons() {
		return patrons;
	}

	public void setPatrons(ConcurrentHashMap<String, Patron> patrons) {
		this.patrons = patrons;
	}

	public ConcurrentHashMap<String, Payment> getPayments() {
		return payments;
	}

	public void setPayments(ConcurrentHashMap<String, Payment> payments) {
		this.payments = payments;
	}

	public ConcurrentHashMap<String, Property> getProperties() {
		return properties;
	}

	public void setProperties(ConcurrentHashMap<String, Property> properties) {
		this.properties = properties;
	}

	public ConcurrentHashMap<String, Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(ConcurrentHashMap<String, Reservation> reservations) {
		this.reservations = reservations;
	}	
}
