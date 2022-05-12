package com.otto15.common.commands;

import com.otto15.common.controllers.CommandManager;
import com.otto15.common.network.Response;

/**
 * Command for grouping by height
 */
public class GroupCountingByHeightCommand extends AbstractCommand {

    public GroupCountingByHeightCommand(CommandManager commandManager) {
        super(commandManager, "group_counting_by_height", "outputs the number of group members", 0);
    }

    @Override
    public Response execute(Object[] args) {
        getCommandManager().getCollectionManager().makeGroupsByHeight();
        return new Response(getCommandManager().getCollectionManager().outputGroupsByHeight());
    }
}
