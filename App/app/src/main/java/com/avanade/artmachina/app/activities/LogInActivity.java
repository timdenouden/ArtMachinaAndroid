package com.avanade.artmachina.app.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.avanade.artmachina.R;
import com.avanade.artmachina.app.models.DataManager;
import com.avanade.artmachina.app.models.DataProvider;
import com.avanade.artmachina.app.models.HttpResponseError;
import com.avanade.artmachina.app.models.User;

import javax.security.auth.login.LoginException;

public class LogInActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText emailEditText;
    EditText passwordEditText;
    Button forgotPasswordButton;
    Button loginButton;
    Button registerButton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        toolbar = findViewById(R.id.loginToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorTextLight), PorterDuff.Mode.SRC_ATOP);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        progressBar = findViewById(R.id.progress);
        loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if(inputIsValid()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    User userCredential = new User();
                    userCredential.setEmail(emailEditText.getText().toString());
                    userCredential.setPassword(passwordEditText.getText().toString());
                    DataManager.getInstance(LogInActivity.this).login(userCredential, new DataProvider.AuthCompletion() {
                        @Override
                        public void complete(String token) {
                            progressBar.setVisibility(View.INVISIBLE);
                            DataManager.getInstance(LogInActivity.this).setToken(token);
                            onBackPressed();
                        }

                        @Override
                        public void failure(HttpResponseError error) {
                            DataManager.getInstance(LogInActivity.this).logOut();
                            Toast.makeText(LogInActivity.this, "Unable to log in.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });

        registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgotPasswordButton = findViewById(R.id.forgotPassword);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.getInstance(LogInActivity.this).getResetUrl(new DataProvider.UrlCompletion() {
                    @Override
                    public void complete(String url) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    }

                    @Override
                    public void failure(HttpResponseError error) {
                        DataManager.getInstance(LogInActivity.this).logOut();
                        Toast.makeText(LogInActivity.this, "Unable to change password, try again later.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }

    private boolean inputIsValid() {
        return emailEditText.getText().length() > 0 && passwordEditText.getText().length() > 0;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
