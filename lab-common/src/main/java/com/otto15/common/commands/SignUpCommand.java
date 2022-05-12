package com.otto15.common.commands;

import com.otto15.common.controllers.CommandManager;
import com.otto15.common.entities.User;
import com.otto15.common.entities.UserLoader;
import com.otto15.common.exceptions.EndOfStreamException;
import com.otto15.common.network.Response;

import java.io.IOException;

public class SignUpCommand extends AbstractCommand {

    public SignUpCommand(CommandManager commandManager) {
        super(commandManager, "sign_up", "sign up user", 0);
    }

    @Override
    public Object[] readArgs(Object[] args) throws EndOfStreamException {
        try {
            UserLoader userLoader = new UserLoader();
            User newUser = userLoader.loadUser();
            return new Object[]{newUser};
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public Response execute(Object[] args) {
        User user = (User) args[0];
        long result = getCommandManager().getDBWorker().addUser(user);
        if (result <= 0) {
            return new Response("Failed to create a user, this login is already occupied.");
        }
        user.setId(result);
        return new Response("Signed up.", user);
    }

}
