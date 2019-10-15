package com.doodle.Search.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.doodle.App;
import com.doodle.R;
import com.doodle.Search.model.Post;
import com.doodle.Search.service.PostClickListener;
import com.doodle.Tool.Operation;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.doodle.Tool.AppConstants.POST_IMAGES;
import static com.doodle.Tool.AppConstants.PROFILE_IMAGE;

public class AdvanceSearchPostAdapter extends RecyclerView.Adapter<AdvanceSearchPostAdapter.PostViewHolder> {

    private Context context;
    private List<Post> mPost;
    private PostClickListener postClickListener;

    public AdvanceSearchPostAdapter(Context context, List<Post> mPost, PostClickListener postClickListener) {
        this.context = context;
        this.mPost = mPost;
        this.postClickListener = postClickListener;
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
            long myMillis = Long.parseLong(post.getPostDate()) * 1000;
            String postDate = Operation.postDateCompare(context, myMillis);
            tvPostDuration.setText(postDate);
            tvPostLike.setText(post.getTotalLike() + " " + context.getString(R.string.likes));
            int totalStar = Integer.parseInt(post.getSliverStars()) + Integer.parseInt(post.getGoldStars());
            tvPostStar.setText(String.valueOf(totalStar) + " " + context.getString(R.string.stars));
            postCategory.setText(post.getCategoryName());
            tvPostText.setText(post.getPostText());
            String imagePhoto = PROFILE_IMAGE + post.getPhoto();
            String imagePost = POST_IMAGES + post.getPostImage();

            Glide.with(App.getAppContext())
                    .load(imagePhoto)
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(imgPostUser);

            Glide.with(App.getAppContext())
                    .load(imagePost)
                    .placeholder(R.drawable.stack)
                    .error(R.drawable.stack)
                    .centerCrop()
                    .into(imgPostImage);

            if (post.getPostType().equals("1") || post.getPostType().equals("6")) {
                imgPostImage.setVisibility(View.GONE);
            } else {
                imgPostImage.setVisibility(View.VISIBLE);
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (post.getPostType().equals("2") || post.getPostType().equals("3") || post.getPostType().equals("4")) {
                        postClickListener.onPostClickListener(post);
                    }
                }
            });
        }
    }

}
