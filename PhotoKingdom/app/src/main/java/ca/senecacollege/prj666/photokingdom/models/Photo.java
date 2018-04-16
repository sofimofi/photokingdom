package ca.senecacollege.prj666.photokingdom.models;

import com.google.gson.annotations.SerializedName;

/**
 * Model class for Photo Album
 * @author Zhihao
 */
public class Photo {
    @SerializedName("Id")
    private int id;
    @SerializedName("PhotoFilePath")
    private String photoFilePath;
    @SerializedName("Lat")
    private Double lat;
    @SerializedName("Lng")
    private Double lng;
    @SerializedName("ResidentId")
    private int residentId;

    private String title;

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setImage_title(String title) {
        this.title = title;
    }

    public String getPhotoFilePath() {
        return photoFilePath;
    }

    public void setPhotoFilePath(String photoFilePath) {
        this.photoFilePath = photoFilePath;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public int getResidentId() {
        return residentId;
    }

    public void setResidentId(int residentId) {
        this.residentId = residentId;
    }
}
