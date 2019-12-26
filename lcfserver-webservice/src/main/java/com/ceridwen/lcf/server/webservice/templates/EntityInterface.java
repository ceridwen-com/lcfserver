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
package com.ceridwen.lcf.server.webservice.templates;

import javax.ws.rs.HeaderParam;

import javax.ws.rs.core.Response;

/**
 *
 * @author Ceridwen Limited
 * @param <E>
 */
public interface EntityInterface<E> {
//	@GET

    /**
     *
     * @param authorization
     * @param lcfPatronCredential
     * @return
     */
	public Response Retrieve(@HeaderParam("Authorization") String authorization, @HeaderParam("lcf-patron-credential") String lcfPatronCredential); 

//	@PUT

    /**
     *
     * @param data
     * @param authorization
     * @param lcfPatronCredential
     * @return
     */
	public Response Modify(E data, @HeaderParam("Authorization") String authorization, @HeaderParam("lcf-patron-credential") String lcfPatronCredential);

//	@DELETE

    /**
     *
     * @param authorization
     * @param lcfPatronCredential
     * @return
     */
	public Response Delete(@HeaderParam("Authorization") String authorization, @HeaderParam("lcf-patron-credential") String lcfPatronCredential); 
}
