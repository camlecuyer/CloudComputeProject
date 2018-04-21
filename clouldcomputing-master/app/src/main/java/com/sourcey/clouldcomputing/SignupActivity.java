package com.sourcey.clouldcomputing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private ProgressDialog pDialog;
    static final String URL =  "http://18.188.150.86/";

    @BindView(R.id.email) EditText emailInput;
    @BindView(R.id.password) EditText passwordInput;
    @BindView(R.id.btn_login) Button loginButton;
    @BindView(R.id.link_reporting) TextView reportLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        reportLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),TaskActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            Toast.makeText(getBaseContext(), "Login failed, please fill out all fields", Toast.LENGTH_LONG);
            return;
        }

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        loginButton.setEnabled(false);

        pDialog = new ProgressDialog(SignupActivity.this, R.style.AppTheme_Dark_Dialog);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Authenticating...");
        pDialog.show();

        String url = URL + "";//"post_issue.php";

        StringRequest strRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        loginButton.setEnabled(true);
                        finish();
                        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        //finish();
                        //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                //params.put("catid", "BLDNG");
                //params.put("buildid", "AdmCnt");

                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Enter a valid email address");
            valid = false;
        }
        else {
            emailInput.setError(null);
        }

        if (password.isEmpty()){
            passwordInput.setError("Please enter a password");
            valid = false;
        }
        else {
            passwordInput.setError(null);
        }

        return valid;
    }
}