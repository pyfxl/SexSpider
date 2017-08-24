package com.fxlweb.mydemo.login.model;

/**
 * Created by feng on 2016/5/29.
 */

public class UserModel implements IUser {
    String name;
    String passwd;

    public UserModel(String name, String passwd) {
        this.name = name;
        this.passwd = passwd;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPasswd() {
        return passwd;
    }

    @Override
    public int checkUserValidity(String name, String passwd) {
        if(name.equals(this.name) && passwd.equals(this.passwd)) {
            return 0;
        }

        return -1;
    }
}
