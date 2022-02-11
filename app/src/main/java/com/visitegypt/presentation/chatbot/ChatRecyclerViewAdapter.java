package com.visitegypt.presentation.chatbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visitegypt.R;
import com.visitegypt.domain.model.Chatbot;

import java.util.ArrayList;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter {

    private final ArrayList<Chatbot> chatbotArrayList;
    private final Context context;

    public ChatRecyclerViewAdapter(ArrayList<Chatbot> chatbotArrayList, Context context) {
        this.chatbotArrayList = chatbotArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_message, parent, false);
                return new UserViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_message, parent, false);
                return new BotViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chatbot chatbot = chatbotArrayList.get(position);
        switch (chatbot.getSender()) {
            case "user":
                ((UserViewHolder) holder).userTextView.setText(chatbot.getMessage());
                break;
            case "bot":
                ((BotViewHolder) holder).botTextView.setText(chatbot.getMessage());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (chatbotArrayList.get(position).getSender()) {
            case "user":
                return 0;
            case "bot":
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return chatbotArrayList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userTextView;

        public UserViewHolder(@NonNull View itenView) {
            super(itenView);
            userTextView = itenView.findViewById(R.id.userTextView);
        }
    }

    public static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView botTextView;

        public BotViewHolder(@NonNull View itenView) {
            super(itenView);
            botTextView = itenView.findViewById(R.id.botTextView);
        }
    }
}