package com.example.musicplayerapp.utils;

import com.example.musicplayerapp.repository.model.SongObject;

import java.util.Comparator;

public class ObjectComparator implements Comparator<SongObject> {

    public int compare(SongObject obj1, SongObject obj2) {
        return obj1.getTitle().compareTo(obj2.getTitle());
    }

}