package com.example.musicplayerapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicplayerapp.R;
import com.example.musicplayerapp.events.TrackSelectEvent;
import com.example.musicplayerapp.repository.model.SongObject;
import com.example.musicplayerapp.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TracklistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public class TracklistViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.textView_song_title) TextView songView;
        @BindView(R.id.textView_song_artist) TextView artistView;
        @BindView(R.id.textView_song_duration) TextView durationView;
        @BindView(R.id.imageView_songItem)ImageView trackAlbumArt;
        public TracklistViewHolder(View songItem)
        {
            super(songItem);
            ButterKnife.bind(this, songItem);
        }
    }

    Context context;
    private TracklistViewHolder viewHolder;
    private ArrayList<SongObject> songsList;
    private static LayoutInflater songInflater = null;

    public TracklistAdapter(Context c, ArrayList<SongObject> songs)
    {
        songsList=songs;
        this.context = c;
        songInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType)
    {
        View view = songInflater.inflate(R.layout.item_song, parent, false);
        viewHolder = new TracklistViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {       try
            {
                SongObject currSong = songsList.get(position);
                viewHolder.songView.setText(currSong.getTitle());
                viewHolder.artistView.setText(currSong.getArtist());
                viewHolder.durationView.setText(Utils.musicServiceTimeConverter(currSong.getDuration()));
                Utils.loadSquarePicture(context, currSong.getArtUri(), viewHolder.trackAlbumArt);
            }
            catch (Exception e)
            {

            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    EventBus.getDefault().post(new TrackSelectEvent(position));
                }
            });
    }

    @Override
    public int getItemCount()
    {
        return (null != songsList ? songsList.size() : 0);
    }
}
