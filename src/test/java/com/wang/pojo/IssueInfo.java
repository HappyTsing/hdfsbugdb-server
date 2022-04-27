package com.wang.pojo;

public class IssueInfo {
    private Integer id;

    private String project;

    private String issuetype;

    private String issuekey;

    private String priority;

    private String status;

    private String resolution;

    private String createdtime;

    private String updatetime;

    private String summary;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project == null ? null : project.trim();
    }

    public String getIssuetype() {
        return issuetype;
    }

    public void setIssuetype(String issuetype) {
        this.issuetype = issuetype == null ? null : issuetype.trim();
    }

    public String getIssuekey() {
        return issuekey;
    }

    public void setIssuekey(String issuekey) {
        this.issuekey = issuekey == null ? null : issuekey.trim();
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority == null ? null : priority.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution == null ? null : resolution.trim();
    }

    public String getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime == null ? null : createdtime.trim();
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime == null ? null : updatetime.trim();
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
    }

    @Override
    public String toString() {
        return "IssueInfo{" +
                "id=" + id +
                ", project='" + project + '\'' +
                ", issuetype='" + issuetype + '\'' +
                ", issuekey='" + issuekey + '\'' +
                ", priority='" + priority + '\'' +
                ", status='" + status + '\'' +
                ", resolution='" + resolution + '\'' +
                ", createdtime='" + createdtime + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }
}