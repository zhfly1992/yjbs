package com.fx.web.util;

import java.io.Serializable;
import java.util.List;

/**
 * @author yc
 * @version 1.0
 * @project yjbs
 * @description TODO  LayUI 数据表格分页默认返回的json数据格式，坑已经踩过，不用前端修改多余的配置
 * @date 2020-04-04 13:02
 */
public class PageResult<T> extends ResponseResult implements Serializable {

    private static final long serialVersionUID = -4283999493688087273L;

    private Integer code;
    private String msg;

    private List<T> data;          //分页数据
    private Long count;          //总记录数

    public PageResult(Long count, List<T> data) {
        this.code = 0;
        this.msg = "";
        this.data = data;
        this.count = count;

    }

    public Integer getCode() {
        return code;
    }


    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

}
