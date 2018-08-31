package io.kong.incheon.nfc_check.item;

public class UserItem {

    static int index;
    static String stid;
    static String stPass;
    static String user_university;
    static String user_name;
    static String user_major;
    static int user_grade;


    public static String getStid() {
        return stid;
    }

    public  void setStid(String stid) {
        this.stid = stid;
    }

    public static String getStPass() {
        return stPass;
    }

    public void setStPass(String stPass) {
        this.stPass = stPass;
    }

    public static String getUser_name() {
        return user_name;
    }

    public static void setUser_name(String user_name) {
        UserItem.user_name = user_name;
    }


    public static int getIndex() {
        return index;
    }

    public static void setIndex(int index) {
        UserItem.index = index;
    }

    public static String getUser_university() {
        return user_university;
    }

    public static void setUser_university(String user_university) {
        UserItem.user_university = user_university;
    }

    public static String getUser_major() {
        return user_major;
    }

    public static void setUser_major(String user_major) {
        UserItem.user_major = user_major;
    }

    public static int getUser_grade() {
        return user_grade;
    }

    public static void setUser_grade(int user_grade) {
        UserItem.user_grade = user_grade;
    }

}
