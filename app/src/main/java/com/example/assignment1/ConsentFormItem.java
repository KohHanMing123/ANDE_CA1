package com.example.assignment1;

public class ConsentFormItem {
    private String title, content, dateCreated, issuedBy;

    public ConsentFormItem(String title, String content, String dateCreated, String issuedBy) {
        this.title = title;
        this.content = content;
        this.dateCreated = dateCreated;
        this.issuedBy = issuedBy;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getIssuedBy() {
        return issuedBy;
    }
}
