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
package io.motown.operatorapi.json.commands;

import io.motown.domain.utils.gson.Gson;
import io.motown.domain.utils.gson.JsonObject;
import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.domain.api.chargingstation.RequestStopTransactionCommand;
import io.motown.domain.api.chargingstation.test.ChargingStationTestUtils;
import io.motown.operatorapi.json.exceptions.UserIdentityUnauthorizedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static io.motown.operatorapi.json.commands.OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class RequestStopTransactionJsonCommandHandlerTest {

    private Gson gson;

    private RequestStopTransactionJsonCommandHandler handler = new RequestStopTransactionJsonCommandHandler();

    private DomainCommandGateway domainCommandGateway = mock(DomainCommandGateway.class);

    @Before
    public void setUp() {
        reset(domainCommandGateway);
        gson = OperatorApiJsonTestUtils.getGson();

        handler.setGson(gson);
        handler.setCommandGateway(domainCommandGateway);
        handler.setRepository(OperatorApiJsonTestUtils.getMockChargingStationRepository());
        handler.setCommandAuthorizationService(OperatorApiJsonTestUtils.getCommandAuthorizationService());
    }

    @Test
    public void testHandleStopTransactionOnRegisteredStation() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{'id' : '" + OperatorApiJsonTestUtils.NUMBERED_TRANSACTION_ID.getId() + "'}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID_STRING, commandObject, ChargingStationTestUtils.ROOT_IDENTITY_CONTEXT);
    }

    @Test
    public void handlerShouldConstructCorrectNumberedTransactionId() throws UserIdentityUnauthorizedException {
        ArgumentCaptor<RequestStopTransactionCommand> argumentCaptor = ArgumentCaptor.forClass(RequestStopTransactionCommand.class);
        JsonObject commandObject = gson.fromJson("{'id' : '" + OperatorApiJsonTestUtils.NUMBERED_TRANSACTION_ID.getId() + "'}", JsonObject.class);

        handler.handle(CHARGING_STATION_ID_STRING, commandObject, ChargingStationTestUtils.ROOT_IDENTITY_CONTEXT);

        verify(domainCommandGateway).send(argumentCaptor.capture(), any(CorrelationToken.class));
        assertEquals(OperatorApiJsonTestUtils.NUMBERED_TRANSACTION_ID, argumentCaptor.getValue().getTransactionId());
    }
}
