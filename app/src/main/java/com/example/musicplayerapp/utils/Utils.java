package com.example.musicplayerapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.musicplayerapp.R;
import com.example.musicplayerapp.app.Application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class Utils {
    private static int sTheme;
    public final static int THEME_DEFAULT = 0;
    public final static int THEME_WHITE = 1;

    public static void changeToTheme(Activity activity, int theme) {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        switch (sTheme) {
            default:
            case THEME_DEFAULT:
                activity.setTheme(R.style.AppTheme);
                break;
            case THEME_WHITE:
                activity.setTheme(R.style.AppThemeLight);
                break;
        }
    }

    public static String musicServiceTimeConverter(long millis) {
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;
        String temp = "", a = "", b = "", c = "";
        if (hour == 0)
            a = "00";
        else if (hour <= 9 && hour > 0)
            a = "0" + String.valueOf(hour);
        else
            a = String.valueOf(hour);
        if (minute == 0)
            b = "00";
        else if (minute <= 9 && minute > 0)
            b = "0" + String.valueOf(minute);
        else
            b = String.valueOf(minute);
        if (second == 0)
            c = "00";
        else if (second <= 9 && second > 0)
            c = "0" + String.valueOf(second);
        else
            c = String.valueOf(second);
        temp = a + ":" + b + ":" + c;
        return temp;
    }

    public static Bitmap getThumbnail(Activity activity, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = activity.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        assert input != null;
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > Application.THUMBNAIL_SIZE) ? (originalSize / Application.THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = activity.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }

    public static void loadRoundPicture(Context context, Uri uri, ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .centerCrop()
                .placeholder(R.drawable.ic_menu_camera)
                .transform(new CircleTransform(context))
                .into(imageView);
    }

    public static void loadSquarePicture(Context context, Uri uri, ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .fitCenter()
                .placeholder(R.drawable.ic_menu_camera)
                .into(imageView);
    }
}
