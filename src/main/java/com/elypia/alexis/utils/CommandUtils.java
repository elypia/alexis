package com.elypia.alexis.utils;

import com.elypia.alexis.discord.annotations.Command;
import com.elypia.alexis.discord.annotations.CommandGroup;
import com.elypia.alexis.discord.events.MessageEvent;
import com.elypia.alexis.discord.handlers.commands.impl.CommandHandler;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static com.elypia.alexis.utils.BotUtils.log;

public final class CommandUtils {

    /**
     * Get the commands this handler holds which are relevent to the
     * current MessageEvent. <br>
     * Eg <strong>&gt;ud define jenni</strong>: will collect all <strong>define</strong> commands. <br>
     * Note: This will return an empty list if there are no commands matching this request.
     *
     * @param event The MessageEvent which contains the module and command.
     * @param handler The module which the MessageEvent references.
     * @return A collection of commands relevent to this event.
     */

    public static Collection<Method> getCommands(MessageEvent event, CommandHandler handler) {
        Set<Method> commands = new HashSet<>();
        Collection<Method> methods = getCommandMethods(handler);
        String command = event.getCommand();
        CommandGroup group = null;

        for (Method method : methods) {
            Command annotation = method.getAnnotation(Command.class);

            if (annotation != null) {
                if (Arrays.asList(annotation.aliases()).contains(command)) {
                    group = method.getAnnotation(CommandGroup.class);
                    commands.add(method);
                    break;
                }
            }
        }

        if (group != null) {
            for (Method method : methods) {
                CommandGroup annotation = method.getAnnotation(CommandGroup.class);

                if (annotation != null) {
                    if (annotation.value().equals(group.value()))
                        commands.add(method);
                }
            }
        }

        return commands;
    }

    /**
     * Goes through the {@link CommandHandler} and retrieves all
     * methods with the {@link Command} or {@link CommandGroup} annotation.
     *
     * @param handler The handler due the command.
     * @return A collection of methods this handler has which execute commands.
     */

    public static Collection<Method> getCommandMethods(CommandHandler handler) {
        Collection<Method> methods = new ArrayList<>();
        Method[] array = handler.getClass().getMethods();

        for (Method method : array) {
            Command command = method.getAnnotation(Command.class);
            CommandGroup group = method.getAnnotation(CommandGroup.class);

            if (command != null || group != null)
                methods.add(method);
        }

        return methods;
    }

    /**
     * Filter a set of command methods by the number of arguments that they have
     * compared to the parameters provided in the MessageEvent. <br>
     * Example: <strong>&gt;ud define jenni false</strong><br>
     * We want the command method with only two arguments to accomodate this command.
     * Note: Will return null if no command matches the same number of arguments.
     *
     * @param event The relevent event containing the module, command and parameters.
     * @param methods A list of command events.
     * @return The method required to execute this command.
     */

    public static Method getByParamCount(MessageEvent event, Collection<Method> methods) {
        methods = methods.stream().filter(o -> {
            return o.getParameterCount() - 1 == event.getParams().size();
        }).collect(Collectors.toList());

        int size = methods.size();

        if (size > 1) {
            String message = "Ambiguous commands found in {0}: {1}";
            log(Level.WARNING, message, event.getModule(), event.getCommand());
        }

        return size > 0 ? methods.iterator().next() : null;
    }

    /**
     * Take the String parameters from the message event and parse them into the required
     * format the command method required to execute.
     *
     * @param event The message event to take parameters from.
     * @param method The method to imitate the fields of.
     * @return An Object[] array of all parameters parsed as required for the given method.
     * @throws IllegalArgumentException If one of the arguments could not be parsed in the required format.
     */

    public static Object[] parseParameters(MessageEvent event, Method method) throws IllegalArgumentException {
        List<Object> inputs = event.getParams();

        Class<?>[] types = method.getParameterTypes();
        Object[] params = new Object[types.length];

        params[0] = event;

        for (int i = 1; i <= inputs.size(); i++) {
            Object input = inputs.get(i - 1);
            Class<?> type = types[i];

            params[i] = ParamParser.parseParam(event, input, type);
        }

        return params;
    }
}