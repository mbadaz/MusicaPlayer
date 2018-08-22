package com.example.mbada.musicaplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

public class myAlbumArtRetriever {

    /**
     * Retrieves the embedded album art image if any from an audio file
     * If no image is found it returns the generic place holder image in the resources folder
     * @param filePath
     * @param context
     * @return
     */
    public final Bitmap getImage(String filePath, Context context){
        MediaMetadataRetriever retriever;
        byte[] image;
        retriever = new MediaMetadataRetriever();
        retriever.setDataSource(filePath);
        image = retriever.getEmbeddedPicture();
        if (image != null) {
            return BitmapFactory.decodeByteArray(image, 0, image.length);
        }else{
            return BitmapFactory.decodeResource(context.getResources(),R.drawable.music_icon);
        }
    }

}
