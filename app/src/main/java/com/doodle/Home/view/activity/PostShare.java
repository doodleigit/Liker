package com.doodle.Home.view.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.doodle.App;
import com.doodle.Home.model.postshare.PostShareItem;
import com.doodle.Home.model.postshare.PostTextIndex;
import com.doodle.Home.service.TextHolder;
import com.doodle.Post.model.Mim;
import com.doodle.Post.service.DataProvider;
import com.doodle.R;
import com.doodle.utils.AppConstants;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vanniktech.emoji.EmojiTextView;

import java.util.ArrayList;
import java.util.List;

import static com.doodle.utils.Utils.containsIllegalCharacters;
import static com.doodle.utils.Utils.extractMentionText;
import static com.doodle.utils.Utils.extractMentionUser;
import static com.doodle.utils.Utils.extractUrls;

public class PostShare extends AppCompatActivity {


    private TextView tvAudience;
    private CardView onlyImage, onlyVideo, onlyLinkScript, onlyLinkScriptYoutube;
    private LinearLayout onlyText,shareContent,onlyMim;
    ArrayList<String> mList;
    //postContent
    private ReadMoreTextView tvPostContent;
    private String full_text;
    public EmojiTextView tvPostEmojiContent;
    List<Mim> viewColors = DataProvider.mimList;
    private Drawable mDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_share);

        PostShareItem item = getIntent().getExtras().getParcelable(TextHolder.ITEM_KEY);
        if (item == null) {
            throw new AssertionError("Null data item received!");
        }

        onlyImage = findViewById(R.id.onlyImage);
        onlyVideo = findViewById(R.id.onlyVideo);
        onlyLinkScript = findViewById(R.id.onlyLinkScript);
        onlyLinkScriptYoutube = findViewById(R.id.onlyLinkScriptYoutube);
        onlyMim = findViewById(R.id.onlyMim);
        onlyText = findViewById(R.id.onlyText);

        shareContent = findViewById(R.id.shareContent);

        tvPostContent =(ReadMoreTextView) findViewById(R.id.tvPostContent);
        tvPostEmojiContent = findViewById(R.id.tvPostEmojiContent);
        mList = new ArrayList<>();

        tvAudience = findViewById(R.id.tvAudience);
        tvAudience.setText(item.getCatName());
        String postType = item.getPostType();
        int viewType = Integer.parseInt(postType);

        String hasMim = item.getHasMeme();
        int mimId = Integer.parseInt(hasMim);

        switch (viewType) {
            case 1:
                if (mimId > 0) {
                    onlyMim.setVisibility(View.VISIBLE);
                    setMim(item);
                } else {
                    onlyText.setVisibility(View.VISIBLE);
                    setText(item);
                }
                break;
            case 2:
                onlyImage.setVisibility(View.VISIBLE);
                break;
            case 3:
                onlyLinkScript.setVisibility(View.VISIBLE);
                break;
            case 4:
                onlyLinkScriptYoutube.setVisibility(View.VISIBLE);
                break;
            case 5:
                onlyVideo.setVisibility(View.VISIBLE);
                break;

        }

    }

    private void setMim(PostShareItem item) {


        String text = item.getPostText();
        if (containsIllegalCharacters(text)) {
            tvPostContent.setVisibility(View.GONE);
            tvPostEmojiContent.setVisibility(View.VISIBLE);
            tvPostEmojiContent.setText(text);

        } else {
            tvPostEmojiContent.setVisibility(View.GONE);
            tvPostContent.setVisibility(View.VISIBLE);
            tvPostContent.setText(text);
        }

        int mimId = Integer.parseInt(item.getHasMeme());
        for (Mim temp : viewColors) {
            int getId = temp.getId() == 1 ? 0 : temp.getId();
            if (mimId == getId && mimId > 0) {
                String mimColor = temp.getMimColor();
                if (mimColor.startsWith("#")) {
                    onlyMim.setBackgroundColor(Color.parseColor(mimColor));
                    tvPostEmojiContent.setText(text);
                    tvPostContent.setText(text);
                    tvPostContent.setTextColor(Color.parseColor("#FFFFFF"));
                    tvPostEmojiContent.setTextColor(Color.parseColor("#FFFFFF"));
                } else {

                    String imageUrl = AppConstants.MIM_IMAGE + mimColor;
                    Picasso.with(App.getInstance()).load(imageUrl).into(target);
                    onlyMim.setBackground(mDrawable);
                    tvPostEmojiContent.setText(text);
                    tvPostContent.setText(text);
                    switch (mimColor) {
                        case "img_bg_birthday.png":
                            tvPostContent.setTextColor(Color.parseColor("#000000"));
                            tvPostEmojiContent.setTextColor(Color.parseColor("#000000"));
                            break;
                        case "img_bg_love.png":
                            tvPostContent.setTextColor(Color.parseColor("#2D4F73"));
                            tvPostEmojiContent.setTextColor(Color.parseColor("#2D4F73"));
                            break;
                        case "img_bg_love2.png":
                            tvPostContent.setTextColor(Color.parseColor("#444748"));
                            tvPostEmojiContent.setTextColor(Color.parseColor("#444748"));
                            break;
                        case "img_bg_red.png":
                            tvPostContent.setTextColor(Color.parseColor("#FFFFFF"));
                            tvPostEmojiContent.setTextColor(Color.parseColor("#FFFFFF"));
                            break;
                        case "img_bg_love3.png":
                            tvPostContent.setTextColor(Color.parseColor("#FFFFFF"));
                            tvPostEmojiContent.setTextColor(Color.parseColor("#FFFFFF"));
                            break;
                    }
                }
            }
        }


    }

    private void setText(PostShareItem item) {

        String text = item.getPostText();
        StringBuilder nameBuilder = new StringBuilder();
        List<String> mentionUrl = extractUrls(item.getPostText());


        for (PostTextIndex temp : item.getPostTextIndex()) {
            String postType = temp.getType();
            if (postType.equalsIgnoreCase("mention")) {
                String mentionUserName = extractMentionUser(temp.getText());
                nameBuilder.append(mentionUserName);
                nameBuilder.append(" ");
            }

        }
        if (containsIllegalCharacters(text)) {
            tvPostContent.setVisibility(View.GONE);
            tvPostEmojiContent.setVisibility(View.VISIBLE);
            tvPostEmojiContent.setText(text);

        } else {
            tvPostEmojiContent.setVisibility(View.GONE);
            tvPostContent.setVisibility(View.VISIBLE);
            tvPostContent.setText(text);
        }

        if (mentionUrl.size() > 0 && extractMentionText(item).trim().length() > 0) {

            full_text = extractMentionText(item).trim();

            if (containsIllegalCharacters(full_text)) {
                tvPostContent.setVisibility(View.GONE);
                tvPostEmojiContent.setVisibility(View.VISIBLE);

                String nameStr = nameBuilder.toString();
                String[] mentionArr = nameStr.split(" ");
                //split strings by space
                String[] splittedWords = full_text.split(" ");
                SpannableString str = new SpannableString(full_text);
                //Check the matching words

                for (int i = 0; i < mentionArr.length; i++) {
                    for (int j = 0; j < splittedWords.length; j++) {
                        if (mentionArr[i].equalsIgnoreCase(splittedWords[j])) {
                            mList.add(mentionArr[i]);
                        }
                    }
                }

                //make the words bold

                for (int k = 0; k < mList.size(); k++) {
                    int val = full_text.indexOf(mList.get(k));
                    ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(App.getAppContext(), "\"You click the text.\"", Toast.LENGTH_SHORT).show();
                        }
                    };
                    if (val >= 0) {
                        str.setSpan(clickableSpan, val, val + mList.get(k).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                tvPostEmojiContent.setText(str);


            } else {
                tvPostEmojiContent.setVisibility(View.GONE);
                tvPostContent.setVisibility(View.VISIBLE);
                String nameStr = nameBuilder.toString();
                String[] mentionArr = nameStr.split(" ");

                //split strings by space
                String[] splittedWords = full_text.split(" ");
                SpannableString str = new SpannableString(full_text);

                //Check the matching words
                for (int i = 0; i < mentionArr.length; i++) {
                    for (int j = 0; j < splittedWords.length; j++) {
                        if (mentionArr[i].equalsIgnoreCase(splittedWords[j])) {
                            mList.add(mentionArr[i]);
                        }
                    }
                }

                //make the words bold

                for (int k = 0; k < mList.size(); k++) {
                    int val = full_text.indexOf(mList.get(k));
                    ClickableSpan clickableSpan = new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(App.getAppContext(), "\"You click the text.\"", Toast.LENGTH_SHORT).show();
                        }
                    };
                    if (val >= 0) {
                        str.setSpan(clickableSpan, val, val + mList.get(k).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                }
                tvPostContent.setText(str);


            }
        }
    }


    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Log.d("TAG", "onBitmapLoaded: "+bitmap);
            mDrawable = new BitmapDrawable(getResources(), bitmap);

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            shareContent.setBackground(errorDrawable);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            shareContent.setBackground(placeHolderDrawable);
        }
    };
}
