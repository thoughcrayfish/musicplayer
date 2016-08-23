package com.example.musicplayerapp.ui.trackList;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import com.example.musicplayerapp.repository.model.SongObject;
import com.example.musicplayerapp.repository.service.MusicService;

import java.io.FileDescriptor;
import java.util.ArrayList;

public class TracklistInteractorImp implements TracklistInteractor
{

}
