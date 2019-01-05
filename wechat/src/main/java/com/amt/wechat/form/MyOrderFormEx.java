package com.amt.wechat.form;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 * @author adu Create on 2019-01-05 18:27
 * @version 1.0
 */
public class MyOrderFormEx extends MyOrderForm implements Serializable {
    private static final long serialVersionUID = 5691039506415940311L;

    private JSONObject servicer;
    private JSONObject score;

    public JSONObject getServicer() {
        return servicer;
    }

    public void setServicer(JSONObject servicer) {
        this.servicer = servicer;
    }
    public JSONObject getScore() {
        return score;
    }

    public void setScore(JSONObject score) {
        this.score = score;
    }

    private class Servicer{
        private int servicerId;
        private String name;

        public int getServicerId() {
            return servicerId;
        }

        public void setServicerId(int servicerId) {
            this.servicerId = servicerId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
