package com.example.chatterly.model.data;

import java.util.List;

public class StringListWrapper {
    private List<String> userIds;

    public StringListWrapper(List<String> userIds) {
        this.userIds = userIds;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
