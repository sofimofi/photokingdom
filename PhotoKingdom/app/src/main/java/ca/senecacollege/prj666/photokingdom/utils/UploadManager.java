package ca.senecacollege.prj666.photokingdom.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

import ca.senecacollege.prj666.photokingdom.models.Image;
import ca.senecacollege.prj666.photokingdom.services.PhotoKingdomService;
import ca.senecacollege.prj666.photokingdom.services.RetrofitServiceGenerator;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Manager class for image upload
 *
 * @author Wonho
 */

public class UploadManager {

    private Context mContext;

    /**
     * Constructor
     * @param context
     */
    public UploadManager(Context context) {
        mContext = context;
    }

    /**
     * Upload an image to server using Retrofit
     * @param uri
     */
    public void uploadImage(Uri uri) {
        if (uri != null && mContext != null) {
            // Create a multipart request body
            File file = new File(getRealPathFromURI(uri));
            RequestBody requestFile = RequestBody.create(MediaType.parse(mContext.getContentResolver().getType(uri)), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            // Create a retrofit service for PhotoKingdomAPI
            PhotoKingdomService service = RetrofitServiceGenerator.createService(PhotoKingdomService.class);

            // Execute a request to upload an image
            Call<Image> uploadImageCall = service.uploadImage(body);
            uploadImageCall.enqueue(new Callback<Image>() {
                @Override
                public void onResponse(Call<Image> call, Response<Image> response) {
                    if (response.isSuccessful()) {
                        mListener.onUploaded(response.body().getPath());
                    } else {
                        try {
                            mListener.onFailure(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Image> call, Throwable t) {
                    mListener.onFailure(t.getMessage());
                }
            });
        }
        else {
            mListener.onFailure("No image");
        }
    }

    /**
     * Get an image path on the device from URI
     * @param imageUri
     * @return String
     */
    private String getRealPathFromURI(Uri imageUri) {
        String result;
        Cursor cursor = mContext.getContentResolver().query(imageUri,
                null, null, null, null);

        if (cursor == null) {
            result = imageUri.getPath();
        } else {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(columnIndex);
            cursor.close();
        }

        return result;
    }

    // Listener
    public interface OnUploadListener {
        void onUploaded(String path);
        void onFailure(String error);
    }

    private OnUploadListener mListener;

    public void setOnUploadListener(OnUploadListener listener) {
        mListener = listener;
    }
}