EntityContainerWebservice(LCFPath, Entity, EntityPath, SubEntity, SelectionCriteria, AlternativePosts, AlternativePuts, AlternativeGets, AlternativeDeletes, CreationQualifiers, VirtualUpdatePaths, AuthenticationSchemes) ::= <<
package com.ceridwen.lcf.server.webservice;

import com.ceridwen.lcf.model.authentication.AuthenticationToken;
import com.ceridwen.lcf.model.authentication.AuthenticationCategory;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("<LCFPath>/<EntityPath>")
@Tag( name = "<Entity>", description="<Entity> Operations" )
public class <Entity>ContainerWebservice extends <Entity>ListWebservice {

    @Path("{identifier}")
<AuthenticationSchemes: {authenticationscheme |
    @SecurityRequirement(name = "<authenticationscheme.key>")}>
    public <Entity>Webservice Locate(
<AuthenticationSchemes: {authenticationscheme |
            @Parameter(hidden=true) @HeaderParam("<authenticationscheme.value>") String <authenticationscheme.key>,}>
            @PathParam("identifier") String identifier
        ) {
        List\<AuthenticationToken\> tokens = new ArrayList\<\>();
        
<AuthenticationSchemes: {authenticationscheme |
        helper.addAuthenthicationTokens(tokens, AuthenticationCategory.<authenticationscheme.key>, <authenticationscheme.key>);}>

        return new <Entity>Webservice(identifier, tokens, uri);
    }
} 
>>

