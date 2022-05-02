package com.otto15.common.commands;

import com.otto15.common.controllers.CommandManager;
import com.otto15.common.entities.Person;
import com.otto15.common.entities.User;
import com.otto15.common.network.Response;

public class RemoveAnyByHeightCommand extends AbstractCommand {

    public RemoveAnyByHeightCommand(CommandManager commandManager) {
        super(commandManager, "remove_any_by_height", "removes collection element by height", 1);
    }

    @Override
    public Object[] readArgs(Object[] args) {
        try {
            long height = Long.parseLong((String) args[0]);
            return new Object[]{height, args[1]};
        } catch (NumberFormatException e) {
            System.out.println("Invalid format of height.");
        }
        return null;
    }

    @Override
    public Response execute(Object[] args) {
        Person deletedPerson = getCommandManager().getCollectionManager().findAnyByHeight((Long) args[0], ((User) args[1]).getLogin());
        if (deletedPerson.getId() == -1) {
            return new Response("No person found with such height.");
        }
        if (getCommandManager().getDBWorker().deletePersonById(deletedPerson.getId()) < 0) {
            return new Response("Could not delete person because of DB problems");
        }
        getCommandManager().getCollectionManager().remove(deletedPerson);
        return new Response("Person[" + deletedPerson.getId() + "] " + deletedPerson.getName() + " was removed.");

    }
}
