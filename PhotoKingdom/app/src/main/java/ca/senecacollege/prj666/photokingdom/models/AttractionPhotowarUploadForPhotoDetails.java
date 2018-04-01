package ca.senecacollege.prj666.photokingdom.models;

import com.google.gson.annotations.SerializedName;

/**
 * AttractionPhotowarUpload With Details about Photowar and Vote Count
 * Used in Photo View
 * Created by sofia on 2018-03-30.
 */

public class AttractionPhotowarUploadForPhotoDetails extends AttractionPhotowarUpload{
    @SerializedName("AttractionPhotowar")
    private AttractionPhotowar attractionPhotowar;

    @SerializedName("ResidentVotesCount")
    private int residentVotesCount;

    @SerializedName("AttractionPhotowarAttractionName")
    private String attractionPhotowarAttractionName;

    public AttractionPhotowar getAttractionPhotowar() {
        return attractionPhotowar;
    }

    public void setAttractionPhotowar(AttractionPhotowar attractionPhotowar) {
        this.attractionPhotowar = attractionPhotowar;
    }

    public int getResidentVotesCount() {
        return residentVotesCount;
    }

    public void setResidentVotesCount(int residentVotesCount) {
        this.residentVotesCount = residentVotesCount;
    }

    public String getAttractionPhotowarAttractionName() {
        return attractionPhotowarAttractionName;
    }

    public void setAttractionPhotowarAttractionName(String attractionPhotowarAttractionName) {
        this.attractionPhotowarAttractionName = attractionPhotowarAttractionName;
    }
}
