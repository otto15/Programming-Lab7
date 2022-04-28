package com.otto15.common.controllers;


import com.otto15.common.entities.User;
import com.otto15.common.network.Response;
import com.otto15.common.network.ResponseExecutor;
import com.otto15.common.state.State;

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

    private static boolean onClient;
    private final Reader reader;
    private User user;

    public CommandListener(Reader reader) {
        this.reader = reader;
    }

    public CommandListener() {
        this(new InputStreamReader(System.in));
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static boolean isOnClient() {
        return onClient;
    }

    public static void setOnClient() {
        CommandListener.onClient = true;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(reader)) {
            while (State.getPerformanceStatus()) {
                if (!(reader.getClass() == FileReader.class)) {
                    System.out.println("===========================");
                }
                if (user != null && !"".equals(user.getLogin())) {
                    System.out.print(ANSI_PURPLE + user.getLogin() + " ↪ " + ANSI_RESET);
                }
                String input = in.readLine();
                if (input == null) {
                    break;
                }
                if (!"".equals(input)) {
                    Response response = CommandManager.onCommandReceived(input, user);
                    if (response != null) {
                        ResponseExecutor responseExecutor = new ResponseExecutor(response, this);
                        responseExecutor.execute();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Invalid output.");
        }

    }

}
