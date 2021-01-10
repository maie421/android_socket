package com.bluetooth.chat.chat;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.bluetooth.chat.R;

import java.net.URISyntaxException;
import java.nio.channels.DatagramChannel;
import io.socket.client.IO;
import io.socket.client.Socket;

public class ChatRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        
    }
}
