package com.visitegypt.presentation.account;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.Post;

import java.util.List;

public class PostsRecyclerViewAdapter extends RecyclerView.Adapter<PostsRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "Posts Adapter";
    int flag = 0;
    private List<Post> postsArrayList;
    private Context context;

    public PostsRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PostsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_card, parent, false);
        return new PostsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsRecyclerViewAdapter.ViewHolder holder, int position) {
        Post currentPost = postsArrayList.get(position);
        holder.userNameMaterialTextView.setText(currentPost.getUserName());
        holder.captionPostMaterialTextView.setText(currentPost.getCaption());
        if (currentPost.getLikes().size() != 0) {
            holder.numOfLikesPostMaterialTextView.setText(" " + currentPost.getLikes().size());
        } else {
            holder.numOfLikesPostMaterialTextView.setText("No Likes");
        }
        Log.d(TAG, "onBindViewHolder: " + currentPost.getListOfImages());

        if (currentPost.getListOfImages().size() != 0) {
            Log.d(TAG, "onBindViewHolder: " + currentPost.getListOfImages().get(0));
            //        holder.imagePostImageView.setImageURI(currentPost.getListOfImages().get(0));
            Picasso.get().load(currentPost.getListOfImages().get(0)).into(holder.imagePostImageView);

        } else {
            holder.imagePostImageView.setVisibility(View.GONE);
        }
        holder.likePostImageView.setOnClickListener(view -> {
            if (flag == 0 || currentPost.getLikes().size() == 0) {
                currentPost.getLikes().add("d");
                flag = 1;
                holder.likePostImageView.setImageResource(R.drawable.liked);
            } else if (currentPost.getLikes().size() != 0) {
                flag = 0;
                currentPost.getLikes().remove(0);
                holder.likePostImageView.setImageResource(R.drawable.like);

            }
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        if (postsArrayList == null)
            return 0;
        return postsArrayList.size();
    }

    public void setPostsArrayList(List<Post> postsArrayList) {
        this.postsArrayList = postsArrayList;
        Log.d(TAG, "setPostsArrayList: " + postsArrayList.size());
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MaterialTextView captionPostMaterialTextView, numOfLikesPostMaterialTextView, userNameMaterialTextView;
        private ImageView imagePostImageView, likePostImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            captionPostMaterialTextView = itemView.findViewById(R.id.captionPostMaterialTextView);
            userNameMaterialTextView = itemView.findViewById(R.id.userNameMaterialTextView);
            imagePostImageView = itemView.findViewById(R.id.imagePostImageView);
            likePostImageView = itemView.findViewById(R.id.likePostImageView);
            numOfLikesPostMaterialTextView = itemView.findViewById(R.id.numOfLikesPostMaterialTextView);
        }
    }
}
