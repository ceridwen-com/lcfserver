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
package com.ceridwen.lcf.model.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.bic.ns.lcf.v1_0.ExceptionCondition;
import org.bic.ns.lcf.v1_0.ExceptionConditionType;
import org.bic.ns.lcf.v1_0.LcfException;
import org.bic.ns.lcf.v1_0.Message;
import org.bic.ns.lcf.v1_0.MessageAlertType; // CONCERNED ABOUT TYPE HERE
import org.bic.ns.lcf.v1_0.ReasonDeniedType;
import org.jvnet.jaxb2_commons.lang.StringUtils;

/**
 *
 * @author Ceridwen Limited
 */
public abstract class EXC00_LCF_Exception extends RuntimeException {

    /**
     *
     */
    public class CustomHeader {

        /**
         *
         */
        public String header;

        /**
         *
         */
        public String value;

        /**
         *
         * @param header
         * @param value
         */
        public CustomHeader(String header, String value) {
            this.header = header;
            this.value = value;
        }
    }

    
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -2641857695689580372L;
	private String longMessage;
	private String shortMessage;
	private String ref;
	
    /**
     *
     * @param shortMessage
     * @param longMessage
     * @param ref
     * @param cause
     */
    public EXC00_LCF_Exception(String shortMessage, String longMessage, String ref, Throwable cause) {
		this.shortMessage = shortMessage;
		this.longMessage = longMessage;
		this.ref = ref;
		if (cause != null) {
                    this.initCause(cause);
		}
	}
        
    /**
     *
     * @return
     */
    public List<CustomHeader> getCustomHeaders() {
            return new ArrayList<>();
        }

    /**
     *
     * @return
     */
    protected abstract ExceptionConditionType getExceptionConditionType();

    /**
     *
     * @return
     */
    protected ReasonDeniedType getReasonDenied() { return null;}
	
    /**
     *
     * @return
     */
    public abstract int getHTTPErrorCode();

    /**
     *
     * @return
     */
    public LcfException getLcfException() {
		LcfException exc = new LcfException();
		
		if (!StringUtils.isEmpty(longMessage)) {
			Message m = new Message();
			m.setMessageType(MessageAlertType.VALUE_1);
			m.getMessageText().add(longMessage);
			exc.getMessage().add(m);
		};

		if (!StringUtils.isEmpty(shortMessage)) {
			Message m = new Message();
			m.setMessageType(MessageAlertType.VALUE_2);
			m.getMessageText().add(shortMessage);
			exc.getMessage().add(m);
		};
		
		if (this.getCause() != null) {
			StringWriter stack = new StringWriter();
			PrintWriter out = new PrintWriter(stack);
			this.getCause().printStackTrace(out);
			Message m = new Message();
			m.setMessageType(MessageAlertType.VALUE_3);
			m.getMessageText().add(stack.toString());
			exc.getMessage().add(m);						
		}
		
		ExceptionCondition c = new ExceptionCondition();
		c.setConditionType(this.getExceptionConditionType());
		if (ref != null) {
			c.setElementId(ref);
		}
		if (this.getReasonDenied() != null) {
			c.setReasonDenied(this.getReasonDenied());
		}
		exc.getExceptionCondition().add(c);

		return exc;
	}
}
