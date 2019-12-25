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

import org.bic.ns.lcf.v1_0.EntityType;
import org.bic.ns.lcf.v1_0.LcfCheckOutResponse;

/**
 *
 * @author Ceridwen Limited
 */
public class LCFResponse_CheckOut extends LCFResponse {

    /**
     *
     */
    private static final long serialVersionUID = -5335981038192006972L;
    private transient LcfCheckOutResponse response;

    /**
     *
     * @param response
     */
    public LCFResponse_CheckOut(LcfCheckOutResponse response) {
        super(201);
        this.response = response;
    }

    /**
     *
     * @return
     */
    @Override
    public Object getLCFResponse() {
        return response;
    }

    /**
     *
     * @param response
     */
    @Override
    public void setResponse(Object response) {
        this.response = (LcfCheckOutResponse) response;
    }

    /**
     *
     * @return
     */
    @Override
    public String getIdentifier() {
        return response.getLoan().getIdentifier();
    }

    /**
     *
     * @return
     */
    @Override
    public EntityType getEntityType() {
        return EntityType.LOANS;
    }

}
