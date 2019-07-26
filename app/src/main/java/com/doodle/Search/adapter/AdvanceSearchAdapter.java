package com.doodle.Search.adapter;

import android.content.Context;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.doodle.App;
import com.doodle.R;
import com.doodle.Search.model.Post;
import com.doodle.Search.model.User;
import com.doodle.utils.GlideApp;
import com.doodle.utils.GlideImageLoader;
import com.doodle.utils.Operation;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.doodle.utils.AppConstants.POST_IMAGES;
import static com.doodle.utils.AppConstants.PROFILE_IMAGE;

public class AdvanceSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final int VIEW_TYPE_USER = 0;
    final int VIEW_TYPE_POST = 1;

    Context context;
    List<User> mUser;
    List<Post> mPost;


    public AdvanceSearchAdapter(Context context, List<User> mUser, List<Post> mPost) {
        this.context = context;
        this.mUser = mUser;
        this.mPost = mPost;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_USER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advance_search_user_item, parent, false);
            //    return new AdvanceSearchAd.UserViewHolder(view);

            return new UserViewHolder(view);
        }

        if (viewType == VIEW_TYPE_POST) {

            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advance_search_post_item, parent, false);
            //  return new AdvanceSearchAd.PostViewHolder(view);


            return new PostViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof UserViewHolder) {
            ((UserViewHolder) viewHolder).populate(mUser.get(position));
        }

        if (viewHolder instanceof PostViewHolder) {
            ((PostViewHolder) viewHolder).populate(mPost.get(position - mUser.size()));

        }
    }

    @Override
    public int getItemCount() {
        return mUser.size() + mPost.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mUser.size()) {
            return VIEW_TYPE_USER;
        }

        if (position - mUser.size() < mPost.size()) {
            return VIEW_TYPE_POST;
        }

        return -1;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvLike, tvStar;
        ImageView imgUser;

        public UserViewHolder(View itemView) {
            super(itemView);

            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvLike = (TextView) itemView.findViewById(R.id.tvLike);
            tvStar = (TextView) itemView.findViewById(R.id.tvStar);
            imgUser = (ImageView) itemView.findViewById(R.id.imgUser);
        }

        public void populate(User user) {
            tvUserName.setText(user.getFullname());
            tvLike.setText(user.getTotalLikes() + " Likes");
            int totalStar = Integer.parseInt(user.getSliverStars()) + Integer.parseInt(user.getGoldStars());
            tvStar.setText(String.valueOf(totalStar) + " Stars");
            tvUserName.setText(user.getFullname());
            String imagePhoto = POST_IMAGES + user.getPhoto();

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.circle)
                    .error(R.drawable.ic_cancel_black_24dp)
                    .priority(Priority.HIGH);


            if (imagePhoto != null && imagePhoto.length() > 0) {
                Picasso.with(App.getAppContext())
                        .load(imagePhoto)
                        .placeholder(R.drawable.drawable_comment)
                        .into(imgUser);

            }
        }
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvPostUserName, tvPostDuration, tvPostLike, tvPostStar, postCategory, tvPostText;
        ImageView    imgPostUser, imgPostPermission, imgPostImage;

        public PostViewHolder(View itemView) {
            super(itemView);

            tvPostUserName = (TextView) itemView.findViewById(R.id.tvPostUserName);
            tvPostDuration = (TextView) itemView.findViewById(R.id.tvPostDuration);
            tvPostLike = (TextView) itemView.findViewById(R.id.tvPostLike);
            tvPostStar = (TextView) itemView.findViewById(R.id.tvPostStar);
            postCategory = (TextView) itemView.findViewById(R.id.postCategory);
            tvPostText = (TextView) itemView.findViewById(R.id.tvPostText);

            imgPostUser = (ImageView) itemView.findViewById(R.id.imgPostUser);
            imgPostPermission = (ImageView) itemView.findViewById(R.id.imgPostPermission);
            imgPostImage = (ImageView) itemView.findViewById(R.id.imgPostImage);


        }

        public void populate(Post post) {

            tvPostUserName.setText(post.getFirstName() + " " + post.getLastName());
            //   String postDate = Operation.getDurationBreakdown(Long.parseLong(post.getPostDate()));
            //  String postDate = Operation.getDate(Long.parseLong(post.getPostDate()), "dd/MM/yyyy hh:mm:ss.SSS");
            long myMillis=Long.parseLong(post.getPostDate())*1000;
            String postDate = Operation.getFormattedDateFromTimestamp(myMillis);
            tvPostDuration.setText(postDate);
            tvPostLike.setText(post.getTotalLike() + " Likes");
            int totalStar = Integer.parseInt(post.getSliverStars()) + Integer.parseInt(post.getGoldStars());
            tvPostStar.setText(String.valueOf(totalStar) + " Stars");
            postCategory.setText(post.getCategoryName());
            tvPostText.setText(post.getPostText());
            String imagePhoto = PROFILE_IMAGE + post.getPhoto();
            String imagePost = POST_IMAGES + post.getPostImage();

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.circle)
                    .error(R.drawable.ic_cancel_black_24dp)
                    .priority(Priority.HIGH);
            if (imagePhoto != null && imagePhoto.length() > 0) {
                Picasso.with(App.getAppContext())
                        .load(imagePhoto)
                        .noFade()
                        .placeholder(R.drawable.drawable_comment)
                        .into(imgPostUser);


            }
            if (imagePost != null && imagePost.length() > 0) {
                Picasso.with(App.getAppContext())
                        .load(imagePost)
                        .placeholder(R.drawable.drawable_comment)
                        .into(imgPostImage);
            }

            int postPermission = Integer.parseInt(post.getPostPermission());
            switch (postPermission) {
                case 0:
                    imgPostPermission.setImageResource(R.drawable.ic_public_black_24dp);
                    break;
                case 1:
                    imgPostPermission.setImageResource(R.drawable.ic_lock_black_24dp);
                    break;
                case 2:
                    imgPostPermission.setImageResource(R.drawable.ic_people_black_24dp);
                    break;
            }
        }
    }

    public void addPagingData(List<Post> postList) {

        for (Post temp : postList
        ) {
            mPost.add(temp);
        }
        notifyDataSetChanged();
    }
}
