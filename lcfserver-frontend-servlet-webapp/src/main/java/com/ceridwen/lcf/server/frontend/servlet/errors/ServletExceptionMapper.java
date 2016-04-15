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
package com.ceridwen.lcf.server.frontend.servlet.errors;

import java.util.HashMap;

import com.ceridwen.lcf.server.core.exceptions.EXC00_LCF_Exception;
import com.ceridwen.lcf.server.core.exceptions.EXC01_ServiceUnavailable;
import com.ceridwen.lcf.server.core.exceptions.EXC04_UnableToProcessRequest;

//TODO Should throw different LCF Exceptions depending on underlying cause

public class ServletExceptionMapper {
	interface ExceptionMap {
		EXC00_LCF_Exception map(Throwable e);
	}
	
	HashMap<Class<? extends Throwable>, ExceptionMap> map = new HashMap<Class<? extends Throwable>, ExceptionMap>() {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4967758068800037975L;

		{
			put(NullPointerException.class, e -> {
				return new EXC04_UnableToProcessRequest("Null pointer error", "Null pointer error", null, e);
			});
		}
		
	};
	
	
	public EXC00_LCF_Exception mapToLcfException(Throwable e) {
		EXC00_LCF_Exception lcf_exception;
		ExceptionMap mapper = map.get(e.getClass());
		if (mapper != null) {
			lcf_exception = mapper.map(e);
		} else {
			lcf_exception = new EXC01_ServiceUnavailable(e.getMessage(), e.getMessage(), null, e);
		}
		return lcf_exception;
	}

}
