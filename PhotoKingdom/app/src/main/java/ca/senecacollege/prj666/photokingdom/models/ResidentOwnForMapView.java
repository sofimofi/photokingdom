package ca.senecacollege.prj666.photokingdom.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sofia on 2018-04-08.
 */
public class ResidentOwnForMapView extends ResidentOwn{
    @SerializedName("ResidentUserName")
    private String residentUserName;
    @SerializedName("ResidentAvatarImagePath")
    private String residentAvatarImagePath;

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
}
