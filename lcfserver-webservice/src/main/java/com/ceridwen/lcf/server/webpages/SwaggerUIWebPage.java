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
package com.ceridwen.lcf.server.webpages;

import com.ceridwen.lcf.server.openapi.OpenApiFilter;
import io.swagger.v3.oas.annotations.Hidden;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Ceridwen Limited
 */
@Path("/swagger-ui.html")
@Hidden
public class SwaggerUIWebPage {

    @Context
    UriInfo uriInfo;
    
    /**
     *
     * @return
     */
    @GET
    @Produces({MediaType.TEXT_HTML})
    public String getHtml() {
    
    OpenApiFilter.uri = uriInfo.getBaseUri().toString();
        
    String page =
"<html lang=\"en\">\n" +
"  <head>\n" +
"    <meta charset=\"UTF-8\">\n" +
"    <title>Swagger UI</title>\n" +
"    <link rel=\"stylesheet\" type=\"text/css\" href=\"https://unpkg.com/swagger-ui-dist@3/swagger-ui.css\" >\n" +
"    <link rel=\"icon\" type=\"image/png\" href=\"https://unpkg.com/swagger-ui-dist@3/favicon-32x32.png\" sizes=\"32x32\" />\n" +
"    <link rel=\"icon\" type=\"image/png\" href=\"https://unpkg.com/swagger-ui-dist@3/favicon-16x16.png\" sizes=\"16x16\" />\n" +
"    <style>\n" +
"      html\n" +
"      {\n" +
"        box-sizing: border-box;\n" +
"        overflow: -moz-scrollbars-vertical;\n" +
"        overflow-y: scroll;\n" +
"      }\n" +
"      *,\n" +
"      *:before,\n" +
"      *:after\n" +
"      {\n" +
"        box-sizing: inherit;\n" +
"      }\n" +
"      body\n" +
"      {\n" +
"        margin:0;\n" +
"        background: #fafafa;\n" +
"      }\n" +
"    </style>\n" +
"  </head>\n" +
"\n" +
"  <body>\n" +
"    <div id=\"swagger-ui\"></div>\n" +
"\n" +
"    <script src=\"https://unpkg.com/swagger-ui-dist@3/swagger-ui-bundle.js\"></script>\n" +
"    <script src=\"https://unpkg.com/swagger-ui-dist@3/swagger-ui-standalone-preset.js\"></script>\n" +
"    <script>\n" +
"    function HideTopbarPlugin() {\n" +
"       // this plugin overrides the Topbar component to return nothing\n" +
"       return {\n" +
"           components: {\n" +
"               Topbar: function() { return null }\n" +
"           }\n" +
"       }\n" +
"    }\n" +
"    </script>\n" +
"    <script>\n" +
"    window.onload = function() {\n" +
"      // Begin Swagger UI call region\n" +
"      const ui = SwaggerUIBundle({\n";
String openapi = uriInfo.getBaseUri() +"openapi.json";
page +=    
"        url: \"" + openapi + "\",\n" +
"        dom_id: '#swagger-ui',\n" +
"        deepLinking: true,\n" +
"        docExpansion: \"none\",\n" +
"        filter: true,\n" +
"        apisSorter: \"alpha\",\n" +
"        operationsSorter: \"alpha\",\n" +
"        presets: [\n" +
"          SwaggerUIBundle.presets.apis,\n" +
"          SwaggerUIStandalonePreset\n" +
"        ],\n" +
"        plugins: [\n" +
"          SwaggerUIBundle.plugins.DownloadUrl,\n" +
"          HideTopbarPlugin\n" +
"        ],\n" +
"        layout: \"StandaloneLayout\"\n" +
"      })\n" +
"      // End Swagger UI call region\n" +
"      window.ui = ui\n" +
"    }\n" +
"  </script>\n" +
"  </body>\n" +
"</html>";
    return page;
  }
}
