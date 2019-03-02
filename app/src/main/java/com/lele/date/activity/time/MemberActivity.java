package com.lele.date.activity.time;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;



import com.lele.date.R;
import com.lele.date.entity.Participant;
import com.lele.date.entity.ReserverInfo;
import com.lele.date.entity.UserInfo;
import com.lele.date.faker.Client;
import com.lele.date.faker.Server;
import com.lele.date.layout.AutoLinefeedLayout;

import java.util.ArrayList;

public class MemberActivity extends AppCompatActivity {

    /**
     * 按时间选择预约模式下的成员选择页面
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
        //从intent获取到未完善的meeting
        final ReserverInfo meeting = (ReserverInfo)getIntent().getSerializableExtra("meeting");
        //AutoLinefeedLayout自定义控件组详见layout.AutoLineFeedLayout.java，主要功能为到填充到段尾时自动切下一行
        AutoLinefeedLayout autolinefeedlayout = findViewById(R.id.autolinefeedlayout);
        autolinefeedlayout.removeAllViews();
        int i = 0;
        final ArrayList<CheckBox> list = new ArrayList<>();
        for(UserInfo userInfo: Server.getUserInfosByOrgId(Client.getOrgId()))//从服务器获取用户列表
        {
            if(userInfo.getId().equals(Client.getUserInfoId()))
                continue;//过滤掉当前客户端用户（自己）
            LayoutInflater inflater = LayoutInflater.from(this);
            inflater.inflate(R.layout.my_checkbox, autolinefeedlayout);//为控件组添加自定义的CheckBox组件
            FrameLayout frameLayout = (FrameLayout)autolinefeedlayout.getChildAt(i++);
            CheckBox checkBox = (CheckBox)frameLayout.getChildAt(0);
            list.add(checkBox);//将CheckBox存入list，方便之后操作
            checkBox.setText(userInfo.getName());//设置CheckBox为用户真名
        }

        Button b_sure = findViewById(R.id.b_sure);
        final ArrayList<UserInfo> userlist = new ArrayList<>();
        b_sure.setOnClickListener(new View.OnClickListener() {
            //确定按钮的响应函数
            @Override
            public void onClick(View view) {
                for(CheckBox checkBox:list)
                {
                    if(checkBox.isChecked())
                    {
                        //从获取到被选中的CheckBox的list上的用户，添加到userlist上
                        userlist.add(Server.getUserInfobyRealName(checkBox.getText().toString(),Client.getOrgId()));                    }
                }
                //为会议设置邀请人名单
                ArrayList<Participant> list = new ArrayList<>();
                for(UserInfo userInfo:userlist){
                    list.add(new Participant(userInfo.getId()));
                }
                meeting.setParticipants(list);
                //为会议设置会议主题
                EditText editText = findViewById(R.id.edit_theme);
                meeting.setMeetingTopic(editText.getText().toString());
                Intent intent = new Intent(getApplicationContext(), RoomActivity.class);
                intent.putExtra("meeting",meeting);
                startActivity(intent);
            }
        });

    }
}
