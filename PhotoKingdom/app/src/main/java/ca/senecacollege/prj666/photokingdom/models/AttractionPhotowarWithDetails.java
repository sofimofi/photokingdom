package ca.senecacollege.prj666.photokingdom.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * AttractionPhotowar With Details class, extends AttractionPhotowar Base class
 * Used in AttractionPhotowar View
 * Created by sofia on 2018-03-30.
 */

public class AttractionPhotowarWithDetails extends AttractionPhotowar {
    @SerializedName("Attraction")
    private Attraction attraction;

    @SerializedName("AttractionPhotowarUploads")
    private List<AttractionPhotowarUploadForPhotowarView> attractionPhotowarUploads;

    // Flag indicating whether Resident is part of photowar (-1 = n/a, 0 = no, 1 = yes)
    @SerializedName("residentInPhotowar")
    private int residentInPhotowar;

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }

    public List<AttractionPhotowarUploadForPhotowarView> getAttractionPhotowarUploads() {
        return attractionPhotowarUploads;
    }

    public void setAttractionPhotowarUploads(List<AttractionPhotowarUploadForPhotowarView> attractionPhotowarUploads) {
        this.attractionPhotowarUploads = attractionPhotowarUploads;
    }

    public int getResidentInPhotowar() {
        return residentInPhotowar;
    }

    public void setResidentInPhotowar(int residentInPhotowar) {
        this.residentInPhotowar = residentInPhotowar;
    }
}
