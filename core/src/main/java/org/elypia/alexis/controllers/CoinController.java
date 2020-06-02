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
import org.elypia.commandler.annotation.Default;
import org.elypia.commandler.annotation.command.StandardCommand;
import org.elypia.commandler.annotation.stereotypes.CommandController;
import org.elypia.commandler.api.Controller;
import org.slf4j.*;

import javax.inject.Inject;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Elypia CIC
 */
@CommandController
@StandardCommand
public class CoinController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(CoinController.class);

    private final AlexisMessages messages;

    @Inject
    public CoinController(AlexisMessages messages) {
        this.messages = messages;
    }

    @Default
    @StandardCommand
    public String flipCoin() {
        boolean isHeads = ThreadLocalRandom.current().nextBoolean();
        return (isHeads) ? messages.coinFlipHeads() : messages.coinClipTails();
    }
}