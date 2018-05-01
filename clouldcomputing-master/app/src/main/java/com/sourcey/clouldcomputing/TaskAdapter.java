package com.sourcey.clouldcomputing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter
{
    // holds context
    private final Context context;

    // holds list of values
    private final ArrayList<TaskData> values;

    // Constructor
    public TaskAdapter(Context context, ArrayList<TaskData> values)
    {
        super(context, R.layout.task_layout, values);
        this.context = context;
        this.values = values;
    } // end constructor

    // Sets items to correct view element
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // inflates layout
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // gets the layout
        View rowView = null;
        if (inflater != null) {
            rowView = inflater.inflate(R.layout.task_layout, parent, false);
        }

        // finds the correct element and assigns data
        TextView build = rowView.findViewById(R.id.txt_build);
        build.setText(values.get(position).getBuildID());

        TextView floor = rowView.findViewById(R.id.txt_floor);
        floor.setText(String.valueOf(values.get(position).getFloor()));

        TextView date = rowView.findViewById(R.id.txt_date);
        date.setText(String.valueOf(values.get(position).getDateOpened()));

        TextView desc = rowView.findViewById(R.id.txt_desc);
        desc.setText(String.valueOf(values.get(position).getDesc()));

        TextView div = rowView.findViewById(R.id.txt_div);
        div.setText(String.valueOf( values.get(position).getDivID()));

        TextView loc = rowView.findViewById(R.id.txt_loc);
        loc.setText(String.valueOf(values.get(position).getLocation()));

        // returns completed view
        return rowView;
    } // end getView
} // end TaskAdapter class
