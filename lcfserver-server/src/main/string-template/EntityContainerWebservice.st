EntityContainerWebservice(LCFPath, Entity, EntityPath, SubEntity) ::= <<
package com.ceridwen.lcf.server.webservice;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.bic.ns.lcf.v1_0.<Entity>;

@Path("<LCFPath>/<EntityPath>")
public class <Entity>ContainerWebservice extends <Entity>ListWebservice implements EntityListInterface\<<Entity>\>, EntityLocatorInterface\<<Entity>\> {
    
    @Path("{identifier}")
    @Override
    public <Entity>Webservice Locate(@PathParam("identifier") String identifier, @HeaderParam("Authorization") String authorization, @HeaderParam("lcf-patron-credential") String lcfPatronCredential) {
        return new <Entity>Webservice(identifier, authorization, lcfPatronCredential);
    }
} 
>>