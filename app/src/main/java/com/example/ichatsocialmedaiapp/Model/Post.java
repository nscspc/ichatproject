package com.example.ichatsocialmedaiapp.Model;

public class Post {

    private String postId;
    private String postImage;
    private String postedBy;
    private String postDescription;
    private long postedAt;
    private int postLike;
    private int commentsCount;//name should be same as in the database.

    public Post(String postId, String postImage, String postedBy, String postDescription, long postedAt) {
        this.postId = postId;
        this.postImage = postImage;
        this.postedBy = postedBy;
        this.postDescription = postDescription;
        this.postedAt = postedAt;
    }

    public Post() {
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }

    public int getPostLike() {
        return postLike;
    }

    public void setPostLike(int postLike) {
        this.postLike = postLike;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentCount) {
        this.commentsCount = commentCount;
    }

    //    int profile,postImage,save;
//    String name,about,like,comment,share;
//
//    public HomeRVmodel(int profile, int postImage,int save, String name, String about, String like, String comment, String share) {
//        this.profile = profile;
//        this.postImage = postImage;
//        this.save=save;
//        this.name = name;
//        this.about = about;
//        this.like = like;
//        this.comment = comment;
//        this.share = share;
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
//    public int getPostImage() {
//        return postImage;
//    }
//
//    public void setPostImage(int postImage) {
//        this.postImage = postImage;
//    }
//
//    public int getSave() {
//        return save;
//    }
//
//    public void setSave(int save) {
//        this.save = save;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getAbout() {
//        return about;
//    }
//
//    public void setAbout(String about) {
//        this.about = about;
//    }
//
//    public String getLike() {
//        return like;
//    }
//
//    public void setLike(String like) {
//        this.like = like;
//    }
//
//    public String getComment() {
//        return comment;
//    }
//
//    public void setComment(String comment) {
//        this.comment = comment;
//    }
//
//    public String getShare() {
//        return share;
//    }
//
//    public void setShare(String share) {
//        this.share = share;
//    }
}
