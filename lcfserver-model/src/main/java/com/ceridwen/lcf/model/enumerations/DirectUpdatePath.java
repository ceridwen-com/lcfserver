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

/**
 *
 * @author Matthew.Dovey
 */
public enum DirectUpdatePath {
    PASSWORD("password", Arrays.asList(EntityTypes.Type.Patron)),
    PIN("pin", Arrays.asList(EntityTypes.Type.Patron));

    private String path;
    private List<EntityTypes.Type> applicable;
    
    private DirectUpdatePath(String path, List<EntityTypes.Type> applicable) {
        this.path = path;
        this.applicable = applicable;
    }
    
    public String getPath() {
        return this.path;
    }
    
    public boolean isApplicable(EntityTypes.Type type) {
        return applicable.contains(type);
    }

}
