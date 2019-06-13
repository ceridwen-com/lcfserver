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
package com.ceridwen.lcf.model;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import org.bic.ns.lcf.v1_0.Authorisation;
import org.bic.ns.lcf.v1_0.Authority;
import org.bic.ns.lcf.v1_0.Charge;
import org.bic.ns.lcf.v1_0.ClassScheme;
import org.bic.ns.lcf.v1_0.ClassTerm;
import org.bic.ns.lcf.v1_0.Contact;
import org.bic.ns.lcf.v1_0.EntityType;
import org.bic.ns.lcf.v1_0.Item;
import org.bic.ns.lcf.v1_0.LcfEntity;
import org.bic.ns.lcf.v1_0.Loan;
import org.bic.ns.lcf.v1_0.Location;
import org.bic.ns.lcf.v1_0.Manifestation;
import org.bic.ns.lcf.v1_0.MessageAlert;
import org.bic.ns.lcf.v1_0.Patron;
import org.bic.ns.lcf.v1_0.Payment;
import org.bic.ns.lcf.v1_0.Reservation;

/**
 *
 * @author Matthew.Dovey
 */
public class EntityCodeListClassMapping {
    static Map<EntityType, Class<? extends LcfEntity>> typeMap; 
    static Map<Class, EntityType> clazzMap; 
    static Map<String, EntityType> clazzNameMap;
    
    static {
        typeMap = new EnumMap<>(EntityType.class);
        clazzMap = new HashMap<>();
        clazzNameMap = new HashMap<>();
        put(EntityType.AUTHORISATIONS, Authorisation.class);
        put(EntityType.AUTHORITIES, Authority.class);
        put(EntityType.CHARGES, Charge.class);
        put(EntityType.CLASS_SCHEMES, ClassScheme.class);
        put(EntityType.CLASS_TERMS, ClassTerm.class);
        put(EntityType.CONTACTS, Contact.class);
        put(EntityType.ITEMS, Item.class);
        put(EntityType.LOANS, Loan.class);
        put(EntityType.MANIFESTATIONS, Manifestation.class);
        put(EntityType.MESSAGES, MessageAlert.class);
        put(EntityType.LOCATIONS, Location.class);
        put(EntityType.PATRONS, Patron.class);
        put(EntityType.PAYMENTS, Payment.class);
        put(EntityType.RESERVATIONS, Reservation.class);
    }
    
    private static void put(EntityType entityType, Class entityClazz) {
        typeMap.put(entityType, entityClazz);
        clazzMap.put(entityClazz, entityType);
        clazzNameMap.put(entityClazz.getSimpleName(), entityType);
    }
    
    public static EntityType getEntityType(Class<? extends LcfEntity> entityClazz) {
        return clazzMap.get(entityClazz);
    }
    
    public static EntityType getEntityType(String simpleClassName) {
        return clazzNameMap.get(simpleClassName);     
    }
    
    public static Class<? extends LcfEntity> getEntityClass(EntityType entityType) {
        return typeMap.get(entityType);
    }
}