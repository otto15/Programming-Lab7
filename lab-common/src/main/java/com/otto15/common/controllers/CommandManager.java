package com.otto15.common.controllers;

import com.otto15.common.commands.AbstractCommand;
import com.otto15.common.commands.AddCommand;
import com.otto15.common.commands.AddIfMinCommand;
import com.otto15.common.commands.ClearCommand;
import com.otto15.common.commands.ExecuteScriptCommand;
import com.otto15.common.commands.ExitCommand;
import com.otto15.common.commands.GroupCountingByHeightCommand;
import com.otto15.common.commands.HelpCommand;
import com.otto15.common.commands.HistoryCommand;
import com.otto15.common.commands.InfoCommand;
import com.otto15.common.commands.RemoveAnyByHeightCommand;
import com.otto15.common.commands.RemoveByIdCommand;
import com.otto15.common.commands.RemoveGreaterCommand;
import com.otto15.common.commands.ShowCommand;
import com.otto15.common.commands.SignInCommand;
import com.otto15.common.commands.SignUpCommand;
import com.otto15.common.commands.SumOfHeightCommand;
import com.otto15.common.commands.UpdateCommand;
import com.otto15.common.db.DBWorker;
import com.otto15.common.entities.User;
import com.otto15.common.network.NetworkListener;
import com.otto15.common.network.Request;
import com.otto15.common.network.Response;
import com.otto15.common.utils.DataNormalizer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;

/**
 * Manager for commands, responsible for invoking.
 */
public final class CommandManager {

    private static final int HISTORY_LENGTH = 10;
    private static final Queue<String> COMMAND_HISTORY = new LinkedList<>();
    private static final Map<String, AbstractCommand> CLIENT_COMMANDS = new HashMap<>();
    private static final Map<String, AbstractCommand> SERVER_COMMANDS = new HashMap<>();
    private static final Map<String, AbstractCommand> COMMANDS_EXECUTING_WITHOUT_SENDING = new HashMap<>();
    private static final Map<String, AbstractCommand> COMMANDS_WITHOUT_AUTH = new HashMap<>();
    private static NetworkListener networkListener;
    private static CollectionManager collectionManager;
    private static DBWorker dbWorker;

    private CommandManager() {

    }

    static {
        CLIENT_COMMANDS.put("add", new AddCommand());
        CLIENT_COMMANDS.put("add_if_min", new AddIfMinCommand());
        CLIENT_COMMANDS.put("clear", new ClearCommand());
        CLIENT_COMMANDS.put("exit", new ExitCommand());
        CLIENT_COMMANDS.put("history", new HistoryCommand());
        CLIENT_COMMANDS.put("info", new InfoCommand());
        CLIENT_COMMANDS.put("remove_any_by_height", new RemoveAnyByHeightCommand());
        CLIENT_COMMANDS.put("remove_by_id", new RemoveByIdCommand());
        CLIENT_COMMANDS.put("remove_greater", new RemoveGreaterCommand());
        CLIENT_COMMANDS.put("sum_of_height", new SumOfHeightCommand());
        CLIENT_COMMANDS.put("update", new UpdateCommand());
        CLIENT_COMMANDS.put("group_counting_by_height", new GroupCountingByHeightCommand());
        CLIENT_COMMANDS.put("execute_script", new ExecuteScriptCommand());
        CLIENT_COMMANDS.put("help", new HelpCommand());
        CLIENT_COMMANDS.put("show", new ShowCommand());
        CLIENT_COMMANDS.put("sign_in", new SignInCommand());
        CLIENT_COMMANDS.put("sign_up", new SignUpCommand());

        SERVER_COMMANDS.put("exit", new ExitCommand());

        COMMANDS_EXECUTING_WITHOUT_SENDING.putAll(SERVER_COMMANDS);
        COMMANDS_EXECUTING_WITHOUT_SENDING.put("execute_script", new ExecuteScriptCommand());

        COMMANDS_WITHOUT_AUTH.put("sign_in", new SignInCommand());
        COMMANDS_WITHOUT_AUTH.put("sign_up", new SignUpCommand());
        COMMANDS_WITHOUT_AUTH.put("exit", new ExitCommand());
    }

    public static Map<String, AbstractCommand> getCommandsWithoutAuth() {
        return COMMANDS_WITHOUT_AUTH;
    }

    public static void setCollectionManager(CollectionManager collectionManager) {
        CommandManager.collectionManager = collectionManager;
    }

    public static void setDbWorker(DBWorker dbWorker) {
        CommandManager.dbWorker = dbWorker;
    }

    public static DBWorker getDBWorker() {
        return dbWorker;
    }

    public static CollectionManager getCollectionManager() {
        return collectionManager;
    }

    public static void setNetworkListener(NetworkListener networkListener) {
        CommandManager.networkListener = networkListener;
    }

    public static NetworkListener getNetworkListener() {
        return networkListener;
    }

    public static Map<String, AbstractCommand> getCommands() {
        return CLIENT_COMMANDS;
    }

    public static Queue<String> getCommandHistory() {
        return COMMAND_HISTORY;
    }

    public static void addCommandToHistory(String commandName) {
        COMMAND_HISTORY.add(commandName);
        if (COMMAND_HISTORY.size() > HISTORY_LENGTH) {
            COMMAND_HISTORY.poll();
        }
    }

    /**
     * Normalize the data and pass it to executeCommand()
     *
     * @param inputData data from listener
     */
    public static Response onCommandReceived(String inputData, User user) {
        boolean fromClient = CommandListener.isOnClient();
        Map<String, AbstractCommand> commands = SERVER_COMMANDS;
        if (fromClient) {
            commands = CLIENT_COMMANDS;
        }
        String[] commandWithRawArgs = DataNormalizer.normalize(inputData);
        String commandName = commandWithRawArgs[0].toLowerCase(Locale.ROOT);
        String[] rawArgs = Arrays.copyOfRange(commandWithRawArgs, 1, commandWithRawArgs.length);
        if (commands.containsKey(commandName)) {
            AbstractCommand command = commands.get(commandName);
            return processCommand(command, rawArgs, user);
        }
        return new Response("No such command, call \"help\" to see the list of commands.");
    }

    public static Response processCommand(AbstractCommand command, String[] rawArgs, User user) {
        if (rawArgs.length == command.getInlineArgsCount()) {
            Object[] rawArgsAndUser = new Object[rawArgs.length + 1];
            System.arraycopy(rawArgs, 0, rawArgsAndUser, 0, rawArgs.length);
            rawArgsAndUser[rawArgsAndUser.length - 1] = user;
            Object[] commandArgs = command.readArgs(rawArgsAndUser);
            if (commandArgs != null) {
                if (COMMANDS_EXECUTING_WITHOUT_SENDING.containsKey(command.getName())) {
                    return executeCommand(command, commandArgs);
                } else {
                    return networkListener.listen(new Request(command, commandArgs));
                }
            }
        } else {
            return new Response("Wrong number of arguments.");
        }
        return null;
    }

    public static Response executeCommand(AbstractCommand command, Object[] args) {
        CommandManager.addCommandToHistory(command.getName());
        return command.execute(args);
    }

}
