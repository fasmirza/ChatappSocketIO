package com.temotion.mirzas.chatappsocketio;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MessageAdapter extends RecyclerView.Adapter {

    private static final int TYPE_MESSAGE_SENT = 0;
    private static final int TYPE_MESSAGE_RECEIVED= 1;
    private static final int TYPE_IMAGE_SENT = 2;
    private static final int TYPE_IMAGE_RECEIVED = 3;

    private LayoutInflater inflater;
    private List<JsonObject> messages = new ArrayList<>();

    public void MessageAdapter(LayoutInflater inflater){
        this.inflater = inflater;
    }



    private class SendMessageHolder extends RecyclerView.ViewHolder{
        TextView messageTxt;
        public SendMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageTxt = itemView.findViewById(R.id.sendingMsg);
        }
    }

    private class SendImageHolder extends RecyclerView.ViewHolder{
        ImageView imageSent;

        public SendImageHolder(@NonNull View itemView) {
            super(itemView);
            imageSent = itemView.findViewById(R.id.sendingImg);
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder{
        TextView nameTxt,messageTxt;
        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            messageTxt = itemView.findViewById(R.id.receivedText);
        }
    }

    private class ReceivedImageolder extends RecyclerView.ViewHolder{
        TextView nametext;
        ImageView receivedImage;
        public ReceivedImageolder(@NonNull View itemView) {
            super(itemView);
            nametext = itemView.findViewById(R.id.nameTxtimge);
            receivedImage = itemView.findViewById(R.id.receivedImage);
        }
    }

    /*Determine the message Type*/

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        JsonObject message = messages.get(position);
        try {
            if (message.get("isSent").getAsBoolean()){
                if (message.has("messagee")){
                    return TYPE_MESSAGE_SENT;
                }else{
                    return TYPE_IMAGE_SENT;
                }
            }else {
                if (message.has("messagee")){
                    return TYPE_MESSAGE_RECEIVED;
                }else{
                    return TYPE_IMAGE_RECEIVED;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case TYPE_MESSAGE_SENT:
                view = inflater.inflate(R.layout.item_sent_message,parent,false);
                return new SendMessageHolder(view);
            case TYPE_IMAGE_SENT:
                view = inflater.inflate(R.layout.item_sent_image,parent,false);
                return new SendMessageHolder(view);
            case TYPE_MESSAGE_RECEIVED:
                view = inflater.inflate(R.layout.item_received_message,parent,false);
                return new SendMessageHolder(view);
            case TYPE_IMAGE_RECEIVED:
                view = inflater.inflate(R.layout.item_received_photo,parent,false);
                return new SendMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        JsonObject message = messages.get(position);

        try {
            if (message.get("isSent").getAsBoolean()){
                if (message.has("messagee")){
                    SendMessageHolder messageHolder = (SendMessageHolder) holder;
                    messageHolder.messageTxt.setText(message.get("message").getAsString());
                }else{
                    SendImageHolder imageHolder = (SendImageHolder) holder;
                    //Bitmap bitmap = bit
                }
            }else {
                if (message.has("messagee")){
                    ReceivedMessageHolder messageHolder = (ReceivedMessageHolder) holder;
                    messageHolder.messageTxt.setText(message.get("message").getAsString());
                }else{
                    Log.d(TAG, "onBindViewHolder: Sent Image");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
/* when ever a new message come through then this method will be called*/
    public void addItems (JsonObject jsonObject){
        messages.add(jsonObject);
        notifyDataSetChanged();
    }

}
