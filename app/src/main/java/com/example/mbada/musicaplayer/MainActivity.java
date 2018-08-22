package com.example.mbada.musicaplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContentResolverCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final int MY_REQUEST_READ_PERMISSION = 786;
    private final ArrayList<Song> songs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle(getResources().getString(R.string.library_label));
        // ask user permission to read storage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_READ_PERMISSION);
        }
    }

    // Uses the result from requestPermissions to decide action to take
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST_READ_PERMISSION:
                if (grantResults.length > 0) { // permission has been granted let's do our normal work
                    searchMusic();
                    SongsAdapter songsAdapter = new SongsAdapter(this, songs);
                    ListView songsList = findViewById(R.id.songs_list);
                    songsList.setOnItemClickListener(this);
                    songsList.setAdapter(songsAdapter);
                } else { // permission has been denied let's inform the user what they need to do
                    Toast.makeText(this, getResources().getString(R.string.notification1),
                            Toast.LENGTH_LONG).show();
                }
        }
    }

    // ListView click listener.
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, NowPlaying.class);
        intent.putParcelableArrayListExtra(Tags.SONGS_LIST, songs);
        intent.putExtra(Tags.CURRENT_SONG_ID, i);
        startActivity(intent);
    }

    //Queries the audio database
    private void searchMusic() {
        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };
        ContentResolver resolverCompat = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor songsCursor = resolverCompat.query(songUri,projection,null,null,null);

        if (songsCursor != null && songsCursor.moveToFirst()) { // get select the song properties to fetch
            //Get the ids of the columns we want to fetch data from
            int idColumn = songsCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int titleColumn = songsCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistColumn = songsCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumColumn = songsCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int dataColumn = songsCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int durationColumn = songsCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

            do { // fetch the properties from each song in music database
                long Id = songsCursor.getLong(idColumn);
                String title = songsCursor.getString(titleColumn);
                String artist = songsCursor.getString(artistColumn);
                String album = songsCursor.getString(albumColumn);
                long duration = songsCursor.getLong(durationColumn);
                String path = songsCursor.getString(dataColumn);
                songs.add(new Song(Id, title, artist, album, duration, path)); // create the current song object
            } while (songsCursor.moveToNext());
        }

        try {
            songsCursor.close();// we're done using our cursor so close it to free resources
        } catch (NullPointerException e) {
        }
    }
}
