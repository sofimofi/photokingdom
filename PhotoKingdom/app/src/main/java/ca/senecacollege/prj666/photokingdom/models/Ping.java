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
    @SerializedName("AttractionId")
    private int attractionId;
    @SerializedName("Attraction")
    private Attraction attraction;
    @SerializedName("Resident")
    private Resident resident;

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

    public int getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(int attractionId) {
        this.attractionId = attractionId;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }

    public Resident getResident() {
        return resident;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
    }
}