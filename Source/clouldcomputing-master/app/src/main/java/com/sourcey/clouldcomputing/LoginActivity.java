package com.sourcey.clouldcomputing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

// Page for Report Sign im
public class LoginActivity extends AppCompatActivity
{
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

        // If sign in button clicked call signIn
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        // Move to Employee Login page
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
    } // end onCreate

    public void signIn()
    {
        // Validates fields
        if (!validate())
        {
            Toast.makeText(getBaseContext(), "Sign in failed, please fill out all fields", Toast.LENGTH_LONG).show();
            return;
        }

        String email = emailInput.getText().toString();
        String mobile = mobileInput.getText().toString();
        String name = nameInput.getText().toString();

        // Adds extra data to intent call
        Intent intent = new Intent(getApplicationContext(), ReportActivity.class);
        intent.putExtra("EXTRA_EMAIL", email);
        intent.putExtra("EXTRA_MOBILE", mobile);
        intent.putExtra("EXTRA_NAME", name);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    } // end signIn

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    } // end onBackPressed

    // Validates fields
    private boolean validate() {
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
    } // end validate
} // end LoginActivity
