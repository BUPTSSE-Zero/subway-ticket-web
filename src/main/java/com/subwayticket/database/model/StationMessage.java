package com.subwayticket.database.model;

import javax.persistence.*;
import java.util.List;
import java.util.Date;

/**
 * Created by shengyun-zhou on 6/16/16.
 */
@Entity
@Table(name = "StationMessage")
public class StationMessage {
    private int stationMessageId;
    private String publisher;
    private String title;
    private Date releaseTime;
    private String content;
    private transient List<SubwayStation> stationList;

    @Id
    @Column(name = "StationMessageID", nullable = false)
    public int getStationMessageId() {
        return stationMessageId;
    }

    public void setStationMessageId(int stationMessageId) {
        this.stationMessageId = stationMessageId;
    }

    @Basic
    @Column(name = "Publisher", nullable = false, length = 30)
    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Basic
    @Column(name = "Title", nullable = false, length = 40)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "ReleaseTime", nullable = false)
    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    @Basic
    @Column(name = "Content", nullable = false, length = -1)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StationMessage that = (StationMessage) o;

        if (stationMessageId != that.stationMessageId) return false;
        if (publisher != null ? !publisher.equals(that.publisher) : that.publisher != null) return false;
        if (releaseTime != null ? !releaseTime.equals(that.releaseTime) : that.releaseTime != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stationMessageId;
        result = 31 * result + (publisher != null ? publisher.hashCode() : 0);
        result = 31 * result + (releaseTime != null ? releaseTime.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "stationMessage")
    public List<SubwayStation> getStationList() {
        return stationList;
    }

    public void setStationList(List<SubwayStation> stationList) {
        this.stationList = stationList;
    }
}
