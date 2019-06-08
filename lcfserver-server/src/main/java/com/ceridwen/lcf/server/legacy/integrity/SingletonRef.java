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

import com.ceridwen.util.indirection.Getter;
import com.ceridwen.util.indirection.Setter;

/**
 *
 * @author Ceridwen Limited
 * @param <E>
 */
public class SingletonRef<E> extends Ref<E> {
	private Getter<E> getter;
	private Setter<E> setter;

    /**
     *
     * @param getter
     * @param setter
     */
    public SingletonRef(Getter<E> getter, Setter<E> setter) {
		this.getter = getter;
		this.setter = setter;
	}
	
    /**
     *
     * @return
     */
    public Getter<E> getGetter() {
		return getter;
	}

    /**
     *
     * @return
     */
    public Setter<E> getSetter() {
		return setter;
	}
}
