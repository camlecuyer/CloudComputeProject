package com.sourcey.clouldcomputing;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

public class TaskActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    static final String URL =  "http://18.219.51.47/";
    private Context appContext;
    private ArrayList<TaskData> tasks;
    private TaskData selectedTask;
    //holds the position of selected item
    private int curPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);

        ListView lv = findViewById(R.id.task_lst);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // sets the curPosition of selectedItem
                curPosition = position;

                // saves the selectedItem info
                selectedTask = (TaskData) parent.getItemAtPosition(position);
            }
        });

        appContext = this;

        populateTasks();
    } // end onCreate

    void populateTasks()
    {
        String url = URL + "get_tasks.php";
        tasks = new ArrayList<>();
        curPosition = -1;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            if(response.getString("success").equals("1"))
                            {
                                ListView lv = findViewById(R.id.task_lst);
                                JSONArray res = response.getJSONArray("results");
                                JSONArray task = res.getJSONObject(0).getJSONArray("tasks");

                                for (int i = 0; i < task.length(); i++) {
                                    JSONObject item = task.getJSONObject(i);
                                    TaskData temp = new TaskData(item.getString("TaskID"), item.getString("DivisionID"), item.getString("BuildingID"), item.getString("IssueID"),
                                            item.getString("TaskDesc"), item.getString("TaskDateOpened"), item.getString("TaskFloorNumber"), item.getString("TaskLocation"));

                                    tasks.add(temp);

                                    lv.setAdapter(new TaskAdapter(appContext, tasks));
                                } // end loop
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
    } // end populate tasks

    void closeTask(View v) {

        ListView lv = findViewById(R.id.task_lst);

        if(curPosition == -1) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Please select a task to close");
            alertDialogBuilder.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });

            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }

        pDialog = new ProgressDialog(TaskActivity.this, R.style.AppTheme);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Closing Task...");
        pDialog.show();

        String url = URL + "put_task.php";

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
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        populateTasks();
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
                params.put("taskid", selectedTask.getTaskID());

                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(strRequest);
    }
}
