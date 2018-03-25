package ca.senecacollege.prj666.photokingdom.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Model class for AttractionPhotowar
 *
 * @author Sofia
 */

public class AttractionPhotowar {
    @SerializedName("Id")
    private int id;
    @SerializedName("StartDate")
    private String startDate;
    @SerializedName("EndDate")
    private String endDate;
    @SerializedName("AttractionId")
    private int attractionId;
    @SerializedName("Attraction")
    private Attraction attraction;
    @SerializedName("AttractionPhotowarUploads")
    private List<AttractionPhotowarUpload> attractionPhotowarUploads;

    // Flag indicating whether Resident is part of photowar (-1 = n/a, 0 = no, 1 = yes)
    @SerializedName("residentInPhotowar")
    private int residentInPhotowar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public List<AttractionPhotowarUpload> getAttractionPhotowarUploads() {
        return attractionPhotowarUploads;
    }

    public void setAttractionPhotowarUploads(List<AttractionPhotowarUpload> attractionPhotowarUploads) {
        this.attractionPhotowarUploads = attractionPhotowarUploads;
    }

    public int getResidentInPhotowar() {
        return residentInPhotowar;
    }

    public void setResidentInPhotowar(int residentInPhotowar) {
        this.residentInPhotowar = residentInPhotowar;
    }
}
