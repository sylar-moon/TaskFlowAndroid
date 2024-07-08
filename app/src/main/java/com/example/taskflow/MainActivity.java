package com.example.taskflow;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskflow.adapter.TaskListViewAdapter;
import com.example.taskflow.adapter.TasksRecycleAdapter;
import com.example.taskflow.interfaces.TaskApi;
import com.example.taskflow.models.TaskModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private final List<TaskModel> newTasks = new ArrayList<>();
    private final List<TaskModel> inProgressTasks = new ArrayList<>();
    private final List<TaskModel> completedTasks = new ArrayList<>();
    private final List<TaskModel> closedTasks = new ArrayList<>();

    private List<ArrayAdapter<TaskModel>> adapterList;

    private TaskApi taskApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        taskApi = RetrofitClient.getRetrofitInstance().create(TaskApi.class);

        initTaskAdapters();
        setTaskRecycle();

        getAllMyTasks();


    }

    private void setTaskRecycle() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        RecyclerView recyclerTaskView = findViewById(R.id.recyclerTasksView);
        recyclerTaskView.setLayoutManager(layoutManager);
        TasksRecycleAdapter tasksRecycleAdapter = new TasksRecycleAdapter(this, adapterList);
        recyclerTaskView.setAdapter(tasksRecycleAdapter);
    }


    public void getAllMyTasks() {


        Call<List<TaskModel>> taskModelCall = taskApi.getTasks();
        taskModelCall.enqueue(new Callback<List<TaskModel>>() {
            @Override
            public void onResponse(Call<List<TaskModel>> call, Response<List<TaskModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    newTasks.clear();
                    inProgressTasks.clear();
                    completedTasks.clear();
                    closedTasks.clear();
                    for (TaskModel task : response.body()) {
                        switch (task.getState()) {
                            case NEW:
                                newTasks.add(task);
                                break;
                            case IN_PROGRESS:
                                inProgressTasks.add(task);
                                break;
                            case COMPLETED:
                                completedTasks.add(task);
                                break;
                            case CLOSED:
                                closedTasks.add(task);
                                break;
                        }
                        notifyAdapters();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<TaskModel>> call, Throwable t) {
                System.out.println(t.fillInStackTrace().getMessage());
                System.out.println(call.request());
                Toast.makeText(MainActivity.this, "Запрост на получение задач не выполнен " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void notifyAdapters() {
        for (ArrayAdapter<TaskModel> adapter : adapterList) {
            adapter.notifyDataSetChanged();
        }
    }

    private void initTaskAdapters() {
        adapterList = new ArrayList<>();
        adapterList.add(new TaskListViewAdapter(this,newTasks));
//        adapterList.add(new ArrayAdapter<>(this, R.layout.task_view, R.id.taskName, newTasks));
        adapterList.add(new ArrayAdapter<>(this, R.layout.task_view, R.id.taskName, inProgressTasks));
        adapterList.add(new ArrayAdapter<>(this, R.layout.task_view, R.id.taskName, completedTasks));
        adapterList.add(new ArrayAdapter<>(this, R.layout.task_view, R.id.taskName, closedTasks));
    }
}