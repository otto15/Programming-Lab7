//package com.otto15;
//
//import com.otto15.client.ConnectionHandler;
//import com.otto15.client.listeners.ClientNetworkListener;
//import com.otto15.common.commands.HelpCommand;
//import com.otto15.common.controllers.CommandManager;
//import com.otto15.common.entities.User;
//import com.otto15.common.network.Request;
//import com.otto15.common.network.Response;
//import com.otto15.common.state.PerformanceState;
//
//public class Test implements Runnable {
//
//    private ClientNetworkListener clientNetworkListener;
//    private CommandManager commandManager;
//    private int count = 0;
//
//    public Test(ClientNetworkListener clientNetworkListener, CommandManager commandManager) {
//        this.commandManager = commandManager;
//        this.clientNetworkListener = clientNetworkListener;
//    }
//
//    @Override
//    public void run() {
//
//        for (int i = 0; i < 100; ++i) {
//            if (test() < 0) {
//                System.out.println(count);
//                break;
//            }
//        }
//    }
//
//
//
//    public int test() {
//        count++;
//        Response response = clientNetworkListener.listen(new Request(new HelpCommand(commandManager), new Object[]{new User()}));
//        if (response == null) {
//            return -1;
//        }
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//
//        }
//        return 1;
//    }
//
//    public static void main(String[] args) {
//        PerformanceState performanceState = new PerformanceState();
//        for (int i = 0; i < 100; ++i) {
//            ConnectionHandler connectionHandler = new ConnectionHandler(performanceState);
//            ClientNetworkListener clientNetworkListener = new ClientNetworkListener(connectionHandler);
//            CommandManager commandManager = new CommandManager(clientNetworkListener, performanceState);
//            connectionHandler.openConnection("localhost", 1);
//            new Thread(new Test(clientNetworkListener, commandManager)) {
//            }.start();
//        }
//    }
//
//}
