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
package io.motown.operatorapi.json.gson;

import com.google.gson.JsonObject;
import io.motown.domain.api.chargingstation.OpeningTime;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class OpeningTimeTypeAdapterTest {

    @Test
    public void testOpeningTimeTypeAdapter() {
        OpeningTimeTypeAdapter adapter = new OpeningTimeTypeAdapter();

        assertEquals(adapter.getAdaptedType(), OpeningTime.class);

        JsonObject openingTimeJson = new JsonObject();
        openingTimeJson.addProperty("day", 1);
        openingTimeJson.addProperty("timeStart", "12:00");
        openingTimeJson.addProperty("timeStop", "15:00");

        OpeningTime openingTime = adapter.deserialize(openingTimeJson, OpeningTime.class, null);

        assertEquals(openingTimeJson.get("day").getAsInt(), openingTime.getDay().value());
        assertEquals(openingTimeJson.get("timeStart").getAsString(), String.format("%02d:%02d", openingTime.getTimeStart().getHourOfDay(), openingTime.getTimeStart().getMinutesInHour()));
        assertEquals(openingTimeJson.get("timeStop").getAsString(), String.format("%02d:%02d", openingTime.getTimeStop().getHourOfDay(), openingTime.getTimeStop().getMinutesInHour()));
    }
}
