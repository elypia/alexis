package com.elypia.alexis.youtube;

public enum ResourceType {

    VIDEO("video"),
    PLAYLIST("playlist"),
    CHANNEL("channel");

    private final String NAME;

    ResourceType(String name) {
        this.NAME = name;
    }

    public String getName() {
        return NAME;
    }
}