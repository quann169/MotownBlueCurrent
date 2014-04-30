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
package io.motown.operatorapi.json.restapi;

import io.motown.operatorapi.json.queries.OperatorApiService;
import io.motown.operatorapi.json.restapi.util.OperatorApiResponseBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/transactions")
@Produces(ApiVersion.V1_JSON)
public final class TransactionResource {

    private static final String OFFSET_PARAMETER = "offset";

    private static final String LIMIT_PARAMETER = "limit";

    private OperatorApiService service;

    @GET
    public Response getTransactions(@Context HttpServletRequest request, @QueryParam(OFFSET_PARAMETER) @DefaultValue("0") int offset, @QueryParam(LIMIT_PARAMETER) @DefaultValue("10") int limit) {
        return Response.ok(OperatorApiResponseBuilder.buildResponse(request, offset, limit, service.getTotalNumberOfTransactions(), service.findAllTransactions(offset, limit))).build();
    }

    public void setService(OperatorApiService service) {
        this.service = service;
    }
}
