package com.visitegypt.presentation.tripmateRequest;


import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.TripMateSentRequest;
import com.visitegypt.presentation.callBacks.OnItemClickCallBack;

import java.util.List;

public class TripMateRequestAdapter extends RecyclerView.Adapter<TripMateRequestAdapter.UserTripMateRequestViewHolder> {
    private List<TripMateSentRequest> tripMateSentRequests;
    int currentPosition = 0;
    private OnItemClickCallBack onItemClickCallBack;

    public TripMateRequestAdapter(OnItemClickCallBack onItemClickCallBack, List<TripMateSentRequest> tripMateSentRequests) {
        this.tripMateSentRequests = tripMateSentRequests;
        this.onItemClickCallBack = onItemClickCallBack;
    }

    @NonNull
    @Override
    public UserTripMateRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserTripMateRequestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card_trip_mate_request_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserTripMateRequestViewHolder holder, int position) {
        if (!tripMateSentRequests.isEmpty() && !tripMateSentRequests.get(position).isApproved()) {
            if (tripMateSentRequests.get(position).getUserName() != null)
                holder.userName.setText(tripMateSentRequests.get(position).getUserName());
            if (tripMateSentRequests.get(position).getFollowersNumber() > 1)
                holder.userFollowRequestMaterialTextView.setText(String.valueOf(tripMateSentRequests.get(position).getFollowersNumber()) + " Followers");
            else
                holder.userFollowRequestMaterialTextView.setText(String.valueOf(tripMateSentRequests.get(position).getFollowersNumber()) + " Follower");
            if (tripMateSentRequests.get(position).getPhotoUrl() != null && !tripMateSentRequests.get(position).getPhotoUrl().isEmpty())
                Picasso.get().load(tripMateSentRequests.get(position).getPhotoUrl()).into(holder.userImage);

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
                if (tripMateSentRequests.get(position).getPhotoUrl() != null && !tripMateSentRequests.get(position).getPhotoUrl().isEmpty())
                    Picasso.get().load(tripMateSentRequests.get(position).getPhotoUrl()).into(dialogUserImage);
                if (tripMateSentRequests.get(position).getUserName() != null)
                    dialogUserName.setText(tripMateSentRequests.get(position).getUserName());
                if (tripMateSentRequests.get(position).getTitle() != null && !tripMateSentRequests.get(position).getTitle().isEmpty()) {
                    dialogTitleTripMateRequest.setText(tripMateSentRequests.get(position).getTitle());

                }
                if (tripMateSentRequests.get(position).getDescription() != null && !tripMateSentRequests.get(position).getDescription().isEmpty()) {
                    txtDescriptionTripMateRequest.setText(tripMateSentRequests.get(position).getDescription());
                }


                builder.setView(dialogLayout);
                builder.setCancelable(true);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                dialogMaterialButton.setOnClickListener(v1 -> {
                    Log.d("TAG", "onBindViewHolder: show user profile ");
                    alertDialog.dismiss();
                    onItemClickCallBack.onItemCallBack(tripMateSentRequests.get(position).getUserID(), 1);
                });

            });
        } else {

        }

    }

    @Override
    public int getItemCount() {
        return tripMateSentRequests.size() != 0 ? tripMateSentRequests.size() : 0;
    }

    public void updateTripMateSentRequestList(List<TripMateSentRequest> tripMateSentRequests) {
        this.tripMateSentRequests = tripMateSentRequests;
        notifyDataSetChanged();
    }

    public void removeUser() {
        tripMateSentRequests.remove(currentPosition);
        notifyItemRemoved(currentPosition);
        notifyItemRangeChanged(currentPosition, tripMateSentRequests.size());
    }

    public class UserTripMateRequestViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView userName, userFollowRequestMaterialTextView;
        private final CircularImageView userImage;
        private final MaterialButton acceptRequestButton;

        public UserTripMateRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            acceptRequestButton = itemView.findViewById(R.id.acceptRequestButton);
            userName = itemView.findViewById(R.id.userNameRequestMaterialTextView);
            userImage = itemView.findViewById(R.id.imgUser);
            userFollowRequestMaterialTextView = itemView.findViewById(R.id.userFollowRequestMaterialTextView);
            acceptRequestButton.setOnClickListener(v -> {
                currentPosition = getAdapterPosition();
                onItemClickCallBack.onItemCallBack(tripMateSentRequests.get(getAdapterPosition()).getId(), 0);
            });

        }
    }
}
