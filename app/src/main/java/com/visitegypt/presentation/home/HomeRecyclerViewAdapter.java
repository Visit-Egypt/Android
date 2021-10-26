package com.visitegypt.presentation.home;

import android.content.Context;
import android.content.Intent;
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
    private static final String TAG = "Home RecyclerView Adapter";

    public static String CHOSEN_PLACE_ID = "placeId";

    private List<Place> placesList;
    private Context context;

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
        holder.titleTextView.setText(placesList.get(position).getTitle());
        holder.descriptionTextView.setText(placesList.get(position).getDescription());
        if (placesList.get(position).getImageUrls() != null)
            Picasso.get().load(placesList.get(position).getImageUrls().get(0)).into(holder.placeImageView);
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
        private final ImageView placeImageView;
        private final TextView titleTextView;
        private final TextView descriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeImageView = itemView.findViewById(R.id.placeCardImageView);
            titleTextView = itemView.findViewById(R.id.placeCardTitleTextView);
            descriptionTextView = itemView.findViewById(R.id.placeCardDescriptionTextView);

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
