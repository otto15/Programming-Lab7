package com.otto15.server.collection;

import com.otto15.common.controllers.CollectionManager;
import com.otto15.common.entities.Person;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Wrapper for HashSet to store the additional info.
 *
 * @author Rakhmatullin R.
 */
public class CollectionManagerImpl implements CollectionManager {
    private final ZonedDateTime creationDate = ZonedDateTime.now();
    private final Set<Person> persons;
    private Map<Long, List<Person>> groupsByHeight;

    public CollectionManagerImpl(Set<Person> persons) {
        this.persons = persons;
    }

    @Override
    public Set<Person> getPersons() {
        return persons;
    }


    @Override
    public void add(Person newPerson) {
        persons.add(newPerson);
    }

    @Override
    public void clear(String userName) {
        persons.removeIf(person -> Objects.equals(person.getAuthor(), userName));
    }

    @Override
    public Collection<Person> show() {
        return persons.stream()
                .sorted((person1, person2) -> person1.getName().compareToIgnoreCase(person2.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Person findById(Long id) {
        Person foundPerson = persons.stream().filter(person -> Objects.equals(person.getId(), id)).findFirst().orElse(new Person("", null, 1, null, null, null, null));
        if (foundPerson.getId() == null) {
            foundPerson.setId(-1L);
        }
        return foundPerson;
    }

    @Override
    public Person findAnyByHeight(long height, String userName) {
        Person foundPerson = persons.stream().filter(person -> (Objects.equals(person.getAuthor(), userName) && Objects.equals(person.getHeight(), height))).findFirst().orElse(new Person("", null, 1, null, null, null, null));
        if (foundPerson.getId() == null) {
            foundPerson.setId(-1L);
        }
        return foundPerson;
    }

    @Override
    public long getSumOfHeights() {
        long sumOfHeights = 0;
        for (Person singlePerson : persons) {
            sumOfHeights += singlePerson.getHeight();
        }
        return sumOfHeights;
    }

    @Override
    public List<String> getInfo() {
        List<String> info = new ArrayList<>();
        info.add(persons.getClass().getName());
        info.add(creationDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm")));
        info.add(String.valueOf(persons.size()));
        return info;
    }

    @Override
    public void remove(Person personToDelete) {
        persons.remove(personToDelete);
    }

    @Override
    public void makeGroupsByHeight() {
        groupsByHeight = persons.stream().collect(Collectors.groupingBy(Person::getHeight));
    }

    @Override
    public String outputGroupsByHeight() {
        if (groupsByHeight.isEmpty()) {
            return "Collection is empty";
        }

        return groupsByHeight.keySet()
                .stream()
                .map((key) -> "Height - " + key + ": " + groupsByHeight.get(key).size() + " member(s).")
                .collect(Collectors.joining("\n"));

    }

    @Override
    public void update(Person updatedPerson) {
        removeById(updatedPerson.getId());
        persons.add(updatedPerson);
    }

    @Override
    public boolean removeById(Long id) {
        Person personToRemove = findById(id);
        if (personToRemove.getId() == -1) {
            return false;
        }
        persons.remove(personToRemove);
        return true;
    }
}


