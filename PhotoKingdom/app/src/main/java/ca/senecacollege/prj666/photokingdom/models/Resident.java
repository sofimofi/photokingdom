package ca.senecacollege.prj666.photokingdom.models;

import com.google.gson.annotations.SerializedName;

/**
 * Model class for a resident
 */
public class Resident {
    @SerializedName("UserName")
    private String userName;
    @SerializedName("Gender")
    private String gender;
    @SerializedName("Email")
    private String email;
    @SerializedName("Password")
    private String password;
    @SerializedName("IsActive")
    private int isActive;
    @SerializedName("AvatarImagePath")
    private String avatarImagePath;
    @SerializedName("CityId")
    private int cityId;
    @SerializedName("CityName")
    private String city;
    @SerializedName("Title")
    private String title;

    public Resident(String userName, String email, String password, String gender, int cityId) {
        this.userName = userName;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.cityId = cityId;
        this.isActive = 1;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getAvatarImagePath() {
        return avatarImagePath;
    }

    public void setAvatarImagePath(String avatarImagePath) {
        this.avatarImagePath = avatarImagePath;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) { this.city = city; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { this.title = title; }
}
