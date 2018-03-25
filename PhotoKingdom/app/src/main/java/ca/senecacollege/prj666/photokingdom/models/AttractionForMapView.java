package ca.senecacollege.prj666.photokingdom.models;

/**
 * Created by sofia on 2018-03-17.
 */

public class AttractionForMapView extends Attraction {
    private String residentUserName;
    private String avatarImagePath;
    private String residentTitle;

    public AttractionForMapView(int id, String googlePlaceId, String name, double lat, double lng,
    int isActive, int cityId, String residentUserName, String avatarImagePath, String residentTitle){
        super(id , googlePlaceId, name, lat, lng, isActive, cityId);
        this.residentUserName = residentUserName;
        this.avatarImagePath = avatarImagePath;
        this.residentTitle = residentTitle;
    }

    public String getResidentUserName() {
        return residentUserName;
    }

    public void setResidentUserName(String residentUserName) {
        this.residentUserName = residentUserName;
    }

    public String getAvatarImagePath() {
        return avatarImagePath;
    }

    public void setAvatarImagePath(String avatarImagePath) {
        this.avatarImagePath = avatarImagePath;
    }

    public String getResidentTitle() {
        return residentTitle;
    }

    public void setResidentTitle(String residentTitle) {
        this.residentTitle = residentTitle;
    }
}
