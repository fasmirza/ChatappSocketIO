package com.temotion.mirzas.chatappsocketio

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.app.ActivityCompat
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private  val TAG = "MainActivity"
    var REQUEST_CODE = 10
    lateinit var startChat :Button
    lateinit var nameEditText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*checking external storate permission*/
//        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            Log.v(TAG,"Permission is granted");
//            ActivityCompat.requestPermissions(this, String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
//        }

        nameEditText = findViewById(R.id.nameEditText)
        startChat = findViewById(R.id.enterButton)

        startChat.setOnClickListener(View.OnClickListener {
            val chatIntent = Intent(this, ChatActivityJ::class.java)
            chatIntent.putExtra("name",nameEditText.text.toString())
            startActivity(chatIntent)


        })

    }
}