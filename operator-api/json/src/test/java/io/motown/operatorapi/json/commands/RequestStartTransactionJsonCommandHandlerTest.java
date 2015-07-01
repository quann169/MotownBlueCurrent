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
import io.motown.domain.utils.gson.JsonParseException;
import io.motown.operatorapi.json.exceptions.UserIdentityUnauthorizedException;
import org.junit.Before;
import org.junit.Test;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.ROOT_IDENTITY_CONTEXT;
import static io.motown.operatorapi.json.commands.OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING;

public class RequestStartTransactionJsonCommandHandlerTest {

    private Gson gson;

    private RequestStartTransactionJsonCommandHandler handler = new RequestStartTransactionJsonCommandHandler();

    @Before
    public void setUp() {
        gson = OperatorApiJsonTestUtils.getGson();

        handler.setGson(gson);
        handler.setCommandGateway(new TestDomainCommandGateway());
        handler.setRepository(OperatorApiJsonTestUtils.getMockChargingStationRepository());
        handler.setCommandAuthorizationService(OperatorApiJsonTestUtils.getCommandAuthorizationService());
    }

    @Test
    public void testStartTransaction() throws UserIdentityUnauthorizedException {
        JsonObject command = gson.fromJson("{evseId:'1',identifyingToken:{token:'1',status:'ACCEPTED'}}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID_STRING, command, ROOT_IDENTITY_CONTEXT);
    }

    @Test
    public void testStartTransactionNoStatus() throws UserIdentityUnauthorizedException {
        JsonObject command = gson.fromJson("{evseId:'1',identifyingToken:{token:'1'}}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID_STRING, command, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void testStartTransactionNoToken() throws UserIdentityUnauthorizedException {
        JsonObject command = gson.fromJson("{evseId:'1'}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID_STRING, command, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = JsonParseException.class)
    public void testStartTransactionIdentifyingTokenIsString() throws UserIdentityUnauthorizedException {
        JsonObject command = gson.fromJson("{evseId:'1',identifyingToken:'ACCEPTED'}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID_STRING, command, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = JsonParseException.class)
    public void testStartTransactionTokenIsObject() throws UserIdentityUnauthorizedException {
        JsonObject command = gson.fromJson("{evseId:'1',identifyingToken:{token:{status:'ACCEPTED'}}}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID_STRING, command, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = JsonParseException.class)
    public void testStartTransactionStatusIsObject() throws UserIdentityUnauthorizedException {
        JsonObject command = gson.fromJson("{evseId:'1',identifyingToken:{token:'1',status:{status:'ACCEPTED'}}}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID_STRING, command, ROOT_IDENTITY_CONTEXT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartTransactionStatusInvalid() throws UserIdentityUnauthorizedException {
        JsonObject command = gson.fromJson("{evseId:'1',identifyingToken:{token:'1',status:'NEW'}}", JsonObject.class);
        handler.handle(CHARGING_STATION_ID_STRING, command, ROOT_IDENTITY_CONTEXT);
    }
}
