package com.example.chatterly.model.authentication;

public class ResetPasswordModel extends LoginModel {
    String newPassword;

    public ResetPasswordModel(String username, String password, String newPassword) {
        super(username, password);
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String toString() {
        return super.toString() + ", newPassword: " + getNewPassword();
    }
}
