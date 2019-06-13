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
package com.ceridwen.lcf.model.enumerations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bic.ns.lcf.v1_0.LcfCheckInResponse;
import org.bic.ns.lcf.v1_0.LcfCheckOutResponse;
import org.bic.ns.lcf.v1_0.LcfEntity;
import org.bic.ns.lcf.v1_0.Loan;

/**
 *
 * @author Ceridwen Limited
 */
public class AlternativeResponseFormats {
    private class ResponseFormats {
        public ResponseFormats(List<Class> get, List<Class> post, List<Class> put, List<Class> delete) {
            this.GET = get;
            this.POST = post;
            this.PUT = put;
            this.DELETE = delete;
        }
        
        private List<Class> GET;
        private List<Class> POST;
        private List<Class> PUT;
        private List<Class> DELETE;
    }
    
    Map<Class<? extends LcfEntity>, ResponseFormats> alternativeFormats = new HashMap<>();
    
    /**
     *
     */
    public AlternativeResponseFormats() 
    {
        alternativeFormats.put(Loan.class, new ResponseFormats(new ArrayList<>(), Arrays.asList(LcfCheckOutResponse.class), Arrays.asList(LcfCheckInResponse.class), new ArrayList<>()));
    }
    
    /**
     *
     * @param clazz
     * @return
     */
    public List<Class> getAlternativeGetFormat(Class<? extends LcfEntity> clazz) {
        if (alternativeFormats.containsKey(clazz)) {
            return alternativeFormats.get(clazz).GET;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     *
     * @param clazz
     * @return
     */
    public List<Class> getAlternativePostFormat(Class<? extends LcfEntity> clazz) {
        if (alternativeFormats.containsKey(clazz)) {
            return alternativeFormats.get(clazz).POST;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     *
     * @param clazz
     * @return
     */
    public List<Class> getAlternativePutFormat(Class<? extends LcfEntity> clazz) {
        if (alternativeFormats.containsKey(clazz)) {
            return alternativeFormats.get(clazz).PUT;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     *
     * @param clazz
     * @return
     */
    public List<Class> getAlternativeDeleteFormat(Class<? extends LcfEntity> clazz) {
        if (alternativeFormats.containsKey(clazz)) {
            return alternativeFormats.get(clazz).DELETE;
        } else {
            return new ArrayList<>();
        }
    }
}
