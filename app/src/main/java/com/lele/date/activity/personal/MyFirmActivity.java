package com.lele.date.activity.personal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.lele.date.R;
import com.lele.date.entity.OrganizationInfo;
import com.lele.date.faker.Client;
import com.lele.date.faker.Server;

public class MyFirmActivity extends AppCompatActivity {

    /**
     * 显示我的公司信息
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_firm);

        //从客户端当前登录用户获取公司对象
        OrganizationInfo organizationInfo = Server.getOrganizationInfoById(Client.getOrgId());
        if(organizationInfo!=null) {
            //新建空的字符串，待填入信息
            String basicinfo = "";
            //填入公司创建人
            basicinfo += "创建人：" + Server.getUserInfoNameById(organizationInfo.getRootId()) + "\n";
            //从服务器获取当前员工人数并填入
            basicinfo += "当前员工：" + Server.getUserInfosNumByOrgId(organizationInfo.getId()) + "人\n";
            //从服务器获取当前会议室数并填入
            basicinfo += "当前会议室：" + Server.getMeetingRoomNumByOrgId(organizationInfo.getId())+ "间\n";
            //从服务器获取当前部门数并填入
            basicinfo += "部门：" + Server.getDepartmentsNumByOrgId(organizationInfo.getId()) + "个\n";
            //显示basicinfo、brief(公司简介)、firmname(公司名)
            TextView t_basicinfo = findViewById(R.id.t_basci_info);
            t_basicinfo.setText(basicinfo);
            TextView t_brief = findViewById(R.id.t_brief);
            t_brief.setText(organizationInfo.getIntroduction());
            TextView t_firmname = findViewById(R.id.t_firmname);
            t_firmname.setText(organizationInfo.getOrgName());
        }
    }
}
