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

package com.ceridwen.lcf.server.legacy.integrity;

import com.ceridwen.util.indirection.Getter;
import com.ceridwen.util.indirection.Setter;

public class SingletonRef<E> extends Ref<E> {
	private Getter<E> getter;
	private Setter<E> setter;

	public SingletonRef(Getter<E> getter, Setter<E> setter) {
		this.getter = getter;
		this.setter = setter;
	}
	
	public Getter<E> getGetter() {
		return getter;
	}
	public Setter<E> getSetter() {
		return setter;
	}
}
