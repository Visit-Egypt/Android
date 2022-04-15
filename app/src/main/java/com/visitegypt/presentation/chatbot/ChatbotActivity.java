package com.visitegypt.presentation.chatbot;

import static com.visitegypt.presentation.gamification.GamificationActivity.ARTIFACTS;
import static com.visitegypt.presentation.gamification.GamificationActivity.INSIGHTS;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.visitegypt.R;
import com.visitegypt.domain.model.Chatbot;
import com.visitegypt.presentation.gamification.GamificationActivity;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatbotActivity extends AppCompatActivity {
    private static final String TAG = "Chatbot Activity";
    private final String USER_KEY = "user";
    private final String BOT_KEY = "bot";
    private RecyclerView chatbotRecyclerView;
    private FloatingActionButton fBtnSendMessage;
    private EditText edtTxtMessage;
    private ArrayList<Chatbot> chatbotArrayList;
    private ChatRecyclerViewAdapter ChatRecyclerViewAdapter;
    private ChatbotViewModel chatbotViewModel;
    private String type;
    private String placeTitle;
//    public String s;
//    public String x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                placeTitle = null;
                type = null;
            } else {
                placeTitle = extras.getString(GamificationActivity.PLACE_TITLE);
                type = extras.getString(GamificationActivity.MSG_TYPE);
            }
        } else {
            placeTitle = (String) savedInstanceState.getSerializable(GamificationActivity.PLACE_TITLE);
            type = (String) savedInstanceState.getSerializable(GamificationActivity.MSG_TYPE);
        }

        Log.d(TAG, "onCreate: type " + type);
        Log.d(TAG, "onCreate: title " + placeTitle);
        initViews();
    }

    private void initViews() {
        chatbotRecyclerView = findViewById(R.id.chatbotRecyclerView);
        fBtnSendMessage = findViewById(R.id.sendMessageFAButton);
        edtTxtMessage = findViewById(R.id.messageEditText);
        chatbotArrayList = new ArrayList<>();
        ChatRecyclerViewAdapter = new ChatRecyclerViewAdapter(chatbotArrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatbotActivity.this, RecyclerView.VERTICAL, false);
        chatbotRecyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        chatbotRecyclerView.setAdapter(ChatRecyclerViewAdapter);
        chatbotViewModel = new ViewModelProvider(this).get(ChatbotViewModel.class);
        if (type != null) {
            if (type.equals(ARTIFACTS)) {
                getResponse("Tell me about the artifacts of " + placeTitle);
                chatbotViewModel.botResponseMutableLiveData.observe(ChatbotActivity.this, new Observer() {
                    @Override
                    public void onChanged(Object o) {
                        Log.d(TAG, "Bot response delivered: " + o);
                        chatbotArrayList.add(new Chatbot(o.toString(), BOT_KEY));
                        ChatRecyclerViewAdapter.notifyDataSetChanged();
                    }
                });
            } else if (type.equals(INSIGHTS)) {
                getResponse("Tell me about the insights of " + placeTitle);
                chatbotViewModel.botResponseMutableLiveData.observe(ChatbotActivity.this, new Observer() {
                    @Override
                    public void onChanged(Object o) {
                        Log.d(TAG, "Bot response delivered: " + o);
                        chatbotArrayList.add(new Chatbot(o.toString(), BOT_KEY));
                        ChatRecyclerViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
        fBtnSendMessage.setOnClickListener(v -> {
            if (edtTxtMessage.getText().toString().isEmpty()) {
                Toast.makeText(ChatbotActivity.this, "Please enter your message..", Toast.LENGTH_SHORT).show();
                return;
            }
            getResponse(edtTxtMessage.getText().toString());
            edtTxtMessage.setText("");
            chatbotViewModel.botResponseMutableLiveData.observe(ChatbotActivity.this, new Observer() {
                @Override
                public void onChanged(Object o) {
                    Log.d(TAG, "Bot response delivered: " + o);
                    chatbotArrayList.add(new Chatbot(o.toString(), BOT_KEY));
                    ChatRecyclerViewAdapter.notifyDataSetChanged();
                }
            });
        });
    }

    private void getResponse(String message) {
        chatbotArrayList.add(new Chatbot(message, USER_KEY));
        ChatRecyclerViewAdapter.notifyDataSetChanged();
        Log.d(TAG, "chatbot activity: " + message);
        chatbotViewModel.setMessage(message);
    }
}