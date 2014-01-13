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

import static io.motown.domain.api.chargingstation.CoreApiTestUtils.getChargingStationId;
import static io.motown.domain.api.chargingstation.CoreApiTestUtils.getVendorId;

public class DataTransferCommandTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithChargingStationIdNull() {
        new DataTransferCommand(null, getVendorId(), "", "");
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithVendorIdNull() {
        new DataTransferCommand(getChargingStationId(), null, "", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionThrownWhenCreatingCommandWithVendorIdEmpty() {
        new DataTransferCommand(getChargingStationId(), "", "", "");
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithMessageIdNull() {
        new DataTransferCommand(getChargingStationId(), getVendorId(), null, "");
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionThrownWhenCreatingCommandWithDataNull() {
        new DataTransferCommand(getChargingStationId(), getVendorId(), "", null);
    }

}
