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
package io.motown.operatorapi.json.commands;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class JsonCommandService {

    private Gson gson;

    private List<JsonCommandHandler> jsonCommandHandlers;

    public void handleCommand(String chargingStationId, String jsonCommand) {
        String commandName = parseCommandName(jsonCommand);
        JsonCommandHandler commandHandler = getCommandHandler(commandName);

        commandHandler.handle(chargingStationId, jsonCommand);
    }

    private String parseCommandName(String jsonCommand) {
        JsonArray command = gson.fromJson(jsonCommand, JsonArray.class);
        return command.get(0).getAsString();
    }

    private JsonCommandHandler getCommandHandler(String commandName) {
        for (JsonCommandHandler commandHandler : this.jsonCommandHandlers) {
            if (commandName.equals(commandHandler.getCommandName())) {
                return commandHandler;
            }
        }

        throw new IllegalArgumentException("No command handler is configured for handling [" + commandName + "].");
    }

    @Autowired
    public void setGson(Gson gson) {
        this.gson = gson;
    }

    @Resource(name = "jsonCommandHandlers")
    public void setJsonCommandHandlers(List<JsonCommandHandler> jsonCommandHandlers) {
        this.jsonCommandHandlers = jsonCommandHandlers;
    }
}