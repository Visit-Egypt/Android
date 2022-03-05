package com.visitegypt.presentation.chatbot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
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
        chatbotRecyclerView.setAdapter(ChatRecyclerViewAdapter);
        chatbotViewModel = new ViewModelProvider(this).get(ChatbotViewModel.class);

        fBtnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtTxtMessage.getText().toString().isEmpty()) {
                    Toast.makeText(ChatbotActivity.this, "Please enter your message..", Toast.LENGTH_SHORT).show();
                    return;
                }
                getResponse(edtTxtMessage.getText().toString());
                edtTxtMessage.setText("");
                chatbotViewModel.botResponseMutableLiveData.observe(ChatbotActivity.this, new Observer() {
                    @Override
                    public void onChanged(Object o) {
                        Log.d(TAG, "Bot response deliveried: " + o);
                        chatbotArrayList.add(new Chatbot(o.toString(), BOT_KEY));
                        ChatRecyclerViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void getResponse(String message) {
        chatbotArrayList.add(new Chatbot(message, USER_KEY));
        ChatRecyclerViewAdapter.notifyDataSetChanged();
        Log.d(TAG, "chatbot activity: " + message);
        chatbotViewModel.setMessage(message);
    }
}