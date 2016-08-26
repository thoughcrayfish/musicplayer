package com.example.musicplayerapp.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.musicplayerapp.R;
import com.example.musicplayerapp.app.Application;
import com.example.musicplayerapp.utils.Utils;

/**
 * Created by Андрей on 12.08.2016.
 */
public class AbstractActivity extends AppCompatActivity {
    Animation animScale, animAlpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);

    }

    protected void startActivity(Class activityClass, boolean lockBackAction, Bundle extras) {
        Intent intent = new Intent(this, activityClass);
        if (lockBackAction)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        if (extras != null) intent.putExtras(extras);

        startActivity(intent);
    }

    protected void startActivity(Class activityClass, boolean lockBackAction) {
        Intent intent = new Intent(this, activityClass);
        if (lockBackAction)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }

    public void animateAlpha(View view) {
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        view.startAnimation(animAlpha);
    }

    public void animateAlpha(View view, long duration) {
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        animAlpha.setDuration(duration * 1000);
        view.startAnimation(animAlpha);
    }

    public void animateScale(View view) {
        animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
        view.startAnimation(animScale);
    }

    public void animateScale(View view, long duration) {
        animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
        animScale.setDuration(duration * 1000);
        view.startAnimation(animScale);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void setTransluscentStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }
}
