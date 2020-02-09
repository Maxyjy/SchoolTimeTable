package com.android.mjutimetable;

import com.android.mjutimetable.Bean.ResultBean;
import com.android.mjutimetable.Bean.TestScheduleBean;
import com.android.mjutimetable.Bean.TimeTableBean;

import java.util.ArrayList;
import java.util.List;

public class ListSaver {
    private  static String shoolDay;

    private static List<TestScheduleBean> testScheduleBeanList = new ArrayList<>();
    private static List<TimeTableBean> timeTableBeanList = new ArrayList<>();
    private static List<ResultBean> resultBeanList = new ArrayList<>();

    public static List<TestScheduleBean> getTestScheduleBeanList() {
        return testScheduleBeanList;
    }

    public static void setTestScheduleBeanList(List<TestScheduleBean> testScheduleBeanList) {
        ListSaver.testScheduleBeanList = testScheduleBeanList;
    }

    public static List<TimeTableBean> getTimeTableBeanList() {
        return timeTableBeanList;
    }

    public static void setTimeTableBeanList(List<TimeTableBean> timeTableBeanList) {
        ListSaver.timeTableBeanList = timeTableBeanList;
    }

    public static List<ResultBean> getResultBeanList() {
        return resultBeanList;
    }

    public static void setResultBeanList(List<ResultBean> resultBeanList) {
        ListSaver.resultBeanList = resultBeanList;
    }

    public static String getShoolDay() {
        return shoolDay;
    }

    public static void setShoolDay(String shoolDay) {
        ListSaver.shoolDay = shoolDay;
    }
}
