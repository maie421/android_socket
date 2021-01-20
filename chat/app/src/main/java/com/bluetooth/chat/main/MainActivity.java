package com.bluetooth.chat.main;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import com.bluetooth.chat.FriendActivity;
import com.bluetooth.chat.chat.ChatClientIO;
import com.bluetooth.chat.chat.ChattingActivity;
import com.bluetooth.chat.R;


public class MainActivity extends ActivityGroup {
    String Tag="메인화면";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost1 = (TabHost) findViewById(R.id.tab_host) ;
        tabHost1.setup(this.getLocalActivityManager()) ;

        // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1") ;
        ts1.setContent(new Intent(this, FriendActivity.class)) ;
        ts1.setIndicator("HOME") ;
        tabHost1.addTab(ts1)  ;

        // 두 번째 Tab. (탭 표시 텍스트:"TAB 2"), (페이지 뷰:"content2")
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2") ;
        ts2.setContent(new Intent(this,ChattingActivity.class)) ;
        ts2.setIndicator("ChatRoom") ;
        tabHost1.addTab(ts2) ;

        // 서비스 시작
        Intent serviceintent = new Intent(getApplicationContext(), ChatClientIO.class);
        startService(serviceintent);
    }

}
