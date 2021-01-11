package com.example.i4u_.Model;

public class UserModel {
    private String uid;
    public String fullName;
    private String userName;
    private String age;
    private String bio;
    private String contactNo;
    private String userEmail;
    private  String userPassword;
    private String userGender;
    private String ProfileUrl;
    public UserModel() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }



    public String getProfileUrl() {
        return ProfileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        ProfileUrl = profileUrl;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public UserModel(String uid, String fullName, String userName, String age, String contactNo, String userEmail, String userPassword, String userGender) {
        this.uid = uid;
        this.fullName = fullName;
        this.userName = userName;
        this.age = age;
        this.contactNo = contactNo;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userGender = userGender;
    }
}
