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
package com.ceridwen.lcf.server.frontend.restlet.core;

import org.restlet.resource.ServerResource;

class Routes<E> {
	private Class<? extends ServerResource> list;
	private Class<? extends ServerResource> editor;
	
	Routes(Class<? extends ServerResource> list, Class<? extends ServerResource> editor) {
		this.list = list;
		this.editor = editor;
	}

	public Class<? extends ServerResource> getList() {
		return list;
	}

	public void setList(Class<? extends ServerResource> list) {
		this.list = list;
	}

	public Class<? extends ServerResource> getEditor() {
		return editor;
	}

	public void setEditor(Class<? extends ServerResource> editor) {
		this.editor = editor;
	}	
}