package com.otto15.common.commands;

import com.otto15.common.controllers.CommandManager;
import com.otto15.common.entities.Person;
import com.otto15.common.entities.PersonLoader;
import com.otto15.common.entities.User;
import com.otto15.common.exceptions.EndOfStreamException;
import com.otto15.common.network.Response;

import java.io.IOException;
import java.util.Collections;


/**
 * Command for adding new element to collection it is minimal
 */
public class AddIfMinCommand extends AbstractCommand {

    public AddIfMinCommand(CommandManager commandManager) {
        super(commandManager, "add_if_min", "adds new person if minimal value", 0);
    }

    @Override
    public Object[] readArgs(Object[] args) throws EndOfStreamException {
        try {
            Person personToAdd = PersonLoader.loadPerson();
            return new Object[]{personToAdd, args[0]};
        } catch (IOException e) {
            System.out.println("Input error.");
            return null;
        }
    }

    @Override
    public Response execute(Object[] args) {
        Person newPerson = (Person) args[0];
        User user = (User) args[1];
        if (newPerson.compareTo(Collections.min(getCommandManager().getCollectionManager().getPersons())) < 0) {
            if (getCommandManager().getDBWorker().addPerson(newPerson, user) <= 0) {
                new Response("Could not add person.");
            }
            getCommandManager().getCollectionManager().add(newPerson);
            return new Response("New person successfully created!");
        } else {
            return new Response("Given person is not minimal.");
        }

    }
}
