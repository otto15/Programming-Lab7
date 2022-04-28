package com.otto15.client;

import com.otto15.client.listeners.ClientCommandListener;
import com.otto15.client.listeners.ClientNetworkListener;
import com.otto15.client.user.UserWorker;
import com.otto15.common.controllers.CommandListener;
import com.otto15.common.controllers.CommandManager;


public final class Client {

    private Client() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        ConnectionHandler connectionHandler = new ConnectionHandler();
        ClientNetworkListener clientListener = new ClientNetworkListener(connectionHandler);
        CommandManager.setNetworkListener(clientListener);
        UserWorker userWorker = new UserWorker();
        ClientCommandListener commandListener = new ClientCommandListener(userWorker);
        CommandListener.setOnClient();
        System.out.println("Salamaleikum!");
        connectionHandler.openConnection();
        commandListener.launch();
    }

}
