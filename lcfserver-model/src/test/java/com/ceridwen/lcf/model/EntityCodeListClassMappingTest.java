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

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlSeeAlso;
import org.bic.ns.lcf.v1_0.EntityType;
import org.bic.ns.lcf.v1_0.LcfEntity;
import org.bic.ns.lcf.v1_0.Property;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Matthew.Dovey
 */
public class EntityCodeListClassMappingTest {
    
    public EntityCodeListClassMappingTest() {
    }
    

    /**
     * Test of getEntityType method, of class EntityTypesToClasses.
     */
    @Test
    public void testTypeToClasses() {
        boolean success = true;
        
        for (EntityType type: EntityType.values()) {
            if (EntityCodeListClassMapping.getEntityClass(type) == null) {
                success = false;
                Logger.getLogger(AbstractReferenceHandlerTest.class.getName()).log(Level.INFO, "Looking up entity class for codelist {0} : FAIL", type.value());      
            } else {
                Logger.getLogger(AbstractReferenceHandlerTest.class.getName()).log(Level.INFO, "Looking up entity class for codelist {0} : SUCCESS", type.value());      
            }
        }
        if (!success) {
            fail("Not all Entity codelist entries mapped to Entity class");
        }
    }

    
    List<Class<? extends LcfEntity>> deprecated = Arrays.asList(Property.class);
    
    @Test
    public void testClassesToType() {
        boolean success = true;
        
        XmlSeeAlso seeAlso = LcfEntity.class.getAnnotation(XmlSeeAlso.class);
        
        
        for (Class clazz: seeAlso.value()) {
            if (EntityCodeListClassMapping.getEntityType(clazz) == null) {
                if (!deprecated.contains(clazz)) {
                    success = false;
                    Logger.getLogger(AbstractReferenceHandlerTest.class.getName()).log(Level.INFO, "Looking up codelist for entity class {0} : FAIL", clazz.getSimpleName());      
                } else {
                    Logger.getLogger(AbstractReferenceHandlerTest.class.getName()).log(Level.INFO, "Looking up codelist for deprecated entity class {0} : SUCCESS", clazz.getSimpleName());      
                }
            } else {
                if (deprecated.contains(clazz)) {
                    success = false;
                    Logger.getLogger(AbstractReferenceHandlerTest.class.getName()).log(Level.INFO, "Looking up codelist for deprecated entity class {0} : FAIL", clazz.getSimpleName());      
                } else {
                    Logger.getLogger(AbstractReferenceHandlerTest.class.getName()).log(Level.INFO, "Looking up codelist for entity class {0} : SUCCESS", clazz.getSimpleName());      
                }
            }
        }
        if (!success) {
            fail("Not all Entity class entries mapped to Entity codelist");
        }
    }
    
}
