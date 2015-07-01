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
package io.motown.ocpp.websocketjson.response.handler;

import io.motown.domain.utils.gson.Gson;
import io.motown.domain.api.chargingstation.ChargingStationComponent;
import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.schema.generated.v15.Changeavailability;
import io.motown.ocpp.websocketjson.schema.generated.v15.ChangeavailabilityResponse;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import org.junit.Before;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static io.motown.ocpp.websocketjson.OcppWebSocketJsonTestUtils.getGson;
import static io.motown.ocpp.websocketjson.schema.generated.v15.ChangeavailabilityResponse.Status;
import static org.mockito.Mockito.*;

public class ChangeAvailabilityResponseHandlerTest {

    private Gson gson;

    private DomainService domainService;

    private String token;
    private CorrelationToken correlationToken;
    private ChangeAvailabilityResponseHandler handler;

    @Before
    public void setup() {
        gson = getGson();
        domainService = mock(DomainService.class);

        correlationToken = new CorrelationToken();
        token = correlationToken.getToken();
        handler = new ChangeAvailabilityResponseHandler(EVSE_ID, Changeavailability.Type.OPERATIVE, correlationToken);
    }

    @Test
    public void handleAcceptedResponse() {
        ChangeavailabilityResponse payload = new ChangeavailabilityResponse();
        payload.setStatus(Status.ACCEPTED);
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, token, gson.toJson(payload));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, ADD_ON_IDENTITY);

        verify(domainService).changeComponentAvailabilityToOperative(CHARGING_STATION_ID, EVSE_ID, ChargingStationComponent.EVSE, correlationToken, ADD_ON_IDENTITY);
    }

    @Test
    public void handleScheduledResponse() {
        ChangeavailabilityResponse payload = new ChangeavailabilityResponse();
        payload.setStatus(Status.SCHEDULED);
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, token, gson.toJson(payload));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, ADD_ON_IDENTITY);

        verify(domainService).changeComponentAvailabilityToOperative(CHARGING_STATION_ID, EVSE_ID, ChargingStationComponent.EVSE, correlationToken, ADD_ON_IDENTITY);
    }

    @Test
    public void handleRejectedResponse() {
        ChangeavailabilityResponse payload = new ChangeavailabilityResponse();
        payload.setStatus(Status.REJECTED);
        WampMessage message = new WampMessage(WampMessage.CALL_RESULT, token, gson.toJson(payload));

        handler.handle(CHARGING_STATION_ID, message, gson, domainService, ADD_ON_IDENTITY);

        verifyZeroInteractions(domainService);
    }

}
