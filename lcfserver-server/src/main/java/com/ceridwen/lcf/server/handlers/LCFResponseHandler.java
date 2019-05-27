package com.ceridwen.lcf.server.handlers;

import com.ceridwen.lcf.server.responses.LCFResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class LCFResponseHandler implements ExceptionMapper<LCFResponse>{
	@Override
	public Response toResponse(final LCFResponse exception) {
		// TODO need to ensure ids in LCFResponse are referenced
		// TODO need to check how data is marshalled
	   	return Response.status(exception.getHTTPStatus()).entity(exception.getLCFResponse()).build(); //type(MediaType.APPLICATIOn_XML?
	   }
}
