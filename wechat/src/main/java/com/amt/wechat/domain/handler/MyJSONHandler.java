package com.amt.wechat.domain.handler;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

/**
 * Copyright (c) 2018 by CANSHU
 *
 * @author adu Create on 2018-12-25 14:09
 * @version 1.0
 */
public class MyJSONHandler extends BaseTypeHandler<JSONObject> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JSONObject parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i,parameter.toJSONString());
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Array arrValue =  rs.getArray(columnName);
        System.out.println(arrValue);

        String columnValue = rs.getString(columnName);
        if (null != columnValue){
            return JSONObject.parseObject(columnValue);
        }
        return null;
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        if (null != columnValue){
            return JSONObject.parseObject(columnValue);
        }
        return null;
    }

    @Override
    public JSONObject getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String columnValue = cs.getString(columnIndex);
        if (null != columnValue){
            return JSONObject.parseObject(columnValue);
        }
        return null;
    }
}