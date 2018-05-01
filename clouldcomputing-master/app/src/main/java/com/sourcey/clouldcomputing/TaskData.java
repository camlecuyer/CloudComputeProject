package com.sourcey.clouldcomputing;

public class TaskData {
    private String taskID;
    private String divID;
    private String buildID;
    private String issueID;
    private String desc;
    private String dateOpened;
    private String floor;
    private String location;

    public TaskData(String taskID, String divID, String buildID, String issueID, String desc, String dateOpened, String floor, String location)
    {
        this.taskID = taskID;
        this.divID = divID;
        this.buildID = buildID;
        this.issueID = issueID;
        this.desc = desc;
        this.dateOpened = dateOpened;
        this.floor = floor;
        this.location = location;
    } // end constructor

    public String getTaskID() {
        return taskID;
    }

    public String getDivID() {
        return divID;
    }

    public String getBuildID() {
        return buildID;
    }

    public String getIssueID() {
        return issueID;
    }

    public String getDesc() {
        return desc;
    }

    public String getDateOpened() {
        return dateOpened;
    }

    public String getFloor() {
        return floor;
    }

    public String getLocation() {
        return location;
    }
}
