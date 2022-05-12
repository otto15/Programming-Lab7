package com.otto15.common.commands;

import com.otto15.common.controllers.CommandManager;
import com.otto15.common.entities.Person;
import com.otto15.common.entities.PersonLoader;
import com.otto15.common.entities.User;
import com.otto15.common.exceptions.EndOfStreamException;
import com.otto15.common.network.Response;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RemoveGreaterCommand extends AbstractCommand {

    public RemoveGreaterCommand(CommandManager commandManager) {
        super(commandManager, "remove_greater", "remove all elements greater than given", 0);
    }

    @Override
    public Object[] readArgs(Object[] args) throws EndOfStreamException {
        try {
            Person comparedPerson = PersonLoader.loadPerson();
            return new Object[]{comparedPerson, args[0]};
        } catch (IOException e) {
            System.out.println("Input error.");
        }
        return null;
    }

    @Override
    public Response execute(Object[] args) {
        User user = (User) args[1];
        int collectionLen = getCommandManager().getCollectionManager().getPersons().size();
        List<Person> personsToDelete = getCommandManager().getCollectionManager().getPersons().stream().filter(person -> (Objects.equals(person.getAuthor(), user.getLogin()) && person.compareTo((Person) args[0]) > 0)).collect(Collectors.toList());
        for (Person personToDelete : personsToDelete) {
            if (getCommandManager().getDBWorker().deletePersonById(personToDelete.getId()) < 0) {
                return new Response("Could not delete because of DB problems.");
            }
            getCommandManager().getCollectionManager().remove(personToDelete);
        }
        return new Response((collectionLen - getCommandManager().getCollectionManager().getPersons().size()) + " object(s) was deleted.");
    }
}
