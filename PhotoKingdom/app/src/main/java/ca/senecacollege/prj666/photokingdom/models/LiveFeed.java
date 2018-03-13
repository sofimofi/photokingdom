package ca.senecacollege.prj666.photokingdom.models;

/**
 * Model class for a live feed
 *
 * @author Wonho
 */
public class LiveFeed {
    private String date;
    private String msg;
    private int imgRes1;
    private int imgRes2;
    private String name1;
    private String name2;

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

    public int getImgRes1() {
        return imgRes1;
    }

    public void setImgRes1(int imgRes1) {
        this.imgRes1 = imgRes1;
    }

    public int getImgRes2() {
        return imgRes2;
    }

    public void setImgRes2(int imgRes2) {
        this.imgRes2 = imgRes2;
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
