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
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CreateAccountActivity extends ActionBarActivity {

    private EditText mCreateUsernameEditText;
    private EditText mCreatePasswordEditText;
    private EditText mCreateEmailEditText;
    private String mCreateUsername;
    private String mCreatePassword;
    private String mCreateEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mCreateUsernameEditText = (EditText)findViewById(R.id.createUsernameEditText);
        mCreatePasswordEditText = (EditText)findViewById(R.id.createPasswordEditText);
        mCreateEmailEditText = (EditText) findViewById(R.id.createEmailEditText);
        Button mCreateAccountButton = (Button) findViewById(R.id.createAccountButton);

        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ProgressDialog progress = new ProgressDialog(CreateAccountActivity.this);
                progress.setTitle(getString(R.string.creatingnewaccount));
                progress.setMessage(getString(R.string.waitregisteraccount));
                progress.show();
                mCreateUsername = mCreateUsernameEditText.getText().toString();
                mCreatePassword = mCreatePasswordEditText.getText().toString();
                mCreateEmail = mCreateEmailEditText.getText().toString();
                if (mCreateUsername.matches("")) {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), getString(R.string.nocreateusernamewarning), Toast.LENGTH_SHORT).show();
                }
                else if (mCreatePassword.matches("")) {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), getString(R.string.nocreatepasswordwarning), Toast.LENGTH_SHORT).show();
                }
                else if (mCreateEmail.matches("")) {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), getString(R.string.nocreateemailwarning), Toast.LENGTH_SHORT).show();
                }
                else {
                    ParseUser user = new ParseUser();
                    user.setUsername(mCreateUsername);
                    user.setPassword(mCreatePassword);
                    user.setEmail(mCreateEmail);

                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            progress.dismiss();
                            if (e == null) {
                                final ProgressDialog loginprogress = new ProgressDialog(CreateAccountActivity.this);
                                loginprogress.setTitle(getString(R.string.loggingin));
                                loginprogress.setMessage(getString(R.string.waitwhileloggingin));
                                loginprogress.show();
                                ParseUser.logInInBackground(mCreateUsername, mCreatePassword, new LogInCallback() {
                                    public void done(ParseUser user, ParseException e) {
                                        if (user != null) {
                                            Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                                            CreateAccountActivity.this.startActivity(intent);
                                        } else {
                                            loginprogress.dismiss();
                                            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else {
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

}
