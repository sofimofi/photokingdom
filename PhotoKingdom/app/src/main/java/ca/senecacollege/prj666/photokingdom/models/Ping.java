package ca.senecacollege.prj666.photokingdom.models;

import com.google.gson.annotations.SerializedName;

/**
 * Model class for a ping
 *
 * @author Wonho
 */
public class Ping {
    @SerializedName("Id")
    private int id;
    @SerializedName("PingDate")
    private String pingDate;
    @SerializedName("ExpiryDate")
    private String expiryDate;
    @SerializedName("ResidentId")
    private int residentId;
    @SerializedName("AttractionName")
    private String attractionName;
    @SerializedName("AttractionGooglePlaceId")
    private String placeId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPingDate() {
        return pingDate;
    }

    public void setPingDate(String pingDate) {
        this.pingDate = pingDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getResidentId() {
        return residentId;
    }

    public void setResidentId(int residentId) {
        this.residentId = residentId;
    }

    public String getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}