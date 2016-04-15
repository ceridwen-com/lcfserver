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
package com.ceridwen.lcf.server.frontend.restlet;

import java.net.BindException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.restlet.Component;
import org.restlet.data.Protocol;

import com.ceridwen.lcf.server.frontend.restlet.core.BicLcfServerApplication;

/**
 * Java main class for running server from command line 
 *
 */
public class StandaloneServer {

	public static void main(String[] args) throws Exception {  

		// Default connection details
		Protocol protocol = Protocol.HTTP;
		String ip = "localhost";
		int port = 8000;
		
		
		Options opt = new Options();
		
		opt.addOption("s", "ssl", false, "Use HTTPS (default HTTP)");
		opt.addOption("i", "ip", true, "IP Address to bind to (default: localhost");
		opt.addOption("p", "port", true, "IP Port to bind to (default: 8000)");
		opt.addOption("h", "help", false, "Usage help");

	    // create the parser
	    CommandLineParser parser = new DefaultParser();
	    try {
	        // parse the command line arguments
	        CommandLine line = parser.parse( opt, args );
	        
	        if (line.hasOption("help")) {
	        	// automatically generate the help statement
	        	HelpFormatter formatter = new HelpFormatter();
	        	formatter.printHelp( "java -jar lcfserver.jar", opt );	        	
	        	
	        	return;
	        }
	        
	        if (line.hasOption("ssl")) {
	        	protocol = Protocol.HTTPS;
	        }
	        
	        if (line.hasOption("port")) {
	        	try {
	        		port = Integer.parseInt(line.getOptionValue("port"));
	        	} catch (NumberFormatException e) {
	        	}
	        }

	        if (line.hasOption("ip")) {
        		ip = line.getOptionValue("ip");
	        }
	    }
	    catch( ParseException exp ) {
	    	System.out.println("Invalid command line options.");
	    	System.out.println("");
        	// automatically generate the help statement
        	HelpFormatter formatter = new HelpFormatter();
        	formatter.printHelp( "java -jar lcfserver.jar", opt );	        	
        	
        	return;
	    }
		
		try {
		    // Create a new Component.  
		    Component component = new Component();  
		
		    // Add a new HTTP server listening on port 8182.  
		    component.getServers().add(protocol, ip, port);  
		
		    // Attach the sample application.  
		    component.getDefaultHost().attach("/lcfserver",  
		            new BicLcfServerApplication());  
		
		    // Start the component.  
		    component.start(); 
		} catch (BindException ex) {
			System.out.println(ex.getMessage());			
		}
	}
}
