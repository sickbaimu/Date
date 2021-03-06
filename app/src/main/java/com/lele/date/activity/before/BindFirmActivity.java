package com.lele.date.activity.before;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lele.date.R;
import com.lele.date.entity.OrganizationInfo;
import com.lele.date.faker.Server;
import com.lele.date.fragment.BindFirmFragment;

public class BindFirmActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_in_firm);
        linearLayout = findViewById(R.id.content);
        linearLayout.setVisibility(View.INVISIBLE);
        final TextView input = findViewById(R.id.input);
        Button b_search = findViewById(R.id.b_search);
        b_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = Integer.valueOf(input.getText().toString());
                OrganizationInfo organizationInfo = Server.getOrganizationInfoById(id);
                showFirm(organizationInfo);
            }
        });


    }
    public void showFirm(final OrganizationInfo organizationInfo){
        if(organizationInfo!=null) {
            linearLayout.setVisibility(View.VISIBLE);
            //新建空的字符串，待填入信息
            String basicinfo = "";
            //填入公司创建人
            Log.d("Rootid",String.valueOf(organizationInfo.getRootId()));

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
            Button b_sure = findViewById(R.id.b_sure);
            b_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BindFirmFragment myDialogFragment = new BindFirmFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("firm", organizationInfo);
                    myDialogFragment.setArguments(bundle);
                    myDialogFragment.show(getFragmentManager(), "Dialog");
                }
            });
        }
    }
}
