package com.example.musicplayerapp.repository.model;

import android.content.ContentUris;
import android.graphics.Bitmap;
import android.net.Uri;

public class SongObject
{
    long id;
    String artist;
    String title;
    String album;
    long duration;
    Uri artUri;

    public SongObject(long id, String artist, String title, String album, long duration, Uri artUri)
    {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.album = album;
        this.duration = duration;
        this.artUri = artUri;
    }

    public long getId()
    {
        return id;
    }
    public String getArtist()
    {
        return artist;
    }
    public String getTitle()
    {
        return title;
    }
    public String getAlbum()
    {
        return album;
    }
    public long getDuration()
    {
        return duration;
    }
    public Uri getArtUri()
    {
        return artUri;
    }

}


