package com.example.taskflow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.taskflow.R;
import com.example.taskflow.enums.StateEnum;
import com.example.taskflow.models.TaskModel;

import java.util.List;

public class TaskListViewAdapter extends ArrayAdapter<TaskModel> {

    private final Context context;
    private final List<TaskModel> tasks;

    public TaskListViewAdapter(@NonNull Context context, List<TaskModel> tasks) {
        super(context,0, tasks);
        this.context=context;
        this.tasks=tasks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.task_view, parent, false);
        }

        TaskModel task = tasks.get(position);

        TextView textView = convertView.findViewById(R.id.taskName);
        Spinner spinner = convertView.findViewById(R.id.taskStatesSpinner);

        textView.setText(task.getName());

        ArrayAdapter<StateEnum> spinnerAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item,StateEnum.values());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(task.getState().ordinal());

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               StateEnum state = (StateEnum) adapterView.getItemAtPosition(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return convertView;
    }
}
