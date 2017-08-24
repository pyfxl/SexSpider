package com.fxlweb.mydemo.login.model;

/**
 * Created by feng on 2016/5/29.
 */

public interface IUser {
    String getName();
    String getPasswd();
    int checkUserValidity(String name, String passwd);
}
