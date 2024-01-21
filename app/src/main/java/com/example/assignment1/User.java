package com.example.assignment1;
public class User {
    public static String user_id;
    public static String user_name;
    public static String class_name;
    public static String phone_no;
    public static String er_name;
    public static String er_relationship;

    public User(String user_id, String user_name, String class_name, String phone_no, String er_name, String er_relationship) {
        User.user_id = user_id;
        User.user_name = user_name;
        User.class_name = class_name;
        User.phone_no = phone_no;
        User.er_name = er_name;
        User.er_relationship = er_relationship;
    }
}
