package com.lele.date.activity.room;

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
import com.lele.date.entity.ReserverInfo;
import com.lele.date.entity.MeetingRoom;
import com.lele.date.faker.Client;
import com.lele.date.faker.Server;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeActivity extends AppCompatActivity {

    /**
     * 按会议室预约模式下的时间等其他信息填写页面
     */
    Button b_selectDate,b_selectTimeBegin,b_date;
    Calendar calendar = Calendar.getInstance();
    MeetingRoom room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r__time);
        //从intent获取到房间信息
        room = (MeetingRoom)getIntent().getSerializableExtra("room");
        initTimeView();
    }

    public void initTimeView() {
        ImageView b_add = findViewById(R.id.b_add);
        ImageView b_sub = findViewById(R.id.b_sub);
        b_date = findViewById(R.id.b_date);
        final EditText edit_theme = findViewById(R.id.edit_theme);
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

        b_date.setOnClickListener(new View.OnClickListener() {
            //持续时间+15min
            @Override
            public void onClick(View view) {
                Date enddate = calendar.getTime();
                Calendar calendar_1 = Calendar.getInstance();
                calendar_1.setTime(enddate);
                calendar_1.set(Calendar.MINUTE,calendar_1.get(Calendar.MINUTE)+Integer.valueOf(time.getText().toString()));
                ReserverInfo meeting = new ReserverInfo(
                        Server.getCnt_reserverinfos(),//会议ID
                        Client.getUserInfoId(),//创建人ID
                        edit_theme.getText().toString(),//会议主题
                        null,//参会人员，由之前的intent获得
                        0,//初始状态为0
                        new Date(),//当前系统时间
                        calendar.getTime(),//开始时间
                        calendar_1.getTime(),//结束时间
                        room.getTrans_id()//会议室ID
                );
                Intent intent = new Intent(getApplicationContext(),MemberActivity.class);
                intent.putExtra("meeting",meeting);
                startActivity(intent);
                overridePendingTransition(0,0);
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
}
