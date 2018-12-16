package com.elypia.alexis.entities.embedded;

import xyz.morphia.annotations.*;

@Embedded
public class MusicSettings {

    @Property("name_sync")
    private boolean syncChannelName;

    public boolean getSyncChannelName() {
        return syncChannelName;
    }

    public void setSyncChannelName(boolean syncChannelName) {
        this.syncChannelName = syncChannelName;
    }
}
