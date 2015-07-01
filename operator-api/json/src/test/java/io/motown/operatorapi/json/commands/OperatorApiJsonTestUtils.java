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
import io.motown.domain.utils.gson.GsonBuilder;
import io.motown.domain.api.chargingstation.*;
import io.motown.domain.api.security.UserIdentity;
import io.motown.domain.commandauthorization.CommandAuthorizationService;
import io.motown.operatorapi.json.gson.*;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;
import io.motown.operatorapi.viewmodel.persistence.repositories.ChargingStationRepository;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class OperatorApiJsonTestUtils {

    public static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static final String CHARGING_STATION_ID_STRING = "TEST_REGISTERED";

    public static final String UNREGISTERED_CHARGING_STATION_ID_STRING = "TEST_UNREGISTERED";

    public static final String COMMUNICATION_PROTOCOL = "OCPPS15";

    public static final ChargingStationId CHARGING_STATION_ID = new ChargingStationId(CHARGING_STATION_ID_STRING);

    public static final TransactionId NUMBERED_TRANSACTION_ID = TransactionIdFactory.createTransactionId("1", CHARGING_STATION_ID, COMMUNICATION_PROTOCOL);

    private OperatorApiJsonTestUtils() {
        // Private no-arg constructor to prevent instantiation of utility class.
    }

    public static Gson getGson() {
        return new GsonBuilder().
                setDateFormat(ISO8601_DATE_FORMAT).
                registerTypeAdapter(EvseId.class, new EvseIdTypeAdapterDeserializer()).
                registerTypeAdapter(TextualToken.class, new TextualTokenTypeAdapterDeserializer()).
                registerTypeAdapter(Coordinates.class, new CoordinatesTypeAdapterDeserializer()).
                registerTypeAdapter(Address.class, new AddressTypeAdapterDeserializer()).
                registerTypeAdapter(OpeningTime.class, new OpeningTimeTypeAdapterDeserializer()).
                registerTypeAdapter(Accessibility.class, new AccessibilityTypeAdapterDeserializer()).
                create();
    }

    public static ChargingStationRepository getMockChargingStationRepository() {
        ChargingStationRepository repo = mock(ChargingStationRepository.class);
        ChargingStation registeredStation = mock(ChargingStation.class);
        when(registeredStation.getProtocol()).thenReturn(COMMUNICATION_PROTOCOL);
        when(registeredStation.isAccepted()).thenReturn(true);
        when(registeredStation.isConfigured()).thenReturn(true);
        when(registeredStation.communicationAllowed()).thenReturn(true);
        ChargingStation unregisteredStation = mock(ChargingStation.class);
        when(unregisteredStation.isAccepted()).thenReturn(false);
        when(unregisteredStation.isConfigured()).thenReturn(false);
        when(unregisteredStation.communicationAllowed()).thenReturn(false);
        when(repo.findOne(CHARGING_STATION_ID_STRING)).thenReturn(registeredStation);
        when(repo.findOne(UNREGISTERED_CHARGING_STATION_ID_STRING)).thenReturn(unregisteredStation);
        return repo;
    }

    public static CommandAuthorizationService getCommandAuthorizationService() {
        return getCommandAuthorizationService(true);
    }

    public static CommandAuthorizationService getCommandAuthorizationService(boolean authorized) {
        CommandAuthorizationService commandAuthorizationService = mock(CommandAuthorizationService.class);
        when(commandAuthorizationService.isAuthorized(any(ChargingStationId.class), any(UserIdentity.class), any(Class.class))).thenReturn(authorized);
        return commandAuthorizationService;
    }
}
