package com.example.musicplayerapp.ui.trackDetail;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.musicplayerapp.repository.service.MusicService;
import com.example.musicplayerapp.utils.Utils;

public class TrackDetailPresenterImp implements TrackDetailPresenter
{
    boolean bound = false;
    public MusicService musicService;
    TrackDetailActivity view;
    TrackDetailInteractorImp interactor;

    @Override
    public void checkIfPlaying(TrackDetailPresenter.OnPlayingListener listener)
    {
        if (musicService != null)
        {
            boolean isPlaying = musicService.isPng();
            if (isPlaying)
                listener.onPlaying();

            else listener.onPaused();
        }
    }

    public void bindMusicService()
    {
        if (musicService == null)
        {
            Intent intent = new Intent(view, MusicService.class);
            view.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }
    public TrackDetailPresenterImp (TrackDetailActivity view)
    {
        this.view = view;
        interactor = new TrackDetailInteractorImp();
    }

    public void updateDetailView()
    {
        view.showDetailView(musicService.getCurrentSong().getArtist(),
                            musicService.getCurrentSong().getTitle(),
                            Utils.musicServiceTimeConverter(musicService.getCurrentSong().getDuration()),
                            musicService.getCurrentSong().getArtUri());

    }

    private ServiceConnection mConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service)
        {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            bound = true;
            updateDetailView();
        }


    @Override
    public void onServiceDisconnected(ComponentName arg0)
        {
            bound = false;
        }
    };

    @Override
    public void pauseTrack()
    {
        musicService.pausePlayer();
    }

    @Override
    public void stopTrack()
    {
        musicService.stopSelf();
    }

    @Override
    public void nextTrack()
    {
        musicService.playNext();
        updateDetailView();
    }

    @Override
    public void previousTrack()
    {
        musicService.playPrev();
        updateDetailView();
    }

    @Override
    public void playTrack()
    {
        musicService.go();
    }

    @Override
    public void toggleShuffle()
    {
        musicService.setShuffle();
    }

    @Override
    public void checkIfShuffle(shuffleToggleListener listener)
    {
        if (musicService.getShuffle()) listener.shuffleEnabled();
        else listener.shuffleDisabled();
    }

    public Integer getSongDuration()
    {
        return musicService.getPosn() / 1000;
    }
    public void setSongDuration(Integer progress)
    {
        musicService.changeSongPosition(progress * 1000);
    }
}
