package com.fxlweb.mydemo.login.presenter;

import android.os.Handler;
import android.os.Looper;

import com.fxlweb.mydemo.login.model.IUser;
import com.fxlweb.mydemo.login.model.UserModel;
import com.fxlweb.mydemo.login.view.ILoginView;

/**
 * Created by feng on 2016/5/29.
 */

public class LoginPresenterImpl implements ILoginPresenter {
    ILoginView loginView;
    IUser user;
    Handler handler;

    public LoginPresenterImpl(ILoginView loginView) {
        this.loginView = loginView;
        initUser();
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void clear() {
        loginView.onClearText();
    }

    @Override
    public void doLogin(String name, String passwd) {
        Boolean isLoginSuccess = true;
        final int code = user.checkUserValidity(name, passwd);
        if(code != 0) isLoginSuccess = false;
        final boolean result = isLoginSuccess;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loginView.onLoginResult(result, code);
            }
        }, 5000);
    }

    @Override
    public void setProgressBarVisibility(int visibility) {
        loginView.onSetProgressBarVisibility(visibility);
    }

    private void initUser() {
        user = new UserModel("test", "test");
    }
}
