package com.example.taskflow.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class AppExceptionDto {
   private int status;
   private String message;
   private LocalDateTime timeStamp;

    public AppExceptionDto(int status, String message, LocalDateTime timeStamp) {
        this.status = status;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public AppExceptionDto() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppExceptionDto that = (AppExceptionDto) o;
        return status == that.status && Objects.equals(message, that.message) && Objects.equals(timeStamp, that.timeStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message, timeStamp);
    }
}
