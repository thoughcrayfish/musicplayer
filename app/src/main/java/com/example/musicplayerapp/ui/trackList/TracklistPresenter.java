package com.example.musicplayerapp.ui.trackList;

import android.app.Activity;
import android.content.Context;
import android.widget.SeekBar;

import com.example.musicplayerapp.repository.model.SongObject;

import java.util.ArrayList;

public interface TracklistPresenter
{
    interface OnListGetListener
    {
        void onSuccess(ArrayList<SongObject> songsList);
        void onError(String errorString);
    }
    void getSongsList(Context context, OnListGetListener listener);

    void playTrack(Activity activity, int songIndex);
    void pauseTrack();
    void resumeTrack();

    void checkIfPlaying(OnPlayingListener listener);
    interface OnPlayingListener
    {
        void onPlaying();
        void onPaused();
    }

}
