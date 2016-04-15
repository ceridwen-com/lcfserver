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
package com.ceridwen.lcf.server.core.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.bic.ns.lcf.v1_0.ExceptionCondition;
import org.bic.ns.lcf.v1_0.ExceptionConditionType;
import org.bic.ns.lcf.v1_0.LcfException;
import org.bic.ns.lcf.v1_0.Message;
import org.bic.ns.lcf.v1_0.MessageType;
import org.bic.ns.lcf.v1_0.ReasonDeniedType;

public abstract class EXC00_LCF_Exception extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2641857695689580372L;
	private String longMessage;
	private String shortMessage;
	private String ref;
	
	public EXC00_LCF_Exception(String shortMessage, String longMessage, String ref, Throwable cause) {
		this.shortMessage = shortMessage;
		this.longMessage = longMessage;
		this.ref = ref;
		if (cause != null) {
			this.initCause(cause);
		}
	}

        protected abstract ExceptionConditionType getExceptionConditionType();

	protected ReasonDeniedType getReasonDenied() { return null;}
	
	public abstract int getHTTPErrorCode();

	public LcfException getLcfException() {
		LcfException exc = new LcfException();
		
		if (longMessage != null) {
			Message m = new Message();
			m.setMessageType(MessageType.VALUE_1);
			m.getMessageText().add(longMessage);
			exc.getMessage().add(m);
		};

		if (shortMessage != null) {
			Message m = new Message();
			m.setMessageType(MessageType.VALUE_2);
			m.getMessageText().add(shortMessage);
			exc.getMessage().add(m);
		};
		
		if (this.getCause() != null) {
			StringWriter stack = new StringWriter();
			PrintWriter out = new PrintWriter(stack);
			this.getCause().printStackTrace(out);
			Message m = new Message();
			m.setMessageType(MessageType.VALUE_3);
			m.getMessageText().add(stack.toString());
			exc.getMessage().add(m);						
		}
		
		ExceptionCondition c = new ExceptionCondition();
		c.setConditionType(this.getExceptionConditionType());
		if (ref != null) {
			c.setElementRef(ref);
		}
		if (this.getReasonDenied() != null) {
			c.setReasonDenied(this.getReasonDenied());
		}
		exc.getExceptionCondition().add(c);

		return exc;
	}
}
