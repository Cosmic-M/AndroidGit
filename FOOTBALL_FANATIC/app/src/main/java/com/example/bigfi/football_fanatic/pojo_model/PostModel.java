package com.example.bigfi.football_fanatic.pojo_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bigfi on 17.12.2017.
 */

public class PostModel {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("fixtures")
    @Expose
    private List<Event> events = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

}