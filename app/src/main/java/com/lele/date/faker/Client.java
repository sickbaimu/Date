package com.lele.date.faker;

import android.util.Log;

import com.lele.date.entity.DepartmentInfo;
import com.lele.date.entity.User;
import com.lele.date.entity.UserInfo;

public class Client {
    private static UserInfo userInfo;
    private static User user;

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

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Client.user = user;
    }
}
