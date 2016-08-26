package com.example.musicplayerapp.ui.trackList;

import android.net.Uri;

import com.example.musicplayerapp.repository.model.SongObject;

import java.util.ArrayList;

public interface TracklistView {
    void playSong(int songIndex);

    void showCurrentPlayingSong(String artistName, String songName, Uri albumArt);
}
