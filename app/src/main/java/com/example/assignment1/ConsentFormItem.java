package com.example.assignment1;

public class ConsentFormItem {
    private static String selectedFormTitle;
    private String content, dateCreated, issuedBy, title;

    private boolean isConsented;



    public ConsentFormItem(String title, String content, String dateCreated, String issuedBy, boolean isConsented) {
        this.title = title;
        this.content = content;
        this.dateCreated = dateCreated;
        this.issuedBy = issuedBy;
        this.isConsented = isConsented;
    }
//    public ConsentFormItem(String selectedFormTitle){
//       this()
//    }1


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

    public boolean getIsConsented(){return isConsented; }
}
