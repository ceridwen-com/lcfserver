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
package com.ceridwen.lcf.server.core.integrity;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


import org.apache.commons.lang.StringUtils;
import org.bic.ns.lcf.v1_0.Charge;
import org.bic.ns.lcf.v1_0.Contact;
import org.bic.ns.lcf.v1_0.Item;
import org.bic.ns.lcf.v1_0.Loan;
import org.bic.ns.lcf.v1_0.Location;
import org.bic.ns.lcf.v1_0.Manifestation;
import org.bic.ns.lcf.v1_0.Patron;
import org.bic.ns.lcf.v1_0.Reservation;

import com.ceridwen.lcf.server.core.EntityTypes;

import org.bic.ns.lcf.v1_0.AssociatedLocation;

// FIXME Complete all relationship entries

public class RelationshipFactory {
	
	private static final List<Relationship<?,?>> relationships = new ArrayList<Relationship<?,?>>() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1;
		{

			add(new Relationship<Manifestation,Item>(
				EntityTypes.Type.Manifestation, 
				new SingletonRef<Item>(
					o -> {return o.getManifestationRef();},
					(o,s) -> {o.setManifestationRef(s);}
				),
				EntityTypes.Type.Item, 
				new ListRef<Manifestation>(
					(o,s) -> {o.getItemRef().add(s);},
					(o,s) -> {o.getItemRef().remove(s);},
					(o,s) -> {return o.getItemRef().contains(s);},
					o -> { return o.getItemRef(); }
				),
				Relationship.Integrity.ParentRequired
			));		
			
			add(new Relationship<Item, Location>(
				EntityTypes.Type.Item,
				new NullListRef<Location>(),
				EntityTypes.Type.Location, 
				new ListRef<Item>(
					(o,s) -> {}, // AssociatedLocation is complex type so cannot be added automatically
					(o,s) -> {	o.getAssociatedLocation().removeIf(
									a -> { return StringUtils.equals(a.getLocationRef(), s); }
								);
							},
					(o,s) -> {	for (AssociatedLocation aloc: o.getAssociatedLocation()) {
									if (StringUtils.equals(aloc.getLocationRef(), s)) {
										return true;
									}
								}
								return false;
							 },
					o -> {    Vector<String> locs = new Vector<>();
                                o.getAssociatedLocation().forEach(
                                	a -> {locs.add(a.getLocationRef()); }
                                );					
								return locs;
							}
				)
			));				
			
			add(new Relationship<Item, Reservation>(
				EntityTypes.Type.Item, 
				new SingletonRef<Reservation>(
					o -> {return o.getItemRef();},
					(o,s) -> {o.setItemRef(s);}
				),
				EntityTypes.Type.Reservation, 
				new ListRef<Item>(
					(o,s) -> {o.getReservationRef().add(s);},
					(o,s) -> {o.getReservationRef().remove(s);},
					(o,s) -> {return o.getReservationRef().contains(s);},
					o -> {return (o.getReservationRef());}
				),
				Relationship.Integrity.ParentRequired
			));

			add(new Relationship<Item, Loan>(
				EntityTypes.Type.Item, 
				new SingletonRef<Loan>(
					o -> {return o.getItemRef();},
					(o,s) -> {o.setItemRef(s);}
				),
				EntityTypes.Type.Loan, 
				new SingletonRef<Item>(
					o -> {return o.getOnLoanRef();},
					(o,s) -> {o.setOnLoanRef(s);}
				),
				Relationship.Integrity.ParentRequired
			));			
			
			add(new Relationship<Patron, Contact>(
				EntityTypes.Type.Patron, 
				new SingletonRef<Contact>(
					o -> {return o.getPatronRef();},
					(o,s) -> {o.setPatronRef(s);}
				),
				EntityTypes.Type.Contact, 
				new ListRef<Patron>(
					(o,s) -> {o.getContactRef().add(s);},
					(o,s) -> {o.getContactRef().remove(s);},
					(o,s) -> {return o.getContactRef().contains(s);},
					o -> {return o.getContactRef();}
				),
				Relationship.Integrity.ParentRequired
			));

			add(new Relationship<Patron, Location>(
				EntityTypes.Type.Patron, 
				new NullListRef<Location>(),
				EntityTypes.Type.Location, 
				new ListRef<Patron>(
					(o,s) -> {}, // AssociatedLocation is complex type so cannot be added automatically
					(o,s) -> {	o.getAssociatedLocation().removeIf(
									a -> { return StringUtils.equals(a.getLocationRef(), s); }
								);
							},
					(o,s) -> {	for (AssociatedLocation aloc: o.getAssociatedLocation()) {
									if (StringUtils.equals(aloc.getLocationRef(), s)) {
										return true;
									}
								}
								return false;
							 },
					o -> {    Vector<String> locs = new Vector<>();
		                        o.getAssociatedLocation().forEach(
		                        	a -> {locs.add(a.getLocationRef()); }
		                        );					
								return locs;
							}
				)
			));
			
			add(new Relationship<Patron, Loan>(
				EntityTypes.Type.Patron, 
				new SingletonRef<Loan>(
					o -> {return o.getPatronRef();},
					(o,s) -> {o.setPatronRef(s);}
				),
				EntityTypes.Type.Loan, 
				new ListRef<Patron>(
					(o,s) -> {o.getLoanRef().add(s);},
					(o,s) -> {o.getLoanRef().remove(s);},
					(o,s) -> {return o.getLoanRef().contains(s);},
					o -> {return o.getLoanRef();}
				),
				Relationship.Integrity.ParentRequired
			));

			add(new Relationship<Patron, Reservation>(
				EntityTypes.Type.Patron, 
				new SingletonRef<Reservation>(
					o -> {return o.getPatronRef();},
					(o,s) -> {o.setPatronRef(s);}
				),
				EntityTypes.Type.Reservation, 
				new ListRef<Patron>(
					(o,s) -> {o.getReservationRef().add(s);},
					(o,s) -> {o.getReservationRef().remove(s);},
					(o,s) -> {return o.getReservationRef().contains(s);},
					o -> {return o.getReservationRef();}
				),
				Relationship.Integrity.ParentRequired
			));
			
			add(new Relationship<Patron, Charge>(
				EntityTypes.Type.Patron, 
				new SingletonRef<Charge>(
					o -> {return o.getPatronRef();},
					(o,s) -> {o.setPatronRef(s);}
				),
				EntityTypes.Type.Charge, 
				new ListRef<Patron>(
					(o,s) -> {o.getChargeRef().add(s);},
					(o,s) -> {o.getChargeRef().remove(s);},
					(o,s) -> {return o.getChargeRef().contains(s);},
					o -> {return o.getChargeRef();}
				),
				Relationship.Integrity.ParentRequired
			));
			
		}
	};
	
	public static List<Relationship<?,?>> getRelationshipsAsParent(EntityTypes.Type parent) {
		List<Relationship<?,?>> result = new Vector<>();		
		for (Relationship<?,?> r: relationships) {
			if (r.getParentType() == parent) {
				result.add(r);
			}
		}
		
		return result;
	}

	public static List<Relationship<?,?>> getRelationshipsAsChild(EntityTypes.Type child) {
		List<Relationship<?,?>> result = new Vector<>();		
		for (Relationship<?,?> r: relationships) {
			if (r.getChildType() == child) {
				result.add(r);
			}
		}
		
		return result;
	}
	
	public static Relationship<?,?> getRelationship(EntityTypes.Type parent, EntityTypes.Type child) {
		for (Relationship<?,?> r: relationships) {
			if (r.getParentType() == parent && r.getChildType() == child) {
				return r;
			}			
		}
		
		return null;		
	}

}
