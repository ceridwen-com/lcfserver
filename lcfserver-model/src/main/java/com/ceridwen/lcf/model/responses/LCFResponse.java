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
package com.ceridwen.lcf.model.responses;

import com.ceridwen.lcf.model.enumerations.EntityTypes;
import com.ceridwen.lcf.model.exceptions.EXC00_LCF_Exception;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ceridwen Limited
 */
public abstract class LCFResponse extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -3104537015001297384L;
    private int httpStatus;

    /**
     *
     * @return
     */
    public abstract String getIdentifier();
    
    /**
     *
     * @return
     */
    public abstract EntityTypes.Type getEntityType();

    /**
     *
     * @return
     */
    public abstract Object getLCFResponse();

    /**
     *
     * @param response
     */
    protected abstract void setResponse(Object response);

    /**
     *
     * @param httpStatus
     */
    public LCFResponse(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    /**
     *
     * @return
     */
    public List<EXC00_LCF_Exception.CustomHeader> getCustomHeaders() {
        return new ArrayList<>();
    }

    /**
     *
     * @return
     */
    public int getHTTPStatus() {
        return httpStatus;
    }

    /**
     *
     * @param response
     */
    public void setLCFResponse(Object response) {
        try {
            this.setResponse(response);
        } catch (Exception e) {
        }
    }
}
