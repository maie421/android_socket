package com.bluetooth.chat.data_list;

import android.util.Log;

public class ChattingModel {

    String nickname; //보낸사람 닉네임

    int user_idx; //보낸사람 idx
    String content; //메세지내용

    String kinds;  //채팅메시지종류
    String created_at;  //서버에서 보낸 시간
    Long front_created_at;  //프론트에서 실제로 보낸 시간
    String TAG = "ChattingModel";

    public ChattingModel(String nickname, int user_idx, String kinds,String content, String created_at, Long front_created_at) {
        this.user_idx=user_idx;
        this.kinds = kinds;

        this.nickname = nickname;
        this.content = content;
        this.created_at = created_at;
        this.front_created_at = front_created_at;
    }

    public String getKinds() {
        return kinds;
    }

    public void setKinds(String kinds) {
        this.kinds = kinds;
    }
    public int getUser_idx() {
        return user_idx;
    }
    public void setUser_idx(int user_idx) {
        this.user_idx = user_idx;
    }
    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }




    public Long getFront_created_at() {
        return front_created_at;
    }

    public void setFront_created_at(Long front_created_at) {
        this.front_created_at = front_created_at;
    }

}
