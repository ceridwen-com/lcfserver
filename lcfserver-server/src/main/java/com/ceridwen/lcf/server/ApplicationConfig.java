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
import com.ceridwen.lcf.model.enumerations.EntityTypes;
import com.ceridwen.lcf.server.filters.GlobalHeadersFilter;
import com.ceridwen.lcf.server.filters.ReferenceHandlingFilter;
import com.ceridwen.lcf.server.handlers.LCFExceptionHandler;
import com.ceridwen.lcf.server.handlers.LCFResponseHandler;
import com.ceridwen.lcf.server.openapi.OpenApiConfiguration;
import com.ceridwen.lcf.server.resources.AbstractResourceManagerInterface;
import com.ceridwen.lcf.server.webpages.DescriptionWebPage;
import com.ceridwen.lcf.server.webpages.SwaggerUIWebPage;
import com.ceridwen.lcf.server.webservice.WebserviceHelper;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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

        //Additional LCF resources
        resources.add(LCFExceptionHandler.class);
        resources.add(LCFResponseHandler.class);
        // TODO: currently handled internally
        resources.add(GlobalHeadersFilter.class);
        // TODO: currently handled internally
        resources.add(ReferenceHandlingFilter.class);
        
        // Main LCF resources
        addLCFResources(resources);
        
        return resources;
    }

    private void addLCFResources(Set<Class<?>> resources) {
        for (EntityTypes.Type type: EntityTypes.Type.values()) {
            try {
                Class entity = Class.forName("org.bic.ns.lcf.v1_0." + type.name());
                Class rmi = Class.forName("com.ceridwen.lcf.server.resources." + type.name() + "ResourceManagerInterface");
                WebserviceHelper helper = new WebserviceHelper(entity, rmi);
                AbstractResourceManagerInterface rm = helper.getResourceManager();
                if (rm != null) {
                    Class webservice = Class.forName("com.ceridwen.lcf.server.webservice." + type.name() + "ContainerWebservice");
                    resources.add(webservice);
                    Logger.getLogger(ApplicationConfig.class.getName()).log(Level.INFO, "{0}: Resource Manager loaded", type.name());
                } else {
                    Logger.getLogger(ApplicationConfig.class.getName()).log(Level.INFO, "{0}: No Resource Manager available", type.name());                    
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ApplicationConfig.class.getName()).log(Level.SEVERE, type.name() + "{0}: Error loading Resource Manager", ex);
            }
        }
    }
 
}
