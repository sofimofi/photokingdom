package ca.senecacollege.prj666.photokingdom.models;

/**
 * Class holding city, province and country for a location
 *
 * @author sofia
 */
public class Locality {
    private String city;
    private String province;
    private String country;

    public Locality(String city, String province, String country){
        this.city = city;
        this.province = province;
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString(){
        return "City = '" + getCity() + "', Province = '" + getProvince() + "', Country = '" + getCountry() + "'";
    }
}
