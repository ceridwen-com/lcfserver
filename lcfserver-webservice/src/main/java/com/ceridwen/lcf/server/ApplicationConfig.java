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
package com.ceridwen.lcf.server;

import com.ceridwen.lcf.server.providers.JacksonJaxbJsonConfigurationProvider;
import com.ceridwen.lcf.server.filters.GlobalHeadersFilter;
import com.ceridwen.lcf.server.filters.ReferenceHandlingFilter;
import com.ceridwen.lcf.server.handlers.LCFErrorHandler;
import com.ceridwen.lcf.server.handlers.LCFExceptionHandler;
import com.ceridwen.lcf.server.handlers.LCFResponseHandler;
import com.ceridwen.lcf.server.openapi.OpenApiConfiguration;
import com.ceridwen.lcf.server.webpages.DescriptionWebPage;
import com.ceridwen.lcf.server.webpages.SwaggerUIWebPage;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Ceridwen Limited
 */
@OpenAPIDefinition( info = @Info(description = "BIC Library Communications Framework ", title = "BIC LCF"), 
        externalDocs = @ExternalDocumentation(description = "BIC LCF Documentation", url = "https://bic-org-uk.githib.io/bic-lcf"))
@SecurityScheme( name= "TERMINAL", description = "Terminal Authentication", type= SecuritySchemeType.HTTP, scheme="basic")
@SecurityScheme( name = "USER", description = "Patron Authentication. Should be of the form \"Basic {credentials}\", where {credentials} is the base64 encoding of id and password joined by a single colon (:).", type=SecuritySchemeType.APIKEY, in=SecuritySchemeIn.HEADER, paramName="lcf-patron-credential" )
public class ApplicationConfig extends Application {
    
    public ApplicationConfig() {
        super();
        // Hack to handle enums, namespaces properly etc.
        OpenApiConfiguration.Config(this);
    }
                    
    /**
     *
     * @return
     */
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        
        // Support JSON with JAXB Annotation support
        resources.add(JacksonJaxbJsonConfigurationProvider.class);

        // Add OpenAPI support
        resources.add(OpenApiResource.class);
        resources.add(AcceptHeaderOpenApiResource.class);

        // Static web page resources
        resources.add(DescriptionWebPage.class);
        resources.add(SwaggerUIWebPage.class);

        //Add LCF Exception handlers
        resources.add(LCFResponseHandler.class);
        resources.add(LCFExceptionHandler.class);
        resources.add(LCFErrorHandler.class);

        // Add LCF filters
        resources.add(GlobalHeadersFilter.class);
        resources.add(ReferenceHandlingFilter.class);
        
        // Main LCF resources
        addLCFResources(resources);
        
        return resources;
    }

    private void addLCFResources(Set<Class<?>> resources) {
        ImplementedOperations.init();
        for (Class clazz: ImplementedOperations.getResources()) {
            resources.add(clazz);
        }
    }
 
}
