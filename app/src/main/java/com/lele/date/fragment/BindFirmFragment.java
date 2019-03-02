package com.lele.date.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lele.date.R;
import com.lele.date.activity.before.LoginActivity;
import com.lele.date.activity.home.HomeActivity;
import com.lele.date.entity.DepartmentInfo;
import com.lele.date.entity.OrganizationInfo;
import com.lele.date.entity.Participant;
import com.lele.date.entity.ReserverInfo;
import com.lele.date.entity.UserInfo;
import com.lele.date.faker.Client;
import com.lele.date.faker.Server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class BindFirmFragment extends DialogFragment implements DialogInterface.OnClickListener{
    /**
     * 自定义的DialogFragment，
     */
    Button b_sure,b_cancel;
    OrganizationInfo organizationInfo;
    EditText editText;
    Spinner spinner;
    int depart_id;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /*初始化界面*/
        Bundle bundle = getArguments();
        if (bundle != null) {
            organizationInfo = (OrganizationInfo) bundle.getSerializable("firm");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.confirm_dialog,null);
        editText = view.findViewById(R.id.edit_name);
        spinner = view.findViewById(R.id.spinner);
        final ArrayList<DepartmentInfo> arrayList = Server.getDepartmentsByOrgId(organizationInfo.getId());
        ArrayList<String> list = new ArrayList<>();
        for(DepartmentInfo departmentInfo:arrayList){
            list.add(departmentInfo.getDepartName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        for(DepartmentInfo departmentInfo:arrayList)
                            if(departmentInfo.getDepartName().equals(parent.getItemAtPosition(position).toString()))
                        depart_id = departmentInfo.getDepartId();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { }
                });
        b_sure = view.findViewById(R.id.sure);
        b_cancel = view.findViewById(R.id.cancel);
        builder.setView(view);

        /*
         * 确定按钮响应函数
         */
        b_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(),""+Server.getUserInfosNumByOrgId(0),Toast.LENGTH_SHORT).show();
                Server.AddUserInfo(Client.getUser().getId(),editText.getText().toString(),depart_id);
                Toast.makeText(view.getContext(),"添加成功！"+Server.getUserInfosNumByOrgId(0),Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), LoginActivity.class));//返回主页
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
}
