package com.lele.date.activity.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.lele.date.R;
import com.lele.date.entity.ReserverInfo;
import com.lele.date.faker.Client;
import com.lele.date.faker.Server;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MeetingInfoActivity extends AppCompatActivity {

    /**
     * 会议信息页面，显示从intent获得的会议的所有信息
     */
    TextView t_meeting_theme,t_room_name,t_num_member,t_begin_time,t_end_time,t_username,t_my_stage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_info);
        t_meeting_theme = findViewById(R.id.t_meeting_theme);
        t_room_name = findViewById(R.id.t_room_name);
        t_num_member = findViewById(R.id.t_num_member);
        t_begin_time = findViewById(R.id.t_begin_time);
        t_end_time = findViewById(R.id.t_end_time);
        t_username = findViewById(R.id.t_username);
        t_my_stage = findViewById(R.id.t_my_stage);
        //从intent获得会议
        ReserverInfo meeting = (ReserverInfo)getIntent().getSerializableExtra("meeting");
        t_meeting_theme.setText(meeting.getMeetingTopic());
        t_room_name.setText(Server.getMeetingRoomNameById(meeting.getRoomId()));
        t_num_member.setText(String.valueOf(meeting.getParticipants().size()));
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("MM-dd EEE HH:mm", Locale.CHINA);
        t_begin_time.setText(bartDateFormat.format(meeting.getStartTime()));
        t_end_time.setText(bartDateFormat.format(meeting.getEndTime()));
        t_username.setText(Server.getUserInfoNameById(meeting.getUserId()));
        String stage = "未知";
        switch (Server.getStage(meeting.getReserverId(),Client.getUserInfoId())){
            case -1:stage = "未邀请";break;
            case 1:stage = "已邀请";break;
            default:break;
        }
        t_my_stage.setText(stage);
    }
}
