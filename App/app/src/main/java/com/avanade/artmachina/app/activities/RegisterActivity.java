package com.avanade.artmachina.app.activities;

import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText email, password, fname, lname, title, company;
    Button createButton, cancelButton;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(this, R.color.colorTextLight), PorterDuff.Mode.SRC_ATOP);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        title = findViewById(R.id.title);
        company = findViewById(R.id.company);

        progressBar = findViewById(R.id.progressBar);

        createButton = findViewById(R.id.create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isEmailValid(email.getText().toString())) {
                    resetEditText(email, "Email is invalid");
                    return;
                }

                if(password.getText().toString().length() < 7) {
                    resetEditText(password, "Password must have more that 6 characters");
                    return;
                }

                if(!isTextSafe(fname.getText().toString())) {
                    resetEditText(fname, "First name cannot contain special characters");
                    return;
                }

                if(!isTextSafe(lname.getText().toString())) {
                    resetEditText(lname, "Last name cannot contain special characters");
                    return;
                }

                if(fname.getText().toString().length() == 0) {
                    resetEditText(fname, "First name cannot be empty");
                    return;
                }

                if(lname.getText().toString().length() == 0) {
                    resetEditText(lname, "Last name cannot be empty");
                    return;
                }

                if(!isTextSafe(title.getText().toString())) {
                    resetEditText(title, "Title cannot contain special characters");
                    return;
                }

                if(!isTextSafe(company.getText().toString())) {
                    resetEditText(company, "company cannot contain special characters");
                    return;
                }

                User user = new User();
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());
                user.setFirstName(fname.getText().toString());
                user.setLastName(lname.getText().toString());
                if(title.getText().toString() != null) {
                    user.setTitle(title.getText().toString());
                }
                if(company.getText().toString() != null) {
                    user.setCompany(company.getText().toString());
                }
                progressBar.setVisibility(View.VISIBLE);
                DataManager.getInstance(RegisterActivity.this).registerUser(user, new DataProvider.AuthCompletion() {
                    @Override
                    public void complete(String token) {
                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        onBackPressed();
                    }

                    @Override
                    public void failure(HttpResponseError error) {
                        Toast.makeText(RegisterActivity.this, "Error registering", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
                user.setPassword("");
            }
        });

        cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void resetEditText(EditText editText, String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        editText.setText("");
        editText.requestFocus();
    }

    private static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static boolean isTextSafe(String text) {
        if(text.length() == 0) {
            return true;
        }
        String expression = "[^a-z0-9 ]";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        return !matcher.matches();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
