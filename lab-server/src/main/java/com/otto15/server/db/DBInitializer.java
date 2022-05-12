package com.otto15.server.db;

import com.otto15.common.entities.enums.Color;
import com.otto15.common.entities.enums.Country;
import com.otto15.server.logging.LogConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBInitializer {

    private final DBConnector dbConnector;

    public DBInitializer(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    private void addConstantsToTable(Connection connection, String insertQuery, Object[] rows) throws SQLException {
        for (Object row : rows) {
            try (PreparedStatement addConstantToTable = connection.prepareStatement(insertQuery);) {
                addConstantToTable.setString(1, row.toString());
                addConstantToTable.execute();
            }
        }
    }

    public int init() {

        try (
                Connection connection = dbConnector.connect();
                PreparedStatement createUsersTable = connection.prepareStatement(DBQuery.CREATE_USERS_TABLE.getQuery());
                PreparedStatement createColorsTable = connection.prepareStatement(DBQuery.CREATE_COLORS_TABLE.getQuery());
                PreparedStatement createCountriesTable = connection.prepareStatement(DBQuery.CREATE_COUNTRIES_TABLE.getQuery());
                PreparedStatement createLocationsTable = connection.prepareStatement(DBQuery.CREATE_LOCATIONS_TABLE.getQuery());
                PreparedStatement createPersonsTable = connection.prepareStatement(DBQuery.CREATE_PERSONS_TABLE.getQuery());
        ) {


            createColorsTable.execute();
            createCountriesTable.execute();
            createLocationsTable.execute();
            createUsersTable.execute();
            createPersonsTable.execute();

            addConstantsToTable(connection, DBQuery.INSERT_CONSTANT_ROW_TO_COLORS.getQuery(), Color.values());
            addConstantsToTable(connection, DBQuery.INSERT_CONSTANT_ROW_TO_COUNTRIES.getQuery(), Country.values());

            LogConfig.LOGGER.info("DB initialization succeeded");
            return 1;
        } catch (SQLException e) {
            LogConfig.LOGGER.error("Problems during DB initialization");
            return -1;
        }

    }

}
