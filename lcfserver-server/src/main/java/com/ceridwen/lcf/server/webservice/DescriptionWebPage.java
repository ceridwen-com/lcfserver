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

import com.ceridwen.lcf.model.enumerations.EntityTypes;
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
@Path("/")
@Hidden
public class DescriptionWebPage {

    @Context
    UriInfo uriInfo;
    
    /**
     *
     * @return
     */
    @GET
    @Produces({MediaType.TEXT_HTML})
    public String getHtml() {
    String page =
"<html>\n" +
"<head>\n" +
"  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
"  <title>BIC LCF Server</title>\n" +
"  <style>\n" +
"  body {\n" +
"    background: rgb(250, 255, 255);\n" +
"    width: 75%;\n" +
"    margin-left: 10%;\n" +
"  }\n" +
"  \n" +
"  div {\n" +
"    overflow-x: hidden;\n" +
"    overflow-y: auto;\n" +
"    padding: 20px;\n" +
"    font-family: 'Helvetica Neue', Helvetica, 'Hiragino Sans GB', 'Microsoft YaHei', STHeiti, SimSun, 'Segoe UI', AppleSDGothicNeo-Medium, 'Malgun Gothic', Arial, freesans, sans-serif;\n" +
"    font-size: 15px;\n" +
"    line-height: 1.6;\n" +
"    -webkit-font-smoothing: antialiased;\n" +
"    background: rgb(240, 255, 255);\n" +
"    border: solid 0.5px rgb(192, 192, 192);\n" +
"  }\n" +
"  \n" +
"  h1 {\n" +
"    clear: both;\n" +
"    font-size: 1.8em; \n" +
"    font-weight: bold; \n" +
"    margin-top: 0.1em;\n" +
"    border-bottom-width: 1px; \n" +
"    border-bottom-style: solid; \n" +
"    border-bottom-color: rgb(230, 230, 230); \n" +
"    line-height: 1.6;\n" +
"  }\n" +
"  \n" +
"  hr {\n" +
"    stroke: none; \n" +
"    stroke: none; \n" +
"    opacity: 0.2;\n" +
"    border: 1px solid rgb(230, 230, 230);\n" +
"  }\n" +
"  \n" +
"  blockquote {\n" +
"    margin: 1em 20px;\n" +
"    border-left-width: 4px; \n" +
"    border-left-style: solid; \n" +
"    border-left-color: rgb(230, 230, 230); \n" +
"    padding: 0px 15px; \n" +
"    font-style: italic;\n" +
"    word-wrap: break-word;\n" +
"    margin-top: 0px;\n" +
"    margin-bottom: 0px;\n" +
"  }\n" +
"  \n" +
"  blockquote a {\n" +
"    text-decoration: none; \n" +
"    vertical-align: \n" +
"    baseline;color: rgb(50, 105, 160);\n" +
"  }\n" +
"  \n" +
"  h2 {\n" +
"    clear: both;\n" +
"    font-size: 1.2em;\n" +
"    font-style: italic;\n" +
"    font-weight: bold; \n" +
"    margin: 1.275em 0px 0.85em;\n" +
"    margin-top: 1.2em;\n" +
"    line-height: 1.2;\n" +
"  }\n" +
"  \n" +
"  code {\n" +
"    word-wrap: break-word; \n" +
"    border: 1px solid rgb(204, 204, 204); \n" +
"    overflow: auto; \n" +
"    display: block; \n" +
"    overflow-x: auto; \n" +
"    color: rgb(101, 123, 131); \n" +
"    background: rgb(253, 246, 227);\n" +
"    display: block;\n" +
"    font-family: Consolas, Inconsolata, Courier, monospace; \n" +
"    font-weight: bold; \n" +
"    white-space: pre; \n" +
"    border-top-left-radius: 3px; \n" +
"    border-top-right-radius: 3px; \n" +
"    border-bottom-right-radius: 3px; \n" +
"    border-bottom-left-radius: 3px; \n" +
"    padding: 0px 5px; \n" +
"    margin: 1em 20px;\n" +
"    font-size: 0.9em; \n" +
"  }\n" +
"  \n" +
"  code a {\n" +
"    color: rgb(101, 123, 131); \n" +
"  }" +
"  </style>\n" +
"</head>\n" +
"<body>\n" +
"<div>\n" +
"<h1>BIC LCF Server</h1>\n" +
"<hr>\n" +
"<blockquote>\n" +
"Documentation on the BIC LCF Standard can be found at <a href=\"https://bic-org-uk.github.io/bic-lcf/\">https://bic-org-uk.github.io/bic-lcf/</a><br>\n";
String openapi = uriInfo.getBaseUri() +"openapi.json";
String swaggerUI = uriInfo.getBaseUri() +"swagger-ui.html";
  page += 
"OpenApi 3.0 Definition can be found at <a href=\"" + openapi + "\">" + openapi + "</a><br>\n" +
"Swagger UI can be found at <a href=\"" + swaggerUI + "\">" + swaggerUI + "</a><br>\n" +
"</blockquote>\n" +
"<hr>\n" +
"<h2>Web Service Endpoints</h2>\n" +
"<code>";
    for (EntityTypes.Type entity: EntityTypes.Type.values()) {
      String href = uriInfo.getBaseUri() + EntityTypes.LCF_PREFIX + "/" + entity.getEntityTypeCodeValue() +"/";
      page += "<a href=\"" + href + "\">" + href + "</a>\n";
    }
  page +=
"</code>\n" +
"</div>\n" +
"</body>\n" +
"</html>";    
    return page;
  }
}
