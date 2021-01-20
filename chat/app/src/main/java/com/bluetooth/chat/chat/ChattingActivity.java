package com.bluetooth.chat.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bluetooth.chat.R;
import com.google.gson.Gson;

import com.bluetooth.chat.SharedSettings;
import com.bluetooth.chat.adapter.ChattingAdapter;
import com.bluetooth.chat.data_list.ChattingModel;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.socket.client.Ack;

import com.google.gson.reflect.TypeToken;

public class ChattingActivity extends AppCompatActivity {

    private String TAG="채팅";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private String userName;

    int user_idx;
    EditText mInputMessage_text;
    ChattingAdapter ChattingAdapter;
    RecyclerView chat_recyclerview;
    SharedSettings sharedSettings;
    SharedSettings sharedSettings_chat;
    ArrayList<ChattingModel> chat_data;
    String nickname;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_room);

        mInputMessage_text= findViewById(R.id.editText);
        chat_recyclerview = findViewById(R.id.chatting_recyclerview);

        // 서버로부터 전달받은 'chat-message' Event 처리.
        gson = new Gson();

        //user 정보
        sharedSettings = new SharedSettings(this, "user_info");
        sharedSettings_chat = new SharedSettings(this, "user_chat");
        nickname=sharedSettings.get_something_string("user_nickname");
        user_idx=sharedSettings.get_something_int("user_idx");

        //LocalBroadcastManager.getInstance(this).registerReceiver(MessageReceiver, new IntentFilter("go_chatingroom"));
    }


    //메세지 전송 버튼
    public void send_message(View view) {
        String sendMessage  = mInputMessage_text.getText().toString().trim();
        Log.d(TAG , "message 전송:" + sendMessage);

        // 서버로 전송할 데이터 생성 및 메시지 이벤트 보냄.
        long send_timemills = System.currentTimeMillis();
        Date mReDate = new Date(send_timemills);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = mFormat.format(mReDate);

        ChattingModel ChattingModel = new ChattingModel(nickname,user_idx,"C",sendMessage,formatDate,send_timemills);
        ChatClientIO.emit_socket(ChattingActivity.this,"message",gson.toJson(ChattingModel),new Ack(){
                @Override
                public void call(Object... args) {
                    Log.d(TAG,"[callback] "+args[0]+args[1]);

                    switch (String.valueOf(args[0])) {
                        case "success":
                            ChattingModel chattingModel = gson.fromJson(args[1].toString(),ChattingModel.class);
                            Log.d(TAG,gson.toJson(chattingModel));
//                            sharedSettings.set_chatroom_messages("chatting",gson.toJson(chattingModel));
                            break;
                    }
                }
            });

            //TODO 문제 채팅저장부분

        //sharedSettings_chat.set_chatroom_messages(gson.toJson(ChattingModel));
        ChattingAdapter.add_front_message(ChattingModel);

        sharedSettings_chat.set_chatroom_messages("chatting",gson.toJson(ChattingModel));
        chat_recyclerview.scrollToPosition(ChattingAdapter.getChat_data().size()-1);
        mInputMessage_text.setText(null);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //chat_data = new ArrayList<>();
        Log.d(TAG,sharedSettings.get_chatroom_messages("chatting"));
        if(!sharedSettings.get_chatroom_messages("chatting").equals("없음")) {
            Log.d(TAG,"없음");
            Type type = new TypeToken<ArrayList<ChattingModel>>() {}.getType();
            chat_data = gson.fromJson(sharedSettings.get_chatroom_messages("chatting"),type);
        }else{
            chat_data = new ArrayList<>();;
        }
        //TODO 서버와 통신해서 ChatroomModel 불러와야 한다. or 쉐어드
        ChattingAdapter = new ChattingAdapter(this,chat_data,user_idx);
        chat_recyclerview.setAdapter(ChattingAdapter);
        chat_recyclerview.setLayoutManager(new LinearLayoutManager(this));


    }
}
