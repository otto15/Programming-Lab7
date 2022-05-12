package com.otto15.common.network;

import com.otto15.common.commands.AbstractCommand;
import com.otto15.common.entities.User;

import java.io.Serializable;
import java.util.Arrays;

public class Request implements Serializable {

    private AbstractCommand command;
    private Object[] args;

    public Request(AbstractCommand command, Object[] args) {
        this.command = command;
        this.args = args;
    }


    public AbstractCommand getCommand() {
        return command;
    }

    public void setCommand(AbstractCommand command) {
        this.command = command;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public User getUser() {
        return (User) args[args.length - 1];
    }

    @Override
    public String toString() {
        return "Request{"
                + "command=" + command
                + ", args=" + Arrays.toString(args)
                + '}';
    }
}
