package com.example.musicplayerapp.ui.trackList;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;

import com.example.musicplayerapp.app.Application;
import com.example.musicplayerapp.repository.model.SongObject;
import com.example.musicplayerapp.repository.service.MusicService;

import java.util.ArrayList;

public class TracklistPresenterImp implements TracklistPresenter
{

    public MusicService musicService;
    private Intent playIntent;
    private boolean musicBound=false;
    private TracklistView view;
    private TracklistInteractor interactor;
    private ArrayList<SongObject> songsList = new ArrayList<SongObject>();

    public TracklistPresenterImp (TracklistView view)
    {
        this.view = view;
        interactor = new TracklistInteractorImp();
    }

    @Override
    public void updateCurrentSongView()
    {
        view.showCurrentPlayingSong(musicService.getCurrentSong().getArtist(),
        musicService.getCurrentSong().getTitle(),
        musicService.getCurrentSong().getArtUri());
    }

    @Override
    public void createMusicService()
    {
        if (musicService == null)
        {
            musicService = new MusicService();
        }
    }

    @Override
    public void getSongsList(ContentResolver musicResolver, final TracklistPresenter.OnListGetListener listener, Intent intent, Activity activity)
    {
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst())
        {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int albumId = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int songDurationColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            //add songs to list
            do
            {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisAlbum = musicCursor.getString(albumColumn);
                long thisSongDuration = musicCursor.getLong(songDurationColumn);

                // grabbing art for track
                Uri uri = ContentUris.withAppendedId(Application.artworkUri,
                        albumId);

                // adding final object to the array
                songsList.add(new SongObject(thisId, thisArtist, thisTitle, thisAlbum, thisSongDuration, uri));
            }
            while (musicCursor.moveToNext());

            if (songsList != null)
            {
                listener.onSuccess(songsList);
            }
            else listener.onError("error");

            if(playIntent == null)
            {
                this.playIntent = intent;
//                playIntent = new Intent(context, MusicService.class);
                activity.bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
                musicService.startService(playIntent);
            }
        }
    }
    @Override
    public void playTrack(Activity activity, int songIndex)
    {
        musicService.setSong(songIndex);
        musicService.playSong();
    }
    @Override
    public void pauseTrack()
    {
        musicService.pausePlayer();
    }
    @Override
    public void resumeTrack()
    {
        musicService.go();
    }

    @Override
    public boolean checkIfPlaying()
    {
        return musicService != null && musicService.isPng();
    }

    @Override
    public int getSongDuration()
    {
        return musicService.getPosn() / 1000;
    }

    @Override
    public MusicService getMusicService()
    {
        return musicService;
    }

    @Override
    public void setSongDuration(int progress)
    {
        musicService.changeSongPosition(progress * 1000);
    }
    private ServiceConnection musicConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicService = binder.getService();
            //pass list
            musicService.setList(songsList);
            musicBound = true;
            updateCurrentSongView();
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            musicBound = false;
        }
    };

}
