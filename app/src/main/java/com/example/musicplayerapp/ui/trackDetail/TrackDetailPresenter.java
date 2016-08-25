package com.example.musicplayerapp.ui.trackDetail;

import android.app.Activity;
import android.widget.SeekBar;

import com.example.musicplayerapp.repository.service.MusicService;

public interface TrackDetailPresenter
{
    void pauseTrack();
    void stopTrack();
    void nextTrack();
    void previousTrack();
    void playTrack();
    void toggleShuffle();

    void bindMusicService(Activity activity);
    MusicService getMusicService();
    int getSongDuration();
    void setSongDuration(Integer progress);

    interface shuffleToggleListener
    {
        void shuffleEnabled();
        void shuffleDisabled();
    }
    void checkIfShuffle(shuffleToggleListener listener);

    boolean checkIfPlaying();
}
