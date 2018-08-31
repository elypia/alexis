package com.elypia.alexis.utils;

import com.elypia.alexis.Alexis;
import com.elypia.alexis.config.BotConfiguration;
import com.elypia.alexis.entities.*;
import com.elypia.alexis.entities.embedded.NanowrimoLink;
import com.elypia.commandler.jda.JDACommand;
import com.elypia.elypiai.runescape.RuneScape;
import com.elypia.elypiai.utils.Language;
import com.elypia.elyscript.ElyScript;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.channel.text.GenericTextChannelEvent;
import net.dv8tion.jda.core.events.guild.GenericGuildEvent;
import net.dv8tion.jda.core.events.guild.member.GenericGuildMemberEvent;
import net.dv8tion.jda.core.events.message.*;
import net.dv8tion.jda.core.events.user.GenericUserEvent;

import java.util.*;
import java.util.logging.Logger;

public final class BotUtils {

	private static final String BOT_URL = "https://discordapp.com/oauth2/authorize?client_id=%s&scope=bot";
	private static final Logger LOGGER = Logger.getLogger(Alexis.class.getName());

	private BotUtils() {
		// Don't construct this
	}

	public static EmbedBuilder createEmbedBuilder(JDACommand event) {
		return createEmbedBuilder(event.getSource().getGuild());
	}

	public static EmbedBuilder createEmbedBuilder(Guild guild) {
		EmbedBuilder builder = new EmbedBuilder();

		if (guild != null)
			builder.setColor(guild.getSelfMember().getColor());

		return builder;
	}

	public static TextChannel getWriteableChannel(GenericGuildEvent event) {
		Guild guild = event.getGuild();
		Collection<TextChannel> channels = guild.getTextChannelsByName("general", true);

		for (TextChannel channel : channels) {
			if (channel.canTalk())
				return channel;
		}

		channels = guild.getTextChannels();

		for (TextChannel channel : channels) {
			if (channel.canTalk())
				return channel;
		}

		return null;
	}

	public static boolean isDatabaseAlive() {
		BotConfiguration config = Alexis.config;
		boolean databaseEnabled = config.getDatabaseConfig().isEnabled();

		if (!databaseEnabled)
			return false;

		long dev = config.getDiscordConfig().getAuthors().get(0).getId();

		try {
			UserData data = UserData.query(dev);
			return data != null;
		} catch (NullPointerException ex) {
			return false;
		}
	}

	public static String formInviteUrl(User user) {
		return String.format(BOT_URL, user.getIdLong());
	}

	public static String getChannelLanguage(Event event) {
		MessageChannel channel;

		if (event instanceof GenericMessageEvent)
			channel = ((GenericMessageEvent)event).getChannel();

		else if (event instanceof GenericTextChannelEvent)
			channel = ((GenericTextChannelEvent)event).getChannel();

		else
			return Language.ENGLISH.getCode();

		MessageChannelData data = MessageChannelData.query(channel.getIdLong());
		return data.getLanguage();
	}

	public static String buildScript(String markup, Event event) {
		return buildScript(markup, event, Map.of());
	}

	public static String buildScript(String markup, Event event, Map<String, Object> p) {
		Map<String, Object> params = addEventParams(event, p);
		return new ElyScript(markup).compile(params);
	}

	public static String getScript(String key, Event event) {
		return getScript(key, event, Map.of());
	}

	public static String getScript(String key, Event event, Map<String, Object> p) {
		String language = getChannelLanguage(event);
		Map<String, Object> params = addEventParams(event, p);
		return Alexis.scriptStore.get(params, key, language);
	}

	private static Map<String, Object> addEventParams(Event event, Map<String, Object> p) {
		Map<String, Object> params = new HashMap<>(p);
		boolean database = isDatabaseAlive();

		if (event instanceof MessageReceivedEvent) {
			Message message = ((MessageReceivedEvent)event).getMessage();
			params.put("message.content", message.getContentRaw());
			params.put("message.id", message.getId());
		}

		if (event instanceof GenericUserEvent) {
			User user = ((GenericUserEvent)event).getUser();
			params.put("user.name", user.getName());
			params.put("user.mention", user.getAsMention());
			params.put("user.type", user.isBot() ? "bot" : "user");
			params.put("user.id", user.getId());
			params.put("user.avatar", user.getEffectiveAvatarUrl());
			params.put("user.discriminator", user.getDiscriminator());

			if (database) {
				UserData userData = UserData.query(user.getIdLong());
				params.put("user.xp", userData.getXp());
				params.put("user.level", RuneScape.parseXpAsLevel(userData.getXp()));
				params.put("user.last_message", userData.getLastMessage().toString());

				NanowrimoLink nano = userData.getNanoLink();
				if (nano.getUsername() != null && !nano.isPrivate())
					params.put("user.nano.name", nano.getUsername());
			}
		}

		if (event instanceof GenericGuildMemberEvent) {
			Member member = ((GenericGuildMemberEvent)event).getMember();
			params.put("member.nickname", member.getEffectiveName());
			params.put("member.role", member.getRoles());

			Game game = member.getGame();
			if (game != null)
				params.put("member.game", game.getName());

			if (database) {
				long userId = member.getUser().getIdLong();
				long guildId = member.getGuild().getIdLong();
				MemberData memberData = MemberData.query(userId, guildId);
				params.put("member.xp", memberData.getXp());
				params.put("member.level", RuneScape.parseXpAsLevel(memberData.getXp()));
				params.put("member.last_message", memberData.getLastMessage());
			}
		}

		if (event instanceof GenericGuildEvent) {
			Guild guild = ((GenericGuildEvent)event).getGuild();
			params.put("guild.name", guild.getName());
			params.put("guild.id", guild.getId());
			params.put("guild.icon", guild.getIconUrl());

			if (database) {
				GuildData guildData = GuildData.query(guild.getIdLong());
				params.put("guild.xp", guildData.getXp());
				params.put("guild.level", RuneScape.parseXpAsLevel(guildData.getXp()));
				params.put("guild.prefix", guildData.getSettings().getPrefix());
			}
		}

		if (params.containsKey("guild.prefix"))
			params.put("prefix", params.get("guild.prefix"));
		else
			params.put("prefix", Alexis.config.getDiscordConfig().getPrefix());

		return params;
	}
}
