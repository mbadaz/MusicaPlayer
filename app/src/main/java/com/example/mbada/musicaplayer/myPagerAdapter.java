package com.example.mbada.musicaplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Generates views for the view pager
 */

public class myPagerAdapter extends PagerAdapter {
    private ArrayList<Song> songs;
    private Context context;

    public myPagerAdapter(Context context, ArrayList<Song> songs){
        this.context = context;
        this.songs = songs;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Song song = songs.get(position);
        View view = LayoutInflater.from(container.getContext()).inflate(
                R.layout.song_slider,container,false);
        TextView title = view.findViewById(R.id.song_title);
        title.setText(song.getArtist());
        title.setSelected(true);
        TextView artist = view.findViewById(R.id.artist);
        artist.setText(song.getArtist());
        artist.setSelected(true);
        TextView album = view.findViewById(R.id.album);
        album.setText(song.getAlbum());
        album.setSelected(true);

        ImageView albumArt = view.findViewById(R.id.album_image);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(500, 500);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        albumArt.setLayoutParams(params);
        Bitmap bitmap = (new myAlbumArtRetriever()).getImage(song.getFilePath(), context);
        albumArt.setImageBitmap(bitmap);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
