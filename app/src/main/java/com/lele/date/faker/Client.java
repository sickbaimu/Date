package com.lele.date.faker;

import android.util.Log;

import com.lele.date.entity.DepartmentInfo;
import com.lele.date.entity.UserInfo;

public class Client {
    private static UserInfo userInfo;

    public static UserInfo getUserInfo() {
        return userInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        Client.userInfo = userInfo;
    }

    public static String getDepartId(){return  userInfo.getDepart();}

    public static int getOrgId(){
        DepartmentInfo departmentInfo = Server.getDepartmentInfoById(userInfo.getDepart());
            return departmentInfo.getOrgId();
    }
    public static int getUserInfoId(){
        return userInfo.getId();
    }
}
