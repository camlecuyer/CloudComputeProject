package com.sourcey.clouldcomputing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

// Page for Employee Login
public class SignupActivity extends AppCompatActivity
{
    private ProgressDialog pDialog;
    static final String URL =  "http://18.219.51.47/";

    @BindView(R.id.email) EditText emailInput;
    @BindView(R.id.password) EditText passwordInput;
    @BindView(R.id.btn_login) Button loginButton;
    @BindView(R.id.link_reporting) TextView reportLink;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        // Call login if Login button pressed
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // Move to Report Sign in page
        reportLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    } // end onCreate

    public void login()
    {
        // Validates data
        if (!validate())
        {
            Toast.makeText(getBaseContext(), "Login failed, please fill out all fields", Toast.LENGTH_LONG).show();
            return;
        } // end if

        loginButton.setEnabled(false);

        // Start progress dialog for Login REST call
        pDialog = new ProgressDialog(SignupActivity.this, R.style.AppTheme);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Authenticating...");
        pDialog.show();

        // Add correct function call to end of URL
        String url = URL + "get_user.php";

        // String request is used as JSONRequest does not accept parameters
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        // Dismiss dialog and attempt to retrieve message
                        pDialog.dismiss();
                        JSONObject temp;

                        try
                        {
                            temp = new JSONObject(response);

                            // If successful move to task page
                            if(temp.getString("success").equals("1"))
                            {
                                Intent intent = new Intent(getApplicationContext(), TaskActivity.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), temp.getString("message"), Toast.LENGTH_SHORT).show();
                                loginButton.setEnabled(true);
                            } // end if
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("pass", password);

                return params;
            }
        }; // end StringRequest

        MySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    } // end onBackPressed

    // Validates fields
    private boolean validate() {
        boolean valid = true;

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (email.isEmpty()) {
            emailInput.setError("Enter a username");
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
    } // end validate
} // end SignupActivity