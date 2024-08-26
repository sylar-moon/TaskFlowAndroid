package com.example.taskflow.dto;

import java.util.Objects;

public class NameTaskDto {
    String name;

    public NameTaskDto(String name) {
        this.name = name;
    }

    public NameTaskDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NameTaskDto that = (NameTaskDto) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
