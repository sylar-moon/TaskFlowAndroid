package com.example.taskflow.interfaces;

import com.example.taskflow.models.TaskModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TaskApi {
    @GET("tasks/my?sortedTask=NAME_UP")
    Call<List<TaskModel>> getTasks();
}
