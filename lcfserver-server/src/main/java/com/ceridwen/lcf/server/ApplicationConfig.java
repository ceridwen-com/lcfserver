/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server;

import com.ceridwen.lcf.model.enumerations.EntityTypes;
import com.ceridwen.lcf.server.handlers.LCFExceptionHandler;
import com.ceridwen.lcf.server.handlers.LCFResponseHandler;
import com.ceridwen.lcf.server.resources.AbstractResourceManagerInterface;
import com.ceridwen.lcf.server.webservice.DescriptionWebPage;
import com.ceridwen.lcf.server.webservice.SwaggerUIWebPage;
import com.ceridwen.lcf.server.webservice.WebserviceHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Application;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 *
 * @author Matthew
 */
@OpenAPIDefinition( info = @Info(description = "BIC Library Communications Framework ", title = "BIC LCF"), 
        externalDocs = @ExternalDocumentation(description = "BIC LCF Documentation", url = "https://bic-org-uk.githib.io/bic-lcf"))
@SecurityScheme( name= "TERMINAL", description = "Terminal Authentication", type= SecuritySchemeType.HTTP, scheme="basic")
@SecurityScheme( name = "USER", description = "Patron Authentication. Should be of the form \"Basic {credentials}\", where {credentials} is the base64 encoding of id and password joined by a single colon (:).", type=SecuritySchemeType.APIKEY, in=SecuritySchemeIn.HEADER, paramName="lcf-patron-credential" )
public class ApplicationConfig extends Application {
        
    public ApplicationConfig() { 
        super();
        
        
        ObjectMapper mapper = Json.mapper();
        /* Configure Swagger Json\Yaml generation */
        mapper.registerModule(new JaxbAnnotationModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);        
        
        /* Hack to fix incorrect generation of enum values as swagger does not honour @XmlEnumValue */
        ModelResolver modelResolver = new ModelResolver(mapper) {
            @Override
            protected void _addEnumProps(Class<?> propClass, Schema property) {
                List<String> xmlEnumValues = new ArrayList<>();
                for (Field field: propClass.getFields()) {
                    if (field.isAnnotationPresent(XmlEnumValue.class)) {
                        xmlEnumValues.add(((XmlEnumValue)field.getAnnotation(XmlEnumValue.class)).value());                      
                    }
                }
                if (xmlEnumValues.isEmpty()) {
                    super._addEnumProps(propClass, property); 
                } else {
                    for (String n: xmlEnumValues) {
                        if (property instanceof StringSchema) {
                            StringSchema sp = (StringSchema) property;
                            sp.addEnumItem(n);
                        }
                    }
                }
            }

        };
        
        ModelConverters.getInstance().addConverter(modelResolver);
        
        SwaggerConfiguration oasConfig = new SwaggerConfiguration()
                .ignoredRoutes(Arrays.asList("/application.wadl"))
                .filterClass(OpenApiFilter.class.getName());

        try {
            new JaxrsOpenApiContextBuilder()
                    .application(this)
                    .openApiConfiguration(oasConfig)
                    .buildContext(true);
        } catch (OpenApiConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }        
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        resources.add(MyJacksonJaxbJsonProvider.class);
        resources.add(OpenApiResource.class);
        resources.add(SwaggerUIWebPage.class);
        resources.add(AcceptHeaderOpenApiResource.class);
        resources.add(DescriptionWebPage.class);
        resources.add(LCFExceptionHandler.class);
        resources.add(LCFResponseHandler.class);
        
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
        
        return resources;
    }
 
}
