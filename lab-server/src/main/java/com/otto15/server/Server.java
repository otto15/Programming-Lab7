package com.otto15.server;

import com.otto15.common.controllers.CollectionManager;
import com.otto15.common.controllers.CommandListener;
import com.otto15.common.controllers.CommandManager;
import com.otto15.common.db.DBWorker;
import com.otto15.common.network.RequestExecutor;
import com.otto15.server.collection.CollectionManagerImpl;
import com.otto15.server.db.DBConnector;
import com.otto15.server.db.DBInitializer;
import com.otto15.server.db.DBWorkerImpl;
import com.otto15.server.utils.Encryptor;
import com.otto15.server.utils.SHA1Encryptor;

import java.io.IOException;

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
            CommandManager.setDbWorker(dbWorker);
            CollectionManager collectionManager = new CollectionManagerImpl(dbWorker.selectAllPersons());
            CommandManager.setCollectionManager(collectionManager);
            RequestExecutor requestExecutor = new RequestExecutor(dbWorker);
            try {
                ConnectionHandler connectionHandler = new ConnectionHandler(requestExecutor);
                Thread commandListenerThread = new Thread(new CommandListener());
                commandListenerThread.start();
                connectionHandler.run();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
