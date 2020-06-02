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
import org.elypia.alexis.persistence.entities.*;
import org.elypia.alexis.persistence.enums.*;
import org.elypia.alexis.persistence.repositories.GuildRepository;
import org.elypia.alexis.discord.enums.*;
import org.elypia.alexis.i18n.AlexisMessages;
import org.elypia.comcord.constraints.*;
import org.elypia.commandler.annotation.Param;
import org.elypia.commandler.annotation.command.StandardCommand;
import org.elypia.commandler.annotation.stereotypes.CommandController;
import org.elypia.commandler.api.Controller;
import org.elypia.commandler.event.ActionEvent;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import java.util.*;

// TODO: Make waiting dispatcher, and review JDA Utils
@CommandController
@StandardCommand
public class GreetingController implements Controller {

    private GuildRepository guildRepo;
    private AlexisMessages messages;

    @Inject
    public GreetingController(GuildRepository guildRepo, AlexisMessages messages) {
        this.guildRepo = guildRepo;
        this.messages = messages;
    }

    // TODO: Instead of write same text twice, if it affects both and they have the same data, just send "BOTH"
    // TODO: Only update if it's actually changed
    @StandardCommand
    public String setEnabled(@Elevated Message message, @Param Greeting greeting, @Param("true") boolean enabled, @Param("both") Recipient recipient) {
        Guild guild = message.getGuild();
        long guildId = guild.getIdLong();
        long userId = message.getAuthor().getIdLong();

        List<Feature> features = getFeatures(greeting, recipient);
        GuildData data = guildRepo.findBy(guildId);
        Map<Feature, GuildFeature> guildFeatures = data.getFeatures();
        StringJoiner joiner = new StringJoiner("\n");

        for (Feature feature : features) {
            GuildFeature guildFeature = guildFeatures.get(feature);

            if (guildFeature == null) {
                joiner.add(messages.greetingToggledFeature(feature.getFriendlyName()));
                guildFeatures.put(feature, new GuildFeature(data, feature, enabled, userId));
                continue;
            }

            if (guildFeature.isEnabled() == enabled) {
                Member lastModifiedBy = guild.getMemberById(guildFeature.getModifiedBy());

                if (lastModifiedBy == null)
                    joiner.add(messages.greetingModifedAlreadyByAnonUser(feature.getFriendlyName()));
                else
                    joiner.add(messages.greetingModifiedAlready(feature.getFriendlyName(), guildFeature.getModifiedAt(), lastModifiedBy.getEffectiveName()));

                continue;
            }

            guildFeature.setEnabled(enabled);
            guildFeature.setModifiedBy(userId);
            guildFeature.setModifiedAt(new Date());
            joiner.add(messages.greetingUpdatedFeature(feature.getFriendlyName()));
        }

        guildRepo.save(data);
        return joiner.toString();
    }

    @StandardCommand
    public String setGreeting(@Elevated Message eventMessage, @Param @NotBlank String body, @Param Greeting greeting, @Param Recipient recipient) {
        Guild guild = eventMessage.getGuild();

        long guildId = guild.getIdLong();
        TextChannel channel = eventMessage.getTextChannel();
        long channelId = channel.getIdLong();

        StringJoiner joiner = new StringJoiner("\n");

        List<GuildMessageType> messageTypes = getMessageTypes(greeting, recipient);
        GuildData data = guildRepo.findBy(guild.getIdLong());
        Map<GuildMessageType, GuildMessage> guildMessages = data.getMessages();

        for (GuildMessageType messageType : messageTypes) {
            GuildMessage oldMessage = guildMessages.get(messageType);

            if (oldMessage == null) {
                joiner.add(messages.greetingSendMessagesTo(messageType.getFriendlyName(), channel.getAsMention()));
                guildMessages.put(messageType, new GuildMessage(data, messageType, channelId, body));
                continue;
            }

            if (!body.equals(oldMessage.getMessage())) {
                String oldBody = oldMessage.getMessage();

                if (oldBody == null)
                    joiner.add(messages.greetingSetNewMessage(messageType.getFriendlyName(), body));
                else
                    joiner.add(messages.greetingUpdateExistingMessage(messageType.getFriendlyName(), body));

                oldMessage.setMessage(body);
            } else {
                joiner.add(messages.greetingMessageNotChanged(messageType.getFriendlyName()));
            }

            if (oldMessage.getChannelId() == null) {
                joiner.add(messages.greetingChannelNotSetSoDefaulting(messageType.getFriendlyName(), channel.getAsMention()));
                oldMessage.setChannelId(channelId);
            } else {
                long oldChannelId = oldMessage.getChannelId();
                TextChannel oldChannel = guild.getTextChannelById(oldChannelId);

                if (oldChannel == null) {
                    joiner.add(messages.greetingChannelSetButDeleted(messageType.getFriendlyName(), channel.getAsMention()));
                    oldMessage.setChannelId(channelId);
                }
            }
        }

        guildRepo.save(data);
        return joiner.toString();
    }

    @StandardCommand
    public String setChannel(@Elevated ActionEvent<Event, Message> event, @Param (value = "${source.channel}", displayAs = "current") @Talkable TextChannel channel) {
//        MessageReceivedEvent source = (MessageReceivedEvent) event.getSource();
//        GuildData data = GuildData.query(source.getMessage().getGuild().getIdLong());
//        GreetingSettings greetingSettings = data.getSettings().getGreetingSettings();
//        long channelId = channel.getIdLong();
//
//        GreetingSetting welcome = greetingSettings.getJoin();
//        welcome.getUser().setChannel(channelId);
//        welcome.getBot().setChannel(channelId);
//
//        GreetingSetting farewell = greetingSettings.getLeave();
//        farewell.getUser().setChannel(channelId);
//        farewell.getBot().setChannel(channelId);
//
//        data.commit();
//
//        var params = Map.of("mention", channel.getAsMention());
//
        return "no u";
    }

    /**
     * @param greeting The type of greeting we're referring to.
     * @param recipient The type of recipients we're referring to.
     * @return Any {@link GuildMessageType} that is applicable to the specified values.
     */
    private List<GuildMessageType> getMessageTypes(Greeting greeting, Recipient recipient) {
        List<GuildMessageType> messageTypes = new ArrayList<>();

        switch (greeting) {
            case WELCOME:
                if (recipient == Recipient.USER || recipient == Recipient.BOTH)
                    messageTypes.add(GuildMessageType.USER_WELCOME);

                if (recipient == Recipient.BOT || recipient == Recipient.BOTH)
                    messageTypes.add(GuildMessageType.BOT_WELCOME);

                break;
            case FAREWELL:
                if (recipient == Recipient.USER || recipient == Recipient.BOTH)
                    messageTypes.add(GuildMessageType.USER_LEAVE);

                if (recipient == Recipient.BOT || recipient == Recipient.BOTH)
                    messageTypes.add(GuildMessageType.BOT_LEAVE);

                break;
            default:
                throw new IllegalStateException("Unknown greeting type given to GreetingController.");
        }

        return messageTypes;
    }

    /**
     * @param greeting The type of greeting we're referring to.
     * @param recipient The type of recipients we're referring to.
     * @return Any {@link GuildMessageType} that is applicable to the specified values.
     */
    private List<Feature> getFeatures(Greeting greeting, Recipient recipient) {
        List<Feature> messageTypes = new ArrayList<>();

        switch (greeting) {
            case WELCOME:
                if (recipient == Recipient.USER || recipient == Recipient.BOTH)
                    messageTypes.add(Feature.USER_JOIN_MESSAGE);

                if (recipient == Recipient.BOT || recipient == Recipient.BOTH)
                    messageTypes.add(Feature.BOT_JOIN_MESSAGE);

                break;
            case FAREWELL:
                if (recipient == Recipient.USER || recipient == Recipient.BOTH)
                    messageTypes.add(Feature.USER_LEAVE_MESSAGE);

                if (recipient == Recipient.BOT || recipient == Recipient.BOTH)
                    messageTypes.add(Feature.BOT_LEAVE_MESSAGE);

                break;
            default:
                throw new IllegalStateException("Unknown feature type given to GreetingController.");
        }

        return messageTypes;
    }
}
