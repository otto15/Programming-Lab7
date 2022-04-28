package com.otto15.client.listeners;

import com.otto15.client.user.UserWorker;
import com.otto15.common.controllers.CommandListener;
import com.otto15.common.entities.User;

public class ClientCommandListener extends CommandListener {

    private final UserWorker userWorker;

    public ClientCommandListener(UserWorker userWorker) {
        super();
        this.userWorker = userWorker;
    }

    public void launch() {
        setUser(new User());
        run();
    }

}
