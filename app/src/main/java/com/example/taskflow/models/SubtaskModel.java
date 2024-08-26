package com.example.taskflow.models;

import java.util.Objects;

public class SubtaskModel {
    private long id;

    private String name;

    private boolean complete;

    public SubtaskModel(long id, String name, boolean complete) {
        this.id = id;
        this.name = name;
        this.complete = complete;
    }

    public SubtaskModel(String name) {
        this.name = name;
    }

    public SubtaskModel() {
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

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubtaskModel that = (SubtaskModel) o;
        return id == that.id && complete == that.complete && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, complete);
    }
}
