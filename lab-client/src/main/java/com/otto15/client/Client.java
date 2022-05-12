package com.otto15.client;

import com.otto15.client.listeners.ClientCommandListener;
import com.otto15.client.listeners.ClientNetworkListener;
import com.otto15.common.controllers.CommandManager;
import com.otto15.common.state.PerformanceState;


public final class Client {

    private Client() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        PerformanceState performanceState = new PerformanceState();
        ConnectionHandler connectionHandler = new ConnectionHandler(performanceState);
        ClientNetworkListener clientListener = new ClientNetworkListener(connectionHandler);
        CommandManager commandManager = new CommandManager(clientListener, performanceState);
        ClientCommandListener commandListener = new ClientCommandListener(commandManager);
        System.out.println("Salamaleikum!");
        connectionHandler.openConnection();
        commandListener.launch();
        connectionHandler.close();
    }

}
