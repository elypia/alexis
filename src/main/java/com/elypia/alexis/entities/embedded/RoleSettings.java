package com.elypia.alexis.entities.embedded;

import xyz.morphia.annotations.*;

@Embedded
public class RoleSettings {

    @Property("enabled")
    private boolean enabled;

    @Property("role_id")
    private long roleId;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }
}
