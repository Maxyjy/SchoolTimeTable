package com.android.mjutimetable.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * 课程表中一节课
 * Created by XSX on 2016/2/21.
 */
public class TimeTableBean implements Serializable {
    /**
     * 课程名
     */
    private String cName;
    /**第几节开始
     *
     */
    private String startNum;
    /**
     * 第几节结束
     */
    private String endNum;
    /**
     * 星期几
     */
    private String day;

    private List<TimeBean> time;

    private String teacherName;



    public TimeTableBean() {
    }

    public TimeTableBean(String cName, String startNum, String endNum, String day, String classroom) {
        this.cName = cName;
        this.startNum = startNum;
        this.endNum = endNum;
        this.day = day;

    }


    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getStartNum() {
        return startNum;
    }

    public void setStartNum(String startNum) {
        this.startNum = startNum;
    }

    public String getEndNum() {
        return endNum;
    }

    public void setEndNum(String endNum) {
        this.endNum = endNum;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<TimeBean> getTime() {
        return time;
    }

    public void setTime(List<TimeBean> time) {
        this.time = time;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
