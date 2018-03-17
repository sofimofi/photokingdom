package ca.senecacollege.prj666.photokingdom.models;

import com.google.gson.annotations.SerializedName;

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
}