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
package com.ceridwen.lcf.server.core.responses;

import org.bic.ns.lcf.v1_0.LcfCheckOutResponse;

public class LCFResponse_CheckOut extends LCFResponse {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5335981038192006972L;
	private transient LcfCheckOutResponse response;

	public LCFResponse_CheckOut(LcfCheckOutResponse response) {
		this.response = response;
	}

	@Override
	public Object getLCFResponse() {
		return response;
	}

	@Override
	public void setResponse(Object response) {
		this.response = (LcfCheckOutResponse)response;
	}

	@Override
	public int getHTTPStatus() {
		return 201;
	}

  @Override
  public String getIdentifier() {
    return response.getLoanRef();
  }

}
