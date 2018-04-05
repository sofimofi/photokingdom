package ca.senecacollege.prj666.photokingdom.models;

import com.google.gson.annotations.SerializedName;

/**
 * AttractionPhotowarUpload for AttractionPhotowar View
 * Used in AttractionPhotowars/{id}/details API
 * Includes Photo and Voting information
 * Created by sofia on 2018-03-30.
 */

public class AttractionPhotowarUploadForPhotowarView extends AttractionPhotowarUpload {
    @SerializedName("Photo")
    private Photo photo;

    @SerializedName("PhotoResidentUserName")
    private String photoResidentUserName;

    @SerializedName("PhotoResidentAvatarImagePath")
    private String photoResidentAvatarImagePath;

    @SerializedName("ResidentVotesCount")
    private int residentVotesCount;

    // flag indicating whether Resident has voted for this photo (-1 = n/a, 0 = no, 1 = yes)
    @SerializedName("residentHasVoted")
    private int residentHasVoted;

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getPhotoResidentUserName() {
        return photoResidentUserName;
    }

    public void setPhotoResidentUserName(String photoResidentUserName) {
        this.photoResidentUserName = photoResidentUserName;
    }

    public String getPhotoResidentAvatarImagePath() {
        return photoResidentAvatarImagePath;
    }

    public void setPhotoResidentAvatarImagePath(String photoResidentAvatarImagePath) {
        this.photoResidentAvatarImagePath = photoResidentAvatarImagePath;
    }

    public int getResidentVotesCount() {
        return residentVotesCount;
    }

    public void setResidentVotesCount(int residentVotesCount) {
        this.residentVotesCount = residentVotesCount;
    }

    public int getResidentHasVoted() {
        return residentHasVoted;
    }

    public void setResidentHasVoted(int residentHasVoted) {
        this.residentHasVoted = residentHasVoted;
    }
}
