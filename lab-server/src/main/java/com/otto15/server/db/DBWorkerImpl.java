package com.otto15.server.db;


import com.otto15.common.db.DBWorker;
import com.otto15.common.entities.Coordinates;
import com.otto15.common.entities.Location;
import com.otto15.common.entities.Person;
import com.otto15.common.entities.User;
import com.otto15.common.entities.enums.Color;
import com.otto15.common.entities.enums.Country;
import com.otto15.server.logging.LogConfig;
import com.otto15.server.utils.Encryptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

public class DBWorkerImpl implements DBWorker {

    private final DBConnector dbConnector;
    private final Encryptor encryptor;

    public DBWorkerImpl(DBConnector dbConnector, Encryptor encryptor) {
        this.dbConnector = dbConnector;
        this.encryptor = encryptor;
    }

    @Override
    public long addPerson(Person person, User user) {
        try (Connection connection = dbConnector.connect();) {
            PreparedStatement addPersonToTable = connection.prepareStatement(DBQuery.INSERT_PERSON.getQuery());
            int paramCounter = 1;
            paramCounter = fillStatementWithPersonData(addPersonToTable, person, paramCounter);
            addPersonToTable.setTimestamp(paramCounter++, Timestamp.from(person.getCreationDate().toInstant()));
            addPersonToTable.setLong(paramCounter, user.getId());

            ResultSet resultSet = addPersonToTable.executeQuery();
            resultSet.next();
            long personId = resultSet.getLong("person_id");
            person.setId(personId);
            person.setAuthor(user.getLogin());
            LogConfig.LOGGER.info("new person added to database with id - " + personId);
            return personId;
        } catch (SQLException e) {
            LogConfig.LOGGER.error(e.getMessage());
            return -1;
        }
    }

    @Override
    public long addUser(User user) {
        try (Connection connection = dbConnector.connect();) {
            PreparedStatement addUserToTable = connection.prepareStatement(DBQuery.INSERT_USER.getQuery());
            addUserToTable.setString(1, user.getLogin());
            addUserToTable.setString(2, encryptor.encrypt(user.getPassword()));

            ResultSet resultSet = addUserToTable.executeQuery();
            resultSet.next();
            long userId = resultSet.getLong("user_id");
            user.setId(userId);
            LogConfig.LOGGER.info("new user added to database with id - " + userId);
            return userId;
        } catch (SQLException e) {
            LogConfig.LOGGER.error(e.getMessage());
            return -1;
        }
    }

    @Override
    public long checkUser(User user) {
        try (Connection connection = dbConnector.connect();) {
            PreparedStatement checkUser = connection.prepareStatement(DBQuery.SELECT_USER_BY_LOGIN_AND_PASSWORD.getQuery());
            checkUser.setString(1, user.getLogin());
            checkUser.setString(2, encryptor.encrypt(user.getPassword()));

            ResultSet resultSet = checkUser.executeQuery();
            if (resultSet.next()) {
                long userId = resultSet.getLong("user_id");
                LogConfig.LOGGER.info("user with id - " + userId + " found");
                return userId;
            }
            return 0;
        } catch (SQLException e) {
            LogConfig.LOGGER.error(e.getMessage());
            return -1;
        }
    }

    @Override
    public long updatePerson(Person person) {
        try (Connection connection = dbConnector.connect()) {
            PreparedStatement updatePerson = connection.prepareStatement(DBQuery.UPDATE_PERSON.getQuery());

            int paramCounter = 1;
            paramCounter = fillStatementWithPersonData(updatePerson, person, paramCounter);
            updatePerson.setLong(paramCounter, person.getId());
            updatePerson.executeUpdate();

            LogConfig.LOGGER.info("person with id - " + person.getId() + " was updated");
            return person.getId();
        } catch (SQLException e) {
            LogConfig.LOGGER.error(e.getMessage());
            return -1;
        }

    }

    @Override
    public long deletePersonsByUser(User user) {
        try (Connection connection = dbConnector.connect()) {
            PreparedStatement deletePersonByUser = connection.prepareStatement(DBQuery.DELETE_PERSONS_BY_AUTHOR.getQuery());

            deletePersonByUser.setString(1, user.getLogin());

            return deletePersonByUser.executeUpdate();
        } catch (SQLException e) {
            LogConfig.LOGGER.error(e.getMessage());
            return -1;
        }
    }

    @Override
    public long deletePersonById(long personId) {
        try (Connection connection = dbConnector.connect()) {
            PreparedStatement deletePersonById = connection.prepareStatement(DBQuery.DELETE_PERSON_BY_ID.getQuery());
            deletePersonById.setLong(1, personId);
            return deletePersonById.executeUpdate();
        } catch (SQLException e) {
            LogConfig.LOGGER.error(e.getMessage());
            return -1;
        }
    }

    @Override
    public Set<Person> selectAllPersons() {
        try (Connection connection = dbConnector.connect()) {
            PreparedStatement selectAllPersons = connection.prepareStatement(DBQuery.SELECT_ALL_PERSONS.getQuery());
            ResultSet resultSet = selectAllPersons.executeQuery();
            return parsePersonsFromResultSet(resultSet);
        } catch (SQLException e) {
            LogConfig.LOGGER.error(e.getMessage());
            return null;
        }
    }

    private Set<Person> parsePersonsFromResultSet(ResultSet resultSet) throws SQLException {
        Set<Person> personSet = new HashSet<>();
        while (resultSet.next()) {
            Person person = new Person();
            person.setId(resultSet.getLong("person_id"));
            person.setAuthor(resultSet.getString("login"));
            person.setName(resultSet.getString("person_name"));
            person.setCoordinates(new Coordinates(resultSet.getDouble("coordinates_x"),
                    resultSet.getDouble("coordinates_y")));
            person.setCreationDate(ZonedDateTime.ofInstant(resultSet.getTimestamp("creation_date").toInstant(), ZoneId.of("UTC")));
            person.setHeight(resultSet.getLong("height"));
            String eyeColor = resultSet.getString("eye_color");
            person.setEyeColor(eyeColor == null ? null : Color.valueOf(eyeColor));
            String hairColor = resultSet.getString("hair_color");
            person.setHairColor(hairColor == null ? null : Color.valueOf(hairColor));
            person.setNationality(Country.valueOf(resultSet.getString("country_name")));
            person.setLocation(new Location(resultSet.getDouble("location_x"),
                    resultSet.getLong("location_y"),
                    resultSet.getFloat("location_z")));
            personSet.add(person);
        }
        return personSet;
    }

    private int fillStatementWithPersonData(PreparedStatement statement, Person person, int givenParamCounter) throws SQLException {
        int paramCounter = givenParamCounter;

        statement.setDouble(paramCounter++, person.getLocation().getX());
        statement.setLong(paramCounter++, person.getLocation().getY());
        statement.setFloat(paramCounter++, person.getLocation().getZ());
        statement.setString(paramCounter++, person.getName());
        statement.setDouble(paramCounter++, person.getCoordinates().getX());
        statement.setDouble(paramCounter++, person.getCoordinates().getY());
        statement.setLong(paramCounter++, person.getHeight());
        if (person.getEyeColor() != null) {
            statement.setString(paramCounter++, person.getEyeColor().name());
        } else {
            statement.setNull(paramCounter++, Types.VARCHAR);
        }
        if (person.getHairColor() != null) {
            statement.setString(paramCounter++, person.getHairColor().name());
        } else {
            statement.setNull(paramCounter++, Types.VARCHAR);
        }
        statement.setString(paramCounter++, person.getNationality().name());
        statement.setDouble(paramCounter++, person.getLocation().getX());
        statement.setLong(paramCounter++, person.getLocation().getY());
        statement.setFloat(paramCounter++, person.getLocation().getZ());

        return paramCounter;
    }

}
