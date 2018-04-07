package ca.senecacollege.prj666.photokingdom.models;

import com.google.gson.annotations.SerializedName;

/**
 * Model class for a ResidentOwn
 *
 * @author Wonho
 */
public class ResidentOwn {
    @SerializedName("Id")
    private int id;
    @SerializedName("StartOfOwn")
    private String startDate;
    @SerializedName("Title")
    private String title;
    @SerializedName("ResidentId")
    private int residentId;
    @SerializedName("Resident")
    private Resident resident;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResidentId() {
        return residentId;
    }

    public void setResidentId(int residentId) {
        this.residentId = residentId;
    }

    public Resident getResident() {
        return resident;
    }

    public void setResident(Resident resident) {
        this.resident = resident;
    }
}
