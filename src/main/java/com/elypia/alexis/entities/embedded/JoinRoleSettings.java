package com.elypia.alexis.entities.embedded;

import xyz.morphia.annotations.*;

@Embedded
public class JoinRoleSettings {

    @Property("apply_bot_to_user")
    private boolean applyUserToBot;

    @Embedded("user")
    private RoleSettings userSettings;

    @Embedded("bot")
    private RoleSettings botSettings;

    public boolean applyUserToBot() {
        return applyUserToBot;
    }

    public void setApplyUserToBot(boolean applyUserToBot) {
        this.applyUserToBot = applyUserToBot;
    }

    public RoleSettings getUserSettings() {
        if (userSettings == null)
            userSettings = new RoleSettings();

        return userSettings;
    }

    public void setUserSettings(RoleSettings userSettings) {
        this.userSettings = userSettings;
    }

    public RoleSettings getBotSettings() {
        if (botSettings == null)
            botSettings = new RoleSettings();

        return botSettings;
    }

    public void setBotSettings(RoleSettings botSettings) {
        this.botSettings = botSettings;
    }
}
