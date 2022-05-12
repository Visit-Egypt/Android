package com.visitegypt.presentation.tripmate;

import static com.visitegypt.utils.Constants.CHOSEN_USER_ID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.Place;
import com.visitegypt.domain.model.User;
import com.visitegypt.presentation.detail.DetailActivity;
import com.visitegypt.presentation.home.parent.Home;
import com.visitegypt.presentation.userProfile.UserProfile;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewAdapter> {
    private static final String TAG = "User Adapter";
    private final Context context;
    private final Fragment fragment;
    private List<User> users;

    public UserAdapter(Context context,Fragment fragment, List<User> users) {
        this.context = context;
        this.users = users;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewAdapter(LayoutInflater.from(parent.getContext()).inflate(R.layout.users_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewAdapter holder, int position) {

        if (users.get(position).getPhotoUrl() != null) {
            Picasso.get().load(users.get(position).getPhotoUrl()).into(holder.imgUser);
        }
        if ((users.get(position).getFirstName() != null) && (users.get(position).getLastName() != null))
        {
            holder.txtUserName.setText(users.get(position).getFirstName() + " " + users.get(position).getLastName() );

        }


    }

    @Override
    public int getItemCount() {
        if (users == null)
            return 0;
        if (users.isEmpty())
            return 0;
        return users.size();
    }
    public void updateUserList(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }
    public class UserViewAdapter extends RecyclerView.ViewHolder {
        private final CircularImageView imgUser;
        private final ShapeableImageView imgSendRequest;
        private final  MaterialTextView txtUserName;

        public UserViewAdapter(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.imgUser);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            imgSendRequest = itemView.findViewById(R.id.imgSendRequest);

            itemView.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString(CHOSEN_USER_ID,users.get(getAdapterPosition()).getUserId());
                ((Home) fragment.getActivity()).changeFragmentWithBundle(new UserProfile(),bundle);

            });
        }

    }
}
