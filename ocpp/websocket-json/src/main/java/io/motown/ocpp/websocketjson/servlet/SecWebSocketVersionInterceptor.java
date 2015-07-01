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
package io.motown.ocpp.websocketjson.servlet;

import org.atmosphere.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SecWebSocketVersionInterceptor implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(OcppWebSocketServlet.class);

    public static final String SEC_WEB_SOCKET_PROTOCOL_HEADER = "Sec-WebSocket-Protocol";

    public static final String SUPPORTED_WEB_SOCKET_PROTOCOL_PARAM = "supportedProtocols";

    private static final String PROTOCOL_SEPARATOR = ",";

    private List<String> supportedProtocols = new ArrayList<>();

    private static final String URL_ENCODER_ENCODING = "UTF-8";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String supportedProtocolsString = filterConfig.getInitParameter(SUPPORTED_WEB_SOCKET_PROTOCOL_PARAM);
        if (supportedProtocolsString != null) {
            String[] temp = supportedProtocolsString.split(PROTOCOL_SEPARATOR);
            for(String protocol:temp) {
                supportedProtocols.add(protocol.toLowerCase());
            }
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest r = HttpServletRequest.class.cast(request);

        if (Utils.webSocketEnabled(r)) {
            String header = r.getHeader(SEC_WEB_SOCKET_PROTOCOL_HEADER);

            if(header != null && !header.isEmpty()) {
            	String validProtocol = "";
                String[] temp = header.toLowerCase().split(PROTOCOL_SEPARATOR);
                for(String protocol:temp) {
                	if(supportedProtocols.contains(protocol)) {
                		if ("".equals(validProtocol)) {
                			validProtocol = protocol;
    					} else {
    						validProtocol = "," + protocol;
    					}
                	}
                }
                if(!"".equals(validProtocol)) {
                    HttpServletResponse.class.cast(response).addHeader(SEC_WEB_SOCKET_PROTOCOL_HEADER, URLEncoder.encode(validProtocol, URL_ENCODER_ENCODING));
                } else {
                    LOG.warn("Invalid websocket protocol [{}] received.", header);
                    HttpServletResponse.class.cast(response).sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "Websocket protocol not supported");
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // We are forced to implement this method from the Filter interface but we currently do not require any
        // clean-up.
    }

}
