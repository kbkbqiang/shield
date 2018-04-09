package com.niiwoo.shield.manage.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mybatis 分页对象
 */
public class Page<T> implements Serializable, Cloneable {

    private static final long serialVersionUID = 6143896253001136506L;
    private static final int maxPageSize = 50;

    private Integer pageNumber = 1;//页码，默认是第一页
    private Integer pageSize = 20;//每页显示的记录数，默认是20
    private int totalRecord;//总记录数
    private int totalPage;//总页数
    private List<T> result;//对应的当前页记录
    @JsonIgnore
    private Map<String, Object> params = new HashMap<>(); //其他的参数我们把它分装成一个Map对象

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        if (pageNumber == null || pageNumber < 1) {
            return; //pageNumber非法参数则已默认值为准
        }
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1 || pageSize > maxPageSize) {
            return; //非法pageSize应用默认值
        }
        this.pageSize = pageSize;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
        //在设置总页数的时候计算出对应的总页数，在下面的三目运算中加法拥有更高的优先级，所以最后可以不加括号。
        int totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1;
        this.setTotalPage(totalPage);
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }


    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void addParam(String key, Object value) {
        params.put(key, value);
    }

    /**
     * 取当前页的第一行数据在数据库中的行号，在操作数据库的时候有用
     *
     * @return
     */
    @JsonIgnore
    public int getOffset() {
        return (pageNumber - 1) * pageSize;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Page [pageNumber=").append(pageNumber)
                .append(", pageSize=").append(pageSize)
                .append(", params=").append(params)
                .append(", result=").append(result)
                .append(", totalPage=").append(totalPage)
                .append(", totalRecord=").append(totalRecord)
                .append("]");
        return builder.toString();
    }

}
