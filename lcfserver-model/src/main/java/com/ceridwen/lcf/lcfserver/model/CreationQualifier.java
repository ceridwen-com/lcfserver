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

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Matthew.Dovey
 */
public enum CreationQualifier {
    CONFIRMATION("confirmation", Arrays.asList(EntityTypes.Type.Charge, EntityTypes.Type.Loan, EntityTypes.Type.Reservation)),
    CHARGE_ACKNOWLEDGED("charge-acknowledged", Arrays.asList(EntityTypes.Type.Loan, EntityTypes.Type.Reservation));

    private String parameter;
    private List<EntityTypes.Type> applicable;
        
    private CreationQualifier(String parameter, List<EntityTypes.Type> applicable) {
        this.parameter = parameter;
        this.applicable = applicable;
        
    }
    
    public String getParameterText() {
        return this.parameter;
    }
    
    public boolean isApplicable(EntityTypes.Type type) {
        return applicable.contains(type);
    }
}
