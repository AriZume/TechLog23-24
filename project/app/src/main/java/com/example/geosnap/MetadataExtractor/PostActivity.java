package com.example.geosnap.MetadataExtractor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.GpsDirectory;
import com.example.geosnap.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    //METADATA
    double Latitude,Longitude,ImgSize=0;
    String ImgHeight,ImgWidth,DateTime="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ImageView gallery = findViewById(R.id.selectPhoto);
        gallery.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 3);
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && data != null){

            Uri selectedImage = data.getData();
            ImageView imageView = findViewById(R.id.selectPhoto);
            //selectPhoto is the clickable imageView id
            imageView.setImageURI(selectedImage);
            extractMetadata(this,selectedImage);
        }
    }

    public void extractMetadata(Context context, Uri photoUri) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(photoUri);

            if (inputStream != null) {
                Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
                if (!containsMetadata(metadata)) {
                    throw new RuntimeException("Selected photo does not contain metadata. Please choose another photo.");
                }
                File file = new File(getRealPathFromURI(photoUri, context));
                ImgSize = file.length();
                for (Directory directory : metadata.getDirectories()) {
                    for (Tag tag : directory.getTags()) {
                        if (tag.getTagName().equals("Image Height")) {
                            ImgHeight =tag.getDescription();
                        }else if (tag.getTagName().equals("Image Width")) {
                            ImgWidth= tag.getDescription();
                        } else if (tag.getTagName().equals("Date/Time")) {
                            DateTime =tag.getDescription();
                        }
                    }
                    if (directory instanceof GpsDirectory) {
                        GpsDirectory gpsDirectory = (GpsDirectory) directory;
                        // Get GPS information
                        Longitude = gpsDirectory.getGeoLocation().getLongitude();
                        Latitude = gpsDirectory.getGeoLocation().getLatitude();

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

    private String getRealPathFromURI(Uri uri, Context context) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            return uri.getPath();
        } else {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(column_index);
            cursor.close();
            return filePath;
        }
    }
    private boolean containsMetadata(Metadata metadata) {
        if (metadata == null) {
            return false;
        }

        Iterable<Directory> directories = metadata.getDirectories();
        if (directories == null || !directories.iterator().hasNext()) {
            return false;
        }

        return true;
    }
}