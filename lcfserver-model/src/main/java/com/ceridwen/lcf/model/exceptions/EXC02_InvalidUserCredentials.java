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
package com.ceridwen.lcf.model.exceptions;

import org.bic.ns.lcf.v1_0.ExceptionConditionType;

/**
 *
 * @author Ceridwen Limited
 */
public class EXC02_InvalidUserCredentials extends EXC00_LCF_Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4809302434767801752L;

    /**
     *
     */
    public EXC02_InvalidUserCredentials() {
		super("Invalid user credentials", "Please submit user credentials via lcf-patron-credential", null, null);
	}
	
    /**
     *
     * @param shortMessage
     * @param longMessage
     * @param ref
     * @param cause
     */
    public EXC02_InvalidUserCredentials(String shortMessage, String longMessage, String ref, Throwable cause) {
		super(shortMessage, longMessage, ref, cause);
	}
     
    /**
     *
     * @return
     */
    @Override
	protected ExceptionConditionType getExceptionConditionType() {
		return ExceptionConditionType.VALUE_2;
	}

    /**
     *
     * @return
     */
    @Override
	public int getHTTPErrorCode() {
		return 403;
	}

}
