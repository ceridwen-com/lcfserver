/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 *
 * @author Matthew
 */
@ApplicationPath(EntityTypes.LCF_PREFIX)
@OpenAPIDefinition( info = @Info(description = "LCF", version = "1.1.0", title = "LCF"), 
                    servers =  @Server(url = "/lcfserver/lcf/1.0"))
public class ApplicationConfig extends Application {
    
    public ApplicationConfig() {        
        ObjectMapper mapper = Json.mapper();
        
        mapper.registerModule(new JaxbAnnotationModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
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
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
//        addRestResourceClasses(resources);
        resources.add(OpenApiResource.class);
        resources.add(AcceptHeaderOpenApiResource.class);
        resources.add(com.ceridwen.lcf.server.webservice.PatronContainerWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.LoanContainerWebservice.class);
        
        
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
//    private void addRestResourceClasses(Set<Class<?>> resources) {
//        resources.add(com.ceridwen.lcf.server.webservice.LoanWebservice.class);
//        resources.add(com.ceridwen.lcf.server.webservice.LoansListWebservice.class);
//        resources.add(com.ceridwen.lcf.server.webservice.LoansWebservice.class);
//        resources.add(com.ceridwen.lcf.server.webservice.PatronWebservice.class);
//        resources.add(com.ceridwen.lcf.server.webservice.PatronsListWebservice.class);
//        resources.add(com.ceridwen.lcf.server.webservice.PatronsWebservice.class);
//    }
    
}
