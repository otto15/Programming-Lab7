package com.otto15.server.collection;

import com.otto15.common.controllers.CollectionManager;
import com.otto15.common.entities.Coordinates;
import com.otto15.common.entities.Location;
import com.otto15.common.entities.Person;
import com.otto15.common.entities.enums.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollectionManagerImplTest {

    CollectionManager collectionManager;

    @BeforeEach
    void setUp() {
        collectionManager = new CollectionManagerImpl();
        collectionManager.add(new Person("vitalik", new Coordinates(1, 2d), 150, null, null,
                Country.CHINA, new Location(1, 2, 3)));
    }

    @Test
    void add() {
        assertEquals(1, collectionManager.getPersons().size());
    }

    @Test
    void findById() {
        Person person = collectionManager.findById(1L);
        assertEquals("vitalik", person.getName());
    }

    @Test
    void update() {
        Person updatedPerson = new Person("ramazan", new Coordinates(1, 2d), 150, null, null,
                Country.CHINA, new Location(1, 2, 3));
        updatedPerson.setId(1L);
        collectionManager.update(updatedPerson);
        assertEquals("ramazan", collectionManager.findById(1L).getName());
    }
}