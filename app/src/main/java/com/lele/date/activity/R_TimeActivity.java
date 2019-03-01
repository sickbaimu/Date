package com.lele.date.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lele.date.R;
import com.lele.date.entity.Participant;
import com.lele.date.entity.ReserverInfo;
import com.lele.date.entity.MeetingRoom;
import com.lele.date.entity.UserInfo;
import com.lele.date.faker.Client;
import com.lele.date.faker.Server;
import com.lele.date.fragment.MyDialogFragment;
import com.lele.date.layout.AutoLinefeedLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class R_TimeActivity extends AppCompatActivity {

    /**
     * 按会议室预约模式下的时间等其他信息填写页面
     */
    Button b_selectDate,b_selectTimeBegin;
    Calendar calendar = Calendar.getInstance();
    ArrayList<CheckBox> list;
    ArrayList<UserInfo> userlist;
    MeetingRoom room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r__time);
        //从intent获取到房间信息
        room = (MeetingRoom)getIntent().getSerializableExtra("room");
        initTimeView();
        initMemberView();
    }

    public void initTimeView() {
        ImageView b_add = findViewById(R.id.b_add);
        ImageView b_sub = findViewById(R.id.b_sub);
        final TextView time = findViewById(R.id.time);
        b_selectDate = findViewById(R.id.selectDate);
        b_selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }
        });
        b_selectTimeBegin = findViewById(R.id.selectTimeBegin);
        b_selectTimeBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTimeBegin();
            }
        });
        b_add.setOnClickListener(new View.OnClickListener() {
            //持续时间+15min
            @Override
            public void onClick(View view) {
                time.setText(String.valueOf(Integer.parseInt(time.getText().toString())+15));
            }
        });
        b_sub.setOnClickListener(new View.OnClickListener() {
            //持续时间-15min
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(time.getText().toString())>=15)
                    time.setText(String.valueOf(Integer.parseInt(time.getText().toString())-15));
            }
        });
        final EditText edit_theme = findViewById(R.id.edit_theme);
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
                //新建一个会议对象，填写主题、开始时间、持续时间、创建人、会议室和成员
                final ArrayList<Participant> participants = new ArrayList<>();
                for(UserInfo userInfo:userlist){
                    participants.add(new Participant(userInfo.getId()));
                }
                Date enddate = calendar.getTime();
                Calendar calendar_1 = Calendar.getInstance();
                calendar_1.setTime(enddate);
                calendar_1.set(Calendar.MINUTE,calendar_1.get(Calendar.MINUTE)+Integer.valueOf(time.getText().toString()));
                ReserverInfo meeting = new ReserverInfo(
                        Server.getCnt_reserverinfos(),//会议ID
                        Client.getUserInfoId(),//创建人ID
                        edit_theme.getText().toString(),//会议主题
                        participants,//参会人员，由之前的intent获得
                        0,//初始状态为0
                        new Date(),//当前系统时间
                        calendar.getTime(),//开始时间
                        calendar_1.getTime(),//结束时间
                        room.getTrans_id()//会议室ID
                );

                //由于会议信息已经全部填写完毕用MyDialogFragment显示出来进行确认
                MyDialogFragment myDialogFragment = new MyDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("meeting", meeting);
                myDialogFragment.setArguments(bundle);
                myDialogFragment.show(getFragmentManager(), "Dialog");
            }
        });
        //初始化ui上信息为当前系统时间
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        b_selectDate.setText(datechange(calendar.getTime(),"yyyy年MM月dd日"));
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        String text = h+"时"+m+"分";
        b_selectTimeBegin.setText(text);
    }
    /**
     * 选择开始时间
     */
    public void selectTimeBegin() {
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String text1 = hourOfDay + "时0" + minute + "分";
                String text2 = hourOfDay + "时" + minute + "分";
                if(minute<10)
                    b_selectTimeBegin.setText(text1);
                else
                    b_selectTimeBegin.setText(text2);
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
            }
        }, Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).get(Calendar.MINUTE),
                true).show();
    }
    /**
     * 选择开始日期
     */
    public void selectDate() {
        Calendar ca = Calendar.getInstance();
        int  mYear = ca.get(Calendar.YEAR);
        int  mMonth = ca.get(Calendar.MONTH);
        int  mDay = ca.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String text = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                b_selectDate.setText(text);
                calendar.set(year,month,dayOfMonth);
            }
        }, mYear, mMonth, mDay).show();
    }

    /**
     * 设置时间显示样式
     * @param date 时间
     * @param pattern 显示格式，如"yyyy年MM月dd日"
     * @return 显示的字符串
     */
    public static String datechange(Date date, String pattern) {
        return new SimpleDateFormat(pattern, Locale.CHINA).format(date);
    }

    /**
     * 初始化成员列表
     */
    public void initMemberView() {
        AutoLinefeedLayout memberView = findViewById(R.id.memberView);
        //AutoLinefeedLayout自定义控件组详见layout.AutoLineFeedLayout.java，主要功能为到填充到段尾时自动切下一行
        int i = 0;
        list = new ArrayList<>();
        for(UserInfo userinfo:Server.getUserInfosByOrgId(Client.getOrgId()))//从服务器获取用户列表
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
    }
}
