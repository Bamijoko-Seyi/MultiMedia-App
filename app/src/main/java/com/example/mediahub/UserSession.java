package com.example.mediahub;

import java.util.ArrayList;
import java.util.List;

public class UserSession {
    private static UserSession instance;
    //Additional values can be added
    private String userId;
    private List<String> categories;

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public List<String> getCategories() {
        return categories;
    }
    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

}
