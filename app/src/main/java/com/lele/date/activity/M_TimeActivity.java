package com.lele.date.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lele.date.R;
import com.lele.date.entity.Participant;
import com.lele.date.entity.ReserverInfo;
import com.lele.date.entity.UserInfo;
import com.lele.date.faker.Client;
import com.lele.date.faker.Server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class M_TimeActivity extends AppCompatActivity {

    /**
     * 按成员预约模式下的时间信息填入页面，同时也填入会议主题、与会人等其他信息
     */
    Button b_selectDate,b_selectTimeBegin;
    Calendar calendar = Calendar.getInstance();
    ArrayList<UserInfo> userlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m__time);
        //从intent获得userlist
        userlist = (ArrayList<UserInfo>)getIntent().getSerializableExtra("userlist");
        initTimeView();
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
        final EditText edit_theme = findViewById(R.id.edit_theme);//会议主题
        Button b_next = findViewById(R.id.b_next);
        final ArrayList<Participant> participants = new ArrayList<>();
        for(UserInfo userInfo:userlist){
            participants.add(new Participant(userInfo.getId()));
        }
        Date enddate = calendar.getTime();
        final Calendar calendar_1 = Calendar.getInstance();
        calendar_1.setTime(enddate);
        calendar_1.set(Calendar.MINUTE,calendar_1.get(Calendar.MINUTE)+Integer.valueOf(time.getText().toString()));
        b_next.setOnClickListener(new View.OnClickListener() {
            //获取会议室按钮响应函数，填写 了会议主题，开始时间、持续时间、创建人等信息，并将从intent获得的成员列表填入
            @Override
            public void onClick(View view) {
                ReserverInfo meeting = new ReserverInfo(
                        Server.getCnt_reserverinfos(),//会议ID
                        Client.getUserInfoId(),//创建人ID
                        edit_theme.getText().toString(),//会议主题
                        participants,//参会人员，由之前的intent获得
                        0,//初始状态为0
                        new Date(),//当前系统时间
                        calendar.getTime(),//开始时间
                        calendar_1.getTime(),//结束时间
                        -1//会议室ID未填写
                        );
                Intent intent = new Intent(getApplicationContext(),M_RoomListActivity.class);//下一步走向选择会议室
                intent.putExtra("meeting",meeting);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
        //初始化ui上的时间信息为当前系统时间
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
}
