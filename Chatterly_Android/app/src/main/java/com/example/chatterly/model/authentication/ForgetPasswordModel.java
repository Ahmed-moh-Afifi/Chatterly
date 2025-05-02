package com.example.chatterly.model.authentication;

public class ForgetPasswordModel {
    private String email;

    public ForgetPasswordModel(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String toString() {
        return "email: " + getEmail();
    }
}
