package com.otto15.common.commands;

import com.otto15.common.controllers.CommandManager;
import com.otto15.common.entities.Person;
import com.otto15.common.entities.PersonLoader;
import com.otto15.common.entities.User;
import com.otto15.common.network.Response;

import java.io.IOException;


/**
 * Command for adding new elements to collection
 */
public class AddCommand extends AbstractCommand {

    public AddCommand() {
        super("add", "adds element to collection", 0);
    }

    @Override
    public Object[] readArgs(Object[] args) {
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
        Person personToAdd = (Person) args[0];
        User user = (User) args[1];
        if (CommandManager.getDBWorker().addPerson(personToAdd, user) <= 0) {
            return new Response("Couldn't create person.");
        }
        CommandManager.getCollectionManager().add(personToAdd);
        return new Response("New person successfully created!");
    }
}
