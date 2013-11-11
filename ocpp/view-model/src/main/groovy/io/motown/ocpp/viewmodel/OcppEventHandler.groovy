/**
 * Copyright (C) 2013 Alliander N.V. (info@motown.io)
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
package io.motown.ocpp.viewmodel

import io.motown.domain.api.chargingstation.ChargingStationBootedEvent
import io.motown.domain.api.chargingstation.ChargingStationCreatedEvent
import io.motown.domain.api.chargingstation.ConnectorNotFoundEvent
import io.motown.domain.api.chargingstation.UnlockConnectorRequestedEvent

import org.axonframework.eventhandling.annotation.EventHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class OcppEventHandler {

    private static final Logger log = LoggerFactory.getLogger(OcppEventHandler.class);

    @EventHandler
    void handle(ChargingStationBootedEvent event) {
        log.info("ChargingStationBootedEvent")
    }

    @EventHandler
    void handle(ChargingStationCreatedEvent event) {
        log.info("ChargingStationCreatedEvent")
    }

    @EventHandler
    void handle(ConnectorNotFoundEvent event) {
        log.info("ConnectorNotFoundEvent")
    }

    @EventHandler
    void handle(UnlockConnectorRequestedEvent event) {
        log.info("UnlockConnectorRequestedEvent")
    }
}
