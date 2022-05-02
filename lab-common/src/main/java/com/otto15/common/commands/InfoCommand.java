package com.otto15.common.commands;

import com.otto15.common.controllers.CommandManager;
import com.otto15.common.network.Response;

public class InfoCommand extends AbstractCommand {

    public InfoCommand(CommandManager commandManager) {
        super(commandManager, "info", "shows the info about collection", 0);
    }

    @Override
    public Response execute(Object[] args) {
        return new Response("Collection info:" + "\n" + getCommandManager().getCollectionManager().getInfo());
    }
}
