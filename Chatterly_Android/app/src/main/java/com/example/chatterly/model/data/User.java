package com.example.chatterly.model.data;

import java.util.Date;

public class User {
    private String id, firstName, lastName, userName, email, photoUrl;
    private boolean gender, verified;
    private Date joinDate, lastOnline;
    private int onlineSessions;

    public User(String id, String firstName, String lastName, String userName, String email, String photoUrl, boolean gender, boolean verified, Date joinDate, Date lastOnline, int onlineSessions) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.photoUrl = photoUrl;
        this.gender = gender;
        this.verified = verified;
        this.joinDate = joinDate;
        this.lastOnline = lastOnline;
        this.onlineSessions = onlineSessions;
    }

    public int getOnlineSessions() {
        return onlineSessions;
    }

    public void setOnlineSessions(int onlineSessions) {
        this.onlineSessions = onlineSessions;
    }

    public Date getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(Date lastOnline) {
        this.lastOnline = lastOnline;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
