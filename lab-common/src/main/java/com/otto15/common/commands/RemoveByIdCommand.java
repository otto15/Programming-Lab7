package com.otto15.common.commands;


import com.otto15.common.controllers.CommandManager;
import com.otto15.common.entities.User;
import com.otto15.common.network.Request;
import com.otto15.common.network.Response;

public class RemoveByIdCommand extends AbstractCommand {

    public RemoveByIdCommand(CommandManager commandManager) {
        super(commandManager, "remove_by_id", "removes collection element by id", 1);
    }

    @Override
    public Object[] readArgs(Object[] args) {
        try {
            User user = (User) args[1];
            long personId = Long.parseLong((String) args[0]);
            Response response = getCommandManager().getNetworkListener().listen(new Request(new FindByIdCommand(getCommandManager()), new Object[]{personId, user}));
            if (response.getMessage() == null) {
                System.out.println("No person found with such id.");
                return null;
            }
            if (response.getUser() == null) {
                System.out.println("You do not have rights to remove this person.");
                return null;
            }

            return new Object[]{personId, user};
        } catch (NumberFormatException e) {
            System.out.println("Invalid format of id.");
        }
        return null;
    }

    @Override
    public Response execute(Object[] args) {
        long personId = (Long) args[0];
        long delResult = getCommandManager().getDBWorker().deletePersonById(personId);
        if (delResult < 0) {
            return new Response("Could not remove person because of DB problems.");
        }
        if (delResult == 0) {
            return new Response("No person found with such id.");
        }
        boolean result = getCommandManager().getCollectionManager().removeById(personId);
        return new Response("Removed successfully!");
    }
}
