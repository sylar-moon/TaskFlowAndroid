package com.example.taskflow;

import static java.lang.System.out;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
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
import com.example.taskflow.dialogs.AddNewTaskDialog;
import com.example.taskflow.dialogs.AuthDialog;
import com.example.taskflow.dialogs.TaskFlowButtonDialog;
import com.example.taskflow.dto.NameTaskDto;
import com.example.taskflow.enums.SortedTaskEnum;
import com.example.taskflow.api.AuthApi;
import com.example.taskflow.api.TaskApi;
import com.example.taskflow.interfece.TaskUpdateListener;
import com.example.taskflow.models.TaskModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements TaskUpdateListener {

    private final List<TaskModel> newTasks = new ArrayList<>();
    private final List<TaskModel> inProgressTasks = new ArrayList<>();
    private final List<TaskModel> completedTasks = new ArrayList<>();
    private final List<TaskModel> closedTasks = new ArrayList<>();

    private List<TaskListViewAdapter> adapterList;

    private TaskApi taskApi;
    private AuthApi authApi;

    private Spinner sortedSpinner;
    public SortedTaskEnum sortedTask = SortedTaskEnum.NAME_UP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("RedirectActivity", "RedirectActivity started");

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new TaskFlowButtonDialog(MainActivity.this, sortedTask).show();

        initSortedSpinner();
        getSpinnerSortedTask();
        initApi();
        new AddNewTaskDialog(MainActivity.this, taskApi, sortedTask).show();

        initTaskAdapters();
        setTaskRecycle();

        getAllMyTasks(sortedTask);

        getData();

    }

    private void getData() {
        Uri uri = getIntent().getData();
        if (uri != null && uri.getQueryParameter("code") != null) {

            out.println("auth uri" + uri);
        }
    }

    public void initApi() {
        RetrofitClient.initialize(this);
        taskApi = RetrofitClient.getRetrofitInstance().create(TaskApi.class);
        authApi = RetrofitClient.getRetrofitInstance().create(AuthApi.class);
    }


    private void getSpinnerSortedTask() {

        sortedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SortedTaskEnum sort = (SortedTaskEnum) adapterView.getItemAtPosition(i);
                out.println("sorted task: " + sort);
                sortedTask = sort;
                getAllMyTasks(sortedTask);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void initSortedSpinner() {
        sortedSpinner = findViewById(R.id.taskSortedSpinner);

        ArrayAdapter<SortedTaskEnum> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, SortedTaskEnum.values());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortedSpinner.setAdapter(spinnerAdapter);
        sortedSpinner.setSelection(SortedTaskEnum.NAME_UP.ordinal());

    }

    private void setTaskRecycle() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        RecyclerView recyclerTaskView = findViewById(R.id.recyclerTasksView);
        recyclerTaskView.setLayoutManager(layoutManager);
        TasksRecycleAdapter tasksRecycleAdapter = new TasksRecycleAdapter(this, adapterList);
        recyclerTaskView.setAdapter(tasksRecycleAdapter);
    }


    public void getAllMyTasks(SortedTaskEnum sorted) {


        Call<List<TaskModel>> taskModelCall = taskApi.getTasks(sorted.name());
        taskModelCall.enqueue(new Callback<List<TaskModel>>() {
            @Override
            public void onResponse(Call<List<TaskModel>> call, Response<List<TaskModel>> response) {

                if (response.code() == 401) {
//                    RetrofitClient.updateToken(logoutToken);
                    new AuthDialog(authApi, sortedTask, MainActivity.this).show();
                }


                out.println("get my task start");
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

                    }
                }
                notifyAdapters();

            }

            @Override
            public void onFailure(Call<List<TaskModel>> call, Throwable t) {
                out.println(t.fillInStackTrace().getMessage());
                out.println(call.request());
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
        adapterList.add(new TaskListViewAdapter(this, newTasks, this));
        adapterList.add(new TaskListViewAdapter(this, inProgressTasks, this));
        adapterList.add(new TaskListViewAdapter(this, completedTasks, this));
        adapterList.add(new TaskListViewAdapter(this, closedTasks, this));
    }

    @Override
    public void onTaskUpdated() {
        getAllMyTasks(sortedTask);
    }
}