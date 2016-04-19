/*
 * Copyright 2016 Ceridwen Limited.
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
package com.ceridwen.lcf.server.frontend.restlet.core;

import com.ceridwen.lcf.server.core.DescriptionWebPage;
import java.io.IOException;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.restlet.util.WrapperRepresentation;

/**
 *
 * @author Matthew.Dovey
 */
public class DescriptionWebPageResource extends ServerResource {
	@Get("*:html")
  public String getHtml() {
    return DescriptionWebPage.getHtml(this.getRootRef().toString());
  }

}





