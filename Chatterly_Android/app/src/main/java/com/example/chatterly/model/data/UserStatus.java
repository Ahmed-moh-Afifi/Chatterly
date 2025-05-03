package com.example.chatterly.model.data;

import java.time.LocalDateTime;

public class UserStatus {
    private final String status;
    private final LocalDateTime lastOnline;

    public UserStatus(String status, LocalDateTime lastOnline) {
        this.status = status;
        this.lastOnline = lastOnline;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getLastOnline() {
        return lastOnline;
    }
}