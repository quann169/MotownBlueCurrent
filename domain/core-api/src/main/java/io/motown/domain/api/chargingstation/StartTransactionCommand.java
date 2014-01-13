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

import com.google.common.collect.ImmutableMap;
import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

import java.util.Date;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@code StartTransactionCommand} is the command which is published when a charging station has started a transaction.
 */
public final class StartTransactionCommand {

    @TargetAggregateIdentifier
    private final ChargingStationId chargingStationId;

    private final TransactionId transactionId;

    private final ConnectorId connectorId;

    private final IdentifyingToken identifyingToken;

    private final int meterStart;

    private final Date timestamp;

    private final Map<String, String> attributes;

    /**
     * Creates a {@code StartTransactionCommand}.
     *
     * In contrast to most of the other classes and methods in the Core API the {@code transactionId} and
     * {@code identifyingToken} are possibly mutable. Some default, immutable implementations of these interfaces are
     * provided but the mutability of these parameters can't be guaranteed.
     *
     * @param chargingStationId the charging station's identifier.
     * @param transactionId     the transaction's identifier.
     * @param connectorId       the connector's identifier or position.
     * @param identifyingToken  the token which started the transaction.
     * @param meterStart        meter value in Wh for the connector when the transaction started.
     * @param timestamp         the time at which the transaction started.
     * @throws NullPointerException if {@code chargingStationId}, {@code transactionId}, {@code identifyingToken} or
     * {@code timestamp} is {@code null}.
     * @throws IllegalArgumentException if {@code connectorId} is negative.
     */
    public StartTransactionCommand(ChargingStationId chargingStationId, TransactionId transactionId, ConnectorId connectorId, IdentifyingToken identifyingToken, int meterStart, Date timestamp) {
        this(chargingStationId, transactionId, connectorId, identifyingToken, meterStart, timestamp, ImmutableMap.<String, String>of());
    }


    /**
     * Creates a {@code StartTransactionCommand}.
     *
     * In contrast to most of the other classes and methods in the Core API the {@code transactionId} and
     * {@code identifyingToken} are possibly mutable. Some default, immutable implementations of these interfaces are
     * provided but the mutability of these parameters can't be guaranteed.
     *
     * @param chargingStationId the charging station's identifier.
     * @param transactionId     the transaction's identifier.
     * @param connectorId       the connector's identifier or position.
     * @param identifyingToken  the token which started the transaction.
     * @param meterStart        meter value in Wh for the connector when the transaction started.
     * @param timestamp         the time at which the transaction started.
     * @param attributes        a {@link java.util.Map} of attributes. These attributes are additional information provided by
     *                          the charging station when it booted but which are not required by Motown. Because
     *                          {@link java.util.Map} implementations are potentially mutable a defensive copy is made.
     * @throws NullPointerException if {@code chargingStationId}, {@code transactionId}, {@code identifyingToken},
     * {@code timestamp} or {@code attributes} is {@code null}.
     * @throws IllegalArgumentException if {@code connectorId} is negative.
     */
    public StartTransactionCommand(ChargingStationId chargingStationId, TransactionId transactionId, ConnectorId connectorId, IdentifyingToken identifyingToken, int meterStart, Date timestamp, Map<String, String> attributes) {
        this.chargingStationId = checkNotNull(chargingStationId);
        this.transactionId = checkNotNull(transactionId);
        this.connectorId = checkNotNull(connectorId);
        this.identifyingToken = checkNotNull(identifyingToken);
        this.meterStart = meterStart;
        this.timestamp = new Date(checkNotNull(timestamp).getTime());
        this.attributes = ImmutableMap.copyOf(checkNotNull(attributes));
    }

    /**
     * Gets the charging station's identifier.
     *
     * @return the charging station's identifier.
     */
    public ChargingStationId getChargingStationId() {
        return chargingStationId;
    }

    /**
     * Gets the transaction's identifier.
     *
     * @return the transaction's identifier
     */
    public TransactionId getTransactionId() {
        return transactionId;
    }

    /**
     * Gets the connector's identifier or position.
     *
     * @return the connector's identifier or position.
     */
    public ConnectorId getConnectorId() {
        return connectorId;
    }

    /**
     * Gets the token which started the transaction.
     *
     * @return the token.
     */
    public IdentifyingToken getIdentifyingToken() {
        return identifyingToken;
    }

    /**
     * Gets the meter value when the transaction started.
     *
     * @return the meter value when the transaction started.
     */
    public int getMeterStart() {
        return meterStart;
    }

    /**
     * Gets the time at which the transaction started.
     *
     * @return the time at which the transaction started.
     */
    public Date getTimestamp() {
        return new Date(timestamp.getTime());
    }

    /**
     * Gets the attributes associated with the start of the transaction.
     *
     * These attributes are additional information provided by the charging station when it started the transaction
     * but which are not required by Motown.
     *
     * @return an immutable {@link java.util.Map} of attributes.
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StartTransactionCommand that = (StartTransactionCommand) o;

        if (attributes != null ? !attributes.equals(that.attributes) : that.attributes != null) return false;
        if (meterStart != that.meterStart) return false;
        if (!chargingStationId.equals(that.chargingStationId)) return false;
        if (!connectorId.equals(that.connectorId)) return false;
        if (!identifyingToken.equals(that.identifyingToken)) return false;
        if (!timestamp.equals(that.timestamp)) return false;
        if (!transactionId.equals(that.transactionId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = chargingStationId.hashCode();
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        result = 31 * result + transactionId.hashCode();
        result = 31 * result + connectorId.hashCode();
        result = 31 * result + identifyingToken.hashCode();
        result = 31 * result + meterStart;
        result = 31 * result + timestamp.hashCode();
        return result;
    }
}
