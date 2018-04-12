package ca.senecacollege.prj666.photokingdom.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhihao on 2018-04-11.
 */

public class AttractionPhotowarAddForm {
    @SerializedName("AttractionId")
    private int attractionId;

    @SerializedName("CompetitorId")
    private int competitorId;
    @SerializedName("PhotoImagePath")
    private String photoImagePath;
    @SerializedName("PhotoLat")
    private double photoLat;
    @SerializedName("PhotoLng")
    private double photoLng;
    @SerializedName("PlaceId")
    private String placeId;

    public AttractionPhotowarAddForm(int attractionId, int competitorId, String photoImagePath, double photoLat, double photoLng, String placeId) {
        this.attractionId = attractionId;
        this.competitorId = competitorId;
        this.photoImagePath = photoImagePath;
        this.photoLat = photoLat;
        this.photoLng = photoLng;
        this.placeId = placeId;
    }

    public int getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(int attractionId) {
        this.attractionId = attractionId;
    }

    public int getCompetitorId() {
        return competitorId;
    }

    public void setCompetitorId(int competitorId) {
        this.competitorId = competitorId;
    }

    public String getPhotoImagePath() {
        return photoImagePath;
    }

    public void setPhotoImagePath(String photoImagePath) {
        this.photoImagePath = photoImagePath;
    }

    public double getPhotoLat() {
        return photoLat;
    }

    public void setPhotoLat(double photoLat) {
        this.photoLat = photoLat;
    }

    public double getPhotoLng() {
        return photoLng;
    }

    public void setPhotoLng(double photoLng) {
        this.photoLng = photoLng;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
