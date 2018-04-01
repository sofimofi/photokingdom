package ca.senecacollege.prj666.photokingdom.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Model class for AttractionPhotowar
 *
 * @author Sofia
 */

public class AttractionPhotowar implements Comparable<AttractionPhotowar>{
    @SerializedName("Id")
    private int id;
    @SerializedName("StartDate")
    private String startDate;
    @SerializedName("EndDate")
    private String endDate;
    @SerializedName("AttractionId")
    private int attractionId;

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

    @Override
    public int compareTo(@NonNull AttractionPhotowar attractionPhotowar) {
        // return by descending order of startdate (from higher start date to lower start date)
        return attractionPhotowar.getStartDate().compareTo(this.getStartDate());
    }
}
