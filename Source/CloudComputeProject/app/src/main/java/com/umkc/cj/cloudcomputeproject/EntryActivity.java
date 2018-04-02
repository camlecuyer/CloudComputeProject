package com.umkc.cj.cloudcomputeproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EntryActivity extends AppCompatActivity {
    // Progress Dialog
    private ProgressDialog pDialog;

    private Context appContext;

    // url to get all products list
    private static String url_all_products = "https://api.androidhive.info/android_connect/get_all_products.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        appContext = this;

        // Loading products in Background Thread
        new LoadAllProducts().execute();
    }

    void getCategories(View v)
    {

    }

    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EntryActivity.this);
            pDialog.setMessage("Loading products. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            String url ="http://13.58.74.12/get_categories.php";

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response)
                        {
                            try {
                                //JSONObject res = response.getJSONObject("result");
                                JSONArray jarray = response.getJSONArray("categories");

                                for(int i = 0; i < jarray.length(); i++)
                                {
                                    JSONObject item = jarray.getJSONObject(i);
                                    String ID = item.getString("CategoryID");
                                    String brand = item.getString("CatName");

                                    System.out.println(ID + "," + brand);

                                } // end loop

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
                            // TODO Auto-generated method stub
                            System.out.println("Error");
                        }
                    });

            MySingleton.getInstance(appContext).addToRequestQueue(jsObjRequest);

            return "";
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                }
            });

        }

    }
}
