package com.otto15.common.commands;

import com.otto15.common.controllers.CommandManager;
import com.otto15.common.entities.User;
import com.otto15.common.entities.UserLoader;
import com.otto15.common.exceptions.EndOfStreamException;
import com.otto15.common.network.Response;

import java.io.IOException;

public class SignInCommand extends AbstractCommand {

    public SignInCommand(CommandManager commandManager) {
        super(commandManager, "sign_in", "sign in user", 0);
    }

    @Override
    public Object[] readArgs(Object[] args) throws EndOfStreamException {
        try {
            UserLoader userLoader = new UserLoader();
            User user = userLoader.loadUser();
            return new Object[]{user};
        } catch (IOException e) {
            return null;
        }

    }

    @Override
    public Response execute(Object[] args) {
        User user = (User) args[0];
        long result = getCommandManager().getDBWorker().checkUser(user);
        if (result <= 0) {
            return new Response("Wrong credentials.");
        }
        user.setId(result);
        return new Response("Signed in.", user);
    }


}
