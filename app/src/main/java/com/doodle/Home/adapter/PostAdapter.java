package com.doodle.Home.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.Home.model.PostFooter;
import com.doodle.Home.model.PostItem;
import com.doodle.Home.model.PostTextIndex;
import com.doodle.Post.model.Mim;
import com.doodle.Post.service.DataProvider;
import com.doodle.R;
import com.doodle.Search.model.Post;
import com.doodle.utils.AppConstants;
import com.doodle.utils.Operation;
import com.doodle.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vanniktech.emoji.EmojiTextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.doodle.utils.Utils.getDomainName;
import static java.lang.Integer.parseInt;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {


    final int VIEW_TYPE_TEXT = 1;
    final int VIEW_TYPE_TEXT_IMAGE = 2;
    final int VIEW_TYPE_TEXT_LINK_SCRIPT = 3;
    final int VIEW_TYPE_TEXT_LINK_SCRIPT_YOUTUBE = 4;

    private List<PostItem> mItems;
    List<Mim> viewColors = DataProvider.mimList;
    private Context mContext;
    Drawable mDrawable;

    public PostAdapter(Context context, List<PostItem> items) {
        this.mContext = context;
        this.mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.post_txt, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PostItem item = mItems.get(position);

        //Utils.getSpannableStringBuilder(item);

        // holder.tvCategoryName.setText(item.getCatName());
        //  holder.tvPostStar.setText(String.format("%d stars", totalStars));
        //  holder.tvPostLike.setText(MessageFormat.format("{0} Likes", item.getUserProfileLikes()));

        String postType = item.getPostType();
        switch (postType) {

            case "1":
                String text = item.getPostText();
                if (containsIllegalCharacters(text)) {
                    holder.tvPostEmojiContent.setVisibility(View.VISIBLE);
                    holder.tvPostEmojiContent.setText(item.getPostText());

                } else {
                    holder.tvPostContent.setVisibility(View.VISIBLE);
                    holder.tvPostContent.setText(item.getPostText());
                }
                holder.holdLinkScript.setVisibility(View.GONE);
                break;
            case "3":
                holder.holdLinkScript.setVisibility(View.VISIBLE);
                holder.tvPostLinkTitle.setText(item.getPostLinkTitle());
                holder.tvPostLinkDescription.setText(item.getPostLinkDesc());
                try {
                    String domainName = getDomainName(item.getPostLinkUrl());
                    holder.tvPostLinkHost.setText(domainName);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


                holder.tvLinkScriptText.setText(item.getPostText());
                Linkify.addLinks(holder.tvLinkScriptText, Linkify.ALL);
                //set user name in blue color and remove underline from the textview
                Utils.stripUnderlines(holder);

                holder.tvPostEmojiContent.setVisibility(View.GONE);
                holder.tvPostContent.setVisibility(View.GONE);
                break;

            case "4":
                holder.holdLinkScript.setVisibility(View.VISIBLE);
                holder.tvPostLinkTitle.setText(item.getPostLinkTitle());
                holder.tvPostLinkDescription.setText(item.getPostLinkDesc());
                try {
                    String domainName = getDomainName(item.getPostLinkUrl());
                    holder.tvPostLinkHost.setText(domainName);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


                holder.tvLinkScriptText.setText(item.getPostText());
                Linkify.addLinks(holder.tvLinkScriptText, Linkify.ALL);
                //set user name in blue color and remove underline from the textview
                Utils.stripUnderlines(holder);

                holder.tvPostEmojiContent.setVisibility(View.GONE);
                holder.tvPostContent.setVisibility(View.GONE);
                break;

        }

        int mimId = Integer.parseInt(item.getHasMeme());
        for (Mim temp : viewColors
        ) {
            if (temp.getId() == mimId) {
                String mimColor = temp.getMimColor();
                if (mimColor.startsWith("#")) {
                    holder.postBodyLayer.setBackgroundColor(Color.parseColor(mimColor));
                    ViewGroup.LayoutParams params = holder.postBodyLayer.getLayoutParams();
                    params.height = 350;
                    holder.postBodyLayer.setLayoutParams(params);
                    holder.postBodyLayer.setGravity(Gravity.CENTER);
                    holder.tvPostContent.setGravity(Gravity.CENTER);
                    holder.tvPostContent.setTextAppearance(mContext, android.R.style.TextAppearance_Large);
                    holder.tvPostContent.setTextColor(Color.parseColor("#FFFFFF"));
                } else {

                    String imageUrl = AppConstants.MIM_IMAGE + mimColor;
                    Picasso.with(App.getInstance()).load(imageUrl).into(target);
                    holder.postBodyLayer.setGravity(Gravity.CENTER);
                    holder.tvPostContent.setGravity(Gravity.CENTER);
                    holder.postBodyLayer.setBackground(mDrawable);
                    holder.tvPostContent.setHeight(150);
                    switch (mimColor) {
                        case "img_bg_birthday.png":
                            holder.tvPostContent.setTextColor(Color.parseColor("#000000"));
                            break;
                        case "img_bg_love.png":
                            holder.tvPostContent.setTextColor(Color.parseColor("#2D4F73"));
                            break;
                        case "img_bg_love2.png":
                            holder.tvPostContent.setTextColor(Color.parseColor("#444748"));
                            break;
                        case "img_bg_red.png":
                            holder.tvPostContent.setTextColor(Color.parseColor("#FFFFFF"));
                            break;
                        case "img_bg_love3.png":
                            holder.tvPostContent.setTextColor(Color.parseColor("#FFFFFF"));
                            break;
                    }
                }


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
                holder.star9.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.star9.setVisibility(View.VISIBLE);
                holder.star10.setVisibility(View.VISIBLE);
                break;
            case 3:
                holder.star9.setVisibility(View.VISIBLE);
                holder.star10.setVisibility(View.VISIBLE);
                holder.star11.setVisibility(View.VISIBLE);
                break;
            case 4:
                holder.star9.setVisibility(View.VISIBLE);
                holder.star10.setVisibility(View.VISIBLE);
                holder.star11.setVisibility(View.VISIBLE);
                holder.star12.setVisibility(View.VISIBLE);
                break;
            case 5:
                holder.star9.setVisibility(View.VISIBLE);
                holder.star10.setVisibility(View.VISIBLE);
                holder.star11.setVisibility(View.VISIBLE);
                holder.star12.setVisibility(View.VISIBLE);
                holder.star13.setVisibility(View.VISIBLE);
                break;
            case 6:
                holder.star9.setVisibility(View.VISIBLE);
                holder.star10.setVisibility(View.VISIBLE);
                holder.star11.setVisibility(View.VISIBLE);
                holder.star12.setVisibility(View.VISIBLE);
                holder.star13.setVisibility(View.VISIBLE);
                holder.star14.setVisibility(View.VISIBLE);
                break;
            case 7:
                holder.star9.setVisibility(View.VISIBLE);
                holder.star10.setVisibility(View.VISIBLE);
                holder.star11.setVisibility(View.VISIBLE);
                holder.star12.setVisibility(View.VISIBLE);
                holder.star13.setVisibility(View.VISIBLE);
                holder.star14.setVisibility(View.VISIBLE);
                holder.star15.setVisibility(View.VISIBLE);
                break;
            case 8:
                holder.star9.setVisibility(View.VISIBLE);
                holder.star10.setVisibility(View.VISIBLE);
                holder.star11.setVisibility(View.VISIBLE);
                holder.star12.setVisibility(View.VISIBLE);
                holder.star13.setVisibility(View.VISIBLE);
                holder.star14.setVisibility(View.VISIBLE);
                holder.star15.setVisibility(View.VISIBLE);
                holder.star16.setVisibility(View.VISIBLE);
                break;
            case 0:
                holder.star9.setVisibility(View.GONE);
                holder.star10.setVisibility(View.GONE);
                holder.star11.setVisibility(View.GONE);
                holder.star12.setVisibility(View.GONE);
                holder.star13.setVisibility(View.GONE);
                holder.star14.setVisibility(View.GONE);
                holder.star15.setVisibility(View.GONE);
                holder.star16.setVisibility(View.GONE);
                break;

        }
        if (goldStar > 8) {
            goldStar = 8;
        }
        switch (goldStar) {
            case 1:
                holder.star1.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.VISIBLE);
                break;
            case 3:
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.VISIBLE);
                holder.star3.setVisibility(View.VISIBLE);
                break;
            case 4:
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.VISIBLE);
                holder.star3.setVisibility(View.VISIBLE);
                holder.star4.setVisibility(View.VISIBLE);
                break;
            case 5:
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.VISIBLE);
                holder.star3.setVisibility(View.VISIBLE);
                holder.star4.setVisibility(View.VISIBLE);
                holder.star5.setVisibility(View.VISIBLE);
                break;
            case 6:
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.VISIBLE);
                holder.star3.setVisibility(View.VISIBLE);
                holder.star4.setVisibility(View.VISIBLE);
                holder.star5.setVisibility(View.VISIBLE);
                holder.star6.setVisibility(View.VISIBLE);
                break;
            case 7:
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.VISIBLE);
                holder.star3.setVisibility(View.VISIBLE);
                holder.star4.setVisibility(View.VISIBLE);
                holder.star5.setVisibility(View.VISIBLE);
                holder.star6.setVisibility(View.VISIBLE);
                holder.star7.setVisibility(View.VISIBLE);
                break;
            case 8:
                holder.star1.setVisibility(View.VISIBLE);
                holder.star2.setVisibility(View.VISIBLE);
                holder.star3.setVisibility(View.VISIBLE);
                holder.star4.setVisibility(View.VISIBLE);
                holder.star5.setVisibility(View.VISIBLE);
                holder.star6.setVisibility(View.VISIBLE);
                holder.star7.setVisibility(View.VISIBLE);
                holder.star8.setVisibility(View.VISIBLE);
                break;
            case 0:
                holder.star1.setVisibility(View.GONE);
                holder.star2.setVisibility(View.GONE);
                holder.star3.setVisibility(View.GONE);
                holder.star4.setVisibility(View.GONE);
                holder.star5.setVisibility(View.GONE);
                holder.star6.setVisibility(View.GONE);
                holder.star7.setVisibility(View.GONE);
                holder.star8.setVisibility(View.GONE);
                break;

        }

        int totalStars = silverStar + goldStar;
        String categoryName = item.getCatName();
//
        SpannableString spannableCategory = new SpannableString(categoryName);
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(Color.BLUE);
        spannableCategory.setSpan(backgroundColorSpan, 2, categoryName.length()-1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        String tvHeaderInfo = String.format("%s Likes | %d Stars | %s Followers | %s", likes, totalStars, followers, spannableCategory);


        holder.tvPostUserName.setText(String.format("%s %s", item.getUserFirstName(), item.getUserLastName()));
        long myMillis = Long.parseLong(item.getDateTime()) * 1000;
        String postDate = Operation.getFormattedDateFromTimestamp(myMillis);
        holder.tvPostTime.setText(postDate);
        holder.tvHeaderInfo.setText(tvHeaderInfo);


        PostFooter postFooter = item.getPostFooter();
        String postLike = postFooter.getPostTotalLike();
        int postTotalShare = postFooter.getPostTotalShare();
        holder.tvImgShareCount.setText(String.valueOf(postTotalShare));
        if ("0".equalsIgnoreCase(postLike)) {
            holder.tvPostLikeCount.setVisibility(View.GONE);
        } else {
            SpannableString content = new SpannableString(postLike);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            holder.tvPostLikeCount.setVisibility(View.VISIBLE);
            holder.tvPostLikeCount.setText(content);

        }


        String userImageUrl = AppConstants.PROFILE_IMAGE + item.getUesrProfileImg();

        if ("default-profile-picture.png".equalsIgnoreCase(item.getPostImage())) {
            holder.imgLinkScript.setVisibility(View.GONE);
        } else {
            holder.imgLinkScript.setVisibility(View.VISIBLE);
            if (item.getPostType().equalsIgnoreCase("3")) {
                String linkImage = AppConstants.Link_IMAGE_PATH + item.getPostImage();
                Glide.with(mContext)
                        .load(linkImage)
                        .centerCrop()
                        .dontAnimate()
//                .placeholder(R.drawable.loading_spinner)
                        //  .crossFade()
                        .into(holder.imgLinkScript);
                //   Linkify.addLinks((Spannable) holder.imgLinkScript, Linkify.ALL);
            } else if (item.getPostType().equalsIgnoreCase("4")) {
                String linkImage = AppConstants.YOUTUBE_IMAGE_PATH + item.getPostImage();
                Glide.with(mContext)
                        .load(linkImage)
                        .centerCrop()
                        .dontAnimate()
//                .placeholder(R.drawable.loading_spinner)
                        //  .crossFade()
                        .into(holder.imgLinkScript);
                //  Linkify.addLinks((Spannable) holder.imgLinkScript, Linkify.ALL);
            }

        }
        holder.imgLinkScript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";
                if (!item.getPostLinkUrl().isEmpty() && item.getPostLinkUrl().matches(pattern)) {
                    /// Valid youtube URL
                    Intent browserIntents = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getPostLinkUrl()));
                  mContext.startActivity(browserIntents);
                } else {

                    // Not Valid youtube URL
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getPostLinkUrl()));
                   mContext.startActivity(browserIntent);
                }

            }
        });


        Glide.with(mContext)
                .load(userImageUrl)
                .centerCrop()
                .dontAnimate()
//                .placeholder(R.drawable.loading_spinner)
                //  .crossFade()
                .into(holder.imagePostUser);

    }


    private void extractMentionUser(PostTextIndex temp, String postType) {
        if (postType.equalsIgnoreCase("mention")) {
            String mentionString = temp.getText();
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
        }
    }


    private boolean containsIllegalCharacters(String displayName) {
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


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addPagingData(List<PostItem> postItemList) {

        for (PostItem temp : postItemList
        ) {
            mItems.add(temp);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvHeaderInfo, tvPostTime, tvPostUserName, tvImgShareCount, tvPostLikeCount, tvLinkScriptText;
        public CircleImageView imagePostUser;
        public ReadMoreTextView tvPostContent;
        public EmojiTextView tvPostEmojiContent;
        public ImageView imgLinkScript, star1, star2, star3, star4, star5, star6, star7, star8, star9, star10, star11, star12, star13, star14, star15, star16;
        public LinearLayout postBodyLayer, holdLinkScript;

        public TextView tvPostLinkTitle, tvPostLinkDescription, tvPostLinkHost;


        public ViewHolder(View itemView) {
            super(itemView);

            tvPostUserName = (TextView) itemView.findViewById(R.id.tvPostUserName);
            imagePostUser = (CircleImageView) itemView.findViewById(R.id.imagePostUser);
            tvHeaderInfo = (TextView) itemView.findViewById(R.id.tvHeaderInfo);
            tvImgShareCount = (TextView) itemView.findViewById(R.id.tvImgShareCount);
            tvPostTime = (TextView) itemView.findViewById(R.id.tvPostTime);
            tvPostLikeCount = (TextView) itemView.findViewById(R.id.tvPostLikeCount);
            //tvPostContent = (ReadMoreTextView) itemView.findViewById(R.id.tvPostContent);
            tvPostContent = (ReadMoreTextView) itemView.findViewById(R.id.tvPostContent);
            tvLinkScriptText = (ReadMoreTextView) itemView.findViewById(R.id.tvLinkScriptText);
            tvPostEmojiContent = (EmojiTextView) itemView.findViewById(R.id.tvPostEmojiContent);
            postBodyLayer = (LinearLayout) itemView.findViewById(R.id.postBodyLayer);
            holdLinkScript = (LinearLayout) itemView.findViewById(R.id.holdLinkScript);

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
            imgLinkScript = itemView.findViewById(R.id.imgLinkScript);

            //LINKSCRIPT
            tvPostLinkTitle = itemView.findViewById(R.id.tvPostLinkTitle);
            tvPostLinkDescription = itemView.findViewById(R.id.tvPostLinkDescription);
            tvPostLinkHost = itemView.findViewById(R.id.tvPostLinkHost);

        }
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


}
