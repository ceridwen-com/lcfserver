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
/**
 * 
 * Mapping of Exceptions to the LCF EXC Exception condition response codes
 * 
 * Code ID 	Code value 	Definition 														Notes
 * EXC01 	01 			Service unavailable 											Equivalent to HTTP response code 503
 * EXC02 	02 			invalid user ID or password 									(as supplied in Q00C01)
 * EXC03 	03 			invalid terminal ID or password 								(as supplied in Q00C04) 	Equivalent to HTTP response code 401
 * EXC04 	04 			Service unable to process request 								Equivalent to HTTP response code 500
 * EXC05 	05 			Invalid entity reference 										For use in all entity-specific responses
 * EXC06 	06 			Invalid data in element 										For use whenever a request specifies data that does not conform to the data type for the data element in question, e.g. an undefined code value, a badly-formed date, or an invalid patron password.
 * EXC07 	07 			Request denied 	
 * EXC08 	08 			No records match the selection criteria in the request 			Equivalent to HTTP response code 404. May be used in response to function 02 requests.
 * EXC09 	09 			Too many records match the selection criteria in the request 	Further information may be provided in an exception description and/or response message. May be used in response to function 02 requests.
 * 
 */
package com.ceridwen.lcf.server.core.exceptions;

