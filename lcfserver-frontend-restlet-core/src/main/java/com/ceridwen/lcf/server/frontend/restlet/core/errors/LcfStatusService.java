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
package com.ceridwen.lcf.server.frontend.restlet.core.errors;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;
import org.restlet.service.StatusService;

import com.ceridwen.lcf.server.core.config.ConfigurationLoader;
import com.ceridwen.lcf.server.core.exceptions.EXC00_LCF_Exception;
import com.ceridwen.lcf.server.core.referencing.Referencer;
import com.ceridwen.lcf.server.core.referencing.editor.ReferenceEditor;
import com.ceridwen.lcf.server.core.responses.LCFResponse;

public class LcfStatusService extends StatusService {

	
	@Override
	public Representation toRepresentation(Status status, Request request, Response response) {
		
		Object representationObject = null;
		if (status.getThrowable() instanceof EXC00_LCF_Exception) {
			representationObject = ((EXC00_LCF_Exception)status.getThrowable()).getLcfException();
		}
		if (status.getThrowable() instanceof LCFResponse) {
			representationObject = ((LCFResponse)status.getThrowable()).getLCFResponse();
		}
		if (representationObject != null) {
			Representation result = null;
            try {
                
            	List<org.restlet.engine.resource.VariantInfo> variants = org.restlet.engine.converter.ConverterUtils
                        .getVariants(representationObject.getClass(), null);
                if (variants == null) {
                    variants = new ArrayList<>();
                }

                Variant variant = this.getConnegService().getPreferredVariant(variants,
                        request, this.getMetadataService());
                result = this.getConverterService().toRepresentation(
                        representationObject, variant);
            } catch (Exception e) {
                Context.getCurrentLogger().log(
                        Level.WARNING,
                        "Could not serialize LCF Exception",
                        e);
            }
            return result;
		} else {
			return super.toRepresentation(status, request, response);
		}
	}

	@Override
	public Status toStatus(Throwable throwable, Resource resource) {
		
		if (throwable instanceof EXC00_LCF_Exception) {
			return  new Status(((EXC00_LCF_Exception)throwable).getHTTPErrorCode(), throwable);			
		} else if (throwable.getCause() instanceof EXC00_LCF_Exception) {
			return new Status(((EXC00_LCF_Exception)throwable.getCause()).getHTTPErrorCode(), throwable.getCause());			
		} else if (throwable instanceof LCFResponse) {
			ReferenceEditor referenceEditor = ConfigurationLoader.getConfiguration().getReferenceEditor();
			if (referenceEditor != null) {
				referenceEditor.init(resource.getRootRef().toString() + "/");
				((LCFResponse)throwable).setLCFResponse(Referencer.factory(((LCFResponse)throwable).getLCFResponse(), referenceEditor).reference());
			}			
			return new Status(((LCFResponse)throwable).getHTTPStatus(), throwable);			
		} else if (throwable.getCause() instanceof LCFResponse) {
			ReferenceEditor referenceEditor = ConfigurationLoader.getConfiguration().getReferenceEditor();
			if (referenceEditor != null) {
				referenceEditor.init(resource.getRootRef().toString() + "/");
				((LCFResponse)throwable.getCause()).setLCFResponse(Referencer.factory(((LCFResponse)throwable.getCause()).getLCFResponse(), referenceEditor).reference());
			}			
			return new Status(((LCFResponse)throwable.getCause()).getHTTPStatus(), throwable.getCause());
		} else if (throwable instanceof org.restlet.resource.ResourceException) {	
			Throwable cause = throwable.getCause();
			if (cause != null) {
				return new RestletExceptionMapper().GetStatus(cause); 
			} else {
				return new RestletExceptionMapper().GetStatus(throwable);
			}
		} else {
			Throwable cause = throwable.getCause();
			if (cause != null) {
				return new RestletExceptionMapper().GetStatus(cause); 
			} else {
				return new RestletExceptionMapper().GetStatus(throwable);
			}
		}		
	}
}
