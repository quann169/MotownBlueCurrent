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

import com.google.common.collect.Maps;
import io.motown.domain.utils.gson.JsonObject;
import io.motown.domain.utils.gson.JsonSyntaxException;
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.domain.api.chargingstation.FirmwareUpdateAttributeKey;
import io.motown.domain.api.chargingstation.RequestFirmwareUpdateCommand;
import io.motown.domain.api.security.IdentityContext;
import io.motown.operatorapi.json.exceptions.UserIdentityUnauthorizedException;
import io.motown.operatorapi.viewmodel.model.UpdateFirmwareApiCommand;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;

import java.util.Map;

class RequestFirmwareUpdateJsonCommandHandler extends JsonCommandHandler {

    private static final String COMMAND_NAME = "RequestFirmwareUpdate";

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

        if (!commandAuthorizationService.isAuthorized(csId, identityContext.getUserIdentity(), RequestFirmwareUpdateCommand.class)) {
            throw new UserIdentityUnauthorizedException(chargingStationId, identityContext.getUserIdentity(), RequestFirmwareUpdateCommand.class);
        }

        try {
            ChargingStation chargingStation = repository.findOne(chargingStationId);
            if (chargingStation != null && chargingStation.communicationAllowed()) {
                UpdateFirmwareApiCommand command = gson.fromJson(commandObject, UpdateFirmwareApiCommand.class);

                Map<String, String> optionalAttributes = Maps.newHashMap();
                if (command.getNumRetries() != null) {
                    optionalAttributes.put(FirmwareUpdateAttributeKey.NUM_RETRIES, command.getNumRetries().toString());
                }
                if (command.getRetryInterval() != null) {
                    optionalAttributes.put(FirmwareUpdateAttributeKey.RETRY_INTERVAL, command.getRetryInterval().toString());
                }

                commandGateway.send(new RequestFirmwareUpdateCommand(csId, command.getLocation(), command.getRetrieveDate(),
                        optionalAttributes, identityContext), new CorrelationToken());
            }
        } catch (JsonSyntaxException ex) {
            throw new IllegalArgumentException("Update firmware command not able to parse the payload, is your json correctly formatted?", ex);
        }
    }
}
