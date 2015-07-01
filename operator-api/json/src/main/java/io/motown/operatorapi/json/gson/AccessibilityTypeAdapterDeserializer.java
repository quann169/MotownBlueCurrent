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

import io.motown.domain.utils.gson.JsonDeserializationContext;
import io.motown.domain.utils.gson.JsonElement;
import io.motown.domain.utils.gson.JsonParseException;
import io.motown.domain.api.chargingstation.Accessibility;
import io.motown.utils.rest.json.gson.TypeAdapterDeserializer;

import java.lang.reflect.Type;

public class AccessibilityTypeAdapterDeserializer implements TypeAdapterDeserializer<Accessibility> {

    @Override
    public Class<?> getAdaptedType() {
        return Accessibility.class;
    }

    @Override
    public Accessibility deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        if (!json.isJsonPrimitive()) {
            throw new JsonParseException("Accessibility must be a JSON string");
        }
        return Accessibility.fromValue(json.getAsString());
    }
}
