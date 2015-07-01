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
import io.motown.domain.api.chargingstation.ChargingStationId;
import io.motown.domain.api.chargingstation.CorrelationToken;
import io.motown.domain.api.security.AddOnIdentity;
import io.motown.ocpp.viewmodel.domain.DomainService;
import io.motown.ocpp.websocketjson.schema.generated.v15.ResetResponse;
import io.motown.ocpp.websocketjson.wamp.WampMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResetResponseHandler extends ResponseHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ResetResponseHandler.class);

    public ResetResponseHandler(CorrelationToken correlationToken) {
        this.setCorrelationToken(correlationToken);
    }

    @Override
    public void handle(ChargingStationId chargingStationId, WampMessage wampMessage, Gson gson, DomainService domainService, AddOnIdentity addOnIdentity) {
        ResetResponse response = gson.fromJson(wampMessage.getPayloadAsString(), ResetResponse.class);

        switch (response.getStatus()) {
            case ACCEPTED:
                LOG.info("Reset was accepted");
                break;
            case REJECTED:
                LOG.info("Reset was rejected");
                break;
            default:
                throw new AssertionError(String.format("Unknown reset response status: %s", response.getStatus()));
        }
    }
}
