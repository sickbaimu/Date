package com.lele.date.entity;

import java.io.Serializable;

public class DepartmentInfo implements Serializable {
    private Integer departId;           //部门ID

    private Integer orgId;                  //组织ID

    private String departName;          //部门名字

    public DepartmentInfo(Integer departId, Integer orgId, String departName) {
        this.departId = departId;
        this.orgId = orgId;
        this.departName = departName;
    }

    public Integer getDepartId() {
        return departId;
    }

    public Integer getOrgId() {
        return orgId;
    }
}
