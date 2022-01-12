// Generated by view binder compiler. Do not edit!
package com.visitegypt.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.visitegypt.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityChatbotBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final RecyclerView chatbotRecyclerView;

  @NonNull
  public final RelativeLayout chatbotRelativeLayout;

  @NonNull
  public final RelativeLayout chatbotRelativeLayout2;

  @NonNull
  public final EditText messageEditText;

  @NonNull
  public final FloatingActionButton sendMessageFAButton;

  @NonNull
  public final View view1;

  private ActivityChatbotBinding(@NonNull ConstraintLayout rootView,
      @NonNull RecyclerView chatbotRecyclerView, @NonNull RelativeLayout chatbotRelativeLayout,
      @NonNull RelativeLayout chatbotRelativeLayout2, @NonNull EditText messageEditText,
      @NonNull FloatingActionButton sendMessageFAButton, @NonNull View view1) {
    this.rootView = rootView;
    this.chatbotRecyclerView = chatbotRecyclerView;
    this.chatbotRelativeLayout = chatbotRelativeLayout;
    this.chatbotRelativeLayout2 = chatbotRelativeLayout2;
    this.messageEditText = messageEditText;
    this.sendMessageFAButton = sendMessageFAButton;
    this.view1 = view1;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityChatbotBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityChatbotBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_chatbot, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityChatbotBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.chatbotRecyclerView;
      RecyclerView chatbotRecyclerView = ViewBindings.findChildViewById(rootView, id);
      if (chatbotRecyclerView == null) {
        break missingId;
      }

      id = R.id.chatbotRelativeLayout;
      RelativeLayout chatbotRelativeLayout = ViewBindings.findChildViewById(rootView, id);
      if (chatbotRelativeLayout == null) {
        break missingId;
      }

      id = R.id.chatbotRelativeLayout2;
      RelativeLayout chatbotRelativeLayout2 = ViewBindings.findChildViewById(rootView, id);
      if (chatbotRelativeLayout2 == null) {
        break missingId;
      }

      id = R.id.messageEditText;
      EditText messageEditText = ViewBindings.findChildViewById(rootView, id);
      if (messageEditText == null) {
        break missingId;
      }

      id = R.id.sendMessageFAButton;
      FloatingActionButton sendMessageFAButton = ViewBindings.findChildViewById(rootView, id);
      if (sendMessageFAButton == null) {
        break missingId;
      }

      id = R.id.view1;
      View view1 = ViewBindings.findChildViewById(rootView, id);
      if (view1 == null) {
        break missingId;
      }

      return new ActivityChatbotBinding((ConstraintLayout) rootView, chatbotRecyclerView,
          chatbotRelativeLayout, chatbotRelativeLayout2, messageEditText, sendMessageFAButton,
          view1);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
