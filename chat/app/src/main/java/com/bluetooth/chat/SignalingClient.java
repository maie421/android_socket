//package com.dmsduf.socketio_test;
//
//
//
//import android.annotation.SuppressLint;
//import android.util.Log;
//
//
//
//import org.json.JSONException;
//import org.json.JSONObject;
////import org.webrtc.IceCandidate;
////import org.webrtc.SessionDescription;
//
//import java.net.URISyntaxException;
//import java.security.KeyManagementException;
//import java.security.NoSuchAlgorithmException;
//import java.security.cert.X509Certificate;
//import java.util.Arrays;
//
//import javax.net.ssl.HostnameVerifier;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSession;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//
//import io.socket.client.IO;
//import io.socket.client.Socket;
//import okhttp3.OkHttpClient;
//
////import com.example.streamapp.SessionInfo;
//
///**
// * Webrtc_Step3
// * Created by vivek-3102 on 11/03/17.
// */
//
//public class SignallingClient {
//
//    private static SignallingClient instance;
//    private String roomName = null;
//    private Socket socket;
//    public boolean isChannelReady = false;
//    public boolean isInitiator = false;
//    public boolean isStarted = false;
//    private SignalingInterface callback;
//    String url = "15.165.252.235";
//    //    public bo15.165.252.235olean isHost=false;
//    //This piece of code should not go into production!!
//    //This will help in cases where the node server is running in non-https server and you want to ignore the warnings
//    @SuppressLint("TrustAllX509TrustManager")
//    private final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//            return new java.security.cert.X509Certificate[]{};
//        }
//
//        public void checkClientTrusted(X509Certificate[] chain,
//                                       String authType) {
//        }
//
//        public void checkServerTrusted(X509Certificate[] chain,
//                                       String authType) {
//        }
//    }};
//
//    public static SignallingClient getInstance() {
//        if (instance == null) {
//            instance = new SignallingClient();
//        }
//        if (instance.roomName == null) {
//            //set the room name here
//
//            instance.roomName = "방이름";
//
//        }
//        return instance;
//    }
//
//    public void init(SignalingInterface signalingInterface) {
//        this.callback = signalingInterface;
//        try {
//                SSLContext sslcontext = SSLContext.getInstance("TLS");
//                sslcontext.init(null, trustAllCerts, null);
//                HostnameVerifier myHostnameVerifier = new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                };
//
//                OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                        .hostnameVerifier(myHostnameVerifier)
//                        .sslSocketFactory(sslcontext.getSocketFactory(), (X509TrustManager)trustAllCerts[0])
//                        .build(); // default settings for all sockets
//
//                IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
//                IO.setDefaultOkHttpCallFactory(okHttpClient);
//
//                socket = IO.socket("https://"+url+":3001");
//                socket.connect();
//                Log.d("SignallingClient", "init() called");
//
//
//                socket.on(Socket.EVENT_CONNECT,args -> {
//
//                    if (roomName!=null) {  // roomName = SessionID - 유저기준으로가장.
//                        emitInitStatement(roomName);
//                    }
//
//                });
//
//                //room created event.
//                socket.on("created", args -> {
//                    Log.d("SignallingClient", "created call() called with: args = [" + Arrays.toString(args) + "]");
//                    isInitiator = true;
//                    callback.onCreatedRoom();
//                });
//
//            //room is full event
//            socket.on("full", args -> Log.d("SignallingClient", "full call() called with: args = [" + Arrays.toString(args) + "]"));
//
//            //peer joined event
//            socket.on("join", args -> {
//                Log.d("SignallingClient", "join call() called with: args = [" + Arrays.toString(args) + "]");
//                isChannelReady = true;
//                callback.onNewPeerJoined();
//            });
//
//            //when you joined a chat room successfully
//            socket.on("joined", args -> {
//                Log.d("SignallingClient", "joined call() called with: args = [" + Arrays.toString(args) + "]");
//                isChannelReady = true;
//                callback.onJoinedRoom();
//            });
//
//            //log event
//            socket.on("log", args -> Log.d("SignallingClient", "log call() called with: args = [" + Arrays.toString(args) + "]"));
//
//            //bye event
//            socket.on("bye", args ->
//                    callback.onRemoteHangUp((String) args[0]));
//            socket.on("onstartcall",args->{
//                Log.d("디버그","제발...");
//                // String data = (String) args[0];
//                //if (data.equalsIgnoreCase("got user media")) {
//                callback.onTryToStart();
//                // }
//
//            });
//
//
//
//            //messages - SDP and ICE candidates are transferred through this
//            socket.on("message", args -> {
//                Log.d("SignallingClient", "message call() called with: args = [" + Arrays.toString(args) + "]");
//                if (args[0] instanceof String) {
//                    Log.d("SignallingClient", "String received :: " + args[0]);
//                    String data = (String) args[0];
//
//                    if (data.equalsIgnoreCase("got user media")) {
//                        isChannelReady = true;
//                        callback.onTryToStart();
//                    }
//                    if (data.equalsIgnoreCase("bye")) {
//                        callback.onRemoteHangUp(data);
//                    }
//                } else if (args[0] instanceof JSONObject) {
//                    try {
//
//                        JSONObject data = (JSONObject) args[0];
//                        Log.d("SignallingClient", "Json Received :: " + data.toString());
//                        String type = data.getString("type");
//                        if (type.equalsIgnoreCase("offer")) {
//                            callback.onOfferReceived(data);
//                        } else if (type.equalsIgnoreCase("answer") && isStarted) {
//                            callback.onAnswerReceived(data);
//                        } else if (type.equalsIgnoreCase("candidate") && isStarted) {
//                            callback.onIceCandidateReceived(data);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//        } catch (URISyntaxException | NoSuchAlgorithmException | KeyManagementException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void emitInitStatement(String message) { // message = roomname = 유저 아이디
//
//        Log.d("SignallingClient", "emitInitStatement() called with: event = [" + "create or join" + "], message = [" + message + "]");
//
//
//        socket.emit("create or join", message);
//    }
//
//    public void emitMessage(String message) {
//        Log.d("SignallingClient", "emitMessage() called with: message = [" + message + "]");
//        socket.emit("message", message);
//    }
//
//    public void emitUserReady(String message) {
//        Log.d("SignallingClient", "emitUserReady() called with: message = [" + message + "]");
//        socket.emit("onready", message);
//    }
//
//
//
//
//
//    public void emitIceCandidate(IceCandidate iceCandidate) {
//        try {
//            JSONObject object = new JSONObject();
//            object.put("type", "candidate");
//            object.put("label", iceCandidate.sdpMLineIndex);
//            object.put("id", iceCandidate.sdpMid);
//            object.put("candidate", iceCandidate.sdp);
//
////            Thread t = new Thread(new Runnable() {
////                @Override
////                public void run() {
////                    try {
////                        Thread.sleep(1000);
////                    } catch (InterruptedException e) {
////                        e.printStackTrace();
////                    }
////                }
////            });
////            t.start();
//            socket.emit("message", object);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void close() {
//        if(socket!=null&&socket.connected()) {
//            socket.emit("bye", roomName);
//            socket.disconnect();
//            socket.close();
//        }
//    }
//
//    public interface SignalingInterface {
//        void onRemoteHangUp(String msg);
//
//        void onOfferReceived(JSONObject data);
//
//        void onAnswerReceived(JSONObject data);
//
//        void onIceCandidateReceived(JSONObject data);
//
//        void onTryToStart();
//
//        void onCreatedRoom();
//
//        void onJoinedRoom();
//
//        void onNewPeerJoined();
//    }
//}