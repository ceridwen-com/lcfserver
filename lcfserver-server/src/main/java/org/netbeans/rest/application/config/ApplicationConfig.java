/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.rest.application.config;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Matthew
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.ceridwen.lcf.server.LCFExceptionHandler.class);
        resources.add(com.ceridwen.lcf.server.LCFResponseHandler.class);
        resources.add(com.ceridwen.lcf.server.webservice.AuthorisationContainerWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.AuthorisationListWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.AuthorisationWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.AuthorityContainerWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.AuthorityListWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.AuthorityWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.ChargeContainerWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.ClassSchemeContainerWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.ClassSchemeListWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.ClassSchemeWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.ClassTermContainerWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.ClassTermListWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.ClassTermWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.ContactContainerWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.ItemContainerWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.ItemListWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.ItemWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.LoanContainerWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.LocationContainerWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.LocationListWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.LocationWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.ManifestationContainerWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.ManifestationListWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.ManifestationWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.MessageAlertContainerWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.MessageAlertListWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.MessageAlertWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.PatronContainerWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.PaymentContainerWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.ReservationContainerWebservice.class);
        resources.add(com.ceridwen.lcf.server.webservice.ReservationListWebservice.class);
    }
    
}
