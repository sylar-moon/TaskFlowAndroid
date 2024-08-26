package com.example.taskflow.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.taskflow.R;
import com.example.taskflow.RetrofitClient;
import com.example.taskflow.dto.AddNewSubtaskDto;
import com.example.taskflow.dto.NameTaskDto;
import com.example.taskflow.enums.StateEnum;
import com.example.taskflow.api.SubtaskApi;
import com.example.taskflow.api.TaskApi;
import com.example.taskflow.interfece.TaskUpdateListener;
import com.example.taskflow.models.SubtaskModel;
import com.example.taskflow.models.TaskModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskListViewAdapter extends ArrayAdapter<TaskModel> {

    private final Context context;
    private final List<TaskModel> tasks;

    private final TaskApi taskApi;
    private final SubtaskApi subtaskApi;

    private final TaskUpdateListener taskUpdateListener;


    public TaskListViewAdapter(@NonNull Context context, List<TaskModel> tasks, TaskUpdateListener taskUpdateListener) {
        super(context, 0, tasks);
        this.context = context;
        this.tasks = tasks;
        taskApi = RetrofitClient.getRetrofitInstance().create(TaskApi.class);
        subtaskApi = RetrofitClient.getRetrofitInstance().create(SubtaskApi.class);
        this.taskUpdateListener = taskUpdateListener;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.task_view, parent, false);
        }


        TaskModel task = tasks.get(position);

        TextView textView = convertView.findViewById(R.id.taskName);
        textView.setText(task.getName());


        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                System.out.println("long click to task");
                showPopupMenu(view, task);
                return false;
            }
        });

        Spinner spinner = initSpinner(convertView, task);

        changeTaskState(spinner, task);


        return convertView;
    }


    private void showPopupMenu(View view, TaskModel task) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {

            if (item.getItemId() == R.id.action_delete) {
                System.out.println("task delete id = " + task.getId());
                Call<TaskModel> taskModelCall = taskApi.deleteTaskById(task.getId());
                executeRequest(taskModelCall, "Запрост на удаление задачи не выполнен ");

                return true;
            } else if (item.getItemId() == R.id.action_edit) {
                System.out.println("task edit id = " + task.getId());
                editTaskNameFromDialog(task.getId());
                return true;
            }else if (item.getItemId() == R.id.action_subtasks) {
                System.out.println("subtasks from task id = " + task.getId());
                showSubtaskDialog(task);
                return true;
            }

            return false;
        });
        popupMenu.show();
    }

    private void showSubtaskDialog( TaskModel task) {
        Set<SubtaskModel> subtasks = task.getSubtasks();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_sub_task_view, null);
        builder.setView(dialogView);

        ListView listSubtasks = dialogView.findViewById(R.id.listSubtasks);
        Button createSubtask = dialogView.findViewById(R.id.addSubtaskButton);
        Button closeButton = dialogView.findViewById(R.id.closeDialogButton);

        SubtaskListViewAdapter adapter = new SubtaskListViewAdapter(context, new ArrayList<>(subtasks));
        listSubtasks.setAdapter(adapter);


        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        createSubtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);

                View dialogView = inflater.inflate(R.layout.task_dialog_view, null);
                builder.setView(dialogView);

                TextView nameDialog = dialogView.findViewById(R.id.nameDialogView);
                EditText newSubtaskName = dialogView.findViewById(R.id.nameNewTask);
                Button createNewSubtaskButton = dialogView.findViewById(R.id.addTaskButton);
                Button closeDialog = dialogView.findViewById(R.id.closeDialogButton);

                nameDialog.setText("Создать новую подзадачу");
                createNewSubtaskButton.setText("Создать");
                newSubtaskName.setHint("Введите название новой подзадачи");



                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                createNewSubtaskButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newName = newSubtaskName.getText().toString();
                        if (!newName.isEmpty()) {
                            Call<TaskModel> taskModelCall = subtaskApi.createNewSubtask(new AddNewSubtaskDto(task.getId(),newName));
                            executeRequest(taskModelCall, "Запрост на создание подзадачи не выполнен ");
                            subtasks.add(new SubtaskModel(newName));
                            notifyDataSetChanged();
                            alertDialog.dismiss();

                        }
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

    private void editTaskNameFromDialog(long taskId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        View dialogView = inflater.inflate(R.layout.task_dialog_view, null);
        builder.setView(dialogView);

        TextView nameDialog = dialogView.findViewById(R.id.nameDialogView);
        EditText setNewNameTask = dialogView.findViewById(R.id.nameNewTask);
        Button editTaskButton = dialogView.findViewById(R.id.addTaskButton);
        Button closeDialog = dialogView.findViewById(R.id.closeDialogButton);

        nameDialog.setText("Изменить имя задачи");
        editTaskButton.setText("Изменить");


        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        editTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = setNewNameTask.getText().toString();
                if (!newName.isEmpty()) {
                    Call<TaskModel> taskModelCall = taskApi.editNameTaskState(taskId, new NameTaskDto(newName));
                    executeRequest(taskModelCall, "Запрост на изменение имени задачи не выполнен ");

                    alertDialog.dismiss();

                }
            }
        });


        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

            }
        });


    }


    private @NonNull Spinner initSpinner(@NonNull View convertView, TaskModel task) {
        Spinner spinner = convertView.findViewById(R.id.taskStatesSpinner);
        ArrayAdapter<StateEnum> spinnerAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, StateEnum.values());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(task.getState().ordinal());
        return spinner;
    }

    private void changeTaskState(Spinner spinner, TaskModel task) {
        spinner.setTag(false); // Используем Tag для хранения состояния инициализации

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if ((Boolean) spinner.getTag()) {
                    StateEnum state = (StateEnum) adapterView.getItemAtPosition(i);

                    Call<TaskModel> taskModelCall = taskApi.updateTaskState(task.getId(), state.name());

                    executeRequest(taskModelCall, "Запрост на изменение статуса задачи не выполнен ");

                } else {
                    spinner.setTag(true); // Устанавливаем флаг на true после первичной инициализации
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void executeRequest(Call<TaskModel> taskModelCall, String message) {
        taskModelCall.enqueue(new Callback<TaskModel>() {
            @Override
            public void onResponse(Call<TaskModel> call, Response<TaskModel> response) {
                System.out.println(call.request()+ " is run");
                if (response.isSuccessful() && response.body() != null) {
                    taskUpdateListener.onTaskUpdated();
                }
            }

            @Override
            public void onFailure(Call<TaskModel> call, Throwable t) {
                System.out.println(t.fillInStackTrace().getMessage());
                System.out.println(call.request());
                Toast.makeText(context, message + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void updateTasks(List<TaskModel> newTasks) {
        System.out.println("adapter is update");
        tasks.clear();
        tasks.addAll(newTasks);
        notifyDataSetChanged();
    }

    public List<TaskModel> getTasks() {
        return tasks;
    }

}
