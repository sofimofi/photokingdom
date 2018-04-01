package ca.senecacollege.prj666.photokingdom.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Photo With Details class extends on Photo base
 * Includes additional details needed for Photo View
 * Used in Photos/{id}/Details API
 * Created by sofia on 2018-03-30.
 */

public class PhotoWithDetails extends Photo {
    @SerializedName("ResidentUserName")
    private String residentUserName;

    @SerializedName("ResidentAvatarImagePath")
    private String residentAvatarImagePath;

    @SerializedName("AttractionPhotowarUploads")
    private List<AttractionPhotowarUploadForPhotoDetails> attractionPhotowarUploads;

    public String getResidentUserName() {
        return residentUserName;
    }

    public void setResidentUserName(String residentUserName) {
        this.residentUserName = residentUserName;
    }

    public String getResidentAvatarImagePath() {
        return residentAvatarImagePath;
    }

    public void setResidentAvatarImagePath(String residentAvatarImagePath) {
        this.residentAvatarImagePath = residentAvatarImagePath;
    }

    public List<AttractionPhotowarUploadForPhotoDetails> getAttractionPhotowarUploads() {
        return attractionPhotowarUploads;
    }

    public void setAttractionPhotowarUploads(List<AttractionPhotowarUploadForPhotoDetails> attractionPhotowarUploads) {
        this.attractionPhotowarUploads = attractionPhotowarUploads;
    }
}
