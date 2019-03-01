package com.lele.date.entity;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private Integer id;
    private String name;
    private String nickname;
    private Boolean sex;
    private String tel;
    private String email;
    private Integer faceid;               //人脸ID
    private Integer orgId;                //组织ID
    private String depart;                //部门ID
    private Integer authority;            //权限

    public UserInfo(Integer id, String name,String depart,int authority) {
        this.id = id;
        this.name = name;
        this.depart = depart;
        this.authority = authority;
    }

    public String getDepart() {
        return depart;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public Boolean getSex() {
        return sex;
    }

    public String getTel() {
        return tel;
    }

    public String getEmail() {
        return email;
    }

    public Integer getFaceid() {
        return faceid;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public Integer getAuthority() {
        return authority;
    }

    public Integer getId() {
        return id;
    }
}
