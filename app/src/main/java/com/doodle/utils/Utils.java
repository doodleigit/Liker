package com.doodle.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.doodle.Home.adapter.PostAdapter;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.model.PostTextIndex;
import com.doodle.Home.model.postshare.PostShareItem;
import com.doodle.R;
import com.doodle.utils.fragment.Network;
import com.marcoscg.materialtoast.MaterialToast;
import com.vanniktech.emoji.EmojiTextView;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager mConnectManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectManager != null) {
            NetworkInfo[] mNetworkInfo = mConnectManager.getAllNetworkInfo();
            for (int i = 0; i < mNetworkInfo.length; i++) {
                if (mNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    return true;
            }
        }



        return false;
    }

    public static void toastMatCol(Context context,String message,int image,int color) {
//        MaterialToast.makeText(this, "Hello, I'm a material toast!",
//                R.mipmap.ic_launcher, Toast.LENGTH_SHORT).setBackgroundColor(Color.RED).show();

        new MaterialToast(context)
                .setMessage(message)
                .setIcon(image)
                .setDuration(Toast.LENGTH_SHORT)
                .setBackgroundColor(color)
                .show();
    }
    public static void toast(Context context,String message,int image) {

        new MaterialToast(context)
                .setMessage(message)
                .setIcon(image)
                .setDuration(Toast.LENGTH_SHORT)
                .show();
    }




    public static void showCustomToast(Context context,View views,String message,int gravity) {


        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_toast_layout, (ViewGroup) views.findViewById(R.id.customToastLayout));
        TextView tv = (TextView) view.findViewById(R.id.textContent);
        tv.setText(message);

        // TODO: Get the custom layout and inflate it

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(gravity| Gravity.CENTER, 0, 120);
        toast.setView(view);
        toast.show();


        // TODO: Build a toast message that uses the custom layout

    }


    public static void showNetworkDialog(FragmentManager manager) {
        Network network = new Network();
        // TODO: Use setCancelable() to make the dialog non-cancelable
        network.setCancelable(false);
        network.show(manager, "NetworkDialogFragment");
    }

    public static void stripUnderlines(EmojiTextView tvPostEmojiContent) {

        Spannable s = new SpannableString(tvPostEmojiContent.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        tvPostEmojiContent.setText(s);
    }

    public static void stripUnderlines(ReadMoreTextView tvPostContent) {

        Spannable s = new SpannableString(tvPostContent.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        tvPostContent.setText(s);
    }

    private void setupAnimation(Context context,View view, final Animation animation,
                                final int animationID) {
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // If the button is checked, load the animation from the given resource
                // id instead of using the passed-in animation paramter. See the xml files
                // for the details on those animations.
                v.startAnimation(
                        AnimationUtils.loadAnimation(context, animationID));
            }
        });
    }


    public static void textLinkup(String originalText,String url, TextView tvContributorStatus) {
        SpannableString spannableStr = new SpannableString(originalText);

        URLSpan urlSpan = new URLSpan(url);

        spannableStr.setSpan(urlSpan, originalText.length()-22, originalText.length()-1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvContributorStatus.setMovementMethod(LinkMovementMethod.getInstance());
        tvContributorStatus.setText(spannableStr);
    }

    public static SpannableStringBuilder getSpannableStringBuilder(PostItem object) {

        String content=object.getPostText();
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString spannableUserStr = new SpannableString(content);
        builder.append(spannableUserStr);

        List<PostTextIndex> textIndices = object.getPostTextIndex();
        for (PostTextIndex temp : textIndices) {
            String postType = temp.getType();
            //  extractMentionUser(temp, postType);
            if (postType.equalsIgnoreCase("mention")) {
                String mentionString = temp.getText();
                // String html = "<p>An <a href='http://example.com/'><b>example</b></a> link.</p>";
                Document doc = Jsoup.parse(mentionString);
                Element link = doc.select("a").first();

                String text = doc.body().text(); // "An example link"
                Log.d("Text", text);
                String linkHref = link.attr("href"); // "http://example.com/"
                Log.d("URL: ", linkHref);
//                Utils.textLinkup(text,linkHref, holder.tvPostContent);
                String linkText = link.text(); // "example""

                String linkOuterH = link.outerHtml();
                // "<a href="http://example.com"><b>example</b></a>"
                String linkInnerH = link.html(); // "<b>example</b>"

                SpannableString spannableString = new SpannableString(text);//name
                String url = "https://developer.android.com";//url
                spannableString.setSpan(new URLSpan(linkHref), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(spannableString);
                return builder;
            }else {
                return builder;
            }

        }

        return builder;


    }


    public static List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {

            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }

    public static void stripUnderlines(PostAdapter.ViewHolder holder) {
        Spannable s = new SpannableString(holder.tvLinkScriptText.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }
        holder.tvLinkScriptText.setText(s);
    }

    public static class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }
    public static String getDomainName(String url) throws MalformedURLException {
        if (!url.startsWith("http") && !url.startsWith("https")) {
            url = "http://" + url;
        }
        URL netUrl = new URL(url);
        String host = netUrl.getHost();
        if (host.startsWith("www")) {
            host = host.substring("www".length() + 1);
        }
        return host;
    }

    public static boolean containsIllegalCharacters(String displayName) {
        final int nameLength = displayName.length();

        for (int i = 0; i < nameLength; i++) {
            final char hs = displayName.charAt(i);

            if (0xd800 <= hs && hs <= 0xdbff) {
                final char ls = displayName.charAt(i + 1);
                final int uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;

                if (0x1d000 <= uc && uc <= 0x1f77f) {
                    return true;
                }
            } else if (Character.isHighSurrogate(hs)) {
                final char ls = displayName.charAt(i + 1);

                if (ls == 0x20e3) {
                    return true;
                }
            } else {
                // non surrogate
                if (0x2100 <= hs && hs <= 0x27ff) {
                    return true;
                } else if (0x2B05 <= hs && hs <= 0x2b07) {
                    return true;
                } else if (0x2934 <= hs && hs <= 0x2935) {
                    return true;
                } else if (0x3297 <= hs && hs <= 0x3299) {
                    return true;
                } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d || hs == 0x3030 || hs == 0x2b55 || hs == 0x2b1c || hs == 0x2b1b || hs == 0x2b50) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isNullOrEmpty(String str) {
        if(str != null && !str.isEmpty())
            return false;
        return true;
    }



    public static SpannableStringBuilder getSpannableStringBuilder(String likes, String followers, int totalStars, String categoryName) {
        String headerInfo = String.format("%s Likes | %d Stars | %s Followers | %s", likes, totalStars, followers, "");
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString spannableUserStr = new SpannableString(headerInfo);
        builder.append(spannableUserStr);
        SpannableString spannableCategory = new SpannableString(String.format(categoryName));
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#60b2fc"));
        spannableCategory.setSpan(foregroundColorSpan, 0, categoryName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.append(spannableCategory);
        return builder;
    }


    public static String getMD5EncryptedString(String encTarget){
        MessageDigest mdEnc = null;
        try {
            mdEnc = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception while encrypting to md5");
            e.printStackTrace();
        } // Encryption algorithm
        mdEnc.update(encTarget.getBytes(), 0, encTarget.length());
        String md5 = new BigInteger(1, mdEnc.digest()).toString(16);
        while ( md5.length() < 32 ) {
            md5 = "0"+md5;
        }
        return md5;
    }



    public static String extractMentionText(PostItem item) {

        String mentionString = item.getPostText();
        // String html = "<p>An <a href='http://example.com/'><b>example</b></a> link.</p>";
        Document doc = Jsoup.parse(mentionString);
        Element link = doc.select("a").first();

        String text = doc.body().text(); // "An example link"
        Log.d("Text", text);
        String linkHref = link.attr("href"); // "http://example.com/"
        Log.d("URL: ", linkHref);
        String linkText = link.text(); // "example""
        String linkOuterH = link.outerHtml();
        // "<a href="http://example.com"><b>example</b></a>"
        String linkInnerH = link.html(); // "<b>example</b>"
        return text;

    }
    public static String extractMentionText(PostShareItem item) {

        String mentionString = item.getPostText();
        // String html = "<p>An <a href='http://example.com/'><b>example</b></a> link.</p>";
        Document doc = Jsoup.parse(mentionString);
        Element link = doc.select("a").first();

        String text = doc.body().text(); // "An example link"
        Log.d("Text", text);
        String linkHref = link.attr("href"); // "http://example.com/"
        Log.d("URL: ", linkHref);
        String linkText = link.text(); // "example""
        String linkOuterH = link.outerHtml();
        // "<a href="http://example.com"><b>example</b></a>"
        String linkInnerH = link.html(); // "<b>example</b>"
        return text;

    }

    public static String getBase64(String imagePath) throws IOException {
      /*  Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try {
            bmp = BitmapFactory.decodeFile(imagePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeString;*/

        FileInputStream objFileIS = null;
        try {
            //  System.out.println("file = >>>> <<<<<" + selectedImagePath);
            objFileIS = new FileInputStream(imagePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream objByteArrayOS = new ByteArrayOutputStream();
        byte[] byteBufferString = new byte[1024];
        try {
            for (int readNum; (readNum = objFileIS.read(byteBufferString)) != -1; ) {
                objByteArrayOS.write(byteBufferString, 0, readNum);
                System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String videodata = Base64.encodeToString(objByteArrayOS.toByteArray(), Base64.DEFAULT);
        Log.d("VideoData**>  ", videodata);
        return videodata;
    }


    public static String extractMentionUser(String mentionText) {


        String mentionString = mentionText;
        // String html = "<p>An <a href='http://example.com/'><b>example</b></a> link.</p>";
        Document doc = Jsoup.parse(mentionString);
        Element link = doc.select("a").first();

        String text = doc.body().text(); // "An example link"

        Log.d("Text", text);
        String linkHref = link.attr("href"); // "http://example.com/"
        Log.d("URL: ", linkHref);
        String linkText = link.text(); // "example""

        String linkOuterH = link.outerHtml();
        // "<a href="http://example.com"><b>example</b></a>"
        String linkInnerH = link.html(); // "<b>example</b>"


        return text;
    }

    public static String chatDateCompare(Context context, long chatTime) {
        long today = Calendar.getInstance().getTimeInMillis();
        DateTime newTime = new DateTime(today);
        DateTime lastTime = new DateTime(chatTime * 1000);
        Days days = Days.daysBetween(lastTime, newTime);
        Minutes minutes = Minutes.minutesBetween(lastTime, newTime);
        Hours hours = Hours.hoursBetween(lastTime, newTime);

        if (minutes.getMinutes() <= 59) {
            if (minutes.getMinutes() < 1) {
                return context.getString(R.string.few_second_ago);
            } else {
                return (minutes.getMinutes() == 1 ? (minutes.getMinutes() + " " + context.getString(R.string.minute_ago)) : (minutes.getMinutes() + " " + context.getString(R.string.minutes_ago)));
            }
        } else if (hours.getHours() <= 23) {
            return (hours.getHours() == 1 ? (hours.getHours() + " " + context.getString(R.string.hour_ago)) : (hours.getHours() + " " + context.getString(R.string.hours_ago)));
        } else {
            if (days.getDays() == 1) {
                return context.getString(R.string.yesterday);
            } else if (days.getDays() < 7) {
                return getDate(chatTime);
            } else {
                return getDate(chatTime);
            }
        }
    }

    private static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("MMM dd", cal).toString();
        return date;
    }


    public static CharSequence colorBackground(String text) {

        Pattern pattern = Pattern.compile("#(.*?)#");

        SpannableStringBuilder ssb = new SpannableStringBuilder(text);

        if (pattern != null) {
            Matcher matcher = pattern.matcher(text);
            int matchesSoFar = 0;
            while (matcher.find()) {
                int start = matcher.start() - (matchesSoFar * 2);
                int end = matcher.end() - (matchesSoFar * 2);
                CharacterStyle span = new ForegroundColorSpan(0xFF1483c9);
                ssb.setSpan(span, start + 1, end - 1, 0);
                ssb.delete(start, start + 1);
                ssb.delete(end - 2, end - 1);
                matchesSoFar++;
            }
        }
        return ssb;
    }
}