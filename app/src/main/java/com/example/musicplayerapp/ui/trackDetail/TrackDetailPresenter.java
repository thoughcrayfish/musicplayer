package com.example.musicplayerapp.ui.trackDetail;

import android.widget.SeekBar;

public interface TrackDetailPresenter
{
    void pauseTrack();
    void stopTrack();
    void nextTrack();
    void previousTrack();
    void playTrack();
    void toggleShuffle();

    interface shuffleToggleListener
    {
        void shuffleEnabled();
        void shuffleDisabled();
    }
    void checkIfShuffle(shuffleToggleListener listener);

    void checkIfPlaying(OnPlayingListener listener);
    interface OnPlayingListener
    {
        void onPlaying();
        void onPaused();
    }

}
