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

import io.motown.domain.utils.gson.JsonObject;
import io.motown.domain.utils.gson.JsonSyntaxException;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.domain.api.chargingstation.RequestReserveNowCommand;
import io.motown.domain.api.security.IdentityContext;
import io.motown.operatorapi.json.exceptions.UserIdentityUnauthorizedException;
import io.motown.operatorapi.viewmodel.model.RequestReserveNowApiCommand;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;

class RequestReserveNowJsonCommandHandler extends JsonCommandHandler {

    private static final String COMMAND_NAME = "RequestReserveNow";

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCommandName() {
        return COMMAND_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(String chargingStationId, JsonObject commandObject, IdentityContext identityContext) throws UserIdentityUnauthorizedException {
        ChargingStationId csId = new ChargingStationId(chargingStationId);

        if (!commandAuthorizationService.isAuthorized(csId, identityContext.getUserIdentity(), RequestReserveNowCommand.class)) {
            throw new UserIdentityUnauthorizedException(chargingStationId, identityContext.getUserIdentity(), RequestReserveNowCommand.class);
        }

        try {
            ChargingStation chargingStation = repository.findOne(chargingStationId);
            if (chargingStation != null && chargingStation.communicationAllowed()) {
                RequestReserveNowApiCommand command = gson.fromJson(commandObject, RequestReserveNowApiCommand.class);

                commandGateway.send(new RequestReserveNowCommand(csId, command.getEvseId(), command.getIdentifyingToken(), command.getExpiryDate(), null, identityContext), new CorrelationToken());
            } else {
                throw new IllegalStateException("It is not possible to request a reservation on a charging station that is not registered");
            }
        } catch (JsonSyntaxException ex) {
            throw new IllegalArgumentException("Reserve now command is not able to parse the payload, is your json correctly formatted?", ex);
        }
    }
}
