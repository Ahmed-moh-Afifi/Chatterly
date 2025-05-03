package com.example.chatterly.model.authentication;

public class ResetPasswordTnModel {
    String email, token, newPassword;

    public ResetPasswordTnModel(String email, String token, String newPassword) {
        this.email = email;
        this.token = token;
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String toString() {
        return "email: " + getEmail() + ", token: " + getToken() + ", newPassword: " + getNewPassword();
    }
}
