package com.lele.date.entity;

import java.io.Serializable;

/**
 * Created by lele on 2018/12/13.
 */

public class OrganizationInfo implements Serializable {
    /**
     *组织实体类
     */
    public int id;
    public int rootId;              //根用户id
    public String orgName;          //组织名称
    public String address;          //组织地址
    public String email;            //企业邮箱
    public String introduction;     //组织介绍
    public String meetingRoomCount; //会议室的数量
    public String userConut;        //该企业用户数量

    public OrganizationInfo(int id, int rootId, String orgName) {
        this.id = id;
        this.rootId = rootId;
        this.orgName = orgName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRootId() {
        return rootId;
    }

    public void setRootId(int rootId) {
        this.rootId = rootId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getMeetingRoomCount() {
        return meetingRoomCount;
    }

    public void setMeetingRoomCount(String meetingRoomCount) {
        this.meetingRoomCount = meetingRoomCount;
    }

    public String getUserConut() {
        return userConut;
    }

    public void setUserConut(String userConut) {
        this.userConut = userConut;
    }
}
