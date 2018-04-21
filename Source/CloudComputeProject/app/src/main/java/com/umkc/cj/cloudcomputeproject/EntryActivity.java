package com.umkc.cj.cloudcomputeproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EntryActivity extends AppCompatActivity {
    // Progress Dialog
    private ProgressDialog pDialog;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private Context appContext;
    private Bitmap bitmap;
    private ImageView imageView;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        imageView = findViewById(R.id.imageHolder);

        appContext = this;

        // Loading products in Background Thread
        //new LoadAllProducts().execute();
    }

    void getCategories(View v)
    {
        sendPic();
    }

    void sendPic()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    /*private void uploadImage() {
        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EntryActivity.this, "Uploading Image", "Please wait...", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {

                HashMap<String, String> param = new HashMap<>();
                param.put("catid", "1");
                param.put("buildid", "1");
                param.put("photo", encodedImage);
                param.put("desc", "broken tile");
                param.put("floor", "2");
                param.put("email", "abc@123.com");
                param.put("name", "Bill");
                param.put("phone", "9135555555");
                param.put("loc", "2.58, 2.68");

                String url = "http://18.218.142.79/post_issue.php";

                String result = rh.sendPostRequest(url, param);

                System.out.println(result);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            //uploadImage();
        }

        String url = "http://18.188.150.86/post_issue.php";


        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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
                params.put("catid", "BLDNG");
                params.put("buildid", "AdmCnt");
                params.put("photo", encodedImage);
                params.put("desc", "broken tile");
                params.put("floor", "2");
                params.put("email", "abc@123.com");
                params.put("name", "Bill");
                params.put("phone", "9135555555");
                params.put("loc", "2.58, 2.68");

                return params;
            }
        };

        MySingleton.getInstance(appContext).addToRequestQueue(strRequest);
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
            String url ="http://18.188.150.86/get_items.php";

            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response)
                        {
                            try {
                                //JSONObject res = response.getJSONObject("results");
                                JSONArray jarray = response.getJSONArray("results");

                                /*for(int i = 0; i < jarray.length(); i++)
                                {
                                    JSONObject item = jarray.getJSONObject(i);
                                    String ID = item.getString("CategoryID");
                                    String brand = item.getString("CatName");

                                    System.out.println(ID + "," + brand);

                                } // end loop*/

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
