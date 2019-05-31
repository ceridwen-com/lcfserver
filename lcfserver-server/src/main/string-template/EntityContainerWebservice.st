EntityContainerWebservice(LCFPath, Entity, EntityPath, SubEntity, SelectionCriteria) ::= <<
package com.ceridwen.lcf.server.webservice;

import io.swagger.v3.oas.annotations.Parameter;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.bic.ns.lcf.v1_0.<Entity>;

@Path("<LCFPath>/<EntityPath>")
public class <Entity>ContainerWebservice extends <Entity>ListWebservice implements EntityLocatorInterface\<<Entity>\> {

    @Path("{identifier}")
    @Override
    public <Entity>Webservice Locate(@PathParam("identifier") String identifier, @Parameter(hidden=true) @HeaderParam("Authorization") String authorization, @Parameter(hidden=true) @HeaderParam("lcf-patron-credential") String lcfPatronCredential) {
        return new <Entity>Webservice(identifier, authorization, lcfPatronCredential, uri);
    }
} 
>>