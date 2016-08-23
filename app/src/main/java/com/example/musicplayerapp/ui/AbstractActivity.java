package com.example.musicplayerapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.example.musicplayerapp.R;
import com.example.musicplayerapp.app.Application;
import com.example.musicplayerapp.utils.Utils;

/**
 * Created by Андрей on 12.08.2016.
 */
public class AbstractActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Utils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);

    }

    protected void startActivity(Class activityClass, boolean lockBackAction, String aditionalParameterName, String aditionalParameterValue)
    {
        Intent intent = new Intent(this, activityClass);
        if (lockBackAction) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        if (aditionalParameterName != null && aditionalParameterValue != null)
        {
            intent.putExtra(aditionalParameterName, aditionalParameterValue);
        }
        startActivity(intent);
    }
}
