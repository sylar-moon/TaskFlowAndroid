package com.example.taskflow.dto;

import java.util.Objects;

public class AddNewSubtaskDto {
    Long taskId;
    String subtaskName;

    public AddNewSubtaskDto(Long taskId, String subtaskName) {
        this.taskId = taskId;
        this.subtaskName = subtaskName;
    }

    public AddNewSubtaskDto() {

    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getSubtaskName() {
        return subtaskName;
    }

    public void setSubtaskName(String subtaskName) {
        this.subtaskName = subtaskName;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddNewSubtaskDto that = (AddNewSubtaskDto) o;
        return Objects.equals(taskId, that.taskId) && Objects.equals(subtaskName, that.subtaskName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, subtaskName);
    }
}
