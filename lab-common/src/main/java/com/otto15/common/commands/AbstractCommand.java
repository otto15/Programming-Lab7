package com.otto15.common.commands;

import com.otto15.common.network.Response;

import java.io.Serializable;


/**
 * Abstract class for commands.
 */
public abstract class AbstractCommand implements Serializable {

    private final String name;
    private final String description;
    private final int inlineArgsCount;

    public AbstractCommand(String name, String description, int inlineArgsCount) {
        this.name = name;
        this.description = description;
        this.inlineArgsCount = inlineArgsCount;
    }

    /**
     * Execution method for all commands.
     * @param args
     * @return true - if execution completed successfully, false - if not
     */
    public abstract Response execute(Object[] args);

    public Object[] readArgs(Object[] args) {
        return new Object[]{args[0]};
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getInlineArgsCount() {
        return inlineArgsCount;
    }
}
