package com.example.musicplayerapp.ui.trackDetail;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.musicplayerapp.R;
import com.example.musicplayerapp.ui.AbstractActivity;
import com.example.musicplayerapp.utils.Utils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TrackDetailActivity extends AbstractActivity implements TrackDetailView, SeekBar.OnSeekBarChangeListener, TrackDetailPresenter.OnPlayingListener
    , TrackDetailPresenter.shuffleToggleListener
{
    @BindView(R.id.imageButton_playerControls_playPause) ImageButton playPauseButton;
    @BindView(R.id.imageButton_playerControls_next) ImageButton nextButton;
    @BindView(R.id.imageButton_playerControls_previous) ImageButton previousButton;
    @BindView(R.id.imageButton_playerControls_shuffle) ImageButton shuffleButton;
    @BindView(R.id.imageButton_playerControls_back) ImageButton backButton;
    @BindView(R.id.imageButton_playerControls_stop) ImageButton stopButton;
    @BindView(R.id.imageView_trackDetail_albumArt) ImageView albumArtView;
    @BindView(R.id.textView_trackDetail_artist_name) TextView artistNameView;
    @BindView(R.id.textView_trackDetail_song_name) TextView songNameView;
    @BindView(R.id.textView_trackDetail_songDuration) TextView songDurationView;
    @BindView(R.id.seekBar_playerControl) SeekBar musicSeekBar;

    Bitmap bitmap;
    private boolean isPause = false;
    TrackDetailPresenterImp presenter;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackdetail);
        ButterKnife.bind(this);
        musicSeekBar.setOnSeekBarChangeListener(this);

        animateButtons();
        setTransluscentStatusBar();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        presenter = new TrackDetailPresenterImp(this);
        presenter.bindMusicService();

        // Updating song progress bar through requesting duration from presenter // - maybe move to presenter?
        musicSeekBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout()
            {
                musicSeekBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) musicSeekBar.getLayoutParams();
                params.setMargins(0, - musicSeekBar.getHeight() / 2, 0, - musicSeekBar.getHeight() / 2);
                musicSeekBar.setLayoutParams(params);
                // I suggest you set the seekbar visible after this so that it won't jump
            }
        });
        TrackDetailActivity.this.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if(presenter.musicService != null)
                {
                    int mCurrentPosition = presenter.getSongDuration();
                    musicSeekBar.setProgress(mCurrentPosition);
                }
                handler.postDelayed(this, 400);  // time in miliseconds to update the bar
            }
        });
    }

    @OnClick({R.id.imageButton_playerControls_playPause, R.id.imageButton_playerControls_next, R.id.imageButton_playerControls_previous, R.id.imageButton_playerControls_back, R.id.imageButton_playerControls_shuffle})
    public void onClick(View view)
    {
        {
            switch (view.getId())
            {
                case R.id.imageButton_playerControls_playPause:
                    if (!isPause)
                    {
                        presenter.pauseTrack();
                    }
                    else
                    {
                        presenter.playTrack();
                    }
                    break;
                case R.id.imageButton_playerControls_next:
                    presenter.nextTrack();
                    break;
                case R.id.imageButton_playerControls_previous:
                    presenter.previousTrack();
                    break;
                case R.id.imageButton_playerControls_back:
                    finish();
                    break;
                case R.id.imageButton_playerControls_shuffle:
                    presenter.toggleShuffle();
                    break;
                case R.id.imageButton_playerControls_stop:
                    presenter.stopTrack();
                    break;
            }
            checkIfPlaying();
            checkIfShuffle();
        }
    }
    protected void animateButtons()
    {
        Animation animScale, animAlpha;
        animScale = AnimationUtils.loadAnimation(this, R.anim.play_button_scale_anim);
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.buttons_alpha_anim);
        playPauseButton.startAnimation(animScale);
        previousButton.startAnimation(animAlpha);
        nextButton.startAnimation(animAlpha);
        backButton.startAnimation(animAlpha);
        shuffleButton.startAnimation(animAlpha);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_track, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        if (fromUser)
        {
            if(presenter.musicService != null)
            {
                presenter.setSongDuration(progress);
            }
        }
    }
    @Override
    public void showDetailView(String artistName, String songName, String duration, Uri albumArt)
    {
        songNameView.setText(songName);
        artistNameView.setText(artistName);
        songDurationView.setText(duration);

        checkIfShuffle();
//        albumArtView.setImageResource(R.drawable.ic_stop_white_24dp);
        try
        {
            bitmap = Utils.getThumbnail(this, albumArt);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        Glide.with(this).load(albumArt).placeholder(R.drawable.abstract_roh)
                .error(R.drawable.ic_menu_camera)
                .centerCrop().into(albumArtView);
    }
    void setTransluscentStatusBar()
    {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
    void checkIfShuffle()
    {
        presenter.checkIfShuffle(this);
    }
    void checkIfPlaying()
    {
        presenter.checkIfPlaying(this);
    }
    @Override
    public void onPlaying()
    {
        isPause = false;
        playPauseButton.setImageResource(R.drawable.ic_pause_white_36dp);
    }

    @Override
    public void onPaused()
    {
        isPause = true;
        playPauseButton.setImageResource(R.drawable.ic_play_arrow_white_36dp);
    }

    @Override
    public void shuffleEnabled()
    {
        shuffleButton.setAlpha(1f);
    }

    @Override
    public void shuffleDisabled()
    {
        shuffleButton.setAlpha(.25f);
    }

    // updating song through SeekBar
    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {

    }
}