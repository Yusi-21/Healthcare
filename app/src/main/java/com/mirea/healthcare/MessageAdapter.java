package com.mirea.healthcare;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> messages;

    private final OnMessageLongClickListener longClickListener;

    public interface OnMessageLongClickListener {
        void showBottomSheet(int position);
    }

    public MessageAdapter(List<Message> messages, OnMessageLongClickListener listener) {
        this.messages = messages;
        this.longClickListener = listener;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.messageText.setText(message.getText());

        // Установка стиля в зависимости от отправителя
        if (message.isUser()) {
            holder.messageText.setBackgroundResource(R.drawable.bg_message_user);
            holder.messageText.setTextColor(Color.WHITE);
            holder.container.setGravity(Gravity.END);
        } else {
            holder.messageText.setBackgroundResource(R.drawable.bg_message_bot);
            holder.messageText.setTextColor(Color.BLACK);
            holder.container.setGravity(Gravity.START);
        }

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.showBottomSheet(position);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container;
        TextView messageText;

        public MessageViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.message_container);
            messageText = itemView.findViewById(R.id.message_text);
        }
    }
}