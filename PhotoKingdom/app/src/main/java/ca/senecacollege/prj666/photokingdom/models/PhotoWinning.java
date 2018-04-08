package ca.senecacollege.prj666.photokingdom.models;

import com.google.gson.annotations.SerializedName;

/**
 * Photo with details for Attraction WinningPhoto history
 *
 * @author sofia
 */
public class PhotoWinning extends Photo {
    @SerializedName("ResidentUserName")
    private String residentUserName;

    @SerializedName("ResidentAvatarImagePath")
    private String residentAvatarImagePath;

    @SerializedName("Votes")
    private int votes;

    @SerializedName("WinningDate")
    private String winningDate;

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

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getWinningDate() {
        return winningDate;
    }

    public void setWinningDate(String winningDate) {
        this.winningDate = winningDate;
    }
}
