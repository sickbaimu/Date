package com.lele.date.faker;

import android.util.Log;

import com.lele.date.entity.DepartmentInfo;
import com.lele.date.entity.OrganizationInfo;
import com.lele.date.entity.Participant;
import com.lele.date.entity.ReserverInfo;
import com.lele.date.entity.MeetingRoom;
import com.lele.date.entity.User;
import com.lele.date.entity.UserInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Administrator on 2019/1/4.
 */

public class Server{
    private String url = "http://192.168.199.227:8080/";
    int version = 1;
    private String usersname[]={"董金华","陈超凡","蒋清","周和夏","虞若何","秦安民","梅古达","马修","福山浩"};
    private String roomsname[] = {"X1001","X1002","X1003","X2001","X2002","X2003","X3001","X3002","X3003","X4001","X4002","X4004","X4004"};
    private String departmentname[]={"技术部","财务部"};
    private String meetingname[]={"例行周会","学年总结会","学科认证会","财务会","董事会"};
    private String firmsname[] = {"成都东东有限责任公司"};
    private static ArrayList<OrganizationInfo> organizationInfos;
    private static ArrayList<User> users;
    private static ArrayList<UserInfo> userInfos;
    private static ArrayList<MeetingRoom> meetingRooms;
    private static ArrayList<DepartmentInfo> departmentInfos;
    private static ArrayList<ReserverInfo> reserveInfos;
    private static int cnt_organzations,cnt_departments,cnt_users,cnt_userinfos,cnt_meetingrooms,cnt_reserverinfos;
    public Server()
    {
        init();
    }
    private void init(){
        initlist();
        initdata();
    }

    /**
     * 初始化相关对象的库和计数器
     */
    private void initlist(){
        //初始化列表
        organizationInfos = new ArrayList<>();
        departmentInfos = new ArrayList<>();
        users = new ArrayList<>();
        userInfos = new ArrayList<>();
        meetingRooms = new ArrayList<>();
        reserveInfos = new ArrayList<>();
        //初始化计数器
        cnt_organzations = 0;
        cnt_departments = 0;
        cnt_meetingrooms = 0;
        cnt_users = 0;
        cnt_userinfos = 0;
        cnt_reserverinfos = 0;
    }

    /**
     * 初始化数据
     */
    private void initdata(){
        /*初始化公司法人*/
        User boss = new User(cnt_users++,"Boss","Boss",2);
        UserInfo boss_info = new UserInfo(cnt_userinfos++,"boss","1",5);
        users.add(boss);
        userInfos.add(boss_info);

        /*初始化公司*/
        OrganizationInfo organizationInfo = new OrganizationInfo(cnt_organzations++,boss.getId(),firmsname[0]);
        organizationInfo.setIntroduction("成都东东有限责任公司成立于2002年10月，位于四川省成都市郫都区，是一家致力于搬家业务的有限责任公司。");
        organizationInfos.add(organizationInfo);

        /*初始化部门*/
        for(String name:departmentname)
            departmentInfos.add(new DepartmentInfo(cnt_departments++,organizationInfo.getId(),name));

        /*初始化用户*/
        for(String name:usersname) {
            users.add(new User(cnt_users++,name,name, 0));
            userInfos.add(new UserInfo(cnt_userinfos++,name,"0",1));
        }

        /*初始化会议室*/
        for(String name:roomsname)
            meetingRooms.add(new MeetingRoom(cnt_meetingrooms++,organizationInfo.getId(),name,30,1,"未设定"));

        /*初始化会议*/
        Calendar calendar_1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));//获得当前系统时间
        Calendar calendar_2 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));//获得当前系统时间
        Calendar calendar_3 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));//获得当前系统时间
        Calendar calendar_4 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));//获得当前系统时间
        Calendar calendar_5 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));//获得当前系统时间
        int year = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).get(Calendar.YEAR);
        int month = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).get(Calendar.MONTH);
        int day = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).get(Calendar.DAY_OF_MONTH);
        int hour = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).get(Calendar.MINUTE);

        calendar_1.set(year,month,day,hour+1,minute);
        calendar_2.set(year,month,day,hour+2,minute);
        calendar_3.set(year,month,day,hour-1,minute);
        calendar_4.set(year,month,day,hour+3,minute);
        calendar_5.set(year,month,day,hour-2,minute);

        ArrayList<Participant> participants = new ArrayList<>();
        for(User user:getUsers())
        {
            participants.add(new Participant(user.getId()));
        }

        reserveInfos.add(new ReserverInfo(cnt_reserverinfos++,1, meetingname[0], participants,0,calendar_1.getTime(), calendar_1.getTime(), calendar_1.getTime(), 2));
        reserveInfos.add(new ReserverInfo(cnt_reserverinfos++,1, meetingname[1], participants,0,calendar_2.getTime(), calendar_2.getTime(), calendar_2.getTime(), 3));
        reserveInfos.add(new ReserverInfo(cnt_reserverinfos++,1, meetingname[2], participants,0,calendar_3.getTime(), calendar_3.getTime(), calendar_3.getTime(), 1));
        reserveInfos.add(new ReserverInfo(cnt_reserverinfos++,1, meetingname[3], participants,0,calendar_4.getTime(), calendar_4.getTime(), calendar_4.getTime(), 2));
        reserveInfos.add(new ReserverInfo(cnt_reserverinfos++,1, meetingname[4], participants,0,calendar_5.getTime(), calendar_5.getTime(), calendar_5.getTime(), 4));
    }

    public String getUrl() {
        return url;
    }

    public static ArrayList<OrganizationInfo> getOrganizationInfos() {
        return organizationInfos;
    }

    public static ArrayList<ReserverInfo> getReserveInfos() {
        return reserveInfos;
    }

    public static int getCnt_organzations() {
        return cnt_organzations;
    }

    public static int getCnt_departments() {
        return cnt_departments;
    }

    public static int getCnt_users() {
        return cnt_users;
    }

    public static int getCnt_userinfos() {
        return cnt_userinfos;
    }

    public static int getCnt_meetingrooms() {
        return cnt_meetingrooms;
    }

    public static int getCnt_reserverinfos() {
        return cnt_reserverinfos;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static ArrayList<UserInfo> getUserInfos() {
        return userInfos;
    }

    public static ArrayList<DepartmentInfo> getDepartmentInfos() {
        return departmentInfos;
    }

    public static ArrayList<MeetingRoom> getMeetingRooms() {
        return meetingRooms;
    }

    public static DepartmentInfo getDepartmentInfoById(String departmentid){
        for(DepartmentInfo departmentInfo:getDepartmentInfos())
        {
            if(String.valueOf(departmentInfo.getDepartId()).equals(departmentid))
                return departmentInfo;
        }
        return null;
    }

    public static User LoginCheck(String username,String pwd){
        for(User user:getUsers())
            if(user.getUsername().equals(username)&&user.getPassword().equals(pwd))
                return user;
        return null;
    }

    public static UserInfo getUserInfoByUserId(int userid){
        for(UserInfo userInfo:getUserInfos())
            if(userInfo.getId()==userid)
                return userInfo;
        return null;
    }

    public static ArrayList<UserInfo> getUserInfosByOrgId(int org_id){
        ArrayList<UserInfo> list = new ArrayList<>();
        for(UserInfo userInfo:getUserInfos())
        {
            if(getDepartmentInfoById(userInfo.getDepart()).getOrgId()==org_id)
                list.add(userInfo);
        }
        return list;
    }

    public static UserInfo getUserInfoByNameAndOrgId(String name,int org_id){
        for(UserInfo userInfo:getUserInfos())
            if(userInfo.getName().equals(name)&&getDepartmentInfoById(userInfo.getDepart()).getOrgId()==org_id)
                return userInfo;
        return null;
    }

    public static ArrayList<MeetingRoom> getMeetingRoomsByOrgId(int org_id){
        ArrayList<MeetingRoom> list = new ArrayList<>();
        for(MeetingRoom meetingRoom:getMeetingRooms())
            if(meetingRoom.getOrgId()==org_id)
                list.add(meetingRoom);
        return list;
    }

    public static MeetingRoom getMeetingRoomByIdAndOrgId(int roomid,int org_id){
        for(MeetingRoom meetingRoom:getMeetingRoomsByOrgId(org_id)){
            if(meetingRoom.getTrans_id()==roomid){
                return meetingRoom;
            }
        }
        return null;
    }

    public static String getMeetingRoomNameById(int room_id) {
        for (MeetingRoom meetingRoom : getMeetingRooms())
            if (meetingRoom.getTrans_id() == room_id)
                return meetingRoom.getRoomName();
            return null;
    }

    public static String getUserInfoNameById(int user_id){
        for (UserInfo userinfo:getUserInfos()){
            Log.d("checkid",String.valueOf(userinfo.getId()));
            if(userinfo.getId() == user_id)
                return userinfo.getName();
        }

        return null;
    }

    public static ReserverInfo getReserverById(int reserveinfoid){
        for(ReserverInfo reserveInfo:getReserveInfos())
            if(reserveInfo.getReserverId()==reserveinfoid)
                return reserveInfo;
            return null;
    }

    public static int getStage(int reserverinfoid,int userinfoid){
            int result = -1;
            ReserverInfo reserverInfo = getReserverById(reserverinfoid);
            if(reserverInfo!=null){
                for(Participant participants:reserverInfo.getParticipants())
                    if(participants.getPersonId()==userinfoid)
                        result = participants.getState();
            }
            return result;
    }

    public static ArrayList<ReserverInfo> getReserveInfosByUserId(int user_id){
        ArrayList<ReserverInfo> reserverInfos = new ArrayList<>();
        for(ReserverInfo reserverInfo:getReserveInfos())
            if(getStage(reserverInfo.getReserverId(),user_id)!=1)
                reserverInfos.add(reserverInfo);
        return reserverInfos;
    }

    public static OrganizationInfo getOrganizationInfoById(int org_id){
        for(OrganizationInfo organizationInfo:getOrganizationInfos())
            if(organizationInfo.getId()==org_id)
                return organizationInfo;
            return null;
    }

    public static int getUserInfosNumByOrgId(int org_id){
        int cnt = 0;
        for(UserInfo userInfo:getUserInfos())
            if(getDepartmentInfoById(userInfo.getDepart()).getOrgId()==org_id)
                cnt++;
            return cnt;
    }

    public static int getMeetingRoomNumByOrgId(int org_id){
        int cnt = 0;
        for(MeetingRoom meetingRoom:getMeetingRooms())
            if(meetingRoom.getOrgId().equals(org_id))
                cnt++;
        return cnt;
    }

    public static int getDepartmentsNumByOrgId(int org_id){
        int cnt = 0;
        for(DepartmentInfo departmentInfo:getDepartmentInfos())
            if(departmentInfo.getOrgId().equals(org_id))
                cnt++;
        return cnt;
    }

    public static UserInfo getUserInfobyRealName(String username,int org_id){
        for(UserInfo userInfo:getUserInfosByOrgId(org_id))
            if(userInfo.getName().equals(username))
                return userInfo;
            return null;

    }

    public static void AddReserverInfo(ReserverInfo reserverInfo){
        cnt_reserverinfos++;
        reserveInfos.add(reserverInfo);
    }
}
