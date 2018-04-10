package ca.senecacollege.prj666.photokingdom.models;

import com.google.gson.annotations.SerializedName;

/**
 * Model class for a photo queue
 *
 * @author Wonho
 */
public class PhotowarQueue {
    @SerializedName("Id")
    private int id;
    @SerializedName("QueueDate")
    private String queueDate;
    @SerializedName("AttractionId")
    private int attractionId;
    @SerializedName("AttractionPhotowarUploadPhotoId")
    private int photoId;
    @SerializedName("AttractionPhotowarUploadPhotoPhotoFilePath")
    private String photoPath;
    @SerializedName("AttractionPhotowarUploadPhotoResidentId")
    private int residentId;
    @SerializedName("AttractionPhotowarUploadPhotoResidentUserName")
    private String residentName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQueueDate() {
        return queueDate;
    }

    public void setQueueDate(String queueDate) {
        this.queueDate = queueDate;
    }

    public int getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(int attractionId) {
        this.attractionId = attractionId;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public int getResidentId() {
        return residentId;
    }

    public void setResidentId(int residentId) {
        this.residentId = residentId;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }
}
