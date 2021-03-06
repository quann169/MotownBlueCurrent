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
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import javax.annotation.Nullable;
import java.util.Date;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code DiagnosticsRequestedEvent} is the event which is published when a charging station's diagnostics are
 * requested. Protocol add-ons should respond to this event (if applicable) and request a charging station to send its
 * diagnostics.
 */
public final class DiagnosticsRequestedEvent implements CommunicationWithChargingStationRequestedEvent {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final String protocol;

    private final String uploadLocation;

    private final Integer numRetries;

    private final Integer retryInterval;

    private final Date periodStartTime;

    private final Date periodStopTime;

    private final IdentityContext identityContext;

    /**
     * Creates a {@code DiagnosticsRequestedEvent}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param protocol          the protocol the charging station supports
     * @param uploadLocation    the location where the diagnostics file should be uploaded to
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code protocol}, {@code uploadLocation} or
     *                              {@code identityContext} is {@code null}.
     */
    public DiagnosticsRequestedEvent(ChargingStationId chargingStationId, String protocol, String uploadLocation, IdentityContext identityContext) {
        this(chargingStationId, protocol, uploadLocation, null, null, null, null, identityContext);
    }

    /**
     * Creates a {@code DiagnosticsRequestedEvent}.
     *
     * @param chargingStationId the identifier of the charging station.
     * @param protocol          the protocol the charging station supports
     * @param uploadLocation    the location where the diagnostics file should be uploaded to
     * @param numRetries        the optional number of retries the charging station should perform in case of failure
     * @param retryInterval     the optional interval in seconds between retry attempts
     * @param periodStartTime   the optional date and time of the oldest logging information to include in the diagnostics report
     * @param periodStopTime    the optional date and time of the latest logging information to include in the diagnostics report
     * @param identityContext   identity context.
     * @throws NullPointerException if {@code chargingStationId}, {@code protocol}, {@code uploadLocation} or
     *                              {@code identityContext} is {@code null}.
     */
    public DiagnosticsRequestedEvent(ChargingStationId chargingStationId, String protocol, String uploadLocation, @Nullable Integer numRetries,
                                     @Nullable Integer retryInterval, @Nullable Date periodStartTime, @Nullable Date periodStopTime, IdentityContext identityContext) {
        this.chargingStationId = checkNotNull(chargingStationId);
        checkNotNull(protocol);
        checkArgument(!protocol.isEmpty());
        this.protocol = protocol;
        this.uploadLocation = checkNotNull(uploadLocation);
        this.numRetries = numRetries;
        this.retryInterval = retryInterval;
        this.periodStartTime = periodStartTime != null ? new Date(periodStartTime.getTime()) : null;
        this.periodStopTime = periodStopTime != null ? new Date(periodStopTime.getTime()) : null;
        this.identityContext = checkNotNull(identityContext);
    }

    /**
     * Gets the charging station identifier.
     *
     * @return the charging station identifier.
     */
    public ChargingStationId getChargingStationId() {
        return this.chargingStationId;
    }

    /**
     * @return the protocol identifier
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @return the location where the diagnostics file should be uploaded to
     */
    public String getUploadLocation() {
        return uploadLocation;
    }

    /**
     * @return the optional number of retries to perform in case the upload fails
     */
    @Nullable
    public Integer getNumRetries() {
        return numRetries;
    }

    /**
     * @return the optional amount of time in seconds to wait before performing a retry
     */
    @Nullable
    public Integer getRetryInterval() {
        return retryInterval;
    }

    /**
     * @return the optional date and time of the oldest logging information to include in the diagnostics report
     */
    @Nullable
    public Date getPeriodStartTime() {
        return periodStartTime != null ? new Date(periodStartTime.getTime()) : null;
    }

    /**
     * @return the optional date and time of the latest logging information to include in the diagnostics report
     */
    @Nullable
    public Date getPeriodStopTime() {
        return periodStopTime != null ? new Date(periodStopTime.getTime()) : null;
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
