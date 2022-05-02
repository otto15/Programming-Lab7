package com.otto15.common.commands;

import com.otto15.common.controllers.CommandManager;
import com.otto15.common.entities.User;
import com.otto15.common.network.Response;

/**
 * Command for clearing the collection
 */
public class ClearCommand extends AbstractCommand {

    public ClearCommand(CommandManager commandManager) {
        super(commandManager, "clear", "deletes all collection elements", 0);
    }

    @Override
    public Object[] readArgs(Object[] args) {
        return new Object[]{args[0]};
    }

    @Override
    public Response execute(Object[] args) {
        User user = (User) args[0];
        if (getCommandManager().getDBWorker().deletePersonsByUser(user) < 0) {
            return new Response("Could not clear persons, db problems.");
        }
        getCommandManager().getCollectionManager().clear(user.getLogin());
        return new Response("Your persons has been deleted.");
    }

}
