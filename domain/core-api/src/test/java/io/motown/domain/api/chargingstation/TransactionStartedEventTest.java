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

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static junit.framework.Assert.assertEquals;

public class TransactionStartedEventTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullChargingStationId() {
        new TransactionStartedEvent(null, TRANSACTION_ID, EVSE_ID, IDENTIFYING_TOKEN, 1, new Date(), Collections.<String, String>emptyMap(), NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullTransactionId() {
        new TransactionStartedEvent(CHARGING_STATION_ID, null, EVSE_ID, IDENTIFYING_TOKEN, 1, new Date(), Collections.<String, String>emptyMap(), NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullIdentifyingToken() {
        new TransactionStartedEvent(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, null, 1, new Date(), Collections.<String, String>emptyMap(), NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullTimestamp() {
        new TransactionStartedEvent(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, IDENTIFYING_TOKEN, 1, null, Collections.<String, String>emptyMap(), NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullAttributes() {
        new TransactionStartedEvent(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, IDENTIFYING_TOKEN, 1, new Date(), null, NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingWithNullIdentityContext() {
        new TransactionStartedEvent(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, IDENTIFYING_TOKEN, 1, new Date(), Collections.<String, String>emptyMap(), null);
    }

    @Test
    public void testImmutableDate() {
        Date now = new Date();
        TransactionStartedEvent event = new TransactionStartedEvent(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, IDENTIFYING_TOKEN, TRANSACTION_NUMBER, now, BOOT_NOTIFICATION_ATTRIBUTES, NULL_USER_IDENTITY_CONTEXT);
        event.getTimestamp().setTime(TWO_MINUTES_AGO.getTime());
        assertEquals(now, event.getTimestamp());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testImmutableMap() {
        new TransactionStartedEvent(CHARGING_STATION_ID, TRANSACTION_ID, EVSE_ID, IDENTIFYING_TOKEN, 1, new Date(), new HashMap<String, String>(), NULL_USER_IDENTITY_CONTEXT).getAttributes().put("key", "value");
    }

    @Test
    public void equalsAndHashCodeShouldBeImplementedAccordingToTheContract() {
        EqualsVerifier.forClass(TransactionStartedEvent.class).usingGetClass().verify();
    }
}
