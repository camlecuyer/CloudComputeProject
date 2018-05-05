package com.sourcey.clouldcomputing;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.input_name) EditText nameInput;
    @BindView(R.id.input_email) EditText emailInput;
    @BindView(R.id.input_mobile) EditText mobileInput;
    @BindView(R.id.btn_toreport) Button reportButton;
    @BindView(R.id.link_login) TextView loginLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Login activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signIn() {
        Log.d(TAG, "SignIn");

        if (!validate()) {
            Toast.makeText(getBaseContext(), "Sign in failed, please fill out all fields", Toast.LENGTH_LONG);
            return;
        }

        String email = emailInput.getText().toString();
        String mobile = mobileInput.getText().toString();
        String name = nameInput.getText().toString();

        Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
        intent.putExtra("EXTRA_EMAIL", email);
        intent.putExtra("EXTRA_MOBILE", mobile);
        intent.putExtra("EXTRA_NAME", name);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailInput.getText().toString();
        String mobile = mobileInput.getText().toString();
        String name = nameInput.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Enter a valid email address");
            valid = false;
        }
        else {
            emailInput.setError(null);
        }

        if (mobile.isEmpty()){
            mobileInput.setError("Please enter a phone number");
            valid = false;
        }
        else {
            mobileInput.setError(null);
        }

        if (name.isEmpty()){
            nameInput.setError("Please enter a name");
            valid = false;
        }
        else {
            nameInput.setError(null);
        }

        return valid;
    }
}
