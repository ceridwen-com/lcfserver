EntityContainerWebservice(LCFPath, Entity, EntityPath, SubEntity, SelectionCriteria, AlternativePosts, AlternativePuts, AlternativeGets, AlternativeDeletes, CreationQualifiers, DirectUpdatePaths, AuthenticationSchemes) ::= <<
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

        return new <Entity>Webservice(identifier, tokens);
    }
} 
>>

