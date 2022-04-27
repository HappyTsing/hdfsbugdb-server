package com.wang.dto;

/**
 * 1. 单表查询
 * 当我们单表查询时，可以用pojo中的对象接收结果。
 * 当使用@RestControll或@ResponseBody注解时，Java对象会自动转化为json字符串，并写入Response对象的body数据区
 *
 * 2. 多表查询（join）
 * 当多表查询时，显然无法用pojo中的对象接收结果，因为会多出数据。
 *          如User表 id、name、School_id
 *           School表 id、name
 * 联表查询时，如何返回数据？
 * 首先在dto中封装一个Result，返回数据时都使用该对象。
 * Result对象中有一个data属性，该属性存放真正的数据。该属性也是一个对象类型。
 * 因此我们在dto中再构建一个如UserSchool的对象，其中包含所有要返回的属性。
 * 在使用时：public Result<UserSchool> getUser(){ return new Result<UserSchool>(HttpStatus.ok,new UserSchool()}
 *
 *
 */
public class Issues {
    private String id;
    private String quality;
    private int code;

    public Issues(String id, String quality,int code) {
        this.id = id;
        this.quality = quality;
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    @Override
    public String toString() {
        return "Issues{" +
                "id='" + id + '\'' +
                ", quality='" + quality + '\'' +
                '}';
    }
}
