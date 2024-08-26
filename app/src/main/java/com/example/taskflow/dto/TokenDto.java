package com.example.taskflow.dto;

import java.util.Objects;

public class TokenDto {
    private String token;

    public TokenDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public TokenDto() {
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenDto tokenDto = (TokenDto) o;
        return Objects.equals(token, tokenDto.token);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(token);
    }
}
