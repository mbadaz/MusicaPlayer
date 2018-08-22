package com.example.mbada.musicaplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

class SongsAdapter extends ArrayAdapter<Song> {
    private Context context;
    public SongsAdapter(@NonNull Context context, @NonNull ArrayList<Song> songs) {
        super(context, 0, songs);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Song song = getItem(position);
        ViewHolder viewHolder;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        if (convertView == null) { // no view to recyle create new view
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.songs_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.title = convertView.findViewById(R.id.song_title);
            viewHolder.artist = convertView.findViewById(R.id.artist);
            viewHolder.album = convertView.findViewById(R.id.album);
            viewHolder.duration = convertView.findViewById(R.id.duration);
            viewHolder.imageView = convertView.findViewById(R.id.album_art);
            convertView.setTag(viewHolder); //cache viewHolder object to the new view
        } else { // view is being recycled
            viewHolder = (ViewHolder) convertView.getTag(); //retrieve cached viewHolder
        }

        // set UI attributes for current song
        viewHolder.title.setText(song.getSongTitle());
        viewHolder.title.setSelected(true);
        viewHolder.artist.setText(song.getArtist());
        viewHolder.artist.setSelected(true);
        viewHolder.album.setText(song.getAlbum());
        viewHolder.album.setSelected(true);
        viewHolder.duration.setText(song.getDuration());

        Bitmap bitmap = (new myAlbumArtRetriever()).getImage(song.getFilePath(), context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        viewHolder.imageView.setImageBitmap(bitmap);
        viewHolder.imageView.setAdjustViewBounds(true);
        viewHolder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        viewHolder.imageView.setLayoutParams(params);

        return convertView;
    }

    /**
     * ViewHolder object is used to cache the call to findViewById() to prevent calling it
     * each time a list item view recycled.
     */
    class ViewHolder {
        TextView title;
        TextView artist;
        TextView album;
        TextView duration;
        ImageView imageView;
    }
}
