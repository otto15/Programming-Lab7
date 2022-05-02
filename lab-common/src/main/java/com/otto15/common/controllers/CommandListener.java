package com.otto15.common.controllers;


import com.otto15.common.entities.User;
import com.otto15.common.network.Response;
import com.otto15.common.network.ResponseExecutor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Listens to stream for commands
 *
 * @author Rakhmatullin R.
 */
public class CommandListener implements Runnable {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    private final boolean onClient;
    private final Reader reader;
    private final CommandManager commandManager;
    private User user;

    public CommandListener(Reader reader, CommandManager commandManager, boolean onClient) {
        this.reader = reader;
        this.commandManager = commandManager;
        this.onClient = onClient;
    }

    public CommandListener(CommandManager commandManager, boolean onClient) {
        this(new InputStreamReader(System.in), commandManager, onClient);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isOnClient() {
        return onClient;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(reader)) {
            while (commandManager.getPerformanceState().getPerformanceStatus()) {
                if (!(reader.getClass() == FileReader.class)) {
                    System.out.println("===========================");
                }
                outputUserName();
                String input = in.readLine();
                if (input == null) {
                    break;
                }
                if (!"".equals(input)) {
                    Response response = commandManager.onCommandReceived(input, isOnClient(), user);
                    if (response != null) {
                        ResponseExecutor responseExecutor = new ResponseExecutor(response, this);
                        responseExecutor.execute();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Invalid i/o.");
        }

    }

    public void outputUserName() {
    }

}
