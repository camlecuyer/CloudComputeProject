package com.sourcey.clouldcomputing;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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

import butterknife.BindView;

public class ReportActivity extends AppCompatActivity {
    // Progress Dialog
    private ProgressDialog pDialog;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final String URL =  "http://18.188.150.86/";

    private Context appContext;
    private Bitmap bitmap;
    private ImageView imageView;
    private String encodedImage;
    private String [] floors = {"0", "1", "2", "3", "4", "5"};
    private crrDataHolder categories;
    private crrDataHolder buildings;

    @BindView(R.id.cat_spn) Spinner catSpin;
    @BindView(R.id.build_spn) Spinner buildSpin;
    @BindView(R.id.floor_spn) Spinner floorSpin;
    @BindView(R.id.cat_txt) TextView catText;
    @BindView(R.id.desc_txt) TextView descText;
    @BindView(R.id.image_btn) Button imageButton;
    @BindView(R.id.submit_btn) Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        imageView = findViewById(R.id.imageHolder);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, floors);
        floorSpin.setAdapter(adapter);

        appContext = this;

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(appContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                {
                    ActivityCompat.requestPermissions((Activity)appContext, new String[] {Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
                }
                else {
                    takePic();
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    void getCategories()
    {
        String url = URL + "get_items.php";

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
    }

    void takePic() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    void submitReport(View v)
    {
        ProgressDialog progressDialog = new ProgressDialog(ReportActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Submitting...");
        progressDialog.show();

        String url = URL + "post_issue.php";

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        pDialog.dismiss();
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
        }
    }
}
