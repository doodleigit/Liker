package com.doodle.Tool;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;


import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Operation {

    public static int height = Resources.getSystem().getDisplayMetrics().heightPixels;
    public static int width = Resources.getSystem().getDisplayMetrics().widthPixels;


    public static long getTimeDifferenceInMillis(String dateTime) {

        //  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        long currentTime = new Date().getTime();
        long endTime = 0;

        try {
            //Parsing the user Inputed time ("yyyy-MM-dd HH:mm:ss")
            endTime = dateFormat.parse(dateTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }

        if (currentTime > endTime)
            return currentTime - endTime;
        else
            return 0;
    }

    public static String getDurationBreakdown(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        if (days >= 1) {
            sb.append(days);
            sb.append("d ");
        }
        if (hours >= 1) {
            sb.append(hours);
            sb.append("h ");
        }
        long mMinutes = hours * 60;
        if (minutes < 60 && minutes >= 0 && minutes > mMinutes) {

            sb.append(minutes);
            sb.append("m ");
        }

        // sb.append(seconds);
        //sb.append(" Seconds");

        return (sb.toString());
    }


    public static boolean isDate(String str) {
        return str.matches("[+-]?\\d*(\\/\\d+)?");
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static Bitmap getFacebookProfilePicture(String userID) {
        Bitmap bitmap = null;
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }


    public static void secondarySplashScreen(View v) {
        /*USE SPLASH SCREEEN*/
//        View v = (View)findViewById(R.id.imageView);

        // Animation animation =new ScaleAnimation(1,5,1,5,.5f,.5f);
        Animation animation = new TranslateAnimation(0, -1500f, 0, 0);
        animation.setStartOffset(500);
        animation.setInterpolator(new AccelerateInterpolator(200));
        animation.setDuration(5000);
        animation.setFillAfter(true);

        Animation animation1 = new AlphaAnimation(1f, 0f);
        animation1.setDuration(2000);
        animation1.setFillAfter(true);


        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(animation);

        animationSet.setDuration(2000);
        animationSet.setFillAfter(true);
        v.startAnimation(animationSet);
        /*USE SPLASH SCREEEN*/
    }


    public static void toggleCustomise(Context context, ActionBarDrawerToggle toggle, String userId) {
        if (!TextUtils.isEmpty(userId)) {

            Bitmap bitmap = Operation.getFacebookProfilePicture(userId);
            Bitmap bitmapResized = Bitmap.createScaledBitmap(bitmap, 90, 90, false);
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmapResized);
            roundedBitmapDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()) / 2.0f);
            roundedBitmapDrawable.setCircular(true);
            toggle.setHomeAsUpIndicator(roundedBitmapDrawable);
        } else {
            // toggle.setHomeAsUpIndicator(R.drawable.ic_sentiment_satisfied_black_24dp);

        }
    }

    public static void secondTimeActiveSplashScreen(PrefManager prefManager, View view) {
        boolean isSecondTimeLaunch = prefManager.isSecondTimeLaunch();

        if (isSecondTimeLaunch) {
            view.setVisibility(View.VISIBLE);
            Operation.secondarySplashScreen(view);

        }

        if (!prefManager.isFirstTimeLaunch()) {
            if (isSecondTimeLaunch) {
                view.setVisibility(View.VISIBLE);
                Operation.secondarySplashScreen(view);
            } else {
                view.setVisibility(View.GONE);
                isSecondTimeLaunch = true;
                prefManager.setSecondTimeLaunch(isSecondTimeLaunch);
            }


        }
    }


    public static SpannableStringBuilder getSpannableStringBuilder(String fixedText) {


        String fixed = fixedText;

//                    String  h2="Click here for more.";
        //  String tripTitle = "Trip: "+object.getTripTitle();

//        SpannableString spannableStr = new SpannableString(fixed);
//        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#46ADE3"));
//        spannableStr.setSpan(foregroundColorSpan, 46, fixed.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//
//        SpannableStringBuilder builder = new SpannableStringBuilder();
//        SpannableString spannableUserStr = new SpannableString(userName);
//        spannableUserStr.setSpan(new ForegroundColorSpan(Color.BLACK), 0, userName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableUserStr.setSpan(new StyleSpan(Typeface.BOLD), 0, userName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        // SpannableString spannableUserStr = new SpannableString(fixed);
        // builder.append(spannableUserStr);
        //   builder.append(fixed);

        SpannableString spannableString = new SpannableString(fixed);
        String url = "https://developer.android.com";
        spannableString.setSpan(new URLSpan(url), 0, fixed.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, fixed.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, fixed.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(spannableString);
        return builder;
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getFormattedDateFromTimestamp(long timestampInMilliSeconds) {

        Date systemDate = Calendar.getInstance().getTime();
        systemDate.setTime(timestampInMilliSeconds);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestampInMilliSeconds);
        int postYear = cal.get(Calendar.YEAR);
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        //String formattedDate=new SimpleDateFormat("MMM d, yyyy").format(date);
        if (postYear == thisYear) {
            String formattedDate = new SimpleDateFormat("d MMM HH:mm aa").format(timestampInMilliSeconds);
            return formattedDate;
        } else {
            String formattedDate = new SimpleDateFormat("d MMM, yyyy HH:mm aa").format(timestampInMilliSeconds);
            return formattedDate;
        }
    }

//
//    public static String getTime(long mili){
//        // milliseconds
//       // long miliSec = 3010;
//
//        // Creating date format
//        DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
//
//        // Creating date from milliseconds
//        // using Date() constructor
//        Date result = new Date(mili);
//
//        Date systemDate = Calendar.getInstance().getTime();
//        long myMili=systemDate.getTime();
//        Date myResult=new Date(myMili);
//
//        // Formatting Date according to the
//        // given format
//        System.out.println(simple.format(result));
//    }
}
