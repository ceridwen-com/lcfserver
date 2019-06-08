/** *****************************************************************************
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
 ****************************************************************************** */
package com.ceridwen.lcf.model.responses;

import com.ceridwen.lcf.model.enumerations.EntityTypes;
import com.ceridwen.lcf.model.exceptions.EXC00_LCF_Exception;
import java.util.ArrayList;
import java.util.List;

public abstract class LCFResponse extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -3104537015001297384L;
    private int httpStatus;

    public abstract String getIdentifier();
    
    public abstract EntityTypes.Type getEntityType();

    public abstract Object getLCFResponse();

    protected abstract void setResponse(Object response);

    public LCFResponse(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public List<EXC00_LCF_Exception.CustomHeader> getCustomHeaders() {
        return new ArrayList<>();
    }

    public int getHTTPStatus() {
        return httpStatus;
    }

    public void setLCFResponse(Object response) {
        try {
            this.setResponse(response);
        } catch (Exception e) {
        }
    }
}
