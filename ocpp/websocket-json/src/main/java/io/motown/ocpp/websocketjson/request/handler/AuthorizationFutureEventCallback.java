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
package io.motown.ocpp.websocketjson.request.handler;

import io.motown.domain.api.chargingstation.AuthorizationResultEvent;
import io.motown.domain.api.chargingstation.AuthorizationResultStatus;
import io.motown.domain.utils.axon.FutureEventCallback;
import io.motown.domain.utils.gson.Gson;
import io.motown.ocpp.viewmodel.domain.AuthorizationResult;
import io.motown.ocpp.websocketjson.schema.generated.v15.AuthorizeResponse;
import io.motown.ocpp.websocketjson.schema.generated.v15.IdTagInfo;
import io.motown.ocpp.websocketjson.wamp.WampMessage;

import java.io.IOException;

import org.atmosphere.websocket.WebSocket;
import org.axonframework.domain.EventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorizationFutureEventCallback extends FutureEventCallback<AuthorizationResult> {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationFutureEventCallback.class);

    private WebSocket webSocket;

    private String callId;

    private Gson gson;

    public AuthorizationFutureEventCallback(String callId, WebSocket webSocket, Gson gson) {
        this.webSocket = webSocket;
        this.callId = callId;
        this.gson = gson;
    }

    @Override
    public boolean onEvent(EventMessage<?> event) {
//		AuthorizationResultEvent resultEvent;
//
//		if (event.getPayload() instanceof AuthorizationResultEvent) {
//			resultEvent = (AuthorizationResultEvent) event.getPayload();
//
//			AuthorizationResult result = new AuthorizationResult(resultEvent
//					.getIdentifyingToken().getToken(),
//					resultEvent.getAuthenticationStatus());
//
//			this.setResult(result);
//
//			this.countDownLatch();
//
//			this.writeResult(result);
//
//			return true;
//		} else {
//			// not the right type of event... not 'handled'
//			return false;
//		}
		
    	AuthorizationResultEvent resultEvent;

        if (!(event.getPayload() instanceof AuthorizationResultEvent)) {
            // not the right type of event... not 'handled'
            return false;
        }

        resultEvent = (AuthorizationResultEvent) event.getPayload();

        IdTagInfo idTagInfo = new IdTagInfo();
        idTagInfo.setStatus(convert(resultEvent.getAuthenticationStatus()));
        AuthorizeResponse response = new AuthorizeResponse();
        response.setIdTagInfo(idTagInfo);
        writeResult(response);

        return true;
		
    }

    public void writeResult(AuthorizeResponse result) {
        try {
        	WampMessage mess = new WampMessage(WampMessage.CALL_RESULT, callId, result);
        	String response = mess.toJson(gson);
        	for (IdTagInfo.Status status : IdTagInfo.Status.values()) {
        		response = response.replace(status.name(), status.toString());
    		}
            webSocket.write(response);
        } catch (IOException e) {
            LOG.error("IOException while writing to web socket.", e);
        }
    }
    
    /**
     * Converts a {@code AuthorizationResultStatus} to a {@code IdTagInfo.Status}. Throws an assertion error is the
     * status is unknown.
     *
     * @param status status to convert.
     * @return converted status.
     */
    private static IdTagInfo.Status convert(AuthorizationResultStatus status) {
        IdTagInfo.Status result;

        switch (status) {
            case ACCEPTED:
                result = IdTagInfo.Status.ACCEPTED;
                break;
            case BLOCKED:
                result = IdTagInfo.Status.BLOCKED;
                break;
            case EXPIRED:
                result = IdTagInfo.Status.EXPIRED;
                break;
            case INVALID:
                result = IdTagInfo.Status.INVALID;
                break;
            default:
                throw new AssertionError("AuthorizationResultStatus has unknown status: " + status);
        }

        return result;
    }
}
