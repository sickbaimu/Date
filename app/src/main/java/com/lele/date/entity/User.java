package com.lele.date.entity;

import java.io.Serializable;

/**
 * Created by lele on 2018/12/13.
 */

public class User implements Serializable {
    /**
     * 用户类
     */
    private Integer id;                           //用户ID
    private String username;                      //用户名称
    private String password;                      //密码
    private int role;                             //权限  0表示普通用户  1 表示管理员用户  2 表示根用户
    private String faceadd;                       //人脸信息存储路径

    public User(Integer id, String username, String password, int role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
