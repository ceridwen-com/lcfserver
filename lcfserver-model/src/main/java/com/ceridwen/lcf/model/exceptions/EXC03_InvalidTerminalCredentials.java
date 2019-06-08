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

import java.util.List;
import org.bic.ns.lcf.v1_0.ExceptionConditionType;

/**
 *
 * @author Ceridwen Limited
 */
public class EXC03_InvalidTerminalCredentials extends EXC00_LCF_Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8635187518663576610L;

    /**
     *
     */
    public EXC03_InvalidTerminalCredentials() {
		super("Invalid terminal credentials", "Please submit terminal credentials via HTTP Auth", null, null);
	}
	
    /**
     *
     * @param shortMessage
     * @param longMessage
     * @param ref
     * @param cause
     */
    public EXC03_InvalidTerminalCredentials(String shortMessage, String longMessage, String ref, Throwable cause) {
		super(shortMessage, longMessage, ref, cause);
	}

    /**
     *
     * @return
     */
    @Override
	protected ExceptionConditionType getExceptionConditionType() {
		return ExceptionConditionType.VALUE_3;
	}

    /**
     *
     * @return
     */
    @Override
	public int getHTTPErrorCode() {
		return 401;
	}

    /**
     *
     * @return
     */
    @Override
    public List<CustomHeader> getCustomHeaders() {
        List<CustomHeader> headers = super.getCustomHeaders(); 
        headers.add(new CustomHeader("WWW-Authenticate", "Basic realm=\"BIC-LCF\", charset=\"UTF-8\""));
        return headers;
    }
}
