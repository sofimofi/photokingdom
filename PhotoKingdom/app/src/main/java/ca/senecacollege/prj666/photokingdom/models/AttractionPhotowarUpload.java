package ca.senecacollege.prj666.photokingdom.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sofia on 2018-03-21.
 */

public class AttractionPhotowarUpload {
    @SerializedName("Id")
    private int id;
    @SerializedName("IsWinner")
    private int isWinner;
    @SerializedName("IsLoser")
    private int isLoser;
    @SerializedName("PhotoId")
    private int photoId;
    @SerializedName("AttractionPhotowarId")
    private int attractionPhotowarId;
    @SerializedName("Photo")
    private Photo photo;

    // special info from AttractionPhotowars/{id}/details API
    @SerializedName("PhotoResidentUserName")
    private String photoResidentUserName;
    @SerializedName("PhotoResidentAvatarImagePath")
    private String photoResidentAvatarImagePath;
    @SerializedName("ResidentVotesCount")
    private int residentVotesCount;

    // flag indicating whether Resident has voted for this photo (-1 = n/a, 0 = no, 1 = yes)
    @SerializedName("residentHasVoted")
    private int residentHasVoted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsWinner() {
        return isWinner;
    }

    public void setIsWinner(int isWinner) {
        this.isWinner = isWinner;
    }

    public int getIsLoser() {
        return isLoser;
    }

    public void setIsLoser(int isLoser) {
        this.isLoser = isLoser;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public int getAttractionPhotowarId() {
        return attractionPhotowarId;
    }

    public void setAttractionPhotowarId(int attractionPhotowarId) {
        this.attractionPhotowarId = attractionPhotowarId;
    }

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
