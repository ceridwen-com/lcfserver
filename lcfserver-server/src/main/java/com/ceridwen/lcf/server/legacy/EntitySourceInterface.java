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
package com.ceridwen.lcf.server.legacy;

import com.ceridwen.lcf.server.resources.QueryResults;

/**
 *
 * @author Ceridwen Limited
 * @param <E>
 */
public interface EntitySourceInterface<E> {

    /**
     *
     * @param parent
     * @param entity
     * @return
     */
    String Create(Object parent, E entity);

    /**
     *
     * @param entity
     * @return
     */
    String Create(E entity);

    /**
     *
     * @param identifier
     * @return
     */
    E Retrieve(String identifier);

    /**
     *
     * @param identifier
     * @param entity
     * @return
     */
    E Modify(String identifier, E entity);

    /**
     *
     * @param identifier
     */
    void Delete(String identifier);

    /**
     *
     * @param parent
     * @param start
     * @param max
     * @param query
     * @return
     */
    QueryResults<E> Query(Object parent, int start, int max, String query);

    /**
     *
     * @param query
     * @param start
     * @param max
     * @return
     */
    QueryResults<E> Query(String query, int start, int max);
}
