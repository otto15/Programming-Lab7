package com.otto15.common.network;

import com.otto15.common.controllers.CommandManager;
import com.otto15.common.db.DBWorker;

public class RequestExecutor {

    private final DBWorker dbWorker;

    public RequestExecutor(DBWorker dbWorker) {
        this.dbWorker = dbWorker;
    }

    public Response execute(Request request) {
        if (CommandManager.getCommandsWithoutAuth().containsKey(request.getCommand().getName())) {
            return CommandManager.executeCommand(request.getCommand(), request.getArgs());
        }
        long checkUserResult = dbWorker.checkUser(request.getUser());
        if (checkUserResult < 0) {
            return new Response("DB problems, try again later.");
        }
        if (checkUserResult == 0) {
            return new Response("Sign in/up first.");
        }
        return CommandManager.executeCommand(request.getCommand(), request.getArgs());
    }


}
