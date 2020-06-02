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

package org.elypia.alexis.discord.controllers;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.Event;
import org.elypia.comcord.constraints.*;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.annotation.command.StandardCommand;
import org.elypia.commandler.annotation.stereotypes.CommandController;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;

import javax.inject.Inject;

/**
 * @author seth@elypia.org (Seth Falco)
 */
@CommandController
@StandardCommand
public class LevelController implements Controller {

    @Inject
    public LevelController() {

    }

    @StandardCommand
    public String message(@Elevated @Channels(ChannelType.TEXT) ActionEvent<Event, Message> event, @Param String message) {
//        Guild guild = EventUtils.getGuild(event.getSource());
//        long id = guild.getIdLong();
//
//        try (Session session = dbService.open()) {
//            GuildData data = session.get(GuildData.class, id);
//
//            if (data == null)
//                data = new GuildData(id);
//
//
//            MessageSettings settings = data.getSettings().getLevelSettings().getNotifySettings();
//            settings.setEnabled(true);
//            settings.setChannel(event.asMessageRecieved().getChannel().getIdLong());
//            settings.setMessage(message);
//
//            data.commit();
//        }
//
//
//        return scripts.get(event.getSource(), "level.message.response");
        return null;
    }

    @StandardCommand
    public String test(@Elevated @Channels(ChannelType.TEXT) ActionEvent<Event, Message> event) {
//        Guild guild = EventUtils.getGuild(event.getSource());
//        long id = guild.getIdLong();
//
//        try (Session session = dbService.open()) {
//            GuildData data = session.get(GuildData.class, id);
//
//            if (data == null)
//                return "You haven't configured any settings yet, maybe check them out first?";
//
//            MessageSettings settings = data.getSettings().getLevelSettings().getNotifySettings();
//            String message = settings.getMessage();
//
//            if (message == null || message.isEmpty())
//                return scripts.get(source, "level.no_message");
//
//            return scripts.get(source, message);
//        }
        return null;
    }
}