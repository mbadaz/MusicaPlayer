package com.example.mbada.musicaplayer;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * The song object stores information retrieved from the audio media database.
 * This information is used to play a song through MediaPlayer and display the
 * attributes of the song in the UI.
 */

public class Song implements Parcelable {
    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
    private final String songTitle;
    private final String artist;
    private final String album;
    private final String filePath;
    private final String songId;
    private final String duration;

    public Song(long songId, String title, String artist, String album, long duration, String filePath) {
        this.songTitle = title;
        this.artist = artist;
        this.album = album;
        this.songId = String.valueOf(songId);
        this.filePath = filePath;
        this.duration = formatTime(duration);
    }

    public Song(Parcel in) {
        String[] data = new String[6];

        in.readStringArray(data);
        this.songId = data[0];
        this.songTitle = data[1];
        this.artist = data[2];
        this.album = data[3];
        this.duration = data[4];
        this.filePath = data[5];
    }

    //Rounds the duration rounded off to 2 decimal places
    @SuppressLint("DefaultLocale")
    public static final String formatTime(long value) {
        int minutes = (int) Math.floor(value / 1000 / 60);
        int seconds = (int) ((value / 1000) - (minutes * 60));
        return minutes + ":" + String.format("%02d", seconds);
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getSongId() {
        return songId;
    }

    public String getDuration() {
        return duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.songId, this.songTitle,
                this.artist, this.album, this.duration, this.filePath});
    }
}
