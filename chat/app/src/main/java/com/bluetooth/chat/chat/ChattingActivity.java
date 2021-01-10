package com.bluetooth.chat.chat;

import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.bluetooth.chat.Constants;
import com.bluetooth.chat.data_list.ChatMessage;
import com.bluetooth.chat.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;
import okhttp3.OkHttpClient;

public class ChattingActivity extends AppCompatActivity {

    private String TAG="채팅";

    private String userName;
    private Socket mSocket;

    EditText mInputMessage_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_room);

        mInputMessage_text= findViewById(R.id.editText);

        getIntentData();

    }

    private void getIntentData() {
        if (getIntent() == null) {
            finish();
            return;
        }

        userName = "1";
        Log.d(TAG,"username:"+ userName);

        setupSocketClient();
    }
    private void setupSocketClient() {
        IO.Options opts = new IO.Options();
        opts.transports = new String[] { WebSocket.NAME };
        try {
            mSocket = IO.socket(Constants.SOCKET_URL, opts);
            mSocket.connect();
            mSocket.emit("add user",userName);
        }catch (URISyntaxException e) {
            Log.d(TAG,"Socket 연결 error:"+getPrintStackTrace(e));
        }
        mSocket.on(Socket.EVENT_CONNECT, onConnect);

        mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, " EVENT_DISCONNECT");
            }
        });
        mSocket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "EVENT_CONNECT_ERROR");


            }
        });
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "EVENT_CONNECT_TIMEOUT");
            }
        });
        mSocket.on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("서버응답에러", args[0].toString());
            }
        });
        //mSocket.on(Constants.EVENT_SYSTEM, onMessageReceived);
        //mSocket.on(Constants.EVENT_MESSAGE, onMessageReceived);

        mSocket.connect();

    }

    /**
     * Socket Server 연결 Listener
     */
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "EVENT_CONNECT");
            Log.d(TAG, "연결되었습니다!!");

            // 서버로 전송할 데이터 생성 및 채널 입장 이벤트 보냄.
            JSONObject sendData = new JSONObject();
            try {
                sendData.put(Constants.SEND_DATA_USERNAME, userName);
                mSocket.emit(Constants.ADD_USER, sendData);
            } catch (JSONException e) {
                Log.d(TAG,"Listener error:"+getPrintStackTrace(e));
            }
        }

    };

    /**
     * Message 전달 Listener
     */
    private Emitter.Listener onMessageReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject rcvData = (JSONObject) args[0];
            String userAction = rcvData.optString("action");
            String messageType = rcvData.optString("type");
            String messageOwner = rcvData.optJSONObject("data").optString("username");
            String messageContent = rcvData.optJSONObject("data").optString("message");

            final ChatMessage message = new ChatMessage(userAction, messageType, messageOwner, messageContent);
            Log.d(TAG ,"Action:" + message.getUserAction());
            Log.d(TAG , "Type:" + message.getMessageType());
            Log.d(TAG , "Owner: " + message.getMessageOwner());
            Log.d(TAG , "message: " + message.getMessageContent());

        }
    };

    //에러 확인
    public static String getPrintStackTrace(Exception e) {

        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        return errors.toString();

    }

    //메세지 전송 버튼
    public void send_message(View view) {
        String sendMessage  = mInputMessage_text.getText().toString().trim();
        Log.d(TAG , "message 전송:" + sendMessage);

        // 서버로 전송할 데이터 생성 및 메시지 이벤트 보냄.
        JSONObject sendData = new JSONObject();
        try {
            sendData.put(Constants.SEND_DATA_MESSAGE, sendMessage);
            mSocket.emit(Constants.EVENT_MESSAGE, sendData);

            // 전송 후, EditText 초기화
            mInputMessage_text.setText(null);

        } catch (JSONException e) {
            Log.d(TAG,"message 전송 error:"+getPrintStackTrace(e));
        }
    }

}
