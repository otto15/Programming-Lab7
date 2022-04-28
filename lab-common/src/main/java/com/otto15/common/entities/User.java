package com.otto15.common.entities;

import java.io.Serializable;

public class User implements Serializable {

    private long id;
    private String login;
    private String password;

    public User() {
        this.login = "";
        this.password = "";
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
