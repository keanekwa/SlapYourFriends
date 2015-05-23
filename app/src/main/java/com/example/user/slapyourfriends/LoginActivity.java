package com.example.user.slapyourfriends;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class LoginActivity extends ActionBarActivity {

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private String mUsername;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();

        mUsernameEditText = (EditText)findViewById(R.id.createUsernameEditText);
        mPasswordEditText = (EditText)findViewById(R.id.createPasswordEditText);
        Button mLoginButton = (Button) findViewById(R.id.loginButton);
        Button mSignUpButton = (Button) findViewById(R.id.signupButton);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ProgressDialog progress = new ProgressDialog(LoginActivity.this);
                progress.setTitle(getString(R.string.loggingin));
                progress.setMessage(getString(R.string.waitwhileloggingin));
                progress.show();
                mUsername = mUsernameEditText.getText().toString();
                mPassword = mPasswordEditText.getText().toString();
                if (mUsername.matches("")) {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), getString(R.string.nologinusernamewarning), Toast.LENGTH_SHORT).show();
                }
                else if (mPassword.matches("")) {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), getString(R.string.nologinpasswordwarning), Toast.LENGTH_SHORT).show();
                }
                else {
                    ParseUser.logInInBackground(mUsername, mPassword, new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {
                            if (user != null) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                LoginActivity.this.startActivity(intent);
                            } else {
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(), getString(R.string.wrongusernamepassword), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });
    }
}
