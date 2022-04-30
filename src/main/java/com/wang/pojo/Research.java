package com.wang.pojo;

/**
 * 数据库Research表，对应的java对象
 *
 * @author happytsing
 */
public class Research {
    private Integer id;

    private Integer issueinfoId;

    private String cause;

    private String impact;

    private String link;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIssueinfoId() {
        return issueinfoId;
    }

    public void setIssueinfoId(Integer issueinfoId) {
        this.issueinfoId = issueinfoId;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause == null ? null : cause.trim();
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact == null ? null : impact.trim();
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link == null ? null : link.trim();
    }

    @Override
    public String toString() {
        return "Research{" +
                "id=" + id +
                ", issueinfoId=" + issueinfoId +
                ", cause='" + cause + '\'' +
                ", impact='" + impact + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}