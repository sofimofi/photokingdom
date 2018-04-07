package ca.senecacollege.prj666.photokingdom.models;

import android.support.annotation.NonNull;

/**
 * Model class for a live feed
 *
 * @author Wonho
 */
public class LiveFeed implements Comparable<LiveFeed> {
    private int type;
    private String date;
    private String msg;

    public Photowar photowar;
    public Own own;

    public LiveFeed(int type) {
        this.type = type;

        switch (type) {
            case FeedEntry.TYPE_PHOTOWAR:
                photowar = new Photowar();
                break;
            case FeedEntry.TYPE_OWN:
                own = new Own();
                break;
        }
    }

    public int getType() {
        return type;
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

    @Override
    public int compareTo(@NonNull LiveFeed liveFeed) {
        return liveFeed.date.compareTo(date);
    }

    // Photowar
    public class Photowar {
        private int photowarId;
        private String photoPath1;
        private String photoPath2;
        private String residentName1;
        private String residentName2;

        public int getPhotowarId() {
            return photowarId;
        }

        public void setPhotowarId(int photowarId) {
            this.photowarId = photowarId;
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

        public String getResidentName1() {
            return residentName1;
        }

        public void setResidentName1(String residentName1) {
            this.residentName1 = residentName1;
        }

        public String getResidentName2() {
            return residentName2;
        }

        public void setResidentName2(String residentName2) {
            this.residentName2 = residentName2;
        }
    }

    // Own
    public class Own {
        private int ownId;
        private int residentId;

        public int getOwnId() {
            return ownId;
        }

        public void setOwnId(int ownId) {
            this.ownId = ownId;
        }

        public int getResidentId() {
            return residentId;
        }

        public void setResidentId(int residentId) {
            this.residentId = residentId;
        }
    }
}
