package com.visitegypt.presentation.tripmateRequest;


import android.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.User;
import com.visitegypt.presentation.callBacks.OnItemClickCallBack;
import com.visitegypt.presentation.home.parent.Home;
import com.visitegypt.presentation.userProfile.UserProfile;

import java.util.List;

public class UserTripMateRequestAdapter extends RecyclerView.Adapter<UserTripMateRequestAdapter.UserTripMateRequestViewHolder> {
    private List<User> users;
    int currentPosition = 0 ;
    private OnItemClickCallBack onItemClickCallBack;

    public UserTripMateRequestAdapter( OnItemClickCallBack onItemClickCallBack, List<User> users) {
        this.users = users;
        this.onItemClickCallBack = onItemClickCallBack;
    }

    @NonNull
    @Override
    public UserTripMateRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserTripMateRequestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card_trip_mate_request_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserTripMateRequestViewHolder holder, int position) {
        if (!users.isEmpty() && !users.get(position).getTripMateSentRequest().isApproved()) {
            if (users.get(position).getFirstName() != null && users.get(position).getLastName() != null)
                holder.userName.setText(users.get(position).getFirstName() + " " + users.get(position).getLastName());
            if (users.get(position).getPhotoUrl() != null && !users.get(position).getPhotoUrl().isEmpty())
                Picasso.get().load(users.get(position).getPhotoUrl()).into(holder.userImage);

            holder.itemView.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                View dialogLayout = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_request_trip_mate, null);
                CircularImageView dialogUserImage;
                MaterialTextView dialogUserName, dialogTitleTripMateRequest, txtDescriptionTripMateRequest;
                dialogUserImage = dialogLayout.findViewById(R.id.dialogImgUser);
                dialogUserName = dialogLayout.findViewById(R.id.dialogUserName);
                dialogTitleTripMateRequest = dialogLayout.findViewById(R.id.dialogTitleTripMateRequest);
                txtDescriptionTripMateRequest = dialogLayout.findViewById(R.id.txtDescriptionTripMateRequest);
                MaterialButton dialogMaterialButton = dialogLayout.findViewById(R.id.dialogProfileButton);
                /************************************************************************************************/
                if (users.get(position).getPhotoUrl() != null && !users.get(position).getPhotoUrl().isEmpty())
                    Picasso.get().load(users.get(position).getPhotoUrl()).into(dialogUserImage);
                if (users.get(position).getFirstName() != null && users.get(position).getLastName() != null)
                    dialogUserName.setText(users.get(position).getFirstName() + " " + users.get(position).getLastName());
                if (users.get(position).getTripMateSentRequest().getTitle() != null && !users.get(position).getTripMateSentRequest().getTitle().isEmpty()) {
                    dialogTitleTripMateRequest.setText(users.get(position).getTripMateSentRequest().getTitle());

                }
                if (users.get(position).getTripMateSentRequest().getDescription() != null && !users.get(position).getTripMateSentRequest().getDescription().isEmpty()) {
                    txtDescriptionTripMateRequest.setText(users.get(position).getTripMateSentRequest().getDescription());
                }


                builder.setView(dialogLayout);
                builder.setCancelable(true);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                dialogMaterialButton.setOnClickListener(v1 -> {
                    Log.d("TAG", "onBindViewHolder: show user profile ");
                    alertDialog.dismiss();
                    onItemClickCallBack.onItemCallBack(users.get(position).getTripMateSentRequest().getUserID(),1);
                });

            });
        }

    }

    @Override
    public int getItemCount() {
        return users.size() != 0 ? users.size() : 0;
    }

    public void updateUserList(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }
    public void removeUser() {
        users.remove(currentPosition);
        notifyItemRemoved(currentPosition);
        notifyItemRangeChanged(currentPosition, users.size());
    }

    public class UserTripMateRequestViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView userName;
        private final CircularImageView userImage;
        private final MaterialButton acceptRequestButton;

        public UserTripMateRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            acceptRequestButton = itemView.findViewById(R.id.acceptRequestButton);
            userName = itemView.findViewById(R.id.txtUserName);
            userImage = itemView.findViewById(R.id.imgUser);
            acceptRequestButton.setOnClickListener(v -> {
                onItemClickCallBack.onItemCallBack(users.get(getAdapterPosition()).getTripMateSentRequest().getId(),0);
                if (users.get(getAdapterPosition()).getTripMateSentRequest().isApproved())
                {
                    currentPosition = getAdapterPosition();
                    Log.d("TAG", "UserTripMateRequestViewHolder: " + currentPosition);
                }
            });

        }
    }
}
