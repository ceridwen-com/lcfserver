/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceridwen.lcf.server.webservice;

import javax.ws.rs.Path;

/**
 *
 * @author Matthew.Dovey
 */
public interface EntitiesLocatorInterface<WS> {
//    @Path("")
    public EntitiesWebserviceInterface<WS> Locate();
}
