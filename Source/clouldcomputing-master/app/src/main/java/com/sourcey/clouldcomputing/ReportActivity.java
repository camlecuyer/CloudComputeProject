package com.sourcey.clouldcomputing;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportActivity extends AppCompatActivity implements LocationListener{
    // Progress Dialog
    private ProgressDialog pDialog;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final String URL =  "http://18.219.51.47/";

    private Context appContext;
    private ImageView imageView;
    private String encodedImage;
    private String [] floors = {"Select a floor", "0", "1", "2", "3", "4", "5"};
    private ArrayList<crrDataHolder> categories = new ArrayList<>();
    private ArrayList<crrDataHolder> buildings = new ArrayList<>();
    private String name;
    private String email;
    private String phone;
    private boolean running = false; // flag to control itemSelected listener in listView
    private double lat = 0;
    private double lng = 0;

    @BindView(R.id.cat_spn) Spinner catSpin;
    @BindView(R.id.build_spn) Spinner buildSpin;
    @BindView(R.id.floor_spn) Spinner floorSpin;
    @BindView(R.id.cat_txt) TextView catText;
    @BindView(R.id.desc_txt) EditText descText;
    @BindView(R.id.image_btn) Button imageButton;
    @BindView(R.id.submit_btn) Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);

        imageView = findViewById(R.id.imageHolder);

        appContext = this;

        // gets Permission for location services
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        } // end if

        // Image button listener
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(appContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                {
                    ActivityCompat.requestPermissions((Activity)appContext, new String[] {Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
                }
                else
                {
                    takePic();
                }
            }
        });

        // Submit button listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                submitReport();
            }
        });

        // Gets the categories and building data
        getCategories();

        // Gets extras
        name = getIntent().getStringExtra("EXTRA_NAME");
        phone = getIntent().getStringExtra("EXTRA_MOBILE");
        email = getIntent().getStringExtra("EXTRA_EMAIL");

        running = true; // Flips boolean for listView listener
    } // end onCreate

    // Goes back to home page
    public void back(View v)
    {
        startActivity(new Intent(ReportActivity.this,LoginActivity.class));
        finish();
    } // end back

    // Gets data from server to populate categories and buildings
    void getCategories()
    {
        // Sets floor spinner data
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, floors);
        floorSpin.setAdapter(adapter);

        // Adds correct function call to URL
        String url = URL + "get_items.php";

        // Use JSON Request to retrieve data from server
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            JSONArray res = response.getJSONArray("results");
                            JSONArray cat = res.getJSONObject(0).getJSONArray("categories");
                            JSONArray build = res.getJSONObject(0).getJSONArray("buildings");

                            // Sets up empty field in spinner
                            crrDataHolder temp = new crrDataHolder("Please select a category", "NULL", "");
                            categories.add(temp);

                            // Loops through data to populate categories data
                            for (int i = 0; i < cat.length(); i++) {
                                JSONObject item = cat.getJSONObject(i);
                                String id = item.getString("CategoryID");
                                String name = item.getString("CatName");
                                String desc = item.getString("CatDesc");

                                temp = new crrDataHolder(name, id, desc);

                                categories.add(temp);
                            } // end loop

                            // Sets up empty field in spinner
                            temp = new crrDataHolder("Please select a Building", "NULL", "");
                            buildings.add(temp);

                            // Loops through data to populate buildings data
                            for(int i = 0; i < build.length(); i++)
                            {
                                JSONObject item = build.getJSONObject(i);
                                String id = item.getString("BuildingID");
                                String name = item.getString("BuildingName");

                                temp = new crrDataHolder(name, id, "");

                                buildings.add(temp);
                            } // end loop

                            // Adds data to spinner
                            ArrayAdapter<crrDataHolder> catAdapter = new ArrayAdapter<>(appContext, android.R.layout.simple_spinner_item, categories);
                            catSpin.setAdapter(catAdapter);

                            // Adds data to spinner
                            ArrayAdapter<crrDataHolder> buildAdapter = new ArrayAdapter<>(appContext, android.R.layout.simple_spinner_item, buildings);
                            buildSpin.setAdapter(buildAdapter);

                            // adds listener to spinner
                            catSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if(running)
                                    {
                                        catText.setText(categories.get(catSpin.getSelectedItemPosition()).getDescription());
                                    } // end if
                                }
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });

                            // adds listener to spinner
                            buildSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if(running)
                                    {
                                        if(buildings.get(buildSpin.getSelectedItemPosition()).getId().equals("Outsid"))
                                        {
                                            floorSpin.setEnabled(false);
                                            floorSpin.setBackgroundColor(getResources().getColor(R.color.monsoon));
                                        }
                                        else
                                        {
                                            floorSpin.setEnabled(true);
                                            floorSpin.setBackgroundColor(getResources().getColor(R.color.iron));
                                        } // end if
                                    } // end if
                                }
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                }
                            });
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
                }); // end JSONObjectRequest

        MySingleton.getInstance(appContext).addToRequestQueue(jsObjRequest);
    } // end getCategories

    void takePic()
    {
        getLocation();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } // end if
    } // end takePic

    // Validates fields
    private boolean validate()
    {
        boolean valid = true;

        String desc = descText.getText().toString().trim();

        if (desc.isEmpty())
        {
            descText.setError("Enter a description");
            valid = false;
        }
        else
        {
            descText.setError(null);
        } // end if

        if (encodedImage == null)
        {
            valid = false;
        } // end if

        if (catSpin.getSelectedItemId() == 0)
        {
            valid = false;
        } // end if

        if (buildSpin.getSelectedItemId() == 0)
        {
            valid = false;
        }
        else
        {
            if(!buildSpin.getSelectedItem().equals("Outside"))
            {
                if(floorSpin.getSelectedItemId() == 0)
                {
                    valid = false;
                } // end if
            } // end if
        } // end if

        return valid;
    } // end validate

    void submitReport()
    {
        if(!validate())
        {
            Toast.makeText(getApplicationContext(), "Please select a category, building, (and floor if necessary), fill out the description, and take a picture", Toast.LENGTH_LONG).show();
            return;
        } // end if

        pDialog = new ProgressDialog(ReportActivity.this, R.style.AppTheme);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Submitting...");
        pDialog.show();

        String url = URL + "post_issue.php";

        // Uses StringRequest as JSONRequest does not allow parameters
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        pDialog.dismiss();
                        try
                        {
                            JSONObject temp = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), temp.getString("message"), Toast.LENGTH_SHORT).show();

                            reset();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        } // end try/catch
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
                String desc = descText.getText().toString().trim();

                String catID = categories.get(catSpin.getSelectedItemPosition()).getId();
                String buildID = buildings.get(buildSpin.getSelectedItemPosition()).getId();
                String floor;

                if(!buildID.equals("Outsid"))
                {
                    floor = floorSpin.getSelectedItem().toString();
                }
                else
                {
                    floor = "0";
                } // end if

                Map<String, String> params = new HashMap<>();
                params.put("catid", catID);
                params.put("buildid", buildID);
                params.put("photo", encodedImage);
                params.put("desc", desc);
                params.put("floor", floor);
                params.put("email", email);
                params.put("name", name);
                params.put("phone", phone);
                params.put("loc", lat + "," + lng);

                return params;
            }
        }; // end StringRequest

        MySingleton.getInstance(appContext).addToRequestQueue(strRequest);
    } // end submitReport

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = null;

            if (extras != null)
            {
                bitmap = (Bitmap) extras.get("data");
            } // end if

            imageView.setImageBitmap(bitmap);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            if (bitmap != null)
            {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            } // end if

            byte[] imageBytes = baos.toByteArray();
            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } // end if
    } // end onActivityResult

    // Resets several fields to prevent multiple requests at once
    void reset()
    {
        encodedImage = null;
        imageView.setImageResource(android.R.color.transparent);
        descText.setText("");
    } // end reset

    // Location code found on https://www.androstock.com/tutorials/getting-current-location-latitude-longitude-country-android-android-studio.html
    // and https://stackoverflow.com/questions/2227292/how-to-get-latitude-and-longitude-of-the-mobile-device-in-android
    void getLocation() {
        LocationManager locationManager;
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (locationManager != null)
            {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                lng =  location.getLongitude();
                lat = location.getLatitude();
            }
            else
            {
                lng = 0;
                lat = 0;
            } // end if
        }
        catch(SecurityException e) {
            e.printStackTrace();
        } // end try/catch
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        //Toast.makeText(ReportActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }
}
