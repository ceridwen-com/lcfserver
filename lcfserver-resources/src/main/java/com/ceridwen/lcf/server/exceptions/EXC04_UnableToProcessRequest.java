/*******************************************************************************
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
 *******************************************************************************/
package com.ceridwen.lcf.server.exceptions;

import org.bic.ns.lcf.v1_0.ExceptionConditionType;

public class EXC04_UnableToProcessRequest extends EXC00_LCF_Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5758999035087097784L;

	private EXC04_UnableToProcessRequest() {
		super(null, null, null, null);
	}
	
	public EXC04_UnableToProcessRequest(String shortMessage, String longMessage, String ref, Throwable cause) {
		super(shortMessage, longMessage, ref, cause);
	}

	@Override
	protected ExceptionConditionType getExceptionConditionType() {
		return ExceptionConditionType.VALUE_4;
	}

	@Override
	public int getHTTPErrorCode() {
		return 500;
	}

}
