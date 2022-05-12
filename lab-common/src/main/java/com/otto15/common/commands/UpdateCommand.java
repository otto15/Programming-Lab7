package com.otto15.common.commands;

import com.otto15.common.controllers.CommandManager;
import com.otto15.common.entities.Person;
import com.otto15.common.entities.PersonLoader;
import com.otto15.common.entities.User;
import com.otto15.common.exceptions.EndOfStreamException;
import com.otto15.common.network.Request;
import com.otto15.common.network.Response;
import com.otto15.common.utils.DataNormalizer;

import java.io.IOException;

public class UpdateCommand extends AbstractCommand {

    public UpdateCommand(CommandManager commandManager) {
        super(commandManager, "update", "updates person value", 1);
    }

    @Override
    public Object[] readArgs(Object[] args) throws EndOfStreamException {
        try {
            long id = Long.parseLong((String) args[0]);
            User user = (User) args[1];
            Response response = getCommandManager().getNetworkListener()
                    .listen(new Request(new FindByIdCommand(getCommandManager()), new Object[]{id, user}));
            if (response.getMessage() == null) {
                System.out.println("No person found with such id.");
                return null;
            }
            if (response.getUser() == null) {
                System.out.println("You do not have rights to update this person.");
                return null;
            }
            Person updatedPerson = PersonLoader.loadPersonWithCurrentValues(DataNormalizer.normalize(response.getMessage()));
            updatedPerson.setId(id);
            return new Object[]{updatedPerson, user};
        } catch (IOException e) {
            System.out.println("Input error.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid format of id.");
        }
        return null;
    }

    @Override
    public Response execute(Object[] args) {
        Person updatedPerson = (Person) args[0];
        User user = (User) args[1];
        updatedPerson.setAuthor(user.getLogin());
        if (getCommandManager().getDBWorker().updatePerson(updatedPerson) <= 0) {
            return new Response("Could not update person because of DB problems.");
        }
        getCommandManager().getCollectionManager().update(updatedPerson);
        return new Response("Person successfully updated!");
    }
}
