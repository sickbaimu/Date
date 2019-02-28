package com.lele.date.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lele.date.R;
import com.lele.date.entity.MeetingRoom;
import com.lele.date.faker.Client;
import com.lele.date.faker.Server;

public class R_RoomInfoActivity extends AppCompatActivity {

    /**
     * 按会议室预约模式下的房间信息
     */
    TextView t_room_name,t_level,t_maxcapacity,t_local,t_media,t_roundtable,t_stage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_info);
        //从intent获取房间id，并访问服务器获得对应对象
        final MeetingRoom room = Server.getMeetingRoomByIdAndOrgId(Integer.valueOf(getIntent().getStringExtra("roomid")),Client.getOrgId());
        t_room_name = findViewById(R.id.t_room_name);
        t_level = findViewById(R.id.t_level);
        t_maxcapacity = findViewById(R.id.t_maxcapacity);
        t_local = findViewById(R.id.t_local);
        t_media = findViewById(R.id.t_media);
        t_roundtable = findViewById(R.id.t_roundtable);
        t_stage = findViewById(R.id.t_stage);
        //若不为空，则将获取到的房间信息显示到ui上
        if(room!=null)
        {
            t_room_name.setText(room.getRoomName());
            t_level.setText(String.valueOf(room.getRk()));
            t_maxcapacity.setText(String.valueOf(room.getCon()));
        }

        Button b_date = findViewById(R.id.b_date);
        b_date.setOnClickListener(new View.OnClickListener() {
            /*
             * 立即预约按钮的响应函数
             */
            @Override
            public void onClick(View view) {
                //将该会议室信息传入intent并启动活动进行时间等其他信息填充
                Intent intent = new Intent(getApplicationContext(), R_TimeActivity.class);
                intent.putExtra("room",room);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }
}
