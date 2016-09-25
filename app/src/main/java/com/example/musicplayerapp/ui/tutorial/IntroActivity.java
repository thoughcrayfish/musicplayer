package com.example.musicplayerapp.ui.tutorial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.musicplayerapp.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;


public class IntroActivity extends AppIntro
{
    private SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        checkIfFirstLaunch();

//        addSlide(FirstSlide);
//        addSlide(SecondSlide);

//        // Ask for CAMERA permission on the second slide
//        askForPermissions(new String[]{Manifest.permission.CAMERA}, 2);


        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance("test1", "testDescription1", R.drawable.ic_arrow_back_black_24dp, R.color.colorAccent));
        addSlide(AppIntroFragment.newInstance("test2", "testDescription2", R.drawable.ic_arrow_back_white_24dp, R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance("test3", "testDescription3", R.drawable.ic_arrow_back_white_24dp, R.color.colorPrimary));

//        askForPermissions(new String[]{Manifest.permission.WAKE_LOCK}, 1);
//        askForPermissions(new String[]{Manifest.permission.VIBRATE}, 1);
//        askForPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
//        askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        // OPTIONAL METHODS
        setFadeAnimation();
        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment)
    {
        super.onSkipPressed(currentFragment);
        startMainActivity();
    }

    @Override
    public void onDonePressed(Fragment currentFragment)
    {
        super.onDonePressed(currentFragment);
        setLaunched();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment)
    {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

    private void startMainActivity()
    {
        Intent intent = new Intent(this, TrackListFragmentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void checkIfFirstLaunch()
    {
        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                //  Initialize SharedPreferences
                prefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = prefs.getBoolean("firstStart", true);
                if (!isFirstStart)
                    {
                    startMainActivity();
                    }
            }
        });

        // Start the thread
        t.start();
    }

    void setLaunched()
    {
        SharedPreferences.Editor e = prefs.edit();

        //  Edit preference to make it false because we don't want this to run again
        e.putBoolean("firstStart", false);
        //  Apply changes
        e.apply();
        startMainActivity();
    }

}
