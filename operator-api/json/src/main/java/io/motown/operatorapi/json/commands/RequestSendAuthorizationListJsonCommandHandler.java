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

import com.google.common.collect.Sets;
import io.motown.domain.utils.gson.JsonObject;
import io.motown.domain.utils.gson.JsonSyntaxException;
import io.motown.domain.api.chargingstation.*;
import io.motown.domain.api.security.IdentityContext;
import io.motown.operatorapi.json.exceptions.UserIdentityUnauthorizedException;
import io.motown.operatorapi.viewmodel.model.SendAuthorizationListApiCommand;
import io.motown.operatorapi.viewmodel.persistence.entities.ChargingStation;

import java.util.Set;

class RequestSendAuthorizationListJsonCommandHandler extends JsonCommandHandler {

    private static final String COMMAND_NAME = "RequestSendAuthorizationList";

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

        if (!commandAuthorizationService.isAuthorized(csId, identityContext.getUserIdentity(), RequestSendAuthorizationListCommand.class)) {
            throw new UserIdentityUnauthorizedException(chargingStationId, identityContext.getUserIdentity(), RequestSendAuthorizationListCommand.class);
        }

        try {
            Set<IdentifyingToken> authorizationList = Sets.newHashSet();

            SendAuthorizationListApiCommand command = gson.fromJson(commandObject, SendAuthorizationListApiCommand.class);

            authorizationList.addAll(command.getItems());

            AuthorizationListUpdateType updateType = AuthorizationListUpdateType.valueOf(command.getUpdateType());

            ChargingStation chargingStation = repository.findOne(chargingStationId);
            if (chargingStation != null && chargingStation.communicationAllowed()) {
                commandGateway.send(new RequestSendAuthorizationListCommand(csId, authorizationList, command.getListVersion(), "", updateType, identityContext), new CorrelationToken());
            } else {
                throw new IllegalStateException("It is not possible to send a authorization list to a charging station that is not registered");
            }
        } catch (JsonSyntaxException ex) {
            throw new IllegalArgumentException("SendAuthorizationList command not able to parse the payload, is your json correctly formatted?", ex);
        }
    }
}
