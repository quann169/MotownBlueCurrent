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
package io.motown.ocpp.websocketjson;

import static io.motown.domain.api.chargingstation.test.ChargingStationTestUtils.CHARGING_STATION_ID;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import io.motown.domain.api.chargingstation.NumberedReservationId;
import io.motown.domain.utils.gson.Gson;
import io.motown.domain.utils.gson.JsonElement;
import io.motown.domain.utils.gson.JsonPrimitive;
import io.motown.domain.utils.gson.JsonSerializationContext;
import io.motown.ocpp.websocketjson.gson.GsonFactoryBean;
import io.motown.ocpp.websocketjson.gson.deserializer.AuthorizationIdTagStatusAdapterDeserializer;
import io.motown.ocpp.websocketjson.gson.deserializer.CancelReservationResponseStatusTypeAdapterDeserializer;
import io.motown.ocpp.websocketjson.gson.deserializer.ChangeAvailabilityTypeAdapterDeserializer;
import io.motown.ocpp.websocketjson.gson.deserializer.ChangeConfigurationResponseStatusTypeAdapterDeserializer;
import io.motown.ocpp.websocketjson.gson.deserializer.ChargePointErrorCodeTypeAdapterDeserializer;
import io.motown.ocpp.websocketjson.gson.deserializer.ChargePointStatusTypeAdapterDeserializer;
import io.motown.ocpp.websocketjson.gson.deserializer.ClearCacheResponseStatusTypeAdapterDeserializer;
import io.motown.ocpp.websocketjson.gson.deserializer.DataTransferResponseStatusTypeAdapterDeserializer;
import io.motown.ocpp.websocketjson.gson.deserializer.DateTypeAdapterDeserializer;
import io.motown.ocpp.websocketjson.gson.deserializer.DiagnosticsStatusTypeAdapterDeserializer;
import io.motown.ocpp.websocketjson.gson.deserializer.FirmwareStatusTypeAdapterDeserializer;
import io.motown.ocpp.websocketjson.gson.deserializer.MessageProcUriAdapterDeserializer;
import io.motown.ocpp.websocketjson.gson.deserializer.RemoteStartTransactionResponseTypeAdapterDeserializer;
import io.motown.ocpp.websocketjson.gson.deserializer.RemoteStopTransactionResponseTypeAdapterDeserializer;
import io.motown.ocpp.websocketjson.gson.deserializer.ReserveNowResponseStatusTypeAdapterDeserializer;
import io.motown.ocpp.websocketjson.gson.deserializer.ResetResponseStatusAdapterDeserializer;
import io.motown.ocpp.websocketjson.gson.deserializer.SendLocalListResponseStatusTypeAdapterDeserializer;
import io.motown.ocpp.websocketjson.gson.deserializer.StopTransactionIdTagStatusAdapterDeserializer;
import io.motown.ocpp.websocketjson.gson.deserializer.TypeAdapterDeserializer;
import io.motown.ocpp.websocketjson.gson.deserializer.UnlockConnectorResponseStatusTypeAdapterDeserializer;
import io.motown.ocpp.websocketjson.gson.serializer.AuthorizationListIdTagStatusTypeAdapterSerializer;
import io.motown.ocpp.websocketjson.gson.serializer.BootnotificationResponseStatusTypeAdapterSerializer;
import io.motown.ocpp.websocketjson.gson.serializer.ChangeAvailabilityTypeAdapterSerializer;
import io.motown.ocpp.websocketjson.gson.serializer.DataTransferResponseStatusTypeAdapterSerializer;
import io.motown.ocpp.websocketjson.gson.serializer.DateTypeAdapterSerializer;
import io.motown.ocpp.websocketjson.gson.serializer.MessageProcUriAdapterSerializer;
import io.motown.ocpp.websocketjson.gson.serializer.ResetTypeAdapterSerializer;
import io.motown.ocpp.websocketjson.gson.serializer.SendLocalListRequestUpdateTypeAdapterSerializer;
import io.motown.ocpp.websocketjson.gson.serializer.StartTransactionIdTagStatusTypeAdapterSerializer;
import io.motown.ocpp.websocketjson.gson.serializer.TypeAdapterSerializer;
import io.motown.ocpp.websocketjson.schema.generated.v15.CancelreservationResponse;
import io.motown.ocpp.websocketjson.schema.generated.v15.ChangeavailabilityResponse;
import io.motown.ocpp.websocketjson.schema.generated.v15.ChangeconfigurationResponse;
import io.motown.ocpp.websocketjson.schema.generated.v15.ClearcacheResponse;
import io.motown.ocpp.websocketjson.schema.generated.v15.Diagnosticsstatusnotification;
import io.motown.ocpp.websocketjson.schema.generated.v15.Firmwarestatusnotification;
import io.motown.ocpp.websocketjson.schema.generated.v15.RemotestarttransactionResponse;
import io.motown.ocpp.websocketjson.schema.generated.v15.RemotestoptransactionResponse;
import io.motown.ocpp.websocketjson.schema.generated.v15.ReservenowResponse;
import io.motown.ocpp.websocketjson.schema.generated.v15.ResetResponse;
import io.motown.ocpp.websocketjson.schema.generated.v15.SendlocallistResponse;
import io.motown.ocpp.websocketjson.schema.generated.v15.Statusnotification;
import io.motown.ocpp.websocketjson.schema.generated.v15.UnlockconnectorResponse;
import io.motown.ocpp.websocketjson.wamp.WampMessage;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.atmosphere.cpr.AtmosphereRequest;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.websocket.WebSocket;

import com.google.common.collect.ImmutableSet;

public class OcppWebSocketJsonTestUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static final String OCPPJ_PROTOCOL = "OCPPJ15";

    public static final NumberedReservationId OCPPJ_RESERVATION_ID = new NumberedReservationId(CHARGING_STATION_ID, OCPPJ_PROTOCOL, 1);

    public static String formatDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        return df.format(date);
    }

    public static Gson getGson() {
        GsonFactoryBean gsonFactoryBean = new GsonFactoryBean();
        gsonFactoryBean.setDateFormat(DATE_FORMAT);
        Set<TypeAdapterSerializer<?>> typeAdapterSerializers = ImmutableSet.<TypeAdapterSerializer<?>>builder()
                .add(new AuthorizationListIdTagStatusTypeAdapterSerializer())
                .add(new BootnotificationResponseStatusTypeAdapterSerializer())
                .add(new ChangeAvailabilityTypeAdapterSerializer())
                .add(new DataTransferResponseStatusTypeAdapterSerializer())
                .add(new ResetTypeAdapterSerializer())
                .add(new SendLocalListRequestUpdateTypeAdapterSerializer())
                .add(new StartTransactionIdTagStatusTypeAdapterSerializer())
                .add(new MessageProcUriAdapterSerializer())
                //The serializers below are only needed during testing
                .add(new ChangeAvailabilityStatusTypeAdapterSerializer())
                .add(new CancelReservationStatusTypeAdapterSerializer())
                .add(new ChangeConfigurationStatusTypeAdapterSerializer())
                .add(new ClearCacheStatusTypeAdapterSerializer())
                .add(new DataTransferResponseStatusTypeAdapterSerializer())
                .add(new RemoteStartTransactionStatusTypeAdapterSerializer())
                .add(new RemoteStopTransactionStatusTypeAdapterSerializer())
                .add(new ReserveNowStatusTypeAdapterSerializer())
                .add(new ResetStatusTypeAdapterSerializer())
                .add(new SendLocalListStatusTypeAdapterSerializer())
                .add(new UnlockConnectorResponseStatusTypeAdapterSerializer())
                .add(new DiagnosticsStatusNotificationStatusTypeAdapterSerializer())
                .add(new FirmwareStatusNotificationStatusTypeAdapterSerializer())
                .add(new StatusnotificationStatusTypeAdapterSerializer())
                .add(new StatusnotificationErrorCodeTypeAdapterSerializer())
                .add(new DateTypeAdapterSerializer())
                .build();
        gsonFactoryBean.setTypeAdapterSerializers(typeAdapterSerializers);

        Set<TypeAdapterDeserializer<?>> typeAdapterDeserializers = ImmutableSet.<TypeAdapterDeserializer<?>>builder()
                .add(new AuthorizationIdTagStatusAdapterDeserializer())
                .add(new CancelReservationResponseStatusTypeAdapterDeserializer())
                .add(new ChangeAvailabilityTypeAdapterDeserializer())
                .add(new ChangeConfigurationResponseStatusTypeAdapterDeserializer())
                .add(new ChargePointErrorCodeTypeAdapterDeserializer())
                .add(new ChargePointStatusTypeAdapterDeserializer())
                .add(new ClearCacheResponseStatusTypeAdapterDeserializer())
                .add(new DataTransferResponseStatusTypeAdapterDeserializer())
                .add(new DiagnosticsStatusTypeAdapterDeserializer())
                .add(new FirmwareStatusTypeAdapterDeserializer())
                .add(new MessageProcUriAdapterDeserializer())
                .add(new RemoteStartTransactionResponseTypeAdapterDeserializer())
                .add(new RemoteStopTransactionResponseTypeAdapterDeserializer())
                .add(new ReserveNowResponseStatusTypeAdapterDeserializer())
                .add(new ResetResponseStatusAdapterDeserializer())
                .add(new SendLocalListResponseStatusTypeAdapterDeserializer())
                .add(new StopTransactionIdTagStatusAdapterDeserializer())
                .add(new UnlockConnectorResponseStatusTypeAdapterDeserializer())
                .add(new DateTypeAdapterDeserializer())
                .build();
        gsonFactoryBean.setTypeAdapterDeserializers(typeAdapterDeserializers);

        return gsonFactoryBean.createGson();
    }

    public static WebSocket getMockWebSocket() {
        WebSocket webSocket = mock(WebSocket.class);

        AtmosphereRequest request = mock(AtmosphereRequest.class);
        when(request.getContextPath()).thenReturn("/motown");
        when(request.getServletPath()).thenReturn("/websockets");
        when(request.getRequestURI()).thenReturn("/motown/websockets/" + CHARGING_STATION_ID.getId());

        AtmosphereResource resource = mock(AtmosphereResource.class);
        when(resource.getRequest()).thenReturn(request);

        when(webSocket.resource()).thenReturn(resource);
        return webSocket;
    }

    public static String createAcceptedCallResult(String callId) {
        return String.format("[%d,\"%s\",{\"status\":\"%s\"}]", WampMessage.CALL_RESULT, callId, "Accepted");
    }

    /**
     * Only needed during tests to translate object to json
     */
    private static class ChangeAvailabilityStatusTypeAdapterSerializer implements TypeAdapterSerializer<ChangeavailabilityResponse.Status> {
        @Override
        public JsonElement serialize(ChangeavailabilityResponse.Status status, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(status.toString());
        }
        @Override
        public Class<?> getAdaptedType() {
            return ChangeavailabilityResponse.Status.class;
        }
    }

    /**
     * Only needed during tests to translate object to json
     */
    private static class CancelReservationStatusTypeAdapterSerializer implements TypeAdapterSerializer<CancelreservationResponse.Status> {
        @Override
        public JsonElement serialize(CancelreservationResponse.Status status, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(status.toString());
        }
        @Override
        public Class<?> getAdaptedType() {
            return CancelreservationResponse.Status.class;
        }
    }

    /**
     * Only needed during tests to translate object to json
     */
    private static class ChangeConfigurationStatusTypeAdapterSerializer implements TypeAdapterSerializer<ChangeconfigurationResponse.Status> {
        @Override
        public JsonElement serialize(ChangeconfigurationResponse.Status status, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(status.toString());
        }
        @Override
        public Class<?> getAdaptedType() {
            return ChangeconfigurationResponse.Status.class;
        }
    }

    /**
     * Only needed during tests to translate object to json
     */
    private static class ClearCacheStatusTypeAdapterSerializer implements TypeAdapterSerializer<ClearcacheResponse.Status> {
        @Override
        public JsonElement serialize(ClearcacheResponse.Status status, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(status.toString());
        }
        @Override
        public Class<?> getAdaptedType() {
            return ClearcacheResponse.Status.class;
        }
    }

    /**
     * Only needed during tests to translate object to json
     */
    private static class RemoteStartTransactionStatusTypeAdapterSerializer implements TypeAdapterSerializer<RemotestarttransactionResponse.Status> {
        @Override
        public JsonElement serialize(RemotestarttransactionResponse.Status status, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(status.toString());
        }
        @Override
        public Class<?> getAdaptedType() {
            return RemotestarttransactionResponse.Status.class;
        }
    }

    /**
     * Only needed during tests to translate object to json
     */
    private static class RemoteStopTransactionStatusTypeAdapterSerializer implements TypeAdapterSerializer<RemotestoptransactionResponse.Status> {
        @Override
        public JsonElement serialize(RemotestoptransactionResponse.Status status, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(status.toString());
        }
        @Override
        public Class<?> getAdaptedType() {
            return RemotestoptransactionResponse.Status.class;
        }
    }

    /**
     * Only needed during tests to translate object to json
     */
    private static class ReserveNowStatusTypeAdapterSerializer implements TypeAdapterSerializer<ReservenowResponse.Status> {
        @Override
        public JsonElement serialize(ReservenowResponse.Status status, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(status.toString());
        }
        @Override
        public Class<?> getAdaptedType() {
            return ReservenowResponse.Status.class;
        }
    }

    /**
     * Only needed during tests to translate object to json
     */
    private static class ResetStatusTypeAdapterSerializer implements TypeAdapterSerializer<ResetResponse.Status> {
        @Override
        public JsonElement serialize(ResetResponse.Status status, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(status.toString());
        }
        @Override
        public Class<?> getAdaptedType() {
            return ResetResponse.Status.class;
        }
    }

    /**
     * Only needed during tests to translate object to json
     */
    private static class SendLocalListStatusTypeAdapterSerializer implements TypeAdapterSerializer<SendlocallistResponse.Status> {
        @Override
        public JsonElement serialize(SendlocallistResponse.Status status, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(status.toString());
        }
        @Override
        public Class<?> getAdaptedType() {
            return SendlocallistResponse.Status.class;
        }
    }

    /**
     * Only needed during tests to translate object to json
     */
    private static class UnlockConnectorResponseStatusTypeAdapterSerializer implements TypeAdapterSerializer<UnlockconnectorResponse.Status> {
        @Override
        public JsonElement serialize(UnlockconnectorResponse.Status status, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(status.toString());
        }
        @Override
        public Class<?> getAdaptedType() {
            return UnlockconnectorResponse.Status.class;
        }
    }

    /**
     * Only needed during tests to translate object to json
     */
    private static class DiagnosticsStatusNotificationStatusTypeAdapterSerializer implements TypeAdapterSerializer<Diagnosticsstatusnotification.Status> {
        @Override
        public JsonElement serialize(Diagnosticsstatusnotification.Status status, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(status.toString());
        }
        @Override
        public Class<?> getAdaptedType() {
            return Diagnosticsstatusnotification.Status.class;
        }
    }

    /**
     * Only needed during tests to translate object to json
     */
    private static class FirmwareStatusNotificationStatusTypeAdapterSerializer implements TypeAdapterSerializer<Firmwarestatusnotification.Status> {
        @Override
        public JsonElement serialize(Firmwarestatusnotification.Status status, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(status.toString());
        }
        @Override
        public Class<?> getAdaptedType() {
            return Firmwarestatusnotification.Status.class;
        }
    }

    /**
     * Only needed during tests to translate object to json
     */
    private static class StatusnotificationStatusTypeAdapterSerializer implements TypeAdapterSerializer<Statusnotification.Status> {
        @Override
        public JsonElement serialize(Statusnotification.Status status, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(status.toString());
        }
        @Override
        public Class<?> getAdaptedType() {
            return Statusnotification.Status.class;
        }
    }

    /**
     * Only needed during tests to translate object to json
     */
    private static class StatusnotificationErrorCodeTypeAdapterSerializer implements TypeAdapterSerializer<Statusnotification.ErrorCode> {
        @Override
        public JsonElement serialize(Statusnotification.ErrorCode code, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(code.toString());
        }
        @Override
        public Class<?> getAdaptedType() {
            return Statusnotification.ErrorCode.class;
        }
    }
}
