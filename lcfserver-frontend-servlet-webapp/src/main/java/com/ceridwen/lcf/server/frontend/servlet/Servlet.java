/*******************************************************************************
 * Copyright (c) 2016, Matthew J. Dovey (www.ceridwen.com).
 *   
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   
 *     http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *    
 *   
 * Contributors:
 *     Matthew J. Dovey (www.ceridwen.com) - initial API and implementation
 *
 *     
 *******************************************************************************/
package com.ceridwen.lcf.server.frontend.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.ServiceConfigurationError;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang.StringUtils;

import com.ceridwen.lcf.server.core.EntityTypes;
import com.ceridwen.lcf.server.core.config.ConfigurationLoader;
import com.ceridwen.lcf.server.core.exceptions.EXC00_LCF_Exception;
import com.ceridwen.lcf.server.core.exceptions.EXC01_ServiceUnavailable;
import com.ceridwen.lcf.server.core.exceptions.EXC04_UnableToProcessRequest;
import com.ceridwen.lcf.server.core.exceptions.EXC05_InvalidEntityReference;
import com.ceridwen.lcf.server.core.exceptions.EXC06_InvalidDataInElement;
import com.ceridwen.lcf.server.core.referencing.Referencer;
import com.ceridwen.lcf.server.core.referencing.editor.ReferenceEditor;
import com.ceridwen.lcf.server.core.responses.LCFResponse;
import com.ceridwen.lcf.server.frontend.servlet.errors.ServletExceptionMapper;
import com.ceridwen.util.xml.XmlUtilities;


/**
 * Servlet implementation class Servlet
 */
@WebServlet(
	name = "lcfserver",
	urlPatterns = "/*",
	initParams = {
            @WebInitParam(name = "publicURL", value = "http://localhost:8080/lcfserver/"),
	}
)

// TODO Implement JSON 
public class Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private boolean debug = false;
	private String banner = null;
	private String baseUrl;

    /**
     * Default constructor. 
     */
    public Servlet() {
    }

	@Override
	public void init() throws ServletException {
		super.init();

		debug = new String("true").equalsIgnoreCase(this.getInitParameter("debug"));
		baseUrl = this.getInitParameter("publicURL");
		banner = this.getInitParameter("banner");
		if (banner == null) {
			banner = this.getClass().getAnnotation(WebServlet.class).displayName();
		}			
	}

	@Override
	public void destroy() {
		super.destroy();
	}
     
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */   
        @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			if (request.getRequestURI().endsWith("/lcf/1.0") ||
				request.getRequestURI().endsWith("/lcf/1.0/") || 
				request.getRequestURI().endsWith("/lcf/1.0/*")) {
				
				doBanner(request, response);
				return;
			}
			
			RestCommand command = new RestCommand(request, response, this.baseUrl);
			
			if (command.getResource() == null) {
				throw new EXC05_InvalidEntityReference("Entity type not found", "Entity type not found", request.getRequestURI(), null);
			}

			Object resp;
			
			if (command.getParentType() == null && command.getParentId() == null) {
				if (command.getId() == null) {
					resp = command.getResource().List(null, null, command.getStartIndex(), command.getCount());
				} else {
					resp = command.getResource().Retrieve(command.getId());
				}
			} else if (command.getParentType() != null && command.getParentId() != null && command.getId() == null) {
				resp = command.getResource().List(command.getParentType().getEntityTypeCodeValue(), command.getParentId(), command.getStartIndex(), command.getCount());
			} else {
				throw new EXC04_UnableToProcessRequest("Invalid URI", "Invalid URI", request.getRequestURI(), null);
			}
			
			String body;
			try {
				body = XmlUtilities.generateXML(resp);
			} catch (JAXBException e) {
				throw new EXC01_ServiceUnavailable("Error generating response", "Error generating response", request.getRequestURI(), e);						
			}
			
			response.setStatus(200);
			setDefaultHeaders(response);
			response.getWriter().append(body);	
		} catch (EXC00_LCF_Exception e) {
			handleException(response, e);
		} catch (Exception |  AssertionError | LinkageError | ServiceConfigurationError e) {
			handleUncaughtException(response, e);
		}
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
        @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			RestCommand command = new RestCommand(request, response, this.baseUrl);
	
			if (command.getResource() == null) {
				throw new EXC05_InvalidEntityReference("Entity type not found", "Entity type not found", request.getRequestURI(), null);
			}
		
			Object resp;
			
			if (command.getId() == null) {
				try {
					Object entity = XmlUtilities.processXML(request.getInputStream(), command.getResource().entityType.getTypeClass());
					if (command.getParentType() == null && command.getParentId() == null) {
						resp = command.getResource().Create(null, null, entity);
					} else if (command.getParentType() != null && command.getParentId() != null) { 
						resp = command.getResource().Create(command.getParentType().getEntityTypeCodeValue(), command.getParentId(), entity);
					} else {
						throw new EXC04_UnableToProcessRequest("Invalid URI", "Invalid URI", request.getRequestURI(), null);
					}
				} catch (JAXBException e) {
					throw new EXC06_InvalidDataInElement("Invalid XML", "InvalidXML", request.getRequestURI(), e);
				}
			} else {
				throw new EXC04_UnableToProcessRequest("Invalid URI", "Invalid URI", request.getRequestURI(), null);
			}

			String body;
			try {
				body = XmlUtilities.generateXML(resp);
			} catch (JAXBException e) {
				throw new EXC01_ServiceUnavailable("Error generating response", "Error generating response", request.getRequestURI(), e);						
			}
	
			response.setStatus(201);
			setDefaultHeaders(response);
			response.getWriter().append(body);
		} catch (EXC00_LCF_Exception | LCFResponse e) {
			handleException(response, e);
		} catch (Exception |  AssertionError | LinkageError | ServiceConfigurationError e) {
			handleUncaughtException(response, e);
		}
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
        @Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			RestCommand command = new RestCommand(request, response, this.baseUrl);
			
			if (command.getResource() == null) {
				throw new EXC05_InvalidEntityReference("Entity type not found", "Entity type not found", request.getRequestURI(), null);
			}
			
			Object resp;
			if (command.getParentType() == null && command.getId() != null) {
				try {
					Object entity = XmlUtilities.processXML(request.getInputStream(), command.getResource().entityType.getTypeClass());
					resp = command.getResource().Modify(command.getId(), entity);
				} catch (JAXBException e) {
					throw new EXC06_InvalidDataInElement("Invalid XML", "InvalidXML", request.getRequestURI(), e);
				}
			} else {
				throw new EXC04_UnableToProcessRequest("Invalid URI", "Invalid URI", request.getRequestURI(), null);
			}

			String body;
			try {
				body = XmlUtilities.generateXML(resp);
			} catch (JAXBException e) {
				throw new EXC01_ServiceUnavailable("Error generating response", "Error generating response", request.getRequestURI(), e);						
			}
			
			response.setStatus(200);
			setDefaultHeaders(response);
			response.getWriter().append(body);
		} catch (EXC00_LCF_Exception e) {
			handleException(response, e);
		} catch (Exception |  AssertionError | LinkageError | ServiceConfigurationError e) {
			handleUncaughtException(response, e);
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
        @Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			RestCommand command = new RestCommand(request, response, this.baseUrl);
				
			if (command.getResource() == null) {
				throw new EXC05_InvalidEntityReference("Entity type not found", "Entity type not found", request.getRequestURI(), null);
			}
			
			if (command.getParentType() == null && command.getId() != null) {
				command.getResource().Delete(command.getId());
			} else {
				throw new EXC04_UnableToProcessRequest("Invalid URI", "Invalid URI", request.getRequestURI(), null);
			}
			
			response.setStatus(204);
			setDefaultHeaders(response);
		} catch (EXC00_LCF_Exception e) {
			handleException(response, e);
		} catch (Exception |  AssertionError | LinkageError | ServiceConfigurationError e) {
			handleUncaughtException(response, e);
		}
	}

	private void handleUncaughtException(HttpServletResponse response, Throwable e) {
		ServletExceptionMapper mapper = new ServletExceptionMapper();
		this.handleException(response, mapper.mapToLcfException(e));
	}

	void handleException(HttpServletResponse response, Object exception) {
		int status = 500;
		Object resp = null;
		
		if (exception instanceof EXC00_LCF_Exception) {
			status = ((EXC00_LCF_Exception)exception).getHTTPErrorCode();
			resp = ((EXC00_LCF_Exception)exception).getLcfException();
		}

		if (exception instanceof LCFResponse) {
			status = ((LCFResponse)exception).getHTTPStatus();
			resp = ((LCFResponse)exception).getLCFResponse();
			ReferenceEditor referenceEditor = ConfigurationLoader.getConfiguration().getReferenceEditor();
			if (referenceEditor != null) {
				referenceEditor.init(this.baseUrl + EntityTypes.LCF_PREFIX + "/");
				resp = Referencer.factory(resp, referenceEditor).reference();
			}			
		}
		
		try {
			String body = XmlUtilities.generateXML(resp);
			response.setStatus(status);
			response.getWriter().append(body);
		} catch (JAXBException | IOException e) {
			response.setStatus(status);
		}    	
    }  
	
	void setDefaultHeaders(HttpServletResponse response) {
		if (StringUtils.isNotEmpty(banner)) {
			response.setHeader("X-Powered-By", banner);
		}
	}

	private void doBanner(HttpServletRequest request, HttpServletResponse response) {
		try {
			String banner =   "<html>"
							+ "<body>"
							+ "<h1>" + this.getClass().getAnnotation(WebServlet.class).displayName() + "</h1>"
							+ "<h2>To do</h2>"
							+ "<ul>"
							+ "<li>Referential integrity - partial implementation</li>"
							+ "<li>Client authentication - not implemented</li>"
							+ "<li>User authentication - not implemented</li>"
							+ "<li>Selection criteria - not implemented</li>"
							+ "</ul></p>";
			
			if (debug) {
				banner += "<h2>Configuration</h2>"
						+ "<p><ul>";
				Enumeration<String> names = this.getInitParameterNames();
				while (names.hasMoreElements()) {
					String name = names.nextElement();
					banner += "<li>" + name + " = " + this.getInitParameter(name) + "</li>";
				}
				banner += "</ul></p>";
			}
			banner += "</body>"
					+ "</html>";
			
			response.setStatus(200);
			response.getWriter().append(banner);
		} catch (IOException e) {
			response.setStatus(404);
			e.printStackTrace();
		}
	}
}
