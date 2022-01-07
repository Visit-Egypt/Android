package com.visitegypt.presentation.chatbot;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.visitegypt.R;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class ChatbotModel extends AppCompatActivity {
    private static final String TAG = "Chatbot Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
    }
}
