package com.example.musicplayerapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicplayerapp.R;
import com.example.musicplayerapp.repository.model.SongObject;
import com.example.musicplayerapp.ui.trackList.TracklistActivity;
import com.example.musicplayerapp.utils.Utils;

import java.util.ArrayList;

public class TracklistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private View view;
    private Context context;
    private TracklistActivity tracklistActivity;
    private TracklistViewHolder viewHolder;
    private ArrayList<SongObject> songsList;
    private static LayoutInflater songInflater = null;

    public TracklistAdapter(TracklistActivity tracklistActivity, Context context, ArrayList<SongObject> songs)
    {
        this.context = context;
        this.tracklistActivity = tracklistActivity;
        songsList=songs;
        songInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType)
    {
        view = songInflater.inflate(R.layout.item_song, parent, false);
        viewHolder = new TracklistViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        if (!viewHolder.assigned)
        {
            SongObject currSong = songsList.get(position);
            viewHolder.songView.setText(currSong.getTitle());
            viewHolder.artistView.setText(currSong.getArtist());
            viewHolder.durationView.setText(Utils.musicServiceTimeConverter(currSong.getDuration()));

            viewHolder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    tracklistActivity.playSong(position);
                }
            });
            viewHolder.assigned = true;
        }
    }

    @Override
    public int getItemCount()
    {
        return (null != songsList ? songsList.size() : 0);
    }
}
