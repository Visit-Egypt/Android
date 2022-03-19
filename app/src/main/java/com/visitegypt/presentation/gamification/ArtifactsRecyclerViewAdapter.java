package com.visitegypt.presentation.gamification;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.visitegypt.R;
import com.visitegypt.domain.model.Explore;
import com.visitegypt.domain.model.Hint;
import com.visitegypt.domain.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ArtifactsRecyclerViewAdapter extends RecyclerView.Adapter<com.visitegypt.presentation.gamification.ArtifactsRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "Artifacts Adapter in Gamification ";
    private List<Item> itemsArrayList;

    private Context context;
    private Explore dummyExplore;


    public ArtifactsRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public com.visitegypt.presentation.gamification.ArtifactsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gamification_item_card, parent, false);
        return new com.visitegypt.presentation.gamification.ArtifactsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(com.visitegypt.presentation.gamification.ArtifactsRecyclerViewAdapter.ViewHolder holder, int position) {

        Item currentItem = itemsArrayList.get(position);
        holder.itemTitleTextView.setText(currentItem.getTitle());

        holder.itemTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: kkkkkkkkk");
                showHints(view, currentItem);
            }
        });
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ssssssssssssss");

                showHints(view, currentItem);
            }
        });
        if (currentItem.getImageUrls() != null) {
            if (!currentItem.getImageUrls().isEmpty()) {
                Log.d(TAG, "found image for item: " + currentItem.getImageUrls().get(0).toString());
                Glide.with(holder.itemView)
                        .load(currentItem.getImageUrls().get(0))
                        .fitCenter()
                        .into(holder.itemImage);
            } else {
            }
        } else {
            holder.itemImage.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (itemsArrayList == null)
            return 0;
        return itemsArrayList.size();
    }

    public void setItemsArrayList(List<Item> itemsArrayList) {
        this.itemsArrayList = itemsArrayList;
        notifyDataSetChanged();
    }

    public void showHints(View view, Item currentItem) {
//        Context contextt;
//        contextt = view.getContext();
//        Intent intent = new Intent(contextt, ItemActivity.class);
//
//        intent.putStringArrayListExtra("images", currentItem.getImageUrls());
//        intent.putExtra("title", currentItem.getTitle());
//        intent.putExtra("place_id", currentItem.getPlaceId());
//        intent.putExtra("description", currentItem.getDescription());
//        contextt.startActivity(intent);

        ArrayList<Hint> hints = new ArrayList<>();
        hints.add(new Hint("He is super handsome"));
        hints.add(new Hint("He is super handsome"));
        hints.add(new Hint("Okay he's ugly, we lied", "https://file1.science-et-vie.com/var/scienceetvie/storage/images/1/0/4/104445/et-momie-revela-ses-secrets.jpg"));
        hints.add(new Hint("Okay he's ugly, we lied", "https://file1.science-et-vie.com/var/scienceetvie/storage/images/1/0/4/104445/et-momie-revela-ses-secrets.jpg"));
        dummyExplore = new Explore("Tout Ankha Amon", "https://images.lpcdn.ca/924x615/201002/13/147005-momie-toutankhamon.jpg", hints);

//        showExploreDialog(dummyExplore);
        showExploreDialog(currentItem);

    }

    private void showExploreDialog(Item item) {
        Dialog dialog = new Dialog(context);
        View v = LayoutInflater.from(context).inflate(R.layout.dialog_ar_explore, null, false);

        ImageView zoomableImageView = v.findViewById(R.id.gamificationHintDialogZoomableImageView);
        MaterialTextView materialTextView = v.findViewById(R.id.gamificationHintDialogTitle);
        materialTextView.setText(item.getTitle());

//        materialTextView.setText(explore.getTitle());

//        Picasso.get().load(explore.getImageUrl()).into(zoomableImageView, new Callback() {
//            @Override
//            public void onSuccess() {
//                PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(zoomableImageView);
//                photoViewAttacher.update();
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        });

//        GamificationHintRecyclerViewAdapter adapter = new GamificationHintRecyclerViewAdapter(item.getHints());
//        RecyclerView recyclerView = v.findViewById(R.id.gamificationHintDialogRecyclerView);
//        recyclerView.setAdapter(adapter);

        dialog.setContentView(v);
        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }


//    private void showExploreDialog(Explore explore) {
//        Dialog dialog = new Dialog(context);
//        View v = LayoutInflater.from(context).inflate(R.layout.dialog_ar_explore, null, false);
//
//        ImageView zoomableImageView = v.findViewById(R.id.gamificationHintDialogZoomableImageView);
//
//        MaterialTextView materialTextView = v.findViewById(R.id.gamificationHintDialogTitle);
//        materialTextView.setText("kkkkkk");
////        materialTextView.setText(explore.getTitle());
//
////        Picasso.get().load(explore.getImageUrl()).into(zoomableImageView, new Callback() {
////            @Override
////            public void onSuccess() {
////                PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(zoomableImageView);
////                photoViewAttacher.update();
////            }
////
////            @Override
////            public void onError(Exception e) {
////
////            }
////        });
//
//        GamificationHintRecyclerViewAdapter adapter = new GamificationHintRecyclerViewAdapter(explore.getHints());
//        RecyclerView recyclerView = v.findViewById(R.id.gamificationHintDialogRecyclerView);
//        recyclerView.setAdapter(adapter);
//
//        dialog.setContentView(v);
//        dialog.show();
//
//        Window window = dialog.getWindow();
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView itemImage;
        private MaterialTextView itemTitleTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage = itemView.findViewById(R.id.itemGamificationCardImageView);
            itemTitleTextView = itemView.findViewById(R.id.itemGamificationCardTitleTextView);
        }
    }
}