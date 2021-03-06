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

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.*;
import static org.junit.Assert.assertEquals;

public class ChargingStationStatusNotificationReceivedEventTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithChargingStationIdNull() {
        new ChargingStationStatusNotificationReceivedEvent(null, ComponentStatus.AVAILABLE, new Date(), Collections.<String, String>emptyMap(), NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithStatusNull() {
        new ChargingStationStatusNotificationReceivedEvent(CHARGING_STATION_ID, null, new Date(), Collections.<String, String>emptyMap(), NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithTimestampNull() {
        new ChargingStationStatusNotificationReceivedEvent(CHARGING_STATION_ID, ComponentStatus.AVAILABLE, null, Collections.<String, String>emptyMap(), NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithAttributesNull() {
        new ChargingStationStatusNotificationReceivedEvent(CHARGING_STATION_ID, ComponentStatus.AVAILABLE, new Date(), null, NULL_USER_IDENTITY_CONTEXT);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingEventWithIdentityContextNull() {
        new ChargingStationStatusNotificationReceivedEvent(CHARGING_STATION_ID, ComponentStatus.AVAILABLE, new Date(), Collections.<String, String>emptyMap(), null);
    }

    @Test
    public void testImmutableDate() {
        Date now = new Date();
        ChargingStationStatusNotificationReceivedEvent event = new ChargingStationStatusNotificationReceivedEvent(CHARGING_STATION_ID, ComponentStatus.AVAILABLE, now, BOOT_NOTIFICATION_ATTRIBUTES, NULL_USER_IDENTITY_CONTEXT);
        event.getTimestamp().setTime(TWO_MINUTES_AGO.getTime());
        assertEquals(now, event.getTimestamp());
    }

    @Test
    public void equalsAndHashCodeShouldBeImplementedAccordingToTheContract() {
        EqualsVerifier.forClass(ChargingStationStatusNotificationReceivedEvent.class).usingGetClass().verify();
    }
}
