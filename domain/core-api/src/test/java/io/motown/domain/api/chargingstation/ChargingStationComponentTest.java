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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ChargingStationComponentTest {

    private ChargingStationComponent chargingStationComponent;

    private String value;

    public ChargingStationComponentTest(ChargingStationComponent chargingStationComponent, String value) {
        this.chargingStationComponent = chargingStationComponent;
        this.value = value;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {ChargingStationComponent.CONNECTOR, "Connector"},
                {ChargingStationComponent.EVSE, "EVSE"}
        });
    }

    @Test
    public void toStringShouldReturnTheGivenStringValue() {
        assertTrue(chargingStationComponent.toString().equals(value));
    }
}
