package com.fxlweb.mydemo.login.presenter;

/**
 * Created by feng on 2016/5/29.
 */

public interface ILoginPresenter {
    void clear();
    void doLogin(String name, String passwd);
    void setProgressBarVisibility(int visibility);
}
