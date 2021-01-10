package com.bluetooth.chat.chat;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bluetooth.chat.Constants;
import com.bluetooth.chat.SharedSettings;
import com.bluetooth.chat.data_list.ChatMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;

public class ChatClientIO extends Service {
    String TAG="ChatClientIO";

    private String userName="1";
    private Socket mSocket;

    SharedSettings sharedSettings;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate 서비스");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy 서비스가 꺼짐");

/*        //소켓이 연결되어있는 상태라면 소켓 연결을 끊도록 한다.
        if (mSocket.connected()) {
            mSocket.disconnect();
        }*/

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand 서비스시작");
        sharedSettings = new SharedSettings(this, "user_info");

        setupSocketClient(); //소켓 연결 준비
        SocketClientConnect(); //소켓 연결

        mSocket.connect();
        return super.onStartCommand(intent, flags, startId);
    }

     /**
     * 소켓 연결 준비
     */
    private void setupSocketClient() {
        IO.Options opts = new IO.Options();
        opts.transports = new String[] { WebSocket.NAME };
        try {
            mSocket = IO.socket(Constants.SOCKET_URL, opts);
            //mSocket.connect();
            //mSocket.emit("add user",userName);
        }catch (URISyntaxException e) {
            Log.d(TAG,"Socket 연결 error:"+getPrintStackTrace(e));
        }


    }

     /**
     * 소켓 연결
     */
    private void SocketClientConnect(){
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

    }

    /**
     * Socket Server 연결 Listener
     */
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String usernickname=sharedSettings.get_something_string("user_nickname");

            Log.d(TAG, "EVENT_CONNECT");
            Log.d(TAG, usernickname+": 연결되었습니다!!");

/*            ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            String topstack_name = mngr.getAppTasks().get(0).getTaskInfo().topActivity.getShortClassName();
            if (topstack_name.equals(".chat.ChattingActivity")) {

                Intent intent = new Intent("socket_connected");
                LocalBroadcastManager.getInstance(ChatClientIO.this).sendBroadcast(intent);

            }*/


            // 서버로 전송할 데이터 생성 및 채널 입장 이벤트 보냄.
            JSONObject sendData = new JSONObject();
            try {
                sendData.put(Constants.SEND_DATA_USERNAME, usernickname);
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

    /**
     * 에러 String 으로 변환
     */
    public static String getPrintStackTrace(Exception e) {

        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        return errors.toString();

    }


}
