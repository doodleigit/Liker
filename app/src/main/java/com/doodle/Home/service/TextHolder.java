package com.doodle.Home.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Home.adapter.BreakingPostAdapter;
import com.doodle.Home.model.PostFooter;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.model.PostTextIndex;
import com.doodle.Home.model.postshare.PostShareItem;
import com.doodle.Home.view.activity.PostShare;
import com.doodle.Post.model.Mim;
import com.doodle.Post.service.DataProvider;
import com.doodle.R;
import com.doodle.utils.AppConstants;
import com.doodle.utils.Operation;
import com.doodle.utils.PrefManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vanniktech.emoji.EmojiTextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doodle.utils.Utils.containsIllegalCharacters;
import static com.doodle.utils.Utils.extractMentionText;
import static com.doodle.utils.Utils.extractUrls;
import static com.doodle.utils.Utils.getSpannableStringBuilder;
import static java.lang.Integer.parseInt;

public class TextHolder extends RecyclerView.ViewHolder {


    public static final String ITEM_KEY = "item_key";

    public TextView tvHeaderInfo, tvPostTime, tvPostUserName, tvImgShareCount, tvPostLikeCount, tvLinkScriptText;
    public CircleImageView imagePostUser;
    public ReadMoreTextView tvPostContent;
    public EmojiTextView tvPostEmojiContent;
    public ImageView star1, star2, star3, star4, star5, star6, star7, star8,
            star9, star10, star11, star12, star13, star14, star15, star16;
    public LinearLayout postBodyLayer;
    private Drawable mDrawable;
    List<Mim> viewColors = DataProvider.mimList;

    PostItem item;

    List<String> mentions;
    ArrayList<String> mList;
    public String full_text;
    public ImageView imagePostShare;
    private PopupMenu popup;
    public HomeService webService;
    public PrefManager manager;
    private String deviceId, profileId, token, userIds;
    private Context mContext;

    public TextHolder(View itemView,Context context) {
        super(itemView);
        mContext=context;
        mentions = new ArrayList<>();
        mList = new ArrayList<>();
        manager = new PrefManager(App.getAppContext());
        deviceId = manager.getDeviceId();
        profileId = manager.getProfileId();
        token = manager.getToken();
        userIds = manager.getProfileId();
        webService = HomeService.mRetrofit.create(HomeService.class);
        tvPostUserName = (TextView) itemView.findViewById(R.id.tvPostUserName);
        imagePostUser = (CircleImageView) itemView.findViewById(R.id.imagePostUser);
        tvHeaderInfo = (TextView) itemView.findViewById(R.id.tvHeaderInfo);
        tvImgShareCount = (TextView) itemView.findViewById(R.id.tvImgShareCount);
        tvPostTime = (TextView) itemView.findViewById(R.id.tvPostTime);
        imagePostShare = (ImageView) itemView.findViewById(R.id.imagePostShare);
        tvPostLikeCount = (TextView) itemView.findViewById(R.id.tvPostLikeCount);
        //tvPostContent = (ReadMoreTextView) itemView.findViewById(R.id.tvPostContent);
        tvPostContent = (ReadMoreTextView) itemView.findViewById(R.id.tvPostContent);
        tvLinkScriptText = (ReadMoreTextView) itemView.findViewById(R.id.tvLinkScriptText);
        tvPostEmojiContent = (EmojiTextView) itemView.findViewById(R.id.tvPostEmojiContent);
        postBodyLayer = (LinearLayout) itemView.findViewById(R.id.postBodyLayer);


        star1 = itemView.findViewById(R.id.star1);
        star2 = itemView.findViewById(R.id.star2);
        star3 = itemView.findViewById(R.id.star3);
        star4 = itemView.findViewById(R.id.star4);
        star5 = itemView.findViewById(R.id.star5);
        star6 = itemView.findViewById(R.id.star6);
        star7 = itemView.findViewById(R.id.star7);
        star8 = itemView.findViewById(R.id.star8);
        star9 = itemView.findViewById(R.id.star9);
        star10 = itemView.findViewById(R.id.star10);
        star11 = itemView.findViewById(R.id.star11);
        star12 = itemView.findViewById(R.id.star12);
        star13 = itemView.findViewById(R.id.star13);
        star14 = itemView.findViewById(R.id.star14);
        star15 = itemView.findViewById(R.id.star15);
        star16 = itemView.findViewById(R.id.star16);



    }


    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mDrawable = new BitmapDrawable(App.getInstance().getResources(), bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

    public void setItem(final PostItem item) {
        this.item = item;
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


        String likes = item.getUserProfileLikes();
        String followers = item.getUserTotalFollowers();
        int silverStar = parseInt(item.getUserSilverStars());
        int goldStar = parseInt(item.getUserGoldStars());

        if (silverStar > 8) {
            silverStar = 8;
        }
        switch (silverStar) {
            case 1:
                star9.setVisibility(View.VISIBLE);
                break;
            case 2:
                star9.setVisibility(View.VISIBLE);
                star10.setVisibility(View.VISIBLE);
                break;
            case 3:
                star9.setVisibility(View.VISIBLE);
                star10.setVisibility(View.VISIBLE);
                star11.setVisibility(View.VISIBLE);
                break;
            case 4:
                star9.setVisibility(View.VISIBLE);
                star10.setVisibility(View.VISIBLE);
                star11.setVisibility(View.VISIBLE);
                star12.setVisibility(View.VISIBLE);
                break;
            case 5:
                star9.setVisibility(View.VISIBLE);
                star10.setVisibility(View.VISIBLE);
                star11.setVisibility(View.VISIBLE);
                star12.setVisibility(View.VISIBLE);
                star13.setVisibility(View.VISIBLE);
                break;
            case 6:
                star9.setVisibility(View.VISIBLE);
                star10.setVisibility(View.VISIBLE);
                star11.setVisibility(View.VISIBLE);
                star12.setVisibility(View.VISIBLE);
                star13.setVisibility(View.VISIBLE);
                star14.setVisibility(View.VISIBLE);
                break;
            case 7:
                star9.setVisibility(View.VISIBLE);
                star10.setVisibility(View.VISIBLE);
                star11.setVisibility(View.VISIBLE);
                star12.setVisibility(View.VISIBLE);
                star13.setVisibility(View.VISIBLE);
                star14.setVisibility(View.VISIBLE);
                star15.setVisibility(View.VISIBLE);
                break;
            case 8:
                star9.setVisibility(View.VISIBLE);
                star10.setVisibility(View.VISIBLE);
                star11.setVisibility(View.VISIBLE);
                star12.setVisibility(View.VISIBLE);
                star13.setVisibility(View.VISIBLE);
                star14.setVisibility(View.VISIBLE);
                star15.setVisibility(View.VISIBLE);
                star16.setVisibility(View.VISIBLE);
                break;
            case 0:
                star9.setVisibility(View.GONE);
                star10.setVisibility(View.GONE);
                star11.setVisibility(View.GONE);
                star12.setVisibility(View.GONE);
                star13.setVisibility(View.GONE);
                star14.setVisibility(View.GONE);
                star15.setVisibility(View.GONE);
                star16.setVisibility(View.GONE);
                break;

        }
        if (goldStar > 8) {
            goldStar = 8;
        }
        switch (goldStar) {
            case 1:
                star1.setVisibility(View.VISIBLE);
                break;
            case 2:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                break;
            case 3:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                break;
            case 4:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                break;
            case 5:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);
                break;
            case 6:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);
                star6.setVisibility(View.VISIBLE);
                break;
            case 7:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);
                star6.setVisibility(View.VISIBLE);
                star7.setVisibility(View.VISIBLE);
                break;
            case 8:
                star1.setVisibility(View.VISIBLE);
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);
                star6.setVisibility(View.VISIBLE);
                star7.setVisibility(View.VISIBLE);
                star8.setVisibility(View.VISIBLE);
                break;
            case 0:
                star1.setVisibility(View.GONE);
                star2.setVisibility(View.GONE);
                star3.setVisibility(View.GONE);
                star4.setVisibility(View.GONE);
                star5.setVisibility(View.GONE);
                star6.setVisibility(View.GONE);
                star7.setVisibility(View.GONE);
                star8.setVisibility(View.GONE);
                break;

        }

        int totalStars = silverStar + goldStar;
        String categoryName = item.getCatName();
//

        SpannableStringBuilder builder = getSpannableStringBuilder(likes, followers, totalStars, categoryName);


        tvPostUserName.setText(String.format("%s %s", item.getUserFirstName(), item.getUserLastName()));
        long myMillis = Long.parseLong(item.getDateTime()) * 1000;
        String postDate = Operation.getFormattedDateFromTimestamp(myMillis);
        tvPostTime.setText(postDate);
        tvHeaderInfo.setText(builder);

        PostFooter postFooter = item.getPostFooter();
        String postLike = postFooter.getPostTotalLike();
        int postTotalShare = postFooter.getPostTotalShare();
        tvImgShareCount.setText(String.valueOf(postTotalShare));
        if ("0".equalsIgnoreCase(postLike)) {
            tvPostLikeCount.setVisibility(View.GONE);
        } else {
            SpannableString content = new SpannableString(postLike);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            tvPostLikeCount.setVisibility(View.VISIBLE);
            tvPostLikeCount.setText(content);
        }


        String userImageUrl = AppConstants.PROFILE_IMAGE + item.getUesrProfileImg();
        Glide.with(App.getAppContext())
                .load(userImageUrl)
                .centerCrop()
                .dontAnimate()
//                .placeholder(R.drawable.loading_spinner)
                //  .crossFade()
                .into(imagePostUser);


        imagePostShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popup = new PopupMenu(App.getAppContext(), v);
                popup.getMenuInflater().inflate(R.menu.share_menu, popup.getMenu());

                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();

                        if (id == R.id.shareAsPost) {
                            String postId = item.getSharedPostId();
                            Call<PostShareItem> call = webService.getPostDetails(deviceId, profileId, token, userIds, postId);
                            sendShareItemRequest(call);

                        }

                        if (id == R.id.shareFacebook) {

                        }
                        if (id == R.id.shareTwitter) {
                            Toast.makeText(App.getAppContext(), "Removed : ", Toast.LENGTH_SHORT).show();

                        }

                        if (id == R.id.copyLink) {

                        }
                        return true;
                    }
                });

            }
        });

    }

    private void sendShareItemRequest(Call<PostShareItem> call) {


        call.enqueue(new Callback<PostShareItem>() {

            @Override
            public void onResponse(Call<PostShareItem> call, Response<PostShareItem> response) {

                PostShareItem postShareItem = response.body();
                Log.d("Data", postShareItem.toString());
                if (postShareItem != null) {
                    //   adapter = new BreakingPostAdapter(getActivity(), postItemList);
                    //  Toast.makeText(mContext, "item selected " + item.getItemName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, PostShare.class);
                    //intent.putExtra(ITEM_ID_KEY,item.getItemId());
                    intent.putExtra(ITEM_KEY, (Parcelable) postShareItem);
                    mContext.startActivity(intent);
                }

            }
            @Override
            public void onFailure(Call<PostShareItem> call, Throwable t) {
                Log.d("MESSAGE: ", t.getMessage());

            }
        });
    }

    private String extractMentionUser(String mentionText) {


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


}
