package com.otto15.common.commands;



import com.otto15.common.controllers.CommandManager;
import com.otto15.common.network.Response;

/**
 * Command for exit
 */
public class ExitCommand extends AbstractCommand {

    public ExitCommand(CommandManager commandManager) {
        super(commandManager, "exit", "closes the app", 0);
    }

    @Override
    public Response execute(Object[] args) {
        getCommandManager().getPerformanceState().switchPerformanceStatus();
        return new Response("Shutting down.");
    }

}
