package com.lele.date.entity;

import java.io.Serializable;

/**
 * Created by lele on 2018/12/12.
 */

public class MeetingRoom implements Serializable {
    /**
     *会议室
     */
    private Integer trans_id;         //会议室id（算法需要）
    private Integer orgId;          //组织编号
    private String roomName;    //会议室名字 如 ：302
    private Integer Con;       //会议室容量（算法需要）
    private String address;     //会议室地址
    private Integer Type;   //类型（算法需要）
    private Integer Rk;     //权限等级（算法需要）
    private MeetingRoomTimeSlice meetingRoomTimeSlice[];

    public MeetingRoom(Integer trans_id, Integer orgId, String roomName,int Con,int Rk,String address) {
        this.trans_id = trans_id;
        this.orgId = orgId;
        this.roomName = roomName;
        this.Con = Con;
        this.Rk = Rk;
        this.address = address;
    }

    public Integer getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(Integer trans_id) {
        this.trans_id = trans_id;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getCon() {
        return Con;
    }

    public void setCon(Integer con) {
        Con = con;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getType() {
        return Type;
    }

    public void setType(Integer type) {
        Type = type;
    }

    public Integer getRk() {
        return Rk;
    }

    public void setRk(Integer rk) {
        Rk = rk;
    }

    public MeetingRoomTimeSlice[] getMeetingRoomTimeSlice() {
        return meetingRoomTimeSlice;
    }

    public void setMeetingRoomTimeSlice(MeetingRoomTimeSlice[] meetingRoomTimeSlice) {
        this.meetingRoomTimeSlice = meetingRoomTimeSlice;
    }

}
