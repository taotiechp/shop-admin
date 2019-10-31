package com.fh.shop.api.classify.po;

import java.io.Serializable;

public class Classify implements Serializable {

    private int id;

    private String name;

    private Integer pid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }
}
