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
import io.motown.domain.utils.gson.JsonObject;
import io.motown.domain.utils.gson.JsonParseException;
import io.motown.domain.api.chargingstation.Coordinates;
import io.motown.utils.rest.json.gson.TypeAdapterDeserializer;

import java.lang.reflect.Type;

/**
 * Type adapter for the {@code Coordinates} JSON object.
 * <pre>
 * {@code
 * {
 *  latitude:   "double",
 *  longitude:  "double"
 * }
 * }
 * </pre>
 */
public class CoordinatesTypeAdapterDeserializer implements TypeAdapterDeserializer<Coordinates> {
    @Override
    public Class<?> getAdaptedType() {
        return Coordinates.class;
    }

    @Override
    public Coordinates deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        JsonObject obj;

        try {
            obj = jsonElement.getAsJsonObject();
            if (obj == null) {
                return null;
            }
        } catch (ClassCastException | IllegalStateException e) {
            throw new JsonParseException("Coordinates must be a JSON object", e);
        }

        double latitude = obj.getAsJsonPrimitive("latitude").getAsDouble();
        double longitude = obj.getAsJsonPrimitive("longitude").getAsDouble();

        return new Coordinates(latitude, longitude);
    }
}
