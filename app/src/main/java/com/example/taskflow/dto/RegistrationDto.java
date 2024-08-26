package com.example.taskflow.dto;

import java.util.Objects;

public class RegistrationDto {

    private String userName;
    private String email;
    private String password;
    private String userPic;

    public RegistrationDto(String userName, String email, String password, String userPic) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.userPic = userPic;
    }

    public RegistrationDto() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationDto that = (RegistrationDto) o;
        return Objects.equals(userName, that.userName) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(userPic, that.userPic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, email, password, userPic);
    }
}




