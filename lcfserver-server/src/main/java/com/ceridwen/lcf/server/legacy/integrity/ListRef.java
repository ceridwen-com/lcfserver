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
package com.ceridwen.lcf.server.legacy.integrity;

import com.ceridwen.util.indirection.Adder;
import com.ceridwen.util.indirection.Contains;
import com.ceridwen.util.indirection.Lister;
import com.ceridwen.util.indirection.Remover;

/**
 *
 * @author Ceridwen Limited
 * @param <E>
 */
public class ListRef<E> extends Ref<E> {
	private Adder<E> adder;
	private Remover<E> remover;
	private Contains<E> contains;
	private Lister<E> lister;

    /**
     *
     * @param adder
     * @param remover
     * @param contains
     * @param lister
     */
    public ListRef(Adder<E> adder, Remover<E> remover, Contains<E> contains, Lister<E> lister) {
		this.adder = adder;
		this.remover = remover;
		this.contains = contains;
		this.lister = lister;
	}
	
    /**
     *
     * @return
     */
    public Adder<E> getAdder() {
		return adder;
	}

    /**
     *
     * @return
     */
    public Remover<E> getRemover() {
		return remover;
	}

    /**
     *
     * @return
     */
    public Contains<E> getContains() {
		return contains;
	}

    /**
     *
     * @return
     */
    public Lister<E> getLister() {
		return lister;
	}
}
