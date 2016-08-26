package com.example.musicplayerapp.ui.Notifications;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Андрей on 26.08.2016.
 */
public class NotificationReturnSlot extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String action = (String) getIntent().getExtras().get("DO");
        assert action != null;
        if (action.equals("playPause")) {
            Log.i("NotificationReturnSlot", "volume");
            //Your code
        } else if (action.equals("next")) {
            //Your code
            Log.i("NotificationReturnSlot", "next");
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}