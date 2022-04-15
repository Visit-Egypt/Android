package com.visitegypt.presentation.home.parent;

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
import com.visitegypt.domain.model.SearchPlace;
import com.visitegypt.presentation.detail.DetailActivity;

import java.util.List;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "Home Adapter";

    public static String CHOSEN_PLACE_ID = "placeId";
    private final Context context;
    private List<SearchPlace> placesList;

    public SearchRecyclerViewAdapter(List<SearchPlace> placesList, Context context) {
        this.placesList = placesList;
        this.context = context;
    }

    public void addPlacesPage(List<SearchPlace> placesList) {
        notifyItemRangeInserted(this.placesList.size(), placesList.size() - this.placesList.size());
        this.placesList = placesList;
    }

    public void updatePlacesList(List<SearchPlace> placesList) {
        this.placesList = placesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.txtTitle.setText(placesList.get(position).getTitle());
        if (placesList.get(position).getDefault_image() != null) {
            Log.d(TAG, "default image found for: " + placesList.get(position).getTitle());
            Picasso.get().load(placesList.get(position).getDefault_image()).into(holder.imgPlace);
        }else {
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
//        private final TextView txtDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPlace = itemView.findViewById(R.id.imgPlaceCardNew);
            txtTitle = itemView.findViewById(R.id.txtPlaceCardNewTitle);

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
