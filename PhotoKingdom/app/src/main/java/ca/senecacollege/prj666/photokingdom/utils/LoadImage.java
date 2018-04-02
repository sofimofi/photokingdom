package ca.senecacollege.prj666.photokingdom.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ca.senecacollege.prj666.photokingdom.R;
import ca.senecacollege.prj666.photokingdom.services.RetrofitServiceGenerator;


/**
 * Created by sofia on 2018-03-30.
 */

public class LoadImage {
    public static void loadImage(Context context, ImageView imageView, final String imagePath){
        String imageUrl = RetrofitServiceGenerator.getBaseUrl() + imagePath;
        Picasso.with(context).load(imageUrl)
                .error(R.mipmap.ic_launcher)
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
//                        Log.d(TAG, "Succeeded photo upload of " + imagePath);
                    }

                    @Override
                    public void onError() {
                        //Toast.makeText(getContext(), R.string.error_avatar_upload, Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, "Failed photo upload of " + imagePath);
                    }
                });
    }
}
