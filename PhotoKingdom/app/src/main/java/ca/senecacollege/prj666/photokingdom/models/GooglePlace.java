package ca.senecacollege.prj666.photokingdom.models;

/**
 * Place from Google Places API Web Service
 *
 * @author Sofia
 */
public class GooglePlace {
    private String place_id;
    private String name;
    private double lat;
    private double lng;

    public GooglePlace(String place_id, String name, double lat, double lng){
        this.place_id = place_id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
