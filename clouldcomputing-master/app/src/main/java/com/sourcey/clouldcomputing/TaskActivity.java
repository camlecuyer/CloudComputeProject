package com.sourcey.clouldcomputing;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

public class TaskActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    static final String URL =  "http://18.219.51.47/";
    private Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);

        appContext = this;

        populateTasks();
    }

    void populateTasks()
    {
        String url = URL + "get_tasks.php";
        //String url = URL + "get_closedtasks.php";
        //String url = URL + "get_issues.php";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            if(response.getString("success").equals("1"))
                            {
                                JSONArray res = response.getJSONArray("results");
                                //JSONArray cat = res.getJSONObject(0).getJSONArray("categories");
                                //JSONArray build = res.getJSONObject(0).getJSONArray("buildings");

                                //crrDataHolder temp = new crrDataHolder("Please select a category", "NULL", "");
                                //categories.add(temp);

                                /*for (int i = 0; i < cat.length(); i++) {
                                    JSONObject item = cat.getJSONObject(i);
                                    String id = item.getString("CategoryID");
                                    String name = item.getString("CatName");
                                    String desc = item.getString("CatDesc");

                                    //temp = new crrDataHolder(name, id, desc);

                                    //categories.add(temp);
                                } // end loop*/
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        } // end try/catch
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error");
                    }
                });

        MySingleton.getInstance(appContext).addToRequestQueue(jsObjRequest);

        /*StringRequest strRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        //loginButton.setEnabled(true);
                        //finish();
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

        MySingleton.getInstance(this).addToRequestQueue(strRequest);*/
    }
}
