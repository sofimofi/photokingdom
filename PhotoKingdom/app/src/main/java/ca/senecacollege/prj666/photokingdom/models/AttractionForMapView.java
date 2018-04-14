package ca.senecacollege.prj666.photokingdom.models;

/**
 * Attraction with additional details needed for Map View
 *
 * @author sofia
 */

public class AttractionForMapView extends Attraction {
    private String residentUserName;
    private String avatarImagePath;
    private String residentTitle;
    private int currentPhotowarId;

    public AttractionForMapView(int id, String googlePlaceId, String name, double lat, double lng,
    int isActive, int cityId, String residentUserName, String avatarImagePath, String residentTitle, int currentPhotowarId){
        super(id , googlePlaceId, name, lat, lng, isActive, cityId);
        this.residentUserName = residentUserName;
        this.avatarImagePath = avatarImagePath;
        this.residentTitle = residentTitle;
        this.currentPhotowarId = currentPhotowarId;
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

    public int getCurrentPhotowarId() {
        return currentPhotowarId;
    }

    public void setCurrentPhotowarId(int currentPhotowarId) {
        this.currentPhotowarId = currentPhotowarId;
    }
}
