package com.bluetooth.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedSettings {
    String TAG = "쉐어드";

    Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String DEFAULT_VALUE_STRING = "";

    public SharedSettings(Context context,String shared_name){
        this.context=context;
        Log.d(TAG,context.getPackageName());
        sharedPreferences = context.getSharedPreferences(shared_name,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

     /**
     * String 값 저장
     * @param name
     * @param item
     */
    public void  set_something_string(String name,String item){
        editor.putString(name,item); // key,value 형식으로 저장
        editor.commit();

    }

    /**
     * String 값 로드
     * @param name
     */
    public String get_something_string(String name){
        String items= sharedPreferences.getString(name,DEFAULT_VALUE_STRING);
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

}
