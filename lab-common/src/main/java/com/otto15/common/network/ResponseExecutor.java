package com.otto15.common.network;

import com.otto15.common.controllers.CommandListener;

public class ResponseExecutor {

    private final Response response;
    private final CommandListener commandListener;

    public ResponseExecutor(Response response, CommandListener commandListener) {
        this.response = response;
        this.commandListener = commandListener;
    }

    public void execute() {
        response.showResult();
        if (response.getUser() != null) {
            commandListener.setUser(response.getUser());
        }
    }

}
