package com.geeker.response;

/**
 * json响应消息对象
 *
 * @author caoquanlong
 */
public interface Response {

    Integer getCode();

    String getMessage();

    Object getData();

    Object getData(String key);

    void putData(String key, Object value);
}
