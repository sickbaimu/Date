package com.lele.date.activity.room;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import com.lele.date.R;
import com.lele.date.entity.Participant;
import com.lele.date.entity.ReserverInfo;
import com.lele.date.entity.UserInfo;
import com.lele.date.faker.Client;
import com.lele.date.faker.Server;
import com.lele.date.fragment.FinalConfirmMeetingFragment;
import com.lele.date.layout.AutoLinefeedLayout;

import java.util.ArrayList;

public class MemberActivity extends AppCompatActivity {

    ArrayList<CheckBox> list;
    ArrayList<UserInfo> userlist;
    ReserverInfo meeting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r__member_list);
        meeting = (ReserverInfo)getIntent().getSerializableExtra("meeting");
        initMemberView();
    }

    /**
     * 初始化成员列表
     */
    public void initMemberView() {
        AutoLinefeedLayout memberView = findViewById(R.id.memberView);
        //AutoLinefeedLayout自定义控件组详见layout.AutoLineFeedLayout.java，主要功能为到填充到段尾时自动切下一行
        int i = 0;
        list = new ArrayList<>();
        for(UserInfo userinfo: Server.getUserInfosByOrgId(Client.getOrgId()))//从服务器获取用户列表
        {
            if(userinfo.getId().equals(Client.getUserInfoId()))
                continue;//过滤掉当前客户端用户（自己）
            LayoutInflater inflater = LayoutInflater.from(this);
            inflater.inflate(R.layout.my_checkbox, memberView);//为控件组添加自定义的CheckBox组件
            FrameLayout frameLayout = (FrameLayout)memberView.getChildAt(i++);
            CheckBox checkBox = (CheckBox)frameLayout.getChildAt(0);
            list.add(checkBox);//将CheckBox存入list，方便之后操作
            checkBox.setText(userinfo.getName());//设置CheckBox为用户真名
        }
        Button b_date = findViewById(R.id.b_date);
        b_date.setOnClickListener(new View.OnClickListener() {
            //预约按钮的响应函数
            @Override
            public void onClick(View view) {
                userlist = new ArrayList<>();
                userlist.clear();
                //从获取到被选中的CheckBox的list上的用户，添加到userlist上
                for(CheckBox checkBox:list)
                {
                    if(checkBox.isChecked())
                    {
                        userlist.add(Server.getUserInfobyRealName(checkBox.getText().toString(),Client.getOrgId()));
                    }
                }
                final ArrayList<Participant> participants = new ArrayList<>();
                for(UserInfo userInfo:userlist){
                    participants.add(new Participant(userInfo.getId()));
                }
                meeting.setParticipants(participants);
                //由于会议信息已经全部填写完毕用MyDialogFragment显示出来进行确认
                FinalConfirmMeetingFragment myDialogFragment = new FinalConfirmMeetingFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("meeting", meeting);
                myDialogFragment.setArguments(bundle);
                myDialogFragment.show(getFragmentManager(), "Dialog");
            }
        });
    }
}

