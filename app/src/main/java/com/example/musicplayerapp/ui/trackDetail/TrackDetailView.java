package com.example.musicplayerapp.ui.trackDetail;

import android.net.Uri;

public interface TrackDetailView

{
    void showDetailView(String artistName, String songName, String duration, Uri albumArt);
}
