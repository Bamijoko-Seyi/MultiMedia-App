package com.example.mediahub;

public class Category {
    private String title;
    private String description;
    private Boolean selected;

    public Category(String title, String description, Boolean selected) {
        this.title = title;
        this.description = description;
        this.selected = selected;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSelected() { return selected; }

    public void setSelected(boolean selected) { this.selected = selected; }

}
