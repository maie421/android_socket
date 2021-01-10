package com.bluetooth.chat;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.bluetooth.chat.chat.ChatClientIO;
import com.bluetooth.chat.main.MainActivity;

public class LoginActivity extends AppCompatActivity {
    String Tag="로그인";

    EditText nickname_text;
    EditText idx_text;
    SharedSettings sharedSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        nickname_text = findViewById(R.id.main_nickname);
        idx_text= findViewById(R.id.main_room_idx);
        sharedSettings = new SharedSettings(this,"user_info");

    }
    public void go_chat(View view){
        sharedSettings.set_something_int("user_idx",Integer.parseInt(idx_text.getText().toString()));
        sharedSettings.set_something_string("user_nickname",nickname_text.getText().toString());

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}