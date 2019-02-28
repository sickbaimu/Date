package com.lele.date.entity;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 2019/1/4.
 */

public class ReserverInfo implements Serializable {
    private Integer reserverId;     //会议id
    private Integer userId;             //申请人id
    private String meetingTopic;    //标题
    private ArrayList<Participant> participants;     //参会人及状态
    private Integer status=0;           //会议完成状态 0表还未开始  1表即将开始（30分钟内） 2表正在进行 3表已过期
    private Date createTime;        //会议创建时间
    private Date startTime;         //开始时间
    private Date endTime;           //截止时间
    private Integer roomId;            //开会的房间

    public ReserverInfo(Integer reserverId, Integer userId, String meetingTopic, ArrayList<Participant> participants, Integer status, Date createTime, Date startTime, Date endTime, Integer roomId) {
        this.reserverId = reserverId;
        this.userId = userId;
        this.meetingTopic = meetingTopic;
        this.participants = participants;
        this.status = status;
        this.createTime = createTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomId = roomId;
    }

    public Integer getReserverId() {
        return reserverId;
    }

    public void setReserverId(Integer reserverId) {
        this.reserverId = reserverId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMeetingTopic() {
        return meetingTopic;
    }

    public void setMeetingTopic(String meetingTopic) {
        this.meetingTopic = meetingTopic;
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Participant> participants) {
        this.participants = participants;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }
}
