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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.motown.operatorapi.json.exceptions.UserIdentityUnauthorizedException;
import org.junit.Before;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.ROOT_IDENTITY_CONTEXT;
import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.UNKNOWN_CHARGING_STATION_ID;

public class RequestSendAuthorizationListJsonCommandHandlerTest {

    private Gson gson;

    private RequestSendAuthorizationListJsonCommandHandler handler = new RequestSendAuthorizationListJsonCommandHandler();

    @Before
    public void setUp() {
        this.gson = OperatorApiJsonTestUtils.getGson();

        handler.setGson(gson);
        handler.setCommandGateway(new TestDomainCommandGateway());
        handler.setRepository(OperatorApiJsonTestUtils.getMockChargingStationRepository());
        handler.setCommandAuthorizationService(OperatorApiJsonTestUtils.getCommandAuthorizationService());
    }

    @Test
    public void testSendAuthorizationListCommand() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{listVersion:1,updateType:'FULL',items:[{token:'1',status:'ACCEPTED'},{token:'2',status:'BLOCKED'}]}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, ROOT_IDENTITY_CONTEXT);
    }

    @Test
    public void testDifferentialUpdateType() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{listVersion:1,updateType:'DIFFERENTIAL',items:[{token:'1',status:'ACCEPTED'},{token:'2',status:'BLOCKED'}]}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUpdateType() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{listVersion:1,updateType:'NEW',items:[{token:'1',status:'ACCEPTED'},{token:'2',status:'BLOCKED'}]}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAuthenticationStatus() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{listVersion:1,updateType:'FULL',items:[{token:'1',status:'NEW'}]}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = IllegalStateException.class)
    public void handleWithNullChargingStation() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{listVersion:1,updateType:'DIFFERENTIAL',items:[{token:'1',status:'ACCEPTED'},{token:'2',status:'BLOCKED'}]}", JsonObject.class);
        handler.handle(UNKNOWN_CHARGING_STATION_ID.getId(), commandObject, ROOT_IDENTITY_CONTEXT);
    }

}
