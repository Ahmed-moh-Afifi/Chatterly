package com.example.chatterly.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatterly.R;
import com.example.chatterly.model.data.Chat;
import com.example.chatterly.model.data.Message;
import com.example.chatterly.model.data.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final List<Message> messages = new ArrayList<>();
    private Chat chat;

    public MessageAdapter(Chat chat) {
        this.chat = chat;
        Log.d("MessageAdapter", "Users count in chat = " + chat.getUsers().size());
    }

    public void setMessages(List<Message> messageList) {
        for (Message message: messageList) {
            for (User user : chat.getUsers()) {
                if (Objects.equals(message.getAuthorId(), user.getId())) {
                    message.setAuthor(user);
                    break;
                }
            }
        }
        messages.clear();
        messages.addAll(messageList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageText;
        private final TextView senderName;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            senderName = itemView.findViewById(R.id.senderName);
        }

        public void bind(Message message) {
            messageText.setText(message.getBody());
            senderName.setText(message.getAuthor().getUserName());
        }
    }
}
