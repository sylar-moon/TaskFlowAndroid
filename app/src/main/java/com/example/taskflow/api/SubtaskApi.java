package com.example.taskflow.api;

import com.example.taskflow.dto.AddNewSubtaskDto;
import com.example.taskflow.dto.SubtaskDto;
import com.example.taskflow.models.TaskModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SubtaskApi {

    @PATCH("subtask/{id}")
    Call<SubtaskDto> updateTaskState(@Path("id") long subtaskId);

    @POST ("subtask")
    Call<TaskModel> createNewSubtask (@Body AddNewSubtaskDto addSubtask);

}
