package com.visitegypt.presentation.home;

import static com.visitegypt.utils.Constants.CustomerType.EGYPTIAN_STUDENT;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.visitegypt.R;
import com.visitegypt.domain.model.Place;
import com.visitegypt.presentation.detailplace.DetailActivity;

import java.util.List;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "Home Adapter";

    public static String CHOSEN_PLACE_ID = "placeId";
    private final Context context;
    private List<Place> placesList;

    public HomeRecyclerViewAdapter(List<Place> placesList, Context context) {
        this.placesList = placesList;
        this.context = context;
    }

    public void addPlacesPage(List<Place> placesList) {
        notifyItemRangeInserted(this.placesList.size(), placesList.size() - this.placesList.size());
        this.placesList = placesList;
    }

    public void updatePlacesList(List<Place> placesList) {
        this.placesList = placesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.txtTitle.setText(placesList.get(position).getTitle());
        if (placesList.get(position).getTicketPrices() != null) {
            try {
                int startingPrice = placesList.get(position).getTicketPrices().get(EGYPTIAN_STUDENT.toString());
                holder.txtDescription.setText("Starting at " + startingPrice + " EGP");
            } catch (Exception e) {
                Log.e(TAG, "couldn't load price text: " + e.getMessage());
                holder.txtDescription.setVisibility(View.GONE);
            }
        } else {
            holder.txtDescription.setVisibility(View.GONE);
        }

        if (placesList.get(position).getDefaultImage() != null) {
            Log.d(TAG, "default image found for: " + placesList.get(position).getTitle());
            Picasso.get().load(placesList.get(position).getDefaultImage()).into(holder.imgPlace);
        } else if (placesList.get(position).getImageUrls() != null) {
            Log.d(TAG, "default image not found! loading first image instead for: " + placesList.get(position).getTitle());
            Picasso.get().load(placesList.get(position).getImageUrls().get(0)).into(holder.imgPlace);
        } else {
            Log.d(TAG, "no images found for: " + placesList.get(position).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        if (placesList == null)
            return 0;
        if (placesList.isEmpty())
            return 0;
        return placesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgPlace;
        private final TextView txtTitle;
        private final TextView txtDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPlace = itemView.findViewById(R.id.imgPlaceCard);
            txtTitle = itemView.findViewById(R.id.txtPlaceCardTitle);
            txtDescription = itemView.findViewById(R.id.txtPlaceCardDescription);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(CHOSEN_PLACE_ID, placesList.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
