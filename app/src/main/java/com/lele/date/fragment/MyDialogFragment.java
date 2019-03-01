package com.lele.date.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lele.date.R;
import com.lele.date.activity.HomeActivity;
import com.lele.date.entity.Participant;
import com.lele.date.entity.ReserverInfo;
import com.lele.date.faker.Client;
import com.lele.date.faker.Server;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MyDialogFragment extends DialogFragment implements DialogInterface.OnClickListener{
    /**
     * 自定义的DialogFragment，只有当会议信息都填写好了之后调用给用户进行最终的确认，确认后为服务器新添加一条会议信息
     */
    TextView content;
    Button b_sure,b_cancel;
    ReserverInfo meeting;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /*初始化界面*/
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.confirm_dialog,null);
        content = view.findViewById(R.id.content);
        b_sure = view.findViewById(R.id.sure);
        b_cancel = view.findViewById(R.id.cancel);
        builder.setView(view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            //从intent获取Meeting对象
            meeting = (ReserverInfo)bundle.getSerializable("meeting");
            //显示会议信息
            content.setText(Info(meeting));
        }
        /*
         * 确定按钮响应函数
         */
        b_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Server.AddReserverInfo(meeting);//为服务器添加一条会议信息
                Toast.makeText(view.getContext(),"预约成功！",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), HomeActivity.class));//返回主页
            }
        });

        /*
         * 取消按钮响应函数
         */
        b_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return builder.create();
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    /**
     * 将一个会议对象转换为文本信息
     * @param meeting 待转化的会议对象
     * @return 该会议的文本信息
     */
    public String Info(ReserverInfo meeting)
    {
        String info = "";
        info += "预约人：" + Server.getUserInfoNameById(meeting.getUserId())+ "\n";
        info += "会议主题：" + meeting.getMeetingTopic() + "\n";
        info += "开始时间：" + datechange(meeting.getStartTime(),"MM-dd EEE HH:mm")+ "\n";
        info += "结束时间：" + datechange(meeting.getEndTime(),"MM-dd EEE HH:mm")+ "\n";
        info += "会议室：" + Server.getMeetingRoomNameById(meeting.getRoomId())+ "\n";
        info += "邀请与会人员：\n";
        for(Participant participant:meeting.getParticipants())
        {
            info += Server.getUserInfoNameById(participant.getPersonId()) + "  ";
        }
        info += "\n";
        return info;
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
