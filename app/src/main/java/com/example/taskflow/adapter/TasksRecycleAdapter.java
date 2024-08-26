package com.example.taskflow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskflow.R;
import com.example.taskflow.models.TaskModel;

import java.util.List;

public class TasksRecycleAdapter extends RecyclerView.Adapter<TasksRecycleAdapter.TaskViewHolder> {

    Context context;
    List<TaskListViewAdapter> arrayAdapters;

    public TasksRecycleAdapter(Context context, List<TaskListViewAdapter> arrayAdapters) {
        this.context = context;
        this.arrayAdapters = arrayAdapters;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View taskItems = LayoutInflater.from(context).inflate(R.layout.list_task_view, parent, false);
        return new TasksRecycleAdapter.TaskViewHolder(taskItems);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        ListView taskList = holder.tasksList;
        taskList.setAdapter(arrayAdapters.get(position));
        switch (position) {
            case 0:
                holder.nameList.setText("New task list");
                break;
            case 1:
                holder.nameList.setText("InProgres task list");
                break;
            case 2:
                holder.nameList.setText("Completed task list");
                break;
            case 3:
                holder.nameList.setText("Closed task list");
                break;
        }


    }

    @Override
    public int getItemCount() {
        return 4;
    }


    public static final class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView nameList;
        ListView tasksList;
        Spinner taskSpinner;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nameList = itemView.findViewById(R.id.nameListTasks);
            this.tasksList = itemView.findViewById(R.id.listTasks);
            this.taskSpinner = itemView.findViewById(R.id.taskStatesSpinner);
        }
    }

}
