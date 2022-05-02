package com.otto15.server;

import com.otto15.common.controllers.CollectionManager;
import com.otto15.common.controllers.CommandListener;
import com.otto15.common.controllers.CommandManager;
import com.otto15.common.db.DBWorker;
import com.otto15.common.entities.Person;
import com.otto15.common.state.PerformanceState;
import com.otto15.server.request.RequestExecutor;
import com.otto15.server.collection.CollectionManagerImpl;
import com.otto15.server.db.DBConnector;
import com.otto15.server.db.DBInitializer;
import com.otto15.server.db.DBWorkerImpl;
import com.otto15.server.utils.Encryptor;
import com.otto15.server.utils.SHA1Encryptor;

import java.io.IOException;
import java.util.Set;

public final class Server {

    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {

        DBConnector dbConnector = new DBConnector();
        DBInitializer dbInitializer = new DBInitializer(dbConnector);
        if (dbInitializer.init() > 0) {
            Encryptor encryptor = new SHA1Encryptor();
            DBWorker dbWorker = new DBWorkerImpl(dbConnector, encryptor);
            Set<Person> personSet = dbWorker.selectAllPersons();
            if (personSet != null) {
                CollectionManager collectionManager = new CollectionManagerImpl(personSet);
                PerformanceState performanceState = new PerformanceState();
                CommandManager commandManager = new CommandManager(collectionManager, dbWorker, performanceState);
                RequestExecutor requestExecutor = new RequestExecutor(commandManager);
                try {
                    ConnectionHandler connectionHandler = new ConnectionHandler(requestExecutor, performanceState);
                    new Thread(new CommandListener(commandManager, false)).start();
                    connectionHandler.run();
                } catch (IOException e) {
                    System.out.print("");
                }
            }
        }
    }
}
