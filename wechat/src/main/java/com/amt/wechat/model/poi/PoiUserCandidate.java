package com.amt.wechat.model.poi;

import java.io.Serializable;

/**
 * Copyright (c) 2019 by CANSHU
 *
 *  等待匹配的候选人
 *
 * @author adu Create on 2019-01-10 11:45
 * @version 1.0
 */
public class PoiUserCandidate implements Serializable {
    private static final long serialVersionUID = 5608346318973471615L;

    /**
     * 候选人手机号
     */
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "PoiUserCandidate{" +
                "mobile='" + mobile + '\'' +
                '}';
    }
}
