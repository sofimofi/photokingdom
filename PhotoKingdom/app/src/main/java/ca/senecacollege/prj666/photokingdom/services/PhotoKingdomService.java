package ca.senecacollege.prj666.photokingdom.services;

import java.util.List;

import ca.senecacollege.prj666.photokingdom.models.Attraction;
import ca.senecacollege.prj666.photokingdom.models.AttractionPhotowar;
import ca.senecacollege.prj666.photokingdom.models.AttractionPhotowarWithDetails;
import ca.senecacollege.prj666.photokingdom.models.Image;
import ca.senecacollege.prj666.photokingdom.models.Continent;
import ca.senecacollege.prj666.photokingdom.models.Country;
import ca.senecacollege.prj666.photokingdom.models.LoginInfo;
import ca.senecacollege.prj666.photokingdom.models.PhotoWinning;
import ca.senecacollege.prj666.photokingdom.models.PhotoWithDetails;
import ca.senecacollege.prj666.photokingdom.models.Ping;
import ca.senecacollege.prj666.photokingdom.models.Province;
import ca.senecacollege.prj666.photokingdom.models.Resident;
import ca.senecacollege.prj666.photokingdom.models.ResidentOwn;
import ca.senecacollege.prj666.photokingdom.models.ResidentOwnForMapView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("api/attractions/googleplaceid/{placeId}/details")
    Call<Attraction> getAttractionByPlaceId(@Path("placeId") String placeId);

    @GET("api/attractionphotowars")
    Call<List<AttractionPhotowarWithDetails>> getPhotowars();

    @GET("api/attractionphotowars/{id}/details")
    Call<AttractionPhotowarWithDetails> getAttractionPhotowar(@Path("id") int attractionPhotowarId,
                                                              @Query("residentId") Integer residentId);

    @GET("api/attractionphotowars/{id}/AddVote")
    Call<AttractionPhotowarWithDetails> addVoteAttractionPhotowar(@Path("id") int attractionPhotowarId,
                                                   @Query("residentId") int residentId,
                                                   @Query("photoUploadId") int photoUploadId);

    @GET("api/attractionphotowars/{id}/RemoveVote")
    Call<AttractionPhotowarWithDetails> removeVoteAttractionPhotowar(@Path("id") int attractionPhotowarId,
                                                       @Query("residentId") int residentId,
                                                       @Query("photoUploadId") int photoUploadId);

    @GET("api/attractions/{id}/WinningPhotos")
    Call<List<PhotoWinning>> getAttractionWinningPhotos(@Path("id") int attractionId);

    @GET("api/attractions/{id}/Photowars")
    Call<List<AttractionPhotowarWithDetails>> getAttractionPhotowarHistory(@Path("id") int attractionId);

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

    @GET("api/photos/{id}/details")
    Call<PhotoWithDetails> getPhoto(@Path("id") int photoId);

    @GET("api/owns/active/attractions")
    Call<List<ResidentOwn>> getAttractionOwns();

    @GET("api/owns/active/cities")
    Call<List<ResidentOwn>> getCityOwns();

    @GET("api/owns/active/city")
    Call<ResidentOwnForMapView> getCityOwnByCityName(@Path("city") String city,
                                                           @Path("province") String province,
                                                           @Path("country") String country);

    @GET("api/owns/active/provinces")
    Call<List<ResidentOwn>> getProvinceOwns();

    @GET("api/owns/active/province")
    Call<ResidentOwnForMapView> getProvinceOwnByProvinceName(@Path("province") String province,
                                                         @Path("country") String country);

    @GET("api/owns/active/countries")
    Call<List<ResidentOwn>> getCountryOwns();

    @GET("api/owns/active/country")
    Call<ResidentOwnForMapView> getCountryOwnByCountryName(@Path("country") String country);

    @GET("api/owns/active/continents")
    Call<List<ResidentOwn>> getContinentsOwns();

    @GET("api/owns/active/continent")
    Call<ResidentOwnForMapView> getContinentOwnByCountryName(@Path("country") String country);

}

