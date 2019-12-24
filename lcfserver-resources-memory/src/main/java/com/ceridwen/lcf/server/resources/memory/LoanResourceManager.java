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
package com.ceridwen.lcf.server.resources.memory;

import com.ceridwen.lcf.server.resources.memory.database.Database;
import com.ceridwen.lcf.model.authentication.AuthenticationToken;
import com.ceridwen.lcf.model.enumerations.CreationQualifier;
import com.ceridwen.lcf.model.enumerations.EntityTypes;
import com.ceridwen.lcf.model.responses.LCFResponse_CheckIn;
import com.ceridwen.lcf.model.responses.LCFResponse_CheckOut;
import com.ceridwen.lcf.server.resources.LoanResourceManagerInterface;
import java.util.List;
import java.util.UUID;
import org.bic.ns.lcf.v1_0.LcfCheckInResponse;
import org.bic.ns.lcf.v1_0.LcfCheckOutResponse;
import org.bic.ns.lcf.v1_0.Loan;
import org.bic.ns.lcf.v1_0.LoanStatusCode;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

/**
 *
 * @author Ceridwen Limited
 */
public class LoanResourceManager extends AbstractResourceManager<Loan> implements LoanResourceManagerInterface{

    @Override
    public Loan Modify(List<AuthenticationToken> authTokens, String identifier, Loan entity) {
        Loan loan = super.Modify(authTokens, identifier, entity); 

        if (loan.getLoanStatus().contains(LoanStatusCode.VALUE_8)) {
            LcfCheckInResponse response = getEasyRandom().nextObject(LcfCheckInResponse.class);
            response.setLoan((Loan)Database.getDatabase().put(EntityTypes.Type.Loan, identifier, loan));
            response.setReturnLocationRef(UUID.randomUUID().toString());
            throw new LCFResponse_CheckIn(response);
        }
        
        return loan;
    }

    @Override
    public String Create(List<AuthenticationToken> authTokens, Object parent, Loan entity, List<CreationQualifier> qualifiers) {
        String loanid = super.Create(authTokens, parent, entity, qualifiers); 
        
        Loan loan = (Loan)Database.getDatabase().get(getType(), loanid);

        LcfCheckOutResponse response = getEasyRandom().nextObject(LcfCheckOutResponse.class);
        response.setLoan((Loan)Database.getDatabase().put(EntityTypes.Type.Loan, loanid, loan));
        throw new LCFResponse_CheckOut(response);
    }
    
    private EasyRandom getEasyRandom() {
        EasyRandomParameters parameters = new EasyRandomParameters()
                .seed(123L)
                .objectPoolSize(100)
                .randomizationDepth(3)
                .stringLengthRange(5, 12)
                .collectionSizeRange(1, 10)
                .scanClasspathForConcreteTypes(true)
                .overrideDefaultInitialization(false)
                .ignoreRandomizationErrors(true);
        EasyRandom easyRandom = new EasyRandom(parameters);
        return easyRandom;
    }
   
}
