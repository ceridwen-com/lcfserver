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

import java.util.Arrays;
import java.util.List;
import org.bic.ns.lcf.v1_0.LcfEntity;
import org.bic.ns.lcf.v1_0.Patron;

/**
 *
 * @author Ceridwen Limited
 */
public enum DirectUpdatePath {

    /**
     *
     */
    PASSWORD("password", Arrays.asList(Patron.class)),

    /**
     *
     */
    PIN("pin", Arrays.asList(Patron.class));

    private String path;
    private List<Class<? extends LcfEntity>> applicable;
    
    private DirectUpdatePath(String path, List<Class<? extends LcfEntity>> applicable) {
        this.path = path;
        this.applicable = applicable;
    }
    
    /**
     *
     * @return
     */
    public String getPath() {
        return this.path;
    }
    
    /**
     *
     * @param clazz
     * @return
     */
    public boolean isApplicable(Class<? extends LcfEntity> clazz) {
        return applicable.contains(clazz);
    }

}
