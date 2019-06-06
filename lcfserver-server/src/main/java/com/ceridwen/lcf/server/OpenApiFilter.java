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
package com.ceridwen.lcf.server;

import com.ceridwen.lcf.lcfserver.model.EntityTypes;
import io.swagger.v3.core.filter.AbstractSpecFilter;
import io.swagger.v3.core.model.ApiDescription;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.servers.Server;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Matthew.Dovey
 */
@Provider
public class OpenApiFilter extends AbstractSpecFilter {

    public static String uri = null;

    @Override
    public Optional<OpenAPI> filterOpenAPI(OpenAPI openAPI, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        Optional<OpenAPI> openApi = super.filterOpenAPI(openAPI, params, cookies, headers); //To change body of generated methods, choose Tools | Templates.

        if (uri != null) {
            openApi.ifPresent((OpenAPI api) -> {
                Server server = new Server();
                server.setUrl(uri);
                server.setDescription("Demo Server");
                List<Server> servers = new ArrayList<>();
                servers.add(server);
                api.setServers(servers);
                api.getInfo().setVersion(EntityTypes.getLCFSpecVersion());
            });
        }
        
        return openApi;
    }
    
    
    
    

//    public static final String WADL = "/application.wadl";
//
//    @Override
//    public Optional<Operation> filterOperation(io.swagger.v3.oas.models.Operation operation, ApiDescription api, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
//        if (api.getPath().startsWith(WADL)) {
//            return Optional.empty();
//        }
//        return Optional.of(operation);
//    }

    @Override
    public Optional<Schema> filterSchema(Schema schema, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
       String namespace = schema.getXml().getNamespace();
       if (namespace == null || namespace.isBlank()) {
           schema.getXml().setNamespace("http://ns.bic.org.uk/lcf/1.0");
       }
       return Optional.of(schema);
    }
}    




