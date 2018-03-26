package com.elypia.alexis.discord.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Command annotiation is used to supply META-DATA
 * to a command. This can be aliases, or the help string to
 * let people know how to use this command.
 *
 * All static data will be stored in an annotiation, reserving the
 * method body for what it's meant for, functionality and dynamic data.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

	/**
	 * @return A list of all the alises that grant the
	 * user access to this command.
	 */

	String[] aliases();

	/**
	 * @return A help string to advise the user of what
	 * this command does.
	 *
	 * If the help String is {@link String#isEmpty() empty} then
	 * it will be hidden from the help docs.
	 */

	String help() default "";
}