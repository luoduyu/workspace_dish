package com.wmt.commons.domain.packet;

import java.io.Serializable;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-06 19:30
 * @version 1.0
 */
public class BizPacket implements Serializable {
    private static final long serialVersionUID = 4939459984532718580L;

    /**
     * 业务编码
     */
    private int code;

    /**
     * 业务提示
     */
    private String msg;

    /**
     * 业务数据
     */
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "BizPacket{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * 成功包
     *
     * @return
     */
    public static BizPacket success() {
        BizPacket packet = new BizPacket();
        packet.setCode(200);
        return packet;
    }

    /**
     * 成功包
     *
     * @param data
     * @return
     */
    public static BizPacket success(Object data) {
        BizPacket packet = new BizPacket();
        packet.setCode(200);
        packet.setData(data);
        return packet;
    }

    /**
     * 成功包
     *
     * @param data
     * @return
     */
    public static BizPacket success(String msg, Object data) {
        BizPacket packet = new BizPacket();
        packet.setCode(200);
        packet.setMsg(msg);
        packet.setData(data);
        return packet;
    }

    /**
     * 失败包
     *
     * @param code
     * @param msg
     * @return
     */
    public static BizPacket error(int code, String msg) {
        BizPacket packet = new BizPacket();
        packet.setCode(code);
        packet.setMsg(msg);
        return packet;
    }

    /**
     * 失败包
     *
     * @param code
     * @param msg
     * @return BizPacket
     */
    public static BizPacket error(int code, String msg, Object data) {
        BizPacket packet = new BizPacket();
        packet.setCode(code);
        packet.setData(data);
        packet.setMsg(msg);
        return packet;
    }

    /**
     * 参数为空
     * @return
     */
    public static BizPacket error_param_null(){

        return error_param_null("参数为空，请确认");
    }

    /**
     * 参数为空
     * @return
     */
    public static BizPacket error_param_null(String msg){
        BizPacket packet = new BizPacket();
        packet.setCode(500);
        packet.setMsg(msg);
        return packet;
    }
}