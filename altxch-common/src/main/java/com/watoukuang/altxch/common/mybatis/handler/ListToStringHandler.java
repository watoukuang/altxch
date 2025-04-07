package com.watoukuang.altxch.common.mybatis.handler;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListToStringHandler extends BaseTypeHandler<List<String>> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, List list, JdbcType jdbcType) throws SQLException {
        if (ObjectUtil.isNotEmpty(list)) {
            preparedStatement.setString(i, JSON.toJSONString(list));
        } else {
            preparedStatement.setString(i, null);
        }

    }

    @Override
    public List getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String result = resultSet.getString(s);
        return result == null ? new ArrayList<>() : JSONArray.parseArray(result);
    }

    @Override
    public List getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String result = resultSet.getString(i);
        return result == null ? new ArrayList<>() : JSONArray.parseArray(result);
    }

    @Override
    public List getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String result = callableStatement.getString(i);
        return result == null ? new ArrayList<>() : JSONArray.parseArray(result);
    }
}

