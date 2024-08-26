package com.example.taskflow.dto;

import java.util.Objects;

public class SubtaskDto {
    Long id;
    Boolean status;

    public SubtaskDto(Long id, Boolean status) {
        this.id = id;
        this.status = status;
    }

    public SubtaskDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubtaskDto that = (SubtaskDto) o;
        return Objects.equals(id, that.id) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status);
    }
}
