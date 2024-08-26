package com.example.taskflow.models;

import com.example.taskflow.enums.StateEnum;

import java.util.Objects;
import java.util.Set;

public class TaskModel {

    private long id;
    private String name;
    private StateEnum state;
    private long personId;
    private Set<SubtaskModel> subtasks;

    public TaskModel(long id, String name, StateEnum state, long personId, Set<SubtaskModel> subtasks) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.personId = personId;
        this.subtasks = subtasks;
    }

    public TaskModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StateEnum getState() {
        return state;
    }

    public void setState(StateEnum state) {
        this.state = state;
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public Set<SubtaskModel> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(Set<SubtaskModel> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskModel taskModel = (TaskModel) o;
        return id == taskModel.id && personId == taskModel.personId && Objects.equals(name, taskModel.name) && state == taskModel.state && Objects.equals(subtasks, taskModel.subtasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, state, personId, subtasks);
    }

    @Override
    public String toString() {
        return "TaskModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", state=" + state +
                ", personId=" + personId +
                ", subtasks=" + subtasks +
                '}';
    }
}
