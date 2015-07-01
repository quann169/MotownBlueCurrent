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

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.IDENTITY_CONTEXT;

public class SetChargingStationOpeningTimesJsonCommandHandlerTest {
    private Gson gson;
    private SetChargingStationOpeningTimesJsonCommandHandler handler = new SetChargingStationOpeningTimesJsonCommandHandler();

    @Before
    public void setUp() {
        gson = OperatorApiJsonTestUtils.getGson();
        handler.setGson(gson);
        handler.setCommandGateway(new TestDomainCommandGateway());
        handler.setRepository(OperatorApiJsonTestUtils.getMockChargingStationRepository());
        handler.setCommandAuthorizationService(OperatorApiJsonTestUtils.getCommandAuthorizationService());
    }

    @Test
    public void testSetOpeningTimes() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{openingTimes:[{day:'monday',timeStart:'12:00',timeStop:'12:30'}]}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, IDENTITY_CONTEXT);
    }

    @Test
    public void testSetOpeningTimesOtherHour() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{openingTimes:[{day:'monday',timeStart:'12:00',timeStop:'13:00'}]}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, IDENTITY_CONTEXT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetOpeningTimesInvalidDay() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{openingTimes:[{day:'maandag',timeStart:'12:00',timeStop:'15:00'}]}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, IDENTITY_CONTEXT);
    }

    @Test(expected = JsonParseException.class)
    public void testSetOpeningTimesInvalidTime() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{openingTimes:[{day:'tuesday',timeStart:'12:60',timeStop:'15:00'}]}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, IDENTITY_CONTEXT);
    }

    @Test(expected = JsonParseException.class)
    public void testSetOpeningTimesInvalidTime2() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{openingTimes:[{day:'tuesday',timeStart:'24:00',timeStop:'15:00'}]}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, IDENTITY_CONTEXT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpeningTimesTimeStartEqualToTimeStop() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{openingTimes:[{day:'tuesday',timeStart:'12:00',timeStop:'12:00'}]}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, IDENTITY_CONTEXT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpeningTimesTimeStartNotBeforeTimeStop() throws UserIdentityUnauthorizedException {
        JsonObject commandObject = gson.fromJson("{openingTimes:[{day:'tuesday',timeStart:'12:00',timeStop:'11:00'}]}", JsonObject.class);
        handler.handle(OperatorApiJsonTestUtils.CHARGING_STATION_ID_STRING, commandObject, IDENTITY_CONTEXT);
    }
}
