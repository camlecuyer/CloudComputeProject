package com.sourcey.clouldcomputing;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TaskActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    static final String URL =  "http://18.188.150.86/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
    }

    void populateTasks()
    {
        String url = URL + "";//"post_issue.php";

        StringRequest strRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        //loginButton.setEnabled(true);
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
}
