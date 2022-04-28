package com.otto15.common.commands;



import com.otto15.common.network.Response;
import com.otto15.common.state.State;

/**
 * Command for exit
 */
public class ExitCommand extends AbstractCommand {

    public ExitCommand() {
        super("exit", "closes the app", 0);
    }

    @Override
    public Response execute(Object[] args) {
        State.switchPerformanceStatus();
        return new Response("Shut down.");
    }

}
