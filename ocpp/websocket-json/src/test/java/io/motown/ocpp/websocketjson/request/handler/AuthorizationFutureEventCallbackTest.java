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

import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getGson;
import io.motown.domain.utils.gson.Gson;
import io.motown.ocpp.websocketjson.schema.generated.v15.AuthorizeResponse;
import io.motown.ocpp.websocketjson.schema.generated.v15.IdTagInfo;
import io.motown.ocpp.websocketjson.schema.generated.v15.IdTagInfo__;
import io.motown.ocpp.websocketjson.schema.generated.v15.StarttransactionResponse;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class AuthorizationFutureEventCallbackTest {
	
	private Gson gson;
	private Gson gson2;

    @Before
    public void setup() {
    	gson = getGson();
    	gson2 = getGson();
    }

	@Test
    public void handleValidRequest() throws IOException {
		AuthorizationFutureEventCallback obj = new AuthorizationFutureEventCallback("", null, gson);
		IdTagInfo idTagInfo = new IdTagInfo();
        idTagInfo.setStatus(IdTagInfo.Status.ACCEPTED);

        System.out.println("++++++++++++++++++++++++++++++++++");
        System.out.println("idTagInfo: " + idTagInfo);        
        
        AuthorizeResponse response = new AuthorizeResponse();
        response.setIdTagInfo(idTagInfo);
        
        System.out.println("idTagInfo: " + idTagInfo.getStatus());
        System.out.println("response: " + response);
        System.out.println("++++++++++++++++++++++++++++++++++");
        obj.writeResult(response);
        
        
        IdTagInfo__ idTagInfo1 = new IdTagInfo__();
        idTagInfo1.setStatus(IdTagInfo__.Status.ACCEPTED);

        StarttransactionResponse response1 = new StarttransactionResponse();
        response1.setTransactionId(1111);
        response1.setIdTagInfo(idTagInfo1);
        StartTransactionFutureEventCallback obj2 = new StartTransactionFutureEventCallback("", null, gson2, null, null, null, null, null);
        obj2.writeResult(response1);
        
    }
}
