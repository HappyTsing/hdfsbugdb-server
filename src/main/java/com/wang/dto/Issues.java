package com.wang.dto;

import lombok.Data;

import java.util.List;

/**
 * 使用lombok的@Data注解生成get、set、toString、equals、hashCode等方法
 *
 * @author happytsing
 */
@Data
public class Issues {
    /**
     * total:总共多少条数据
     * pageSize:每一页多少数据
     * pageNum：当前请求的第几页的数据
     * sum：本次返回了多少条数据。一般是sum=pageSize，但最后几条无法取整，因此此时sum<pageSize
     * columns:data中的所有的key
     * data：数据
     */
    Integer total;
    Integer pageSize;
    Integer pageNum;
    Integer sum;
    List data;
}
