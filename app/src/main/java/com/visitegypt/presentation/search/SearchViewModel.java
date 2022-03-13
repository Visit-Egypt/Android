package com.visitegypt.presentation.search;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

@HiltViewModel
public class SearchViewModel extends ViewModel {
    private OkHttpClient okHttpClient;
    private Request request;
    MutableLiveData<String> mutableLiveDataText = new MutableLiveData<>();
    private WebSocket webSocket;
    private EchoWebSocketListener echoWebSocketListener = new EchoWebSocketListener();
    @Inject
    public SearchViewModel(OkHttpClient okHttpClient, Request request) {
        this.okHttpClient = okHttpClient;
        this.request = request;
    }
    public void webSocketConnet() {
        webSocket = okHttpClient.newWebSocket(request, echoWebSocketListener);
//        okHttpClient.dispatcher().executorService().shutdown();
    }
    public void search(String text)
    {
        webSocket.send(text);
    }
    public void onResume()
    {

    }


    private class EchoWebSocketListener  extends WebSocketListener {
        public EchoWebSocketListener() {
            super();
        }

        @Override
        public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            super.onClosed(webSocket, code, reason);
        }

        @Override
        public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            super.onClosing(webSocket, code, reason);
        }

        @Override
        public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
            super.onMessage(webSocket, text);
            Log.d("TAG", "Web sockets Test onMessage: " + text);
            mutableLiveDataText.postValue(text);



        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
            super.onMessage(webSocket, bytes);
        }

        @Override
        public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
            super.onOpen(webSocket, response);
        }
    }
}
