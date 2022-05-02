package com.otto15.common.commands;


import com.otto15.common.controllers.CommandManager;
import com.otto15.common.network.Response;

public class SumOfHeightCommand extends AbstractCommand {

    public SumOfHeightCommand(CommandManager commandManager) {
        super(commandManager, "sum_of_height", "outputs the sum of person's heights", 0);
    }

    @Override
    public Response execute(Object[] args) {
        System.out.println();
        return new Response(String.valueOf(getCommandManager().getCollectionManager().getSumOfHeights()));
    }
}
