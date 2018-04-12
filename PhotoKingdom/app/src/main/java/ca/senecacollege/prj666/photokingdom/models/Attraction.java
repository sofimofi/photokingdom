package ca.senecacollege.prj666.photokingdom.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Model class for an attraction
 *
 * @author Wonho
 */

public class Attraction {
    @SerializedName("Id")
    private int id;
    @SerializedName("googlePlaceId")
    private String googlePlaceId;
    @SerializedName("Name")
    private String name;
    @SerializedName("Lat")
    private double lat;
    @SerializedName("Lng")
    private double lng;
    @SerializedName("IsActive")
    private int isActive;
    @SerializedName("CityId")
    private int cityId;
    @SerializedName("PhotoImagePath")
    private String photoImagePath;
    @SerializedName("OwnerName")
    private String ownerName;
    // for adding an attraction
    @SerializedName("CityName")
    private String cityName;
    @SerializedName("CountryName")
    private String countryName;
    @SerializedName("CurrentAttractionPhotowarUploadsCount")
    private int currentAttractionPhotowarUploadsCount;

    public Attraction() {}

    public Attraction(int id, String googlePlaceId, String name, double lat, double lng, int isActive, int cityId){
        this.id = id;
        this.googlePlaceId = googlePlaceId;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.isActive = isActive;
        this.cityId = cityId;
    }

    // constructor for adding attraction
    public Attraction(String googlePlaceId, String name, double lat, double lng, String cityName, String countryName){
        this.googlePlaceId = googlePlaceId;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.cityName = cityName;
        this.countryName = countryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGooglePlaceId() {
        return googlePlaceId;
    }

    public void setGooglePlaceId(String googlePlaceId) {
        this.googlePlaceId = googlePlaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getPhotoImagePath() {
        return photoImagePath;
    }

    public void setPhotoImagePath(String photoImagePath) {
        this.photoImagePath = photoImagePath;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getCurrentAttractionPhotowarUploadsCount(){
        return currentAttractionPhotowarUploadsCount;
    }
}