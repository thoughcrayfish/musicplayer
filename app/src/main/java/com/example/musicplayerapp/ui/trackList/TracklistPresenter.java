package com.example.musicplayerapp.ui.trackList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.widget.SeekBar;

import com.example.musicplayerapp.repository.model.SongObject;
import com.example.musicplayerapp.repository.service.MusicService;

import java.util.ArrayList;

public interface TracklistPresenter {
    void updateCurrentSongView();

    void setSongDuration(int progress);

    int getSongDuration();

    MusicService getMusicService();

    void createMusicService();

    void bindMusicService(Activity activity, Intent intent);

    void unBindMusicService(Activity activity);

    boolean getBindingState();

    interface OnListGetListener {
        void onSuccess(ArrayList<SongObject> songsList);

        void onError(String errorString);
    }

    void getSongsList(ContentResolver musicResolver, OnListGetListener listener);

    void playTrack(Activity activity, int songIndex);

    void pauseTrack();

    void resumeTrack();

    boolean checkIfPlaying();
}
