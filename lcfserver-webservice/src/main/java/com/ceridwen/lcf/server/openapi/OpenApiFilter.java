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

import com.ceridwen.lcf.model.LcfConstants;
import com.ceridwen.lcf.server.ImplementedOperations;
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
import org.bic.ns.lcf.v1_0.EntityType;

/**
 *
 * @author Ceridwen Limited
 */
public class OpenApiFilter extends AbstractSpecFilter {

    /**
     *
     */
    public static String uri = null;

    /**
     *
     * @param openAPI
     * @param params
     * @param cookies
     * @param headers
     * @return
     */
    @Override
    public Optional<OpenAPI> filterOpenAPI(OpenAPI openAPI, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        Optional<OpenAPI> openApi = super.filterOpenAPI(openAPI, params, cookies, headers); //To change body of generated methods, choose Tools | Templates.

        if (uri != null) {
            openApi.ifPresent((OpenAPI api) -> {
                Server server = new Server();
                server.setUrl(uri);
                server.setDescription("Demo Server"); //TDOD
                List<Server> servers = new ArrayList<>();
                servers.add(server);
                api.setServers(servers);
                api.getInfo().setVersion(LcfConstants.LCF_VERSION);
            });
        }
        
        return openApi;
    }
    
    @Override
    public Optional<Operation> filterOperation(io.swagger.v3.oas.models.Operation operation, ApiDescription api, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
        for (EntityType unimplemented: ImplementedOperations.getUnimplemented()) {
            if (api.getPath().contains("/" + unimplemented.value() + "/") || api.getPath().endsWith("/" + unimplemented.value())) {
                return Optional.empty();
            }
        }
        return Optional.of(operation);
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

    /**
     *
     * @param schema
     * @param params
     * @param cookies
     * @param headers
     * @return
     */

    @Override
    public Optional<Schema> filterSchema(Schema schema, Map<String, List<String>> params, Map<String, String> cookies, Map<String, List<String>> headers) {
       String namespace = schema.getXml().getNamespace();
       if (namespace == null || namespace.isBlank()) {
           schema.getXml().setNamespace("http://ns.bic.org.uk/lcf/1.0");
       }
       return Optional.of(schema);
    }
}    




