package com.bluetooth.chat;

import android.accounts.Account;
import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.util.Log;

import com.bluetooth.chat.data_list.ChattingModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.bluetooth.chat.chat.ChatClientIO.gson;

public class SharedSettings {
    String TAG = "쉐어드";

    Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferences_chat;
    private SharedPreferences.Editor editor;
    private SharedPreferences.Editor chat_editor;
    private static final String DEFAULT_VALUE_STRING = "";
    private static final int DEFAULT_VALUE_INT = -1;


    public SharedSettings(Context context,String shared_name){
        this.context=context;
        Log.d(TAG,context.getPackageName());
        sharedPreferences = context.getSharedPreferences(shared_name,Context.MODE_PRIVATE);
        sharedPreferences_chat = context.getSharedPreferences("user_chat",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        chat_editor = sharedPreferences_chat.edit();

    }

     /**
     * String 값 저장
     * @param key
     * @param item
     */
    public void  set_something_string(String key,String item){
        editor.putString(key,item); // key,value 형식으로 저장
        editor.commit();

    }

    /**
     * String 값 로드
     * @param key
     */
    public String get_something_string(String key){
        String items= sharedPreferences.getString(key,DEFAULT_VALUE_STRING);
        return items;
    }

    /**
     * INT 값 저장
     * @param name
     * @param item
     */
    public void  set_something_int(String name, int item){
        editor.putInt(name,item); // key,value 형식으로 저장
        editor.commit();

    }
    /**
     * INT 값 저장
     * @param name
     */
    public int get_something_int(String name){
        int items=sharedPreferences.getInt(name,DEFAULT_VALUE_INT);
        return items;
    }

    /**
     * 메세지 로드
     * @param key 방번호
     */
    public String get_chatroom_messages(String key){
        return sharedPreferences_chat.getString(key,"없음");

    }

    /**
     * 메세지 저장
     * @param key 방번호
     * @param chattingModels 메세지
     *
     */
    public void set_chatroom_messages(String key,String chattingModels){
        Log.d(TAG,"방번호: "+key +"저장되는메시지 : "+chattingModels);
        String messages = get_chatroom_messages(key);
        Log.d(TAG,"retrun"+messages);
        if(messages.equals("없음")){
            chat_editor.putString(key, "["+chattingModels+"]"); // key,value 형식으로 저장
            chat_editor.commit();

        }else {
            /*chat_editor.clear();
            chat_editor.commit();*/
            //Type type = new TypeToken<ArrayList<ChattingModel>>() {}.getType();
           ArrayList<ChattingModel> chat_data = gson.fromJson(messages,new TypeToken<List<ChattingModel>>(){}.getType());

            chat_data.add(gson.fromJson(chattingModels,ChattingModel.class));
            chat_editor.putString(key,gson.toJson(chat_data));
            chat_editor.commit();
        }

    }
}
