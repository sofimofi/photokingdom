package ca.senecacollege.prj666.photokingdom.services;

import java.util.List;

import ca.senecacollege.prj666.photokingdom.models.Image;
import ca.senecacollege.prj666.photokingdom.models.Continent;
import ca.senecacollege.prj666.photokingdom.models.Country;
import ca.senecacollege.prj666.photokingdom.models.LoginInfo;
import ca.senecacollege.prj666.photokingdom.models.Ping;
import ca.senecacollege.prj666.photokingdom.models.Province;
import ca.senecacollege.prj666.photokingdom.models.Resident;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Retrofit service interface for PhotokingdomAPI
 *
 * @author Wonho
 */
public interface PhotoKingdomService {
    @Multipart
    @POST("upload/images")
    Call<Image> uploadImage(@Part MultipartBody.Part file);

    @POST("api/residents")
    Call<Resident> createResident(@Body Resident resident);

    @POST("api/residents/login")
    Call<Resident> loginResident(@Body LoginInfo info);

    // TODO: get residentId ?
    @GET("api/residents/{id}")
    Call<Resident> getResident(@Path("id") int residentId);

    @GET("api/residents/{id}/pings")
    Call<List<Ping>> getPings(@Path("id") int residentId);

    @GET("api/continents")
    Call<List<Continent>> getContinents();

    @GET("api/continents/{id}")
    Call<Continent> getContinentWithCountries(@Path("id") int continentId);

    @GET("api/countries/{id}")
    Call<Country> getCountryWithProvinces(@Path("id") int countryId);

    @GET("api/provinces/{id}")
    Call<Province> getProvinceWithCities(@Path("id") int provinceId);
}

