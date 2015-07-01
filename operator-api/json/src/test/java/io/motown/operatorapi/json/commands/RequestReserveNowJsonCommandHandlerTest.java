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
import io.motown.operatorapi.json.exceptions.UserIdentityUnauthorizedException;
import org.junit.Before;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.ROOT_IDENTITY_CONTEXT;
import static io.motown.operatorapi.json.commands.OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING;

public class RequestReserveNowJsonCommandHandlerTest {

    private Gson gson;

    private RequestReserveNowJsonCommandHandler handler = new RequestReserveNowJsonCommandHandler();

    @Before
    public void setUp() {
        this.gson = OperatorApiJsonTestUtils.getGson();
        handler.setGson(gson);
        handler.setCommandGateway(new TestDomainCommandGateway());
        handler.setRepository(OperatorApiJsonTestUtils.getMockChargingStationRepository());
        handler.setCommandAuthorizationService(OperatorApiJsonTestUtils.getCommandAuthorizationService());
    }

    @Test
    public void testCommand() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{evseId:'1',identifyingToken:{token:'1'},expiryDate:'2014-02-24T12:00:00Z'}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID_STRING, commandObject, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void testCommandNoDate() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{evseId:'1',identifyingToken:{token:'1'}}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID_STRING, commandObject, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommandInvalidDate() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{evseId:'1',identifyingToken:{token:'1'},expiryDate:'2014-02-24'}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID_STRING, commandObject, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommandInvalidStatus() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{evseId:'1',identifyingToken:{token:'1',status:'NEW'},expiryDate:'2014-02-24'}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID_STRING, commandObject, ROOT_IDENTITY_CONTEXT);
    }
}
