package com.otto15.client.listeners;

import com.otto15.common.controllers.CommandListener;
import com.otto15.common.controllers.CommandManager;
import com.otto15.common.entities.User;

public class ClientCommandListener extends CommandListener {

    public ClientCommandListener(CommandManager commandManager) {
        super(commandManager, true);
    }

    public void launch() {
        setUser(new User());
        run();
    }

    @Override
    public void outputUserName() {
        System.out.print(ANSI_PURPLE + getUser().getLogin() + "$ " + ANSI_RESET);
    }

}
