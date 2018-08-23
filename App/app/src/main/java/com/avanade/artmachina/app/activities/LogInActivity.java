package com.avanade.artmachina.app.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.io.Console;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
    CallbackManager callbackManager;
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
                            loginFailure();
                        }
                    });
                }
            }
        });

        /* used to generate key for facebook auth
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.avanade.artmachina",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        } */


        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        LoginButton facebookLoginButton = findViewById(R.id.facebook_login_button);
        facebookLoginButton.setReadPermissions("email", "public_profile");
        facebookLoginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        // App code
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if(response.getError() != null) {
                                    loginFailure();
                                }
                                else {
                                    Log.d("register", me.toString());
                                    User user = new User();
                                    user.setEmail(me.optString("email"));
                                    user.setFirstName(me.optString("first_name"));
                                    user.setLastName(me.optString("last_name"));
                                    user.setFacebookToken(loginResult.getAccessToken().getToken());
                                    progressBar.setVisibility(View.VISIBLE);
                                    DataManager.getInstance(LogInActivity.this).updateFacebookProfile(user, new DataProvider.AuthCompletion() {
                                        @Override
                                        public void complete(String token) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            DataManager.getInstance(LogInActivity.this).setToken(token);
                                            onBackPressed();
                                        }

                                        @Override
                                        public void failure(HttpResponseError error) {
                                            loginFailure();
                                        }
                                    });
                                }
                            }
                        });
                        Bundle params = new Bundle();
                        params.putString("fields", "email,first_name,last_name");
                        request.setParameters(params);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        loginFailure();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        loginFailure();
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
                        loginFailure();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
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

    private void loginFailure() {
        DataManager.getInstance(LogInActivity.this).logOut();
        Toast.makeText(LogInActivity.this, "Unable to log in.", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.INVISIBLE);
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
                    DataManager.getInstance(LogInActivity.this).setToken(token);
                    onBackPressed();
                }

                @Override
                public void failure(HttpResponseError error) {
                    loginFailure();
                }
            });


        } catch (ApiException e) {
            loginFailure();
        }
    }

    private boolean inputIsValid() {
        return emailEditText.getText().length() > 0 && passwordEditText.getText().length() > 0;
    }


}
