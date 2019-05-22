/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server;

import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;

/**
 *
 * @author Matthew
 */
@ApplicationPath("lcf/1.0")
@OpenAPIDefinition( info = @Info(description = "LCF", version = "1.1.0", title = "LCF"), 
                    servers =  @Server(url = "/lcfserver/lcf/1.0"))
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
//        addRestResourceClasses(resources);
        resources.add(com.ceridwen.lcf.server.webservice.PatronsWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.LoansWebservice.class);
        resources.add(OpenApiResource.class);
        resources.add(AcceptHeaderOpenApiResource.class);
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
