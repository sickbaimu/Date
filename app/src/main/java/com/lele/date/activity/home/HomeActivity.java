package com.lele.date.activity.home;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lele.date.R;
import com.lele.date.activity.before.LoginActivity;
import com.lele.date.activity.member.TimeActivity;
import com.lele.date.activity.personal.MyFirmActivity;
import com.lele.date.activity.personal.PersonalSettingActivity;
import com.lele.date.activity.personal.SystemSettingActivity;
import com.lele.date.activity.personal.VersionActivity;
import com.lele.date.activity.room.RoomInfoActivity;
import com.lele.date.activity.time.MemberActivity;
import com.lele.date.entity.ReserverInfo;
import com.lele.date.entity.MeetingRoom;
import com.lele.date.entity.UserInfo;
import com.lele.date.faker.Client;
import com.lele.date.faker.Server;
import com.lele.date.layout.AutoLinefeedLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class HomeActivity extends AppCompatActivity {
    /**
     * 主页
     */
    enum TYPE{Room,Time,Member}//分别对应按房间、时间、成员选择
    TYPE type = TYPE.Member;
    GridView gridView;
    LinearLayout timeView;
    LinearLayout memberView;
    Button b_type_room,b_type_time,b_type_member;
    Button b_selectDate,b_selectTimeBegin;
    Calendar calendar = Calendar.getInstance();
    TextView firm_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initDrawerLayout();         //初始化左侧DrawerLayout
        initGirdView();             //初始化按房间选择的Tab
        initTimeView();             //初始化按时间选择的Tab
        initMemberView();           //初始化按成员选择的Tab
        initTopButton();            //初始化顶部按钮，用来切换选择模式
        initBottomButton();         //初始化底部按钮，查看未来和现在的会议记录
    }


    /**
     * 初始化按成员选择的Tab
     */
    public void initMemberView() {
        memberView = findViewById(R.id.memberView);
        //AutoLinefeedLayout自定义控件组详见layout.AutoLineFeedLayout.java，主要功能为到填充到段尾时自动切下一行
        AutoLinefeedLayout autoLinefeedLayout = findViewById(R.id.autolinefeedlayout);
        int i = 0;
        final ArrayList<CheckBox> list = new ArrayList<>();
        for(UserInfo userInfo:Server.getUserInfosByOrgId(Client.getOrgId()))//从服务器获取用户列表
        {
            if(userInfo.getId().equals(Client.getUserInfoId()))
                continue;//过滤掉当前客户端用户（自己）
            LayoutInflater inflater = LayoutInflater.from(this);
            inflater.inflate(R.layout.my_checkbox, autoLinefeedLayout);//为控件组添加自定义的CheckBox组件
            FrameLayout frameLayout = (FrameLayout)autoLinefeedLayout.getChildAt(i++);
            CheckBox checkBox = (CheckBox)frameLayout.getChildAt(0);
            list.add(checkBox);//将CheckBox存入list，方便之后操作
            checkBox.setText(userInfo.getName());//设置CheckBox为用户真名
        }

        Button b_member_next = findViewById(R.id.b_member_next);
        b_member_next.setOnClickListener(new View.OnClickListener() {
            /**
             * 单击确定按钮时，生成userlist对象，传递给intent并启动intent
             * @param view 选择房间和时间按钮
             */
            @Override
            public void onClick(View view) {
                ArrayList<UserInfo> userlist = new ArrayList<>();
                //对于每个被选中的CheckBox，获取其上的用户真名，从服务器获取到对应用户对象，添加到userlist
                for(CheckBox checkBox:list)
                {
                    if(checkBox.isChecked())
                    {
                        userlist.add(Server.getUserInfoByNameAndOrgId(checkBox.getText().toString(),Client.getOrgId()));
                    }
                }
                //启动活动到时间等其他信息填写，传递userlist
                Intent intent = new Intent(getApplicationContext(), TimeActivity.class);
                intent.putExtra("userlist",userlist);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
    }

    /**
     * 初始化时间相关控件
     */
    public void initTimeView(){
        timeView = findViewById(R.id.timeView);
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

        Button b_time_next = findViewById(R.id.b_time_next);
        b_time_next.setOnClickListener(new View.OnClickListener() {
            //获取空闲房间的响应函数
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MemberActivity.class);
                Date enddate = calendar.getTime();
                Calendar calendar_1 = Calendar.getInstance();
                calendar_1.setTime(enddate);
                calendar_1.set(Calendar.MINUTE,calendar_1.get(Calendar.MINUTE)+Integer.valueOf(time.getText().toString()));
                //构造新的Meeting对象，填入开始时间和持续时间、创办人，其他待填
                ReserverInfo reserveInfo = new ReserverInfo(Server.getCnt_reserverinfos(),Client.getUserInfoId(),"",null,0,new Date(),calendar.getTime(),calendar_1.getTime(),-1);
                //传入Meeting对象，启动活动选择会议室
                intent.putExtra("meeting",reserveInfo);
                startActivity(intent);
                overridePendingTransition(0, 0);
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
     * 设置时间显示样式
     * @param date 时间
     * @param pattern 显示格式，如"yyyy年MM月dd日"
     * @return 显示的字符串
     */
    public static String datechange(Date date, String pattern) {
        return new SimpleDateFormat(pattern, Locale.CHINA).format(date);
    }

    /**
     * 选择开始时间
     */
    public void selectTimeBegin() {
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String text1 = hourOfDay + ":0" + minute;
                String text2 = hourOfDay + ":" + minute;
                if(minute<10)//补充十位上的0
                    b_selectTimeBegin.setText(text1);
                else
                    b_selectTimeBegin.setText(text2);
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
            }
        }, Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).get(Calendar.MINUTE),
                true).show();//设置初始时间为当前时间，为为北京时间24小时制
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
                String text = year + "-" + (month + 1) + "-" + dayOfMonth;
                b_selectDate.setText(text);//月份从0开始，因此+1
                calendar.set(year,month,dayOfMonth);
            }
        }, mYear, mMonth, mDay).show();
    }

    /**
     * 初始化中间GirdView布局，选择会议室
     */
    public void initGirdView() {
        gridView = findViewById(R.id.grid_view);
        final List<Map<String, Object>> dataList = new ArrayList<>();
        for(MeetingRoom room: Server.getMeetingRoomsByOrgId(Client.getOrgId()))//从服务器获取当前公司的会议室
        {
            Map<String, Object> map = new HashMap<>();
            map.put("img", R.drawable.img_room_free);
            map.put("text",room.getRoomName());
            map.put("id",room.getTrans_id());
            dataList.add(map);
        }

        //将会议室对象与图层适配器来（隐藏了会议室id）
        String[] from={"img","text","id"};
        int[] to={R.id.img,R.id.text,R.id.room_id};
        SimpleAdapter adapter=new SimpleAdapter(this, dataList, R.layout.gird_meeting, from, to);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * 单个会议室的点击相应函数
             * @param arg0 适配器
             * @param arg1 会议室的view图层
             * @param arg2 在适配器里的位置
             * @param arg3 大部分时候同arg2
             */
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //获取到了单击会议室的隐藏会议室id，并且传入intent并启动活动到房间信息查看
                LinearLayout linearLayout = (LinearLayout)gridView.getAdapter().getView(arg2,arg1,null);
                TextView textView = linearLayout.findViewById(R.id.room_id);
                String roomid = textView.getText().toString();
                Intent intent = new Intent(getApplicationContext(), RoomInfoActivity.class);
                intent.putExtra("roomid",roomid);
                startActivity(intent);overridePendingTransition(0,0);
            }
        });

    }

    /**
     * 初始化左侧DrawerLayout
     */
    public void initDrawerLayout() {

        ImageView b_drawer = findViewById(R.id.b_drawer);
        b_drawer.setOnClickListener(new View.OnClickListener() {
            //单击b_drawer按钮时弹出DrawerLayout，防止用户不知道这个功能
            @Override
            public void onClick(View view) {
                DrawerLayout drawer_layout = findViewById(R.id.drawer_layout);
                //使得DrawerLayout从左侧弹出
                drawer_layout.openDrawer(GravityCompat.START);
            }
        });

        //初始化DrawerLayout上的用户名和LV信息
        TextView t_username = findViewById(R.id.t_username);
        TextView t_userlevle = findViewById(R.id.t_userlevel);
        t_username.setText(Client.getUserInfo().getName());
        String lv = "LV."+Client.getUserInfo().getAuthority();
        t_userlevle.setText(lv);

        //依次为DrawerLayout按钮添加功能
        Button b_system_setting = findViewById(R.id.b_system_setting);
        Button b_personal_setting = findViewById(R.id.b_personal_setting);
        Button b_my_firm =findViewById(R.id.b_my_firm);
        Button b_version = findViewById(R.id.b_version);
        Button b_logout = findViewById(R.id.b_logout);
        Button b_exit = findViewById(R.id.b_exit);

        //系统设置
        b_system_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SystemSettingActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        //个人设置
        b_personal_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PersonalSettingActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        //组织详情
        b_my_firm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MyFirmActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        //版本信息
        b_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), VersionActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        //注销（返回登陆页面）
        b_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Client();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        //退出，还没写
        b_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
    /**
     * 初始化底部按钮
     */
    public void initBottomButton() {
        ImageView b_my_sign = findViewById(R.id.b_my_sign);
        ImageView b_my_message = findViewById(R.id.b_my_message);
        ImageView b_book = findViewById(R.id.b_book);
        //过去的会议
        b_my_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MeetingListActivity.class);
                intent.putExtra("filter","gone");
                startActivity(intent);overridePendingTransition(0, 0);
            }
        });
        //我的消息（会议邀请）
        b_my_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MeetingListActivity.class);
                intent.putExtra("filter","new");
                startActivity(intent);overridePendingTransition(0, 0);
            }
        });
        //所有的会议
        b_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MeetingListActivity.class);
                intent.putExtra("filter","all");
                startActivity(intent);overridePendingTransition(0, 0);
            }
        });

    }
    /**
     * 初始化顶部按钮，来选择预约方式
     */
    public void initTopButton(){
        firm_name = findViewById(R.id.firm_name);
        firm_name.setText(Server.getOrganizationInfoById(Client.getOrgId()).getOrgName());
        b_type_room = findViewById(R.id.b_type_room);
        b_type_time = findViewById(R.id.b_type_time);
        b_type_member = findViewById(R.id.b_type_member);
        //按房间预约
        b_type_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = TYPE.Room;
                update_type();
            }
        });
        //按时间预约
        b_type_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = TYPE.Time;
                update_type();
            }
        });
        //按成员预约
        b_type_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = TYPE.Member;
                update_type();
            }
        });
        update_type();//根据选择来更新界面
    }
    /**
     * 根据不同类型来更新UI控件的可见性
     */
    public void update_type() {
        gridView.setVisibility(View.INVISIBLE);
        timeView.setVisibility(View.INVISIBLE);
        memberView.setVisibility(View.INVISIBLE);
        switch (type)
        {
            case Room:
                b_type_room.setTextColor(Color.BLUE);
                b_type_member.setTextColor(Color.GRAY);
                b_type_time.setTextColor(Color.GRAY);
                gridView.setVisibility(View.VISIBLE);
                break;
            case Time:
                b_type_room.setTextColor(Color.GRAY);
                b_type_member.setTextColor(Color.GRAY);
                b_type_time.setTextColor(Color.BLUE);
                timeView.setVisibility(View.VISIBLE);
                break;
            case Member:
                b_type_room.setTextColor(Color.GRAY);
                b_type_member.setTextColor(Color.BLUE);
                b_type_time.setTextColor(Color.GRAY);
                memberView.setVisibility(View.VISIBLE);
                break;
            default:break;
        }
    }
}
