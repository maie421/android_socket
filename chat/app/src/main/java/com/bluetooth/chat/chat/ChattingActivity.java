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
import com.bluetooth.chat.chat.ChatClientIO;
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
            Log.d(TAG,"message 전송 error:"+ChatClientIO.getPrintStackTrace(e));
        }
    }

}
