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

/**
 * {@code DataTransferRequestResult} contains the result of a datatransfer request.
 */
public final class DataTransferRequestResult {

    private final RequestResult requestResult;

    private final String data;

    public DataTransferRequestResult(RequestResult requestResult, String data) {
        this.requestResult = requestResult;
        this.data = data;
    }

    public RequestResult getRequestResult() {
        return requestResult;
    }

    public String getData() {
        return data;
    }

    public boolean isSuccessfull() {
        return RequestResult.SUCCESS.equals(requestResult);
    }
}
