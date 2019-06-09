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
package com.ceridwen.lcf.server.openapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.core.Application;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 *
 * @author Ceridwen Limited
 */
public class OpenApiConfiguration {

    public OpenApiConfiguration(Application application) {
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
                    .application(application)
                    .openApiConfiguration(oasConfig)
                    .buildContext(true);
        } catch (OpenApiConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }        
    }    
    
}
