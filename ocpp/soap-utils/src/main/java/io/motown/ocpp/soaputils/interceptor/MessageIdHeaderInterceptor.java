/**
 * Copyright (C) 2013 Motown.IO (info@motown.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.motown.ocpp.soaputils.interceptor;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageIdHeaderInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final String SOAP_HEADER_KEY = "org.apache.cxf.headers.Header.list";
    private static final String NAMESPACE_URI   = "http://www.w3.org/2005/08/addressing";
    private static final String QUALIFIED_NAME  = "wsa:MessageID";
    private static final String LOCAL_NAME      = "MessageID";

    public MessageIdHeaderInterceptor() {
        // phases: http://cxf.apache.org/docs/interceptors.html
        // 'pre protocol' seems to be the best moment to check the header, if we do it earlier the headers don't exist in the message object so we can't re-use the 'owner document'
        // if we do it later the unmarshalMAPs method (in MAPCodec.java) will already have processed the headers and will not process our added header
        super(Phase.PRE_PROTOCOL);
    }

    @Override
    public void handleMessage(Message message) {
        List<SoapHeader> headers = (ArrayList<SoapHeader>) message.get(SOAP_HEADER_KEY);

        // if the header doesn't exist and we have at least one header to access 'owner document' we can create and add our own MessageID header
        if(!messageIdHeaderExists(headers) && !headers.isEmpty()) {
            Element existingHeaderElement = (Element) headers.get(0).getObject();

            // use the existing header element to create our own MessageID header with random UUID
            Element element = existingHeaderElement.getOwnerDocument().createElementNS(NAMESPACE_URI, QUALIFIED_NAME);
            element.appendChild(existingHeaderElement.getOwnerDocument().createTextNode("uuid:" + UUID.randomUUID().toString()));

            QName qname = new QName(NAMESPACE_URI, LOCAL_NAME);
            SoapHeader header = new SoapHeader(qname, element);

            // by default a SoapHeader is created with 'direction out'
            header.setDirection(Header.Direction.DIRECTION_IN);

            headers.add(header);
        }
    }

    /**
     * Checks if the MessageID header exists in the list of headers.
     *
     * @param headers list of headers
     * @return true if the MessageID header exists, false if not
     */
    private Boolean messageIdHeaderExists(List<SoapHeader> headers) {
        for(SoapHeader header:headers) {
            if(header.getName().getLocalPart().equalsIgnoreCase(LOCAL_NAME)) {
                return true;
            }
        }
        return false;
    }

}
