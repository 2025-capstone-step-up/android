package com.example.capston3;

public class InfoItem {
    private String title;
    private String description;

    // 기본 생성자 (Retrofit용)
    public InfoItem() {}

    public InfoItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
