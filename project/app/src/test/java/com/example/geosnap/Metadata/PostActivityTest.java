package com.example.geosnap.Metadata;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.Mockito;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import android.widget.Button;
import android.widget.EditText;


public class PostActivityTest {
    private String imageURL, tag, desc;
    @Mock
    private PostActivity mockedPost;
    @Mock
    private ImageMetadataUtil mockedImageMetadata;

    @Before
    public void setUp() {
        imageURL = "mockedImageURL";
        tag = "mockedTag";
        desc = "mockedDescription";

        mockedImageMetadata= Mockito.mock(ImageMetadataUtil.class);
        mockedImageMetadata= new ImageMetadataUtil(40.123, 20.123, 4012.3, "555","888", "13-1-2024");

        mockedPost= Mockito.mock(PostActivity.class);
    }

    @Test
    public void testUploadData() {
        mockedPost.uploadData(imageURL, mockedImageMetadata, mockedImageMetadata.getDateTime());
        verify(mockedPost).uploadData(imageURL, mockedImageMetadata, mockedImageMetadata.getDateTime());
    }
}