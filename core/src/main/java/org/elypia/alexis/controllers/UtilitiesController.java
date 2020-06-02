/*
 * Copyright 2019-2020 Elypia CIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elypia.alexis.controllers;

import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.commandler.annotation.*;
import org.elypia.commandler.annotation.command.StandardCommand;
import org.elypia.commandler.annotation.stereotypes.CommandController;
import org.elypia.commandler.api.Controller;

import javax.inject.Inject;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@CommandController
@StandardCommand
public class UtilitiesController implements Controller {

    private final AlexisMessages messages;

    @Inject
    public UtilitiesController(final AlexisMessages messages) {
        this.messages = messages;
    }

    @Static
    @StandardCommand
    public String count(@Param String input) {
        return messages.utilitiesTotalCharacters(input.length());
    }
}