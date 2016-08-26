package com.example.musicplayerapp.ui.trackList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.musicplayerapp.R;
import com.example.musicplayerapp.adapters.TracklistAdapter;
import com.example.musicplayerapp.events.TrackSelectEvent;
import com.example.musicplayerapp.repository.model.SongObject;
import com.example.musicplayerapp.repository.service.MusicService;
import com.example.musicplayerapp.ui.AbstractActivity;
import com.example.musicplayerapp.ui.trackDetail.TrackDetailActivity;
import com.futuremind.recyclerviewfastscroll.FastScroller;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class TracklistActivity extends AbstractActivity implements TracklistView, NavigationView.OnNavigationItemSelectedListener,
        TracklistPresenter.OnListGetListener, SeekBar.OnSeekBarChangeListener
{
    private TracklistPresenter presenter;
    final Context context = this;
    private boolean isPause = false;
    private Handler handler = new Handler();
    private EventBus bus = EventBus.getDefault();

    @BindView(R.id.imageButton_playingRightNow_playPause) ImageButton playPauseButton;
    @BindView(R.id.imageView_currentSong_album_art) ImageView currentSongThumbnail;
    @BindView(R.id.seekBar_currentSong) SeekBar musicSeekBar;
    @BindView(R.id.recyclerView_trackList) RecyclerView recyclerView;
    @BindView(R.id.fastscroll) FastScroller fastScroller;
    @BindView(R.id.textView_currentSong_artist) TextView currentSongArtist;
    @BindView(R.id.textView_currentSong_title) TextView currentSongTitle;
    @BindView(R.id.relativeLayout_playingRightNow) RelativeLayout playingRightNowView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracklist);
        ButterKnife.bind(this);
        init();

        presenter = new TracklistPresenterImp(this);
        presenter.createMusicService();
    }

    private void init()
    {
        musicSeekBar.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        Intent playIntent = new Intent(context, MusicService.class);
        presenter.getSongsList(getContentResolver(), this);
        presenter.bindMusicService(this, playIntent);
        setMusicSeekBar();
        checkIfPlaying();
        if (presenter.getMusicService()!= null)
            presenter.updateCurrentSongView();

        bus.register(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        bus.unregister(this);
    }
    @Override
    protected void onStop()
    {
        super.onStop();
//        if (presenter.getMusicService()!= null)
//        presenter.unBindMusicService(this);
    }
    @Subscribe
    public void onEvent(TrackSelectEvent event)
    {
        playSong(event.getTrackSelectedNumber());
    }

    @OnClick({R.id.imageButton_playingRightNow_playPause, R.id.relativeLayout_playingRightNow})
    public void onClick(View view)
    {
        {
            switch (view.getId())
            {
                case R.id.imageButton_playingRightNow_playPause:
                    if (!isPause) presenter.pauseTrack();
                    else presenter.resumeTrack();

                    checkIfPlaying();
                    break;

                case R.id.relativeLayout_playingRightNow:
                    currentTrackOnClick();
                    break;
            }
        }
    }
    void checkIfPlaying()
    {
        boolean isPlaying = presenter.checkIfPlaying();
        if (isPlaying) onPlaying();
        else onPaused();
    }
    void setMusicSeekBar()
    {
        TracklistActivity.this.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if(presenter.getMusicService() != null)
                {
                    int mCurrentPosition = presenter.getSongDuration();
                    musicSeekBar.setProgress(mCurrentPosition);
                }
                handler.postDelayed(this, 400);  // time in miliseconds to update the bar
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        if (fromUser)
        {
            if(presenter.getMusicService() != null)
            {
                presenter.setSongDuration(progress);
            }
        }
    }
    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tracklist, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
//            startActivity(SettingsActivity.class, false, null, null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_share)
        {

        } else if (id == R.id.nav_send)
        {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void currentTrackOnClick()
    {
        loadTrackDetailActivity();
    }

    public void loadTrackDetailActivity()
    {
        startActivity(TrackDetailActivity.class, false);
    }
    @Override
    public void playSong(int songIndex)
    {
        presenter.playTrack(this, songIndex);
        presenter.updateCurrentSongView();
    }

    @Override
    public void showCurrentPlayingSong(String artistName, String songName, Uri albumArt)
    {
        currentSongTitle.setText(songName);
        currentSongArtist.setText(artistName);
        Glide.with(this).load(albumArt)
                .placeholder(R.drawable.ic_menu_camera)
                .error(R.drawable.ic_menu_manage)
                .crossFade().centerCrop().into(currentSongThumbnail);
    }

    @Override
    public void onSuccess(ArrayList<SongObject> songsList)
    {
        TracklistAdapter songsAdapter = new TracklistAdapter(this, songsList);
        recyclerView.setAdapter(songsAdapter);
        fastScroller.setRecyclerView(recyclerView);
        checkIfPlaying();
    }

    @Override
    public void onError(String errorString)
    {
        // todo
    }

    public void onPlaying()
    {
        isPause = false;
        playPauseButton.setImageResource(R.drawable.ic_pause_circle_outline_white_24dp);
    }

    public void onPaused()
    {
        isPause = true;
        playPauseButton.setImageResource(R.drawable.ic_play_circle_outline_white_24dp);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {

    }

}
