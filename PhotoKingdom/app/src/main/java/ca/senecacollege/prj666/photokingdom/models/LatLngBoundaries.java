package ca.senecacollege.prj666.photokingdom.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Lat and Lng boundaries for a desired region
 */
public class LatLngBoundaries {
    private double maxLat;
    private double minLat;
    private double maxLng;
    private double minLng;

    public LatLngBoundaries(double maxLat, double minLat, double maxLng, double minLng){
        this.maxLat = maxLat;
        this.minLat = minLat;
        this.maxLng = maxLng;
        this.minLng = minLng;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(double maxLat) {
        this.maxLat = maxLat;
    }

    public double getMinLat() {
        return minLat;
    }

    public void setMinLat(double minLat) {
        this.minLat = minLat;
    }

    public double getMaxLng() {
        return maxLng;
    }

    public void setMaxLng(double maxLng) {
        this.maxLng = maxLng;
    }

    public double getMinLng() {
        return minLng;
    }

    public void setMinLng(double minLng) {
        this.minLng = minLng;
    }

    @Override
    public String toString(){
        return "maxLat = " + getMaxLat() + ", minLat = " + getMinLat() +
                ", maxLng = " + getMaxLng() + ", minLng = " + getMinLng();
    }
}
