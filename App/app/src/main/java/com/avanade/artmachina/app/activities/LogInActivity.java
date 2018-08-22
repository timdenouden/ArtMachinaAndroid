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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.io.Console;

import javax.security.auth.login.LoginException;

public class LogInActivity extends AppCompatActivity {

    private static final int GOOGLE_SIGN_IN = 2;

    Toolbar toolbar;
    EditText emailEditText;
    EditText passwordEditText;
    Button forgotPasswordButton;
    Button loginButton;
    Button registerButton;
    ProgressBar progressBar;
    GoogleSignInClient mGoogleSignInClient;

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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInButton googleSignInButton = findViewById(R.id.sign_in_button);
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully
            Log.d("Login", "googleToken: " + account.getIdToken());
            User user = new User();
            user.setEmail(account.getEmail());
            user.setGoogleToken(account.getIdToken());
            user.setFirstName(account.getGivenName());
            user.setLastName(account.getFamilyName());
            DataManager.getInstance(this).updateGoogleProfile(user, new DataProvider.AuthCompletion() {
                @Override
                public void complete(String token) {
                    Log.d("google", "success: " + token);
                    DataManager.getInstance(LogInActivity.this).setToken(token);
                    onBackPressed();
                }

                @Override
                public void failure(HttpResponseError error) {
                    Log.d("google", "failure " + error.getErrorMessage());
                }
            });


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Login", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "Unable to log in via google", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean inputIsValid() {
        return emailEditText.getText().length() > 0 && passwordEditText.getText().length() > 0;
    }


}
