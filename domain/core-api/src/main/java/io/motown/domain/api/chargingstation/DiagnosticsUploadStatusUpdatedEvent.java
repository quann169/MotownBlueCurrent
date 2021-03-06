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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code DiagnosticsUploadStatusUpdatedEvent} is the event which is published when diagnostics status update is
 * received.
 */
public final class DiagnosticsUploadStatusUpdatedEvent {

    private final ChargingStationId chargingStationId;

    private final boolean isUploaded;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code DiagnosticsUploadStatusUpdatedEvent}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param isUploaded        indicator if the diagnostics file has been uploaded or not.
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code protocol} or {@code identityContext} is {@code null}.
     */
    public DiagnosticsUploadStatusUpdatedEvent(ChargingStationId chargingStationId, boolean isUploaded, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.isUploaded = isUploaded;
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * @return boolean indicating if the upload has succeeded or not
     */
    public boolean isUploaded() {
        return isUploaded;
    }

    /**
     * Gets the identity context.
     *
     * @return the identity context.
     */
    public IdentityContext getIdentityContext() {
        return identityContext;
    }
}
