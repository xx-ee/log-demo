package com.log.model;

import tk.mybatis.mapper.annotation.ColumnType;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tongji2")
public class Tongji2 {
    @Id
    @Column(name = "reportTime")
    @GeneratedValue(generator = "JDBC")
    private Date reporttime;

    private Integer pv;

    private Integer uv;

    private Integer vv;

    private Integer newip;

    private Integer newcust;
    @Transient
    private String webTime;

    /**
     * @return reportTime
     */
    public Date getReporttime() {
        return reporttime;
    }

    /**
     * @param reporttime
     */
    public void setReporttime(Date reporttime) {
        this.reporttime = reporttime;
    }

    /**
     * @return pv
     */
    public Integer getPv() {
        return pv;
    }

    /**
     * @param pv
     */
    public void setPv(Integer pv) {
        this.pv = pv;
    }

    /**
     * @return uv
     */
    public Integer getUv() {
        return uv;
    }

    /**
     * @param uv
     */
    public void setUv(Integer uv) {
        this.uv = uv;
    }

    /**
     * @return vv
     */
    public Integer getVv() {
        return vv;
    }

    /**
     * @param vv
     */
    public void setVv(Integer vv) {
        this.vv = vv;
    }

    /**
     * @return newip
     */
    public Integer getNewip() {
        return newip;
    }

    /**
     * @param newip
     */
    public void setNewip(Integer newip) {
        this.newip = newip;
    }

    /**
     * @return newcust
     */
    public Integer getNewcust() {
        return newcust;
    }

    /**
     * @param newcust
     */
    public void setNewcust(Integer newcust) {
        this.newcust = newcust;
    }

    public String getWebTime() {
        return webTime;
    }

    public void setWebTime(String webTime) {
        this.webTime = webTime;
    }
}