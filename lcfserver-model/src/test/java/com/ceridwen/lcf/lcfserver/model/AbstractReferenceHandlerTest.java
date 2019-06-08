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
package com.ceridwen.lcf.lcfserver.model;

import com.ceridwen.lcf.model.referencing.AbstractReferenceHandler;
import com.ceridwen.lcf.model.enumerations.EntityTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bic.ns.lcf.v1_0.LcfCheckInResponse;
import org.bic.ns.lcf.v1_0.LcfCheckOutResponse;
import org.bic.ns.lcf.v1_0.LcfEntityListResponse;
import org.bic.ns.lcf.v1_0.LcfException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Ceridwen Limited
 */


class TestReferenceHandler extends AbstractReferenceHandler {
    
    public boolean testReferences(Class clazz) {
        return iterateProperties(clazz, null, null, null, "", "");
    }
    
    @Override
    protected void handleReference(Class clazz, Object instance, String propertyReference, String refprefix) {
    }
    
}

/**
 *
 * @author Ceridwen Limited
 */
public class AbstractReferenceHandlerTest {
    
    /**
     *
     */
    public AbstractReferenceHandlerTest() {
    }
    
    /**
     *
     */
    @BeforeAll
    public static void setUpClass() {
    }
    
    /**
     *
     */
    @AfterAll
    public static void tearDownClass() {
    }
    
    /**
     *
     */
    @BeforeEach
    public void setUp() {
    }
    
    /**
     *
     */
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of checkReferences method, of class ReferenceHandler.
     */
    @org.junit.jupiter.api.Test
    public void testCheckReferences() {
        Logger.getLogger(AbstractReferenceHandlerTest.class.getName()).log(Level.INFO, "Checking references");      
        List<Class> clazzes = new ArrayList<>();
        for (EntityTypes.Type type: EntityTypes.Type.values()) {
            clazzes.add(type.getTypeClass());
        }
        
        clazzes.add(LcfEntityListResponse.class);
        clazzes.add(LcfCheckInResponse.class);
        clazzes.add(LcfCheckOutResponse.class);
        clazzes.add(LcfException.class);
        
        boolean success = true;
        for (Class clazz: clazzes) {
            if (new TestReferenceHandler().testReferences(clazz)) {
                Logger.getLogger(AbstractReferenceHandlerTest.class.getName()).log(Level.INFO, "{0} : SUCCESS", clazz.getName());      
            } else {
                Logger.getLogger(AbstractReferenceHandlerTest.class.getName()).log(Level.WARNING, "{0} : FAILURE", clazz.getName());      
                success = false;
            }
        }

        if (!success) {
            fail("Unhandled references");
        }
    }
    
}
