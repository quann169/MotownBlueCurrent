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
package io.motown.domain.api.chargingstation;

import io.motown.domain.api.security.IdentityContext;

import java.util.Date;
import java.util.Map;

/**
 * {@code ChargingStationStatusNotificationReceivedEvent} is the event which is published when a charging station has
 * notified Motown about its status.
 */
public class ChargingStationStatusNotificationReceivedEvent extends StatusNotificationReceivedEvent {

    /**
     * Creates a {@code ComponentStatusNotificationReceivedEvent}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param status            the status of the component
     * @param timestamp         the optional date and time
     * @param attributes        optional attributes
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code component}, {@code componentId}, {@code status},
     *                          {@code attributes} or {@code identityContext} is {@code null}.
     */
    public ChargingStationStatusNotificationReceivedEvent(ChargingStationId chargingStationId, ComponentStatus status, Date timestamp,
                                                          Map<String, String> attributes, IdentityContext identityContext) {
        super(chargingStationId, status, timestamp, attributes, identityContext);
    }
}
