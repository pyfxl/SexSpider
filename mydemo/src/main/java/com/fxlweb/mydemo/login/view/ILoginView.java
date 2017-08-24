package com.fxlweb.mydemo.login.view;

/**
 * Created by feng on 2016/5/29.
 */

public interface ILoginView {
    public void onClearText();
    public void onLoginResult(Boolean result, int code);
    public void onSetProgressBarVisibility(int visibility);
}
