package com.sharad.oc.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "a_appointment")
public class Apointment {

    @Id
    private int id;

    private int userId;
    private int consultantId;

    @Temporal(TemporalType.TIME)
    Date startTime;

    @Temporal(TemporalType.TIME)
    Date endTime;

    private String status;

    public Apointment() {

    }

    public Apointment(int id, int userId, int consultantId, Date startTime, Date endTime, String status) {
        this.id = id;
        this.userId = userId;
        this.consultantId = consultantId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(int consultantId) {
        this.consultantId = consultantId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
