package com.bluetooth.chat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bluetooth.chat.R;
import com.bluetooth.chat.data_list.ChattingModel;

import java.util.HashMap;
import java.util.List;


public class ChattingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<ChattingModel> chat_data;
    final static int my_chat = 0;
    final static int opponent_chat = 2;
    final static int speaker = 3;
    String TAG = "채팅어댑터";
    int my_idx;



    public ChattingAdapter(final Context context, final List<ChattingModel> data,int my_idx) {
        this.context = context;
        this.chat_data = data;
        this.my_idx = my_idx;

    }

    public List<ChattingModel> getChat_data() {
        return chat_data;
    }
    //통신후 새로받은 채팅메시지  업데이트 할떄 사용
    public void setChat_data(List<ChattingModel> chat_data) {
        this.chat_data = chat_data;
        //notify_with_handler();
    }

    //리사이클러뷰 업데이트 오류떄문에 쓴것.  https://gogorchg.tistory.com/entry/Android-Cannot-call-this-method-while-RecyclerView-is-computing-a-layout-or-scrolling
/*    public void notify_with_handler(){
        handler.post(r);
    }*/
    //TODO 프론트에서 보낸시간을 기준으로 메세지를 체크 하고있는데 이게 맞을까?

    //성공적으로 메세지를 보냈을 경우 해당하는 메세지를 보냈던 정확한 시간(currenttimemills)을 찾아서 업데이트 시켜준다.
    public void set_message_success(ChattingModel server_msg) {

       /* for (int i = chat_data.size(); i >0; i--) {
            Log.d(TAG,"set_message_success메시지찾기");
            if (chat_data.get(i-1).getFront_created_at() - server_msg.getFront_created_at() == 0) {  //프론트가 보냈던 시간과 맞는 데이터를 찾은 후 메시지를 바꾼다.
                chat_data.set(i-1,server_msg);

                break;
            }
        }*/

        //notify_with_handler();
    }


    //내가 메세지를 받았을 경우 : 시간을 보낸 정확한 시간을 가지고 키값에 넣는다.
    public void add_message(ChattingModel ChattingModel) {

        chat_data.add(ChattingModel);

        notifyDataSetChanged();

    }

    //내가 메세지를 보냈을경우 : 시간을 보낸 정확한 시간을 가지고 키값에 넣는다.
    public void add_front_message(ChattingModel ChattingModel) {
        chat_data.add(ChattingModel);

    }
    @Override
    public int getItemViewType(int position) {

        ChattingModel data_ = chat_data.get(position);
        if (data_.getUser_idx() == my_idx) {
            return my_chat;
        } else {
            return opponent_chat;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder:"+viewType);
        if (viewType == my_chat) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_chat_me, parent, false);

            my_chat_view_holder viewholder = new my_chat_view_holder(view);
            return viewholder;
        }else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recyclerview_chat_me, parent, false);
            my_chat_view_holder viewholder = new my_chat_view_holder(view);
            return viewholder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder:"+ holder.getItemViewType());
        switch (holder.getItemViewType()) {
            case my_chat:
                ((my_chat_view_holder) holder).chat.setText(chat_data.get(position).getContent());
                ((my_chat_view_holder) holder).time.setText(chat_data.get(position).getCreated_at());

                break;
        }
    }


    @Override
    public int getItemCount() {
        return chat_data.size();
    }

    public class my_chat_view_holder extends RecyclerView.ViewHolder {
        TextView chat;
        TextView time;
        TextView count;


        public my_chat_view_holder(@NonNull View itemView) {
            super(itemView);
            this.chat = itemView.findViewById(R.id.recycler_chat_me);
            this.time = itemView.findViewById(R.id.recycler_chat_me_time);
            this.count = itemView.findViewById(R.id.recycler_chat_me_count);

        }

    }

}




