package com.elypia.alexis.discord.handlers;

import com.elypia.alexis.discord.annotations.Database;
import com.elypia.alexis.discord.events.impl.GenericEvent;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * There are events that non-command messages have to
 * go through and may consist of misc tasks such as
 * nudity detection, use of banned words, granting XP
 * and so on. Actual commands will come here also, before
 * we bother to processing the actual intent.
 */

public class GlobalMessageHandler {

    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> guilds;
    private MongoCollection<Document> users;
    private MongoCollection<Document> members;

    public GlobalMessageHandler(MongoClient client) {
        database = client.getDatabase("alexis");

        guilds = database.getCollection("guilds");
        users = database.getCollection("users");
        members = database.getCollection("members");
    }

    @Database
    public void handleXp(GenericEvent event) {
        String content = event.getMessage().getContentRaw();
        int messageXp = content.split("\\s+").length;

        event.getUserData().gainXp(event);
    }
}
