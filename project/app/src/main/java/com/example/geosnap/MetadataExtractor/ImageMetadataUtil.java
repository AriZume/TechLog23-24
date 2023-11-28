package com.example.geosnap.MetadataExtractor;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.GpsDirectory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageMetadataUtil {
    private double latitude, longitude, imgSize;

    private String imgHeight, imgWidth, dateTime;

    public ImageMetadataUtil(){
        this.latitude=0;
        this.longitude=0;
        this.imgSize=0;
        this.imgHeight="0";
        this.imgWidth="0";
        this.dateTime="0";
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getImgSize() {
        return imgSize;
    }

    public String getImgHeight() {
        return imgHeight;
    }

    public String getImgWidth() {
        return imgWidth;
    }

    public String getDateTime() {
        return dateTime;
    }


    public void extractMetadata(Context context, Uri photoUri,ContentResolver activityContentResolver) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(photoUri);

            if (inputStream != null) {
                Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
                if (!containsMetadata(metadata)) {
                    throw new RuntimeException("Selected photo does not contain metadata. Please choose another photo.");
                }
                File file = new File(getRealPathFromURI(photoUri,activityContentResolver));
                imgSize = file.length();
                Log.d("PhotoMetadata", "File Size: " + imgSize + " bytes");
                for (Directory directory : metadata.getDirectories()) {
                    for (Tag tag : directory.getTags()) {
                        if (tag.getTagName().equals("Image Height")) {
                            imgHeight =tag.getDescription();
                        }else if (tag.getTagName().equals("Image Width")) {
                            imgWidth = tag.getDescription();
                        } else if (tag.getTagName().equals("Date/Time")) {
                            dateTime =tag.getDescription();
                        }
                    }
                    if (directory instanceof GpsDirectory) {
                        GpsDirectory gpsDirectory = (GpsDirectory) directory;
                        // Get GPS information
                        longitude = gpsDirectory.getGeoLocation().getLongitude();
                        latitude = gpsDirectory.getGeoLocation().getLatitude();
                        Log.d("Longitude", "extractMetadata: " + longitude);
                        Log.d("Latitude", "extractMetadata: " + latitude);
                    }
                }
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ImageProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean containsMetadata(Metadata metadata) {
        if (metadata == null) {
            return false;
        }
        Iterable<Directory> directories = metadata.getDirectories();
        return directories != null && directories.iterator().hasNext();
    }

    private String getRealPathFromURI(Uri contentURI,ContentResolver contentResolver) {
        String result;
        Cursor cursor = contentResolver.query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public boolean metadataValidator(){
        return getLatitude() == 0 || getLongitude() == 0 || getDateTime().equals("0");
    }
}



