package com.elypia.alexis.commandler.validators;

import net.dv8tion.jda.core.Permission;

import java.lang.annotation.*;

/**
 * Check if the user has the {@link Permission#MANAGE_SERVER} permission. <br>
 * The bot doesn't have to have this permission.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Elevated {

}