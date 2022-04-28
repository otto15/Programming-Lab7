package com.otto15.client.user;

import com.otto15.common.commands.SignInCommand;
import com.otto15.common.commands.SignUpCommand;
import com.otto15.common.controllers.CommandManager;
import com.otto15.common.entities.User;
import com.otto15.common.network.Response;

import java.util.Scanner;

public class UserWorker {

    private final Scanner scanner = new Scanner(System.in);

    public User setUpUser() {
        User user = null;
        while (user == null) {
            System.out.println("Do you want sign in(1) or sign up(2)");
            String answer = scanner.nextLine().trim();
            if ("1".equals(answer)) {
                user = authorize();
            } else if ("2".equals(answer)) {
                user = register();
            }
        }
        return user;
    }

    private User authorize() {
        Response response = CommandManager.processCommand(new SignInCommand(), new String[0], null);
        if (response != null) {
            System.out.println(response.getMessage());
            if (response.getUser() != null) {
                return response.getUser();
            }
        }
        return null;
    }


    private User register() {
        Response response = CommandManager.processCommand(new SignUpCommand(), new String[0], null);
        if (response != null) {
            System.out.println(response.getMessage());
            if (response.getUser() != null) {
                return response.getUser();
            }
        }
        return null;
    }


}
