package com.fxlweb.mydemo.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fxlweb.mydemo.R;
import com.fxlweb.mydemo.login.presenter.ILoginPresenter;
import com.fxlweb.mydemo.login.presenter.LoginPresenterImpl;
import com.fxlweb.mydemo.login.view.ILoginView;

public class LoginActivity extends Activity implements ILoginView, View.OnClickListener {
    private EditText etName;
    private EditText etPasswd;
    private Button btnLogin;
    private Button btnClear;
    private ProgressBar pbLogining;
    ILoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etName = (EditText) this.findViewById(R.id.et_login_name);
        etPasswd = (EditText) this.findViewById(R.id.et_login_passwd);
        btnLogin = (Button) this.findViewById(R.id.btn_login_login);
        btnClear = (Button) this.findViewById(R.id.btn_login_clear);
        pbLogining = (ProgressBar) this.findViewById(R.id.pb_login_logining);

        btnLogin.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        loginPresenter = new LoginPresenterImpl(this);
        loginPresenter.setProgressBarVisibility(View.INVISIBLE);
    }

    @Override
    public void onClearText() {
        etName.setText("");
        etPasswd.setText("");
    }

    @Override
    public void onLoginResult(Boolean result, int code) {
        loginPresenter.setProgressBarVisibility(View.INVISIBLE);
        btnLogin.setEnabled(true);
        btnClear.setEnabled(true);
        if(result) {
            Toast.makeText(this,"Login Success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"Login Fail, code = " + code, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSetProgressBarVisibility(int visibility) {
        pbLogining.setVisibility(visibility);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login_login:
                loginPresenter.setProgressBarVisibility(View.VISIBLE);
                btnLogin.setEnabled(false);
                btnClear.setEnabled(false);
                loginPresenter.doLogin(etName.getText().toString(), etPasswd.getText().toString());
                break;
            case R.id.btn_login_clear:
                loginPresenter.clear();
                break;
        }
    }
}
