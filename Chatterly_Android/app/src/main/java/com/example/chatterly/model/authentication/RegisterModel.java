package com.example.chatterly.model.authentication;

public class RegisterModel {
    private String firstName, lastName, email, username, password;
    private Boolean gender;

    public RegisterModel(
            String firstName,
            String lastName,
            Boolean gender,
            String email,
            String username,
            String password
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getGender() {
        return this.gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "firstName: " + getFirstName()
                + ", lastName: " + getLastName()
                + ", gender: " + getGender()
                + ", email: " + getEmail()
                + ", username: " + getUsername()
                + ", password: " + getPassword();
    }
}
