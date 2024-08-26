package com.example.taskflow.api;

import com.example.taskflow.dto.NameTaskDto;
import com.example.taskflow.models.TaskModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TaskApi {
    @GET("tasks/my")
    Call<List<TaskModel>> getTasks(@Query("sortedTask")String sortedTask);


    @PATCH("tasks/{id}/state")
    Call<TaskModel> updateTaskState(@Path("id") long taskId, @Query("taskState") String taskState);

    @PATCH("tasks/{id}/name")
    Call<TaskModel> editNameTaskState(@Path("id") long taskId, @Body NameTaskDto nameTask);


    @POST("tasks")
    Call<TaskModel> addNewTask(@Body NameTaskDto nameTask);


    @DELETE("tasks/{id}")
    Call<TaskModel> deleteTaskById(@Path("id") long taskId);

}
