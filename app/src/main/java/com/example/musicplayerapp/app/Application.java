package com.example.musicplayerapp.app;

import android.content.Context;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.musicplayerapp.R;

public class Application extends android.app.Application {
    final public static Uri artworkUri = Uri
            .parse("content://media/external/audio/albumart");
    final public static Integer THUMBNAIL_SIZE = 64;
}
