package io.kong.incheon.nfc_check.item;

public class UserItem {
    static String stid;
    static String stPass;

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

}
