package com.otto15.common.controllers;

import com.otto15.common.entities.Person;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CollectionManager {

    Set<Person> getPersons();
    void add(Person newPerson);
    void clear(String userName);
    void remove(Person person);
    Collection<Person> show();
    Person findById(Long id);
    long getSumOfHeights();
    List<String> getInfo();
    Person findAnyByHeight(long height, String userName);
    void makeGroupsByHeight();
    String outputGroupsByHeight();
    void update(Person updatedPerson);
    boolean removeById(Long id);
}
