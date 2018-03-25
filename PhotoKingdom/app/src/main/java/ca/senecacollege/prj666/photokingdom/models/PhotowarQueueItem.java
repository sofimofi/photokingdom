package ca.senecacollege.prj666.photokingdom.models;

/**
 * Model class for a photowar queue item
 *
 * @author Wonho
 */
public class PhotowarQueueItem {
    private String date;
    private int imgResId;
    private String name;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImgResId() {
        return imgResId;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
