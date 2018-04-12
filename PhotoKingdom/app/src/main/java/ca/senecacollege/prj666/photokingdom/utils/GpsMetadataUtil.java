package ca.senecacollege.prj666.photokingdom.utils;

import android.content.Context;
import android.net.Uri;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.GpsDirectory;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhihao on 2018-04-06.
 */

public class GpsMetadataUtil {

    public static GpsDirectory getGpsDirectory(Context context, Uri imageUri) {
        UploadManager manager = new UploadManager(context);
        String realFilePath = manager.getRealPathFromURI(imageUri);
        File imgFile = new File(realFilePath);

        try {
            Metadata metadata = ImageMetadataReader.readMetadata(imgFile);
            GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
            if(gpsDirectory != null){
                return gpsDirectory;
            }else{
                throw new MetadataException("Gps metadata not found");
            }

        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MetadataException e){
            e.printStackTrace();
        }

        return null;
    }
}
