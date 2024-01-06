package com.example.chattingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<MyMessage> {

    public MessageAdapter(@NonNull Context context, List<MyMessage> messages) {
        super(context,0, messages);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        MyMessage message = getItem(position);
        if (convertView == null){
//            convertView = LayoutInflater.from(getContext()).inflate(
//                    message.isOutgoing()? R.layout.item_message_outgoing : R.layout.item_message_incoming,
//                    parent,
//                    false
//            );

            convertView = LayoutInflater.from(getContext()).inflate(
                    message.getIsOutgoing() ? R.layout.item_message_outgoing : R.layout.item_message_incoming,
                    parent,
                    false
            );

        }
        TextView senderTextView = convertView.findViewById(R.id.senderTextView);
        TextView messageTextView = convertView.findViewById(R.id.messageTextView);

        senderTextView.setText(message.getSender());
        messageTextView.setText(message.getText());

        return convertView;
    }
}
