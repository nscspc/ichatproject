package com.example.ichatsocialmedaiapp.Model;

public class notificationModel {

    private String notificationBy,type,postID,postedBy;
    private long notificationAt;
    private boolean checkOpen;
    private String notificationID;

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public String getNotificationBy() {
        return notificationBy;
    }

    public void setNotificationBy(String notificationBy) {
        this.notificationBy = notificationBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public long getNotificationAt() {
        return notificationAt;
    }

    public void setNotificationAt(long notificationAt) {
        this.notificationAt = notificationAt;
    }

    public boolean isCheckOpen() {
        return checkOpen;
    }

    public void setCheckOpen(boolean checkOpen) {
        this.checkOpen = checkOpen;
    }
}

//    int profile;
//    String notificationdata,notificationtime;
//
//    public notificationModel(int profile, String notificationdata, String notificationtime) {
//        this.profile = profile;
//        this.notificationdata = notificationdata;
//        this.notificationtime = notificationtime;
//    }
//
//    public int getProfile() {
//        return profile;
//    }
//
//    public void setProfile(int profile) {
//        this.profile = profile;
//    }
//
//    public String getNotificationdata() {
//        return notificationdata;
//    }
//
//    public void setNotificationdata(String notificationdata) {
//        this.notificationdata = notificationdata;
//    }
//
//    public String getNotificationtime() {
//        return notificationtime;
//    }
//
//    public void setNotificationtime(String notificationtime) {
//        this.notificationtime = notificationtime;
//    }
//}
