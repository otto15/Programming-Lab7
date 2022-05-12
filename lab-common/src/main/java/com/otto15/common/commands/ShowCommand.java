package com.otto15.common.commands;


import com.otto15.common.controllers.CommandManager;
import com.otto15.common.network.Response;

public class ShowCommand extends AbstractCommand {

    public ShowCommand(CommandManager commandManager) {
        super(commandManager, "show", "outputs all collection elements", 0);
    }


    @Override
    public Response execute(Object[] args) {
        return new Response(getCommandManager().getCollectionManager().show());
    }

}
