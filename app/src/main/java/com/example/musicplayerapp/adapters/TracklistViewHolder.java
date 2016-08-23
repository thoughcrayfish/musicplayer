package com.example.musicplayerapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.musicplayerapp.R;
import com.example.musicplayerapp.ui.trackList.TracklistActivity;

public class TracklistViewHolder extends RecyclerView.ViewHolder
{
    protected TextView songView;
    protected TextView artistView;
    protected TextView durationView;
    protected boolean assigned;
    public TracklistViewHolder(View songItem)
    {
        super(songItem);

        this.songView = (TextView)songItem.findViewById(R.id.textView_song_title);
        this.artistView = (TextView)songItem.findViewById(R.id.textView_song_artist);
        this.durationView = (TextView) songItem.findViewById(R.id.textView_song_duration);
    }


}
