package ca.senecacollege.prj666.photokingdom.models;

/**
 * Model class for a live feed
 *
 * @author Wonho
 */
public class LiveFeed {
    private int photowarId;
    private String date;
    private String msg;
    private String photoPath1;
    private String photoPath2;
    private String name1;
    private String name2;

    public int getPhotowarId() {
        return photowarId;
    }

    public void setPhotowarId(int photowarId) {
        this.photowarId = photowarId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPhotoPath1() {
        return photoPath1;
    }

    public void setPhotoPath1(String photoPath1) {
        this.photoPath1 = photoPath1;
    }

    public String getPhotoPath2() {
        return photoPath2;
    }

    public void setPhotoPath2(String photoPath2) {
        this.photoPath2 = photoPath2;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }
}
