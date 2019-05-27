package com.ceridwen.lcf.server.handlers;

import com.ceridwen.lcf.lcfserver.model.exceptions.EXC00_LCF_Exception;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;



@Provider
public class LCFExceptionHandler implements ExceptionMapper<EXC00_LCF_Exception>{
    @Override
	public Response toResponse(final EXC00_LCF_Exception exception) {
		// TODO need to check how data is marshalled
    	return Response.status(exception.getHTTPErrorCode()).entity(exception.getLcfException()).build(); //type(MediaType.APPLICATIOn_XML?
    }
}
