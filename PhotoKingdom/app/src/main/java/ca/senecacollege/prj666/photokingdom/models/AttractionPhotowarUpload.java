package ca.senecacollege.prj666.photokingdom.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sofia on 2018-03-21.
 */

public class AttractionPhotowarUpload implements Comparable<AttractionPhotowarUpload> {
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

    @Override
    public int compareTo(@NonNull AttractionPhotowarUpload attractionPhotowarUpload) {
        // sort by descending order (from larger Id to smaller ID)
        return attractionPhotowarUpload.getId() - this.getId();
    }
}
