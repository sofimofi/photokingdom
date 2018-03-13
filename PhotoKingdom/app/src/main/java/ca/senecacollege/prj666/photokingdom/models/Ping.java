package ca.senecacollege.prj666.photokingdom.models;

/**
 * Model class for a ping
 *
 * @author Wonho
 */
public class Ping {
    private String pingDate;
    private String expiryDate;
    private int residentId;
    private int attractionId;
    private String attractionName;

    public String getPingDate() {
        return pingDate;
    }

    public void setPingDate(String pingDate) {
        this.pingDate = pingDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getResidentId() {
        return residentId;
    }

    public void setResidentId(int residentId) {
        this.residentId = residentId;
    }

    public int getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(int attractionId) {
        this.attractionId = attractionId;
    }

    public String getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }
}
