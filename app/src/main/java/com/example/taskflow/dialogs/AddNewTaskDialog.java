package com.example.taskflow.dialogs;

import static java.lang.System.out;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskflow.MainActivity;
import com.example.taskflow.R;
import com.example.taskflow.api.TaskApi;
import com.example.taskflow.dto.NameTaskDto;
import com.example.taskflow.enums.SortedTaskEnum;
import com.example.taskflow.models.TaskModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewTaskDialog {
    private FloatingActionButton addNewTask;
   private final MainActivity mainActivity;
   private final TaskApi taskApi;
   private final SortedTaskEnum sortedTask;

    public AddNewTaskDialog(MainActivity mainActivity, TaskApi taskApi, SortedTaskEnum sortedTask) {
        this.mainActivity = mainActivity;
        this.taskApi = taskApi;
        this.sortedTask = sortedTask;
    }

    public void show() {
        addNewTask = mainActivity.findViewById(R.id.addNewTaskButton);
        addNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                LayoutInflater inflater = mainActivity.getLayoutInflater();

                View dialogView = inflater.inflate(R.layout.task_dialog_view, null);
                builder.setView(dialogView);

                EditText setNameNewTask = dialogView.findViewById(R.id.nameNewTask);
                Button addTaskButton = dialogView.findViewById(R.id.addTaskButton);
                Button closeDialog = dialogView.findViewById(R.id.closeDialogButton);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                addTaskButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        out.println("add new task start");
                        out.println(setNameNewTask.getText().toString() + ": text from editText");


                        Call<TaskModel> taskModelCall = taskApi.addNewTask(new NameTaskDto(setNameNewTask.getText().toString()));
                        taskModelCall.enqueue(new Callback<TaskModel>() {
                            @Override
                            public void onResponse(Call<TaskModel> call, Response<TaskModel> response) {
                                mainActivity.getAllMyTasks(sortedTask);
                                setNameNewTask.setText("");
                            }

                            @Override
                            public void onFailure(Call<TaskModel> call, Throwable t) {
                                out.println(t.fillInStackTrace().getMessage());
                                out.println(call.request());
                                Toast.makeText(mainActivity, "Запрост на создание задачи не выполнен " + t.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });

                closeDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertDialog.dismiss();

                    }
                });
            }


        });
    }
}
