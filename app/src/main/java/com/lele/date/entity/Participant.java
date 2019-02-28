package com.lele.date.entity;

import java.io.Serializable;

public class Participant implements Serializable {
    private Integer personId;
    private Integer state;
    private Integer ReserveId;//对应的会议id

    public Participant(Integer personId) {
        this.personId = personId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getReserveId() {
        return ReserveId;
    }

    public void setReserveId(Integer reserveId) {
        ReserveId = reserveId;
    }
}
