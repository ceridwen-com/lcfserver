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
package com.ceridwen.lcf.lcfserver.model.exceptions;

import org.bic.ns.lcf.v1_0.ExceptionConditionType;
import org.bic.ns.lcf.v1_0.ReasonDeniedType;

public class EXC07_RequestDenied extends EXC00_LCF_Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8483322867344634031L;
	
	private ReasonDeniedType reasonDenied;
	
	private EXC07_RequestDenied() {
		super(null, null, null, null);
	}
	public EXC07_RequestDenied(String shortMessage, String longMessage, String ref, ReasonDeniedType reasonDenied, Throwable cause) {
		super(shortMessage, longMessage, ref, cause);
		this.reasonDenied = reasonDenied;
	}

	@Override
	protected ExceptionConditionType getExceptionConditionType() {
		return ExceptionConditionType.VALUE_7;
	}
	
	@Override
	protected ReasonDeniedType getReasonDenied() {
		return this.reasonDenied;
	}

	@Override
	public int getHTTPErrorCode() {
		return 403;
	}

}
