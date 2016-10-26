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
package com.ceridwen.lcf.server.core.referencing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.function.UnaryOperator;

import org.apache.commons.lang.StringUtils;
import org.bic.ns.lcf.v1_0.Charge;
import org.bic.ns.lcf.v1_0.ClassScheme;
import org.bic.ns.lcf.v1_0.ClassTerm;
import org.bic.ns.lcf.v1_0.Contact;
import org.bic.ns.lcf.v1_0.Item;
import org.bic.ns.lcf.v1_0.LcfCheckInResponse;
import org.bic.ns.lcf.v1_0.LcfCheckOutResponse;
import org.bic.ns.lcf.v1_0.LcfEntityListResponse;
import org.bic.ns.lcf.v1_0.Loan;
import org.bic.ns.lcf.v1_0.Location;
import org.bic.ns.lcf.v1_0.Manifestation;
import org.bic.ns.lcf.v1_0.Patron;
import org.bic.ns.lcf.v1_0.Payment;
import org.bic.ns.lcf.v1_0.Property;
import org.bic.ns.lcf.v1_0.Reservation;

import com.ceridwen.lcf.server.core.EntityTypes;
import com.ceridwen.lcf.server.core.referencing.editor.ReferenceEditor;
import com.ceridwen.lcf.server.core.referencing.modifier.ChargeModifier;
import com.ceridwen.lcf.server.core.referencing.modifier.ClassSchemeModifier;
import com.ceridwen.lcf.server.core.referencing.modifier.ClassTermModifier;
import com.ceridwen.lcf.server.core.referencing.modifier.ContactModifier;
import com.ceridwen.lcf.server.core.referencing.modifier.ItemModifier;
import com.ceridwen.lcf.server.core.referencing.modifier.LcfCheckInResponseModifier;
import com.ceridwen.lcf.server.core.referencing.modifier.LcfCheckOutResponseModifier;
import com.ceridwen.lcf.server.core.referencing.modifier.LcfEntityListResponseModifier;
import com.ceridwen.lcf.server.core.referencing.modifier.LoanModifier;
import com.ceridwen.lcf.server.core.referencing.modifier.LocationModifier;
import com.ceridwen.lcf.server.core.referencing.modifier.ManifestationModifier;
import com.ceridwen.lcf.server.core.referencing.modifier.NullModifier;
import com.ceridwen.lcf.server.core.referencing.modifier.PatronModifier;
import com.ceridwen.lcf.server.core.referencing.modifier.PaymentModifier;
import com.ceridwen.lcf.server.core.referencing.modifier.PropertyModifier;
import com.ceridwen.lcf.server.core.referencing.modifier.ReservationModifier;
import com.ceridwen.util.xml.XmlUtilities;

public abstract class Referencer<EntityType> {
	protected ReferenceEditor editor;
	protected EntityType entity;
	
	protected Referencer(EntityType entity, ReferenceEditor editor) {
		this.entity = entity;
		this.editor = editor;
	}
		
	public EntityType dereference() {
		return updateReferences(entity, false);
	}

	public EntityType reference() {
		return updateReferences(entity, true);
	}
	
	protected UnaryOperator<String> getUpdateOperator(final EntityTypes.Type entityType, final boolean addUrlPrefix) {
		return new UnaryOperator<String>() {
			@Override
			public String apply(String t) {
				return update(t, entityType, addUrlPrefix);
			}				
		};	
	}
	
	protected String update(String id, EntityTypes.Type entityType, boolean addUrlPrefix) {
		if (StringUtils.isNotBlank(id)) {
			if (addUrlPrefix) {
				return editor.reference(id, entityType);
			} else {
				return editor.dereference(id, entityType);
			}
		} else {
			return id;
		}
	}
	
	protected abstract EntityType updateReferences(EntityType entityType, final boolean addUrlPrefix);

	public static Object deepSerialiseClone(Object object) {
	   try {
	     ByteArrayOutputStream baos = new ByteArrayOutputStream();
	     ObjectOutputStream oos = new ObjectOutputStream(baos);
	     oos.writeObject(object);
	     ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	     ObjectInputStream ois = new ObjectInputStream(bais);
	     return ois.readObject();
	   }
	   catch (Exception e) {
	     e.printStackTrace();
	     return null;
	   }
	 }

	 @SuppressWarnings("unchecked")
	public static <T>T deepJasbClone(T data) {	
		 try {
		     ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			 OutputStreamWriter writer = new OutputStreamWriter(buffer, "UTF-8");
			 XmlUtilities.generateXML(writer, data);
		     ByteArrayInputStream stream = new ByteArrayInputStream(buffer.toByteArray());
			 return XmlUtilities.processXML(stream, (Class<T>)data.getClass());
		 } catch (Exception e) {
		     e.printStackTrace();
		     return null;
	     }
	 }

	public static <T> Referencer<?> factory(T entity, ReferenceEditor editor) {	
		if (entity instanceof Charge) {
                    return new ChargeModifier((Charge)deepJasbClone(entity), editor);
                } 
		if (entity instanceof ClassTerm) {
                    return new ClassTermModifier((ClassTerm)deepJasbClone(entity), editor);
                } 
		if (entity instanceof ClassSchemeModifier) {
                    return new ClassSchemeModifier((ClassScheme)deepJasbClone(entity), editor);
                } 
		if (entity instanceof Contact) {
                    return new ContactModifier((Contact)deepJasbClone(entity), editor);
                } 
		if (entity instanceof Item) {
                    return new ItemModifier((Item)deepJasbClone(entity), editor);
                } 
		if (entity instanceof Loan) {
                    return new LoanModifier((Loan)deepJasbClone(entity), editor);
                }
		if (entity instanceof Location) {
                    return new LocationModifier((Location)deepJasbClone(entity), editor);
                } 
		if (entity instanceof Manifestation) {
                    return new ManifestationModifier((Manifestation)deepJasbClone(entity), editor);
                } 
		if (entity instanceof Patron) {
                    return new PatronModifier((Patron)deepJasbClone(entity), editor);
                } 
		if (entity instanceof Payment) {
                    return new PaymentModifier((Payment)deepJasbClone(entity), editor);
                } 
		if (entity instanceof Property) {
                    return new PropertyModifier((Property)deepJasbClone(entity), editor);
                } 
		if (entity instanceof Reservation) {
                    return new ReservationModifier((Reservation)deepJasbClone(entity), editor);
                }
			
		if (entity instanceof LcfEntityListResponse) {
                    return new LcfEntityListResponseModifier((LcfEntityListResponse)deepJasbClone(entity), editor);
                } 
		if (entity instanceof LcfCheckInResponse) {
                    return new LcfCheckInResponseModifier((LcfCheckInResponse)deepJasbClone(entity), editor);
                } 
		if (entity instanceof LcfCheckOutResponse) {
                    return new LcfCheckOutResponseModifier((LcfCheckOutResponse)deepJasbClone(entity), editor);
                } 
	
		return new NullModifier(entity, editor);
		
//		throw new EXC01_ServiceUnavailable("Unrecognised XML content", "Unrecognised XML content", null, new Exception("Referencing not defined for class " + entity.getClass().getName()));
	}
}
