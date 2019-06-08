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
package com.ceridwen.lcf.server.filters;

import com.ceridwen.lcf.model.authentication.AuthenticationToken;
import com.ceridwen.lcf.model.authentication.AuthenticationCategory;
import java.util.List;
import java.util.Map;
import org.bic.ns.lcf.v1_0.SelectionCriterion;

/**
 *
 * @author Ceridwen Limited
 * @param <E>
 */
public interface ResourceFilterInterface<E> {

    /**
     *
     * @param authTokens
     * @param parent
     * @param entity
     * @return
     */
    E Create(Map<AuthenticationCategory, AuthenticationToken> authTokens, Object parent, E entity);

    /**
     *
     * @param authTokens
     * @param Entity
     * @return
     */
    E Retrieve(Map<AuthenticationCategory, AuthenticationToken> authTokens, E Entity);

    /**
     *
     * @param authTokens
     * @param identifier
     * @param entity
     * @return
     */
    E Modify(Map<AuthenticationCategory, AuthenticationToken> authTokens, String identifier, E entity);

    /**
     *
     * @param authTokens
     * @param identifier
     */
    void Delete(Map<AuthenticationCategory, AuthenticationToken> authTokens, String identifier);

    /**
     *
     * @param authTokens
     * @param parent
     * @param startIndex
     * @param count
     * @param selection
     * @param results
     * @return
     */
    List<E> Query(Map<AuthenticationCategory, AuthenticationToken> authTokens, Object parent, int startIndex, int count, List<SelectionCriterion> selection, List<E> results);
}
