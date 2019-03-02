package com.lele.date.activity.home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lele.date.R;
import com.lele.date.entity.ReserverInfo;
import com.lele.date.faker.Client;
import com.lele.date.faker.Server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MeetingListActivity extends AppCompatActivity {

    /**
     * 会议列表界面，显示各种类型下的会议列表
     */
    enum Filter{Gone,Future,All,New}//Gone为已经结束的会议，Future为未来的已经确认邀请的会议，All为所有会议，New为未来的还没有确认邀请的会议
    Filter filter = Filter.Future;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);
        ArrayList<ReserverInfo> meetingList;
        //从intent中获取要筛选的会议样式
        Intent intent = getIntent();
        switch (intent.getStringExtra("filter"))
        {
            case "gone":filter = Filter.Gone;break;
            case "future":filter = Filter.Gone;break;
            case "all":filter = Filter.All;break;
            case "new":filter = Filter.New;break;
            default:filter = Filter.All;break;

        }
        //根据样式不同，选择标题
        TextView t_title = findViewById(R.id.t_title);
        switch (filter)
        {
            case All:meetingList = Server.getReserveInfosByUserId(Client.getUserInfoId());t_title.setText("我的会议");break;
            case Gone:meetingList = GetGoneMeeting(Server.getReserveInfosByUserId(Client.getUserInfoId()));t_title.setText("结束的会议");break;
            case Future:meetingList = GetFutureMeeting(Server.getReserveInfosByUserId(Client.getUserInfoId()),1);t_title.setText("将来的会议");break;
            case New:meetingList = GetFutureMeeting(Server.getReserveInfosByUserId(Client.getUserInfoId()),0);t_title.setText("我的消息");break;
            default:meetingList = Server.getReserveInfosByUserId(Client.getUserInfoId());t_title.setText("我的会议");break;
        }
        //设置recylerView的适配器
        RecyclerView recyclerView =  findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        MeetingAdapter meetingAdapter = new MeetingAdapter(meetingList);
        recyclerView.setAdapter(meetingAdapter);
    }

    /**
     * 筛选出邀请了用户的过去的会议
     * @param list 筛选的会议集合对象
     * @return 目标会议集合中符合时间小于现在的会议
     */
    public ArrayList<ReserverInfo> GetGoneMeeting(ArrayList<ReserverInfo> list)
    {
        ArrayList<ReserverInfo> gone_list = new ArrayList<>();
        Date date = new Date();
        for(ReserverInfo meeting:list)
        {
            if(meeting.getStartTime().before(date))
                gone_list.add(meeting);
        }
        return gone_list;
    }

    /**
     * 筛选出邀请了用户的将来的会议，该方法还待完善
     * @param list 筛选的会议集合对象
     * @param type 确认的情况，0表示未读，1表示已确认出席，2表示请假中，3表示已经准假
     * @return 邀请了用户并且已经确认出席的将来的会议
     */
    public ArrayList<ReserverInfo> GetFutureMeeting(ArrayList<ReserverInfo> list, int type)
    {
        ArrayList<ReserverInfo> future_list = new ArrayList<>();
        Date date = new Date();
        for(ReserverInfo meeting:list)
        {
            if(meeting.getStartTime().after(date))
                future_list.add(meeting);
        }
        return future_list;
    }

    class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.ViewHolder> {
        private List<ReserverInfo> meetingList;
        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;//图像
            TextView textView;//主题
            TextView local_and_time;//时间地点
            TextView id;//会议的id（隐藏）
            ViewHolder(View view) {
                super(view);
                imageView = view.findViewById(R.id.meeting_image);
                textView = view.findViewById(R.id.meeting_theme);
                id = view.findViewById(R.id.meeting_id);
                local_and_time = view.findViewById(R.id.local_and_time);
            }
        }

        MeetingAdapter(List<ReserverInfo> meetingList)
        {
            this.meetingList = meetingList;
        }

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meeting,parent,false);
            MeetingAdapter.ViewHolder holder = new MeetingAdapter.ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //从点击的CradView上获取到会议信息，并访问服务器获得Meeting对象，传入intent，启动活动到会议信息页面
                    CardView cardView =(CardView)view;
                    TextView textView = cardView.findViewById(R.id.meeting_id);
                    String meetingid = textView.getText().toString();
                    ReserverInfo meeting = Server.getReserverById(Integer.valueOf(meetingid));
                    Intent intent = new Intent(view.getContext(), MeetingInfoActivity.class);
                    intent.putExtra("meeting",meeting);
                    view.getContext().startActivity(intent);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position){
            //按照获取到的会议设置各文字、图片的显示
            ReserverInfo meeting = meetingList.get(position);
            holder.imageView.setImageResource(R.drawable.email_close);
            holder.textView.setText(meeting.getMeetingTopic());
            holder.id.setText(String.valueOf(meeting.getReserverId()));
            SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM-dd EEE HH:mm",Locale.CHINA);
            String info = Server.getMeetingRoomNameById(meeting.getRoomId())+" "+bartDateFormat.format(meeting.getStartTime());
            holder.local_and_time.setText(info);
        }

        @Override
        public int getItemCount(){
            return meetingList.size();
        }
    }
}
