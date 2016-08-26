package com.example.musicplayerapp.events;

/**
 * Created by Андрей on 25.08.2016.
 */
public class TrackSelectEvent {
    private int trackSelectedNumber;

    public TrackSelectEvent(int trackNumber) {
        this.trackSelectedNumber = trackNumber;
    }

    public int getTrackSelectedNumber() {
        return trackSelectedNumber;
    }

}
