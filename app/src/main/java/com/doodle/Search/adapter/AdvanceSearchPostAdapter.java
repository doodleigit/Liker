package com.doodle.Search.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.doodle.App;
import com.doodle.R;
import com.doodle.Search.model.Post;
import com.doodle.Tool.Operation;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.doodle.Tool.AppConstants.POST_IMAGES;
import static com.doodle.Tool.AppConstants.PROFILE_IMAGE;

public class AdvanceSearchPostAdapter extends RecyclerView.Adapter<AdvanceSearchPostAdapter.PostViewHolder> {

    private Context context;
    private List<Post> mPost;

    public AdvanceSearchPostAdapter(Context context, List<Post> mPost) {
        this.context = context;
        this.mPost = mPost;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advance_search_post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder viewHolder, int position) {
        viewHolder.populate(mPost.get(position));
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvPostUserName, tvPostDuration, tvPostLike, tvPostStar, postCategory, tvPostText;
        ImageView imgPostUser, imgPostPermission, imgPostImage;

        public PostViewHolder(View itemView) {
            super(itemView);

            tvPostUserName = itemView.findViewById(R.id.tvPostUserName);
            tvPostDuration = itemView.findViewById(R.id.tvPostDuration);
            tvPostLike = itemView.findViewById(R.id.tvPostLike);
            tvPostStar = itemView.findViewById(R.id.tvPostStar);
            postCategory = itemView.findViewById(R.id.postCategory);
            tvPostText = itemView.findViewById(R.id.tvPostText);

            imgPostUser = itemView.findViewById(R.id.imgPostUser);
            imgPostPermission = itemView.findViewById(R.id.imgPostPermission);
            imgPostImage = itemView.findViewById(R.id.imgPostImage);
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
            String imagePhoto = post.getPhoto();
            String imagePost = post.getPostImage();

            if (imagePhoto != null && imagePhoto.length() > 0) {
                Picasso.with(App.getAppContext())
                        .load(PROFILE_IMAGE + imagePhoto)
                        .noFade()
                        .placeholder(R.drawable.drawable_comment)
                        .into(imgPostUser);
            }
            if (imagePost != null && imagePost.length() > 0) {
                imgPostImage.setVisibility(View.VISIBLE);
                Picasso.with(App.getAppContext())
                        .load(POST_IMAGES + imagePost)
                        .placeholder(R.drawable.drawable_comment)
                        .into(imgPostImage);
            } else {
                imgPostImage.setVisibility(View.GONE);
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

}
