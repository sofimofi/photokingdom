package ca.senecacollege.prj666.photokingdom.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zcai on 2018-04-11.
 */

public class AttractionWithWin {
    @SerializedName("AttractionId")
    private int attractionId;
    @SerializedName("PhotoImagePath")
    private String photoImagePath;
    @SerializedName("PhotoLat")
    private double photoLat;
    @SerializedName("PhotoLng")
    private double photoLng;
    @SerializedName("OwnerId")
    private int ownerId;
    @SerializedName("PlaceId")
    private String placeId;

    public AttractionWithWin(int attractionId, String photoImagePath, double photoLat, double photoLng, int ownerId, String placeId) {
        this.attractionId = attractionId;
        this.photoImagePath = photoImagePath;
        this.photoLat = photoLat;
        this.photoLng = photoLng;
        this.ownerId = ownerId;
        this.placeId = placeId;
    }

    public int getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(int attractionId) {
        this.attractionId = attractionId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
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
}
