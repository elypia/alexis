package com.elypia.alexis.handlers.modules;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.commandler.Database;
import com.elypia.alexis.entities.MessageChannelData;
import com.elypia.alexis.utils.BotUtils;
import com.elypia.commandler.annotations.*;
import com.elypia.commandler.events.MessageEvent;
import com.elypia.commandler.modules.CommandHandler;
import com.elypia.elypiai.cleverbot.Cleverbot;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.mongodb.morphia.*;
import org.mongodb.morphia.query.*;

@Database
@Module(name = "CleverBot", aliases = {"cleverbot", "cb"}, description = "Come talk to cleverbot.")
public class CleverbotHandler extends CommandHandler {

    private Cleverbot cleverbot;

    public CleverbotHandler(String apiKey) {
        cleverbot = new Cleverbot(apiKey);
    }

    @Command(name = "Talk to Cleverbot", aliases = {"say", "ask"}, help = "Say something to Cleverbot.")
    @Param(name = "body", help = "What you want to say.")
    public void say(MessageEvent event, String body) {
        MessageChannel channel = event.getMessageEvent().getChannel();
        MessageChannelData data = MessageChannelData.query(channel.getIdLong());
        String cs = data.getCleverState();

        cleverbot.say(body, cs, response -> {
            event.reply(response.getOutput());
            setChannelCleverState(data, response.getCS());
        }, failure -> BotUtils.sendHttpError(event, failure));
    }

    @Command(name = "Channel Chat History", aliases = {"history", "his"}, help = "Track previous conversation in this channel.")
    public String getHistory(MessageEvent event) {
        MessageChannel channel = event.getMessageEvent().getChannel();
        MessageChannelData data = MessageChannelData.query(channel.getIdLong());
        String cs = data.getCleverState();
        String history = cleverbot.getHistory(cs);

        if (history == null)
            return "Maybe try talk first and I can grab your history later?";
        else
            return String.format("```\n%s\n```", history);
    }

    private void setChannelCleverState(MessageChannelData data, String cs) {
        Datastore store = Alexis.getChatbot().getDatastore();

        UpdateOptions options = new UpdateOptions();
        options.upsert(true);

        Query<MessageChannelData> query = store.createQuery(MessageChannelData.class);
        query = query.filter("channel_id", data.getChannelId());

        UpdateOperations<MessageChannelData> update = store.createUpdateOperations(MessageChannelData.class);
        update.set("clever_state", cs);

        store.update(query, update, options);
    }
}