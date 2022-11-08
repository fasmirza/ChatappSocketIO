package com.temotion.mirzas.chatappsocketio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Base64;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ChatActivityJ extends AppCompatActivity implements TextWatcher {

    String TAG = "ChatActivity";
    String name ;
    WebSocket webSocket;
    EditText messageEdit;
    TextView sendButton;
    ImageView pickImageButton;
    RecyclerView recyclerView;
    Integer IMAGE_REQUEST_ID = 1;
    String SERVER_PATH = "ws://echo.websocket.org";
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        name = getIntent().getStringExtra("name").toString();

        InitiateSocket();
    }

    private void InitiateSocket() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String string = s.toString().trim(); // Remove all white spaces from the string
        if (string.isEmpty()){
            resetMessageEdit();
        }else {
            sendButton.setVisibility(View.VISIBLE);
            pickImageButton.setVisibility(View.INVISIBLE);
        }

    }

    private void resetMessageEdit(){
        messageEdit.removeTextChangedListener(this);
        messageEdit.setText("");
        sendButton.setVisibility(View.INVISIBLE);
        pickImageButton.setVisibility(View.VISIBLE);
        messageEdit.addTextChangedListener(this);
    }

    private class SocketListener extends WebSocketListener{
        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
            super.onMessage(webSocket, text);
            runOnUiThread(()->{
               JsonObject jsonObject = new JsonObject();
               jsonObject.addProperty("isSent",false); // Is Sent set to false
                messageAdapter.addItems(jsonObject);
            });

        }

        @Override
        public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
            super.onOpen(webSocket, response);
            runOnUiThread(()->{
                Toast.makeText(ChatActivityJ.this, "Socket Connected",Toast.LENGTH_LONG).show();
                InitializeView();
            });
        }
    }
    private void InitializeView(){
        messageEdit = findViewById(R.id.messageEdit);
        sendButton = findViewById(R.id.sendTextview);
        pickImageButton = findViewById(R.id.pickeImage);
        recyclerView = findViewById(R.id.chatRecyclerView);

        messageAdapter = new MessageAdapter();
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageEdit.addTextChangedListener(this);

        sendButton.setOnClickListener(v->{
            JsonObject jsonObject = new JsonObject();

            try {
                jsonObject.addProperty("name",name);
                jsonObject.addProperty("message",messageEdit.getText().toString());
                webSocket.send(jsonObject.toString());
                jsonObject.addProperty("isSent",true);
                messageAdapter.addItems(jsonObject);
                resetMessageEdit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        pickImageButton.setOnClickListener(v -> {
            Intent pickImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
            pickImageIntent.setType("image/*");
            startActivityForResult(Intent.createChooser(pickImageIntent,"Pick Image"),IMAGE_REQUEST_ID);

        });

//        @Override
//        protected void onActivityResult(int requestcode, int rewultCode, @Nullable Intent intent){
//            super.onActivityResult();
//        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_ID && resultCode == RESULT_OK){
            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                Bitmap imgage = BitmapFactory.decodeStream(is);
                sendImgage(imgage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendImgage(Bitmap imgage) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imgage.compress(Bitmap.CompressFormat.JPEG,50,outputStream);

        String base64String = Base64.getEncoder().encodeToString(outputStream.toByteArray());
        JsonObject imageJsonObject = new JsonObject();

        try {
            imageJsonObject.addProperty("name",name);
            imageJsonObject.addProperty("message",base64String);
            webSocket.send(imageJsonObject.toString());
            imageJsonObject.addProperty("isSent",true);
            messageAdapter.addItems(imageJsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}