package com.android.mjutimetable.Util;

import android.util.Log;

import com.android.mjutimetable.Bean.ResultBean;
import com.android.mjutimetable.Bean.TestScheduleBean;
import com.android.mjutimetable.Bean.TimeBean;
import com.android.mjutimetable.Bean.TimeTableBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ParseTool {
    public  List<TimeTableBean> parseTimeTableHtml(String html) {
        List<TimeTableBean> timeTableBeens = new ArrayList<>();
        //先解析
        Document document = Jsoup.parse(html);
        //先解析出年级、学院、专业、班级代号
        //getParm(document);
        //再解析__VIEWSTATE
        //Element viewstateTag = document.getElementsByTag("input").get(2);
        //__VIEWSTATE = viewstateTag.val();
        //---------------------------------
        Element element = document.getElementById("Table1");
        //得到所有的行
        Elements trs = element.getElementsByTag("tr");
        for (int i = 0; i < trs.size(); i++) {

            //只需要处理行号为2（第一节）、4（第三节课）、6（第五节课）、8（第七节课）、10（第九节课）
            if (i == 2 || i == 4 || i == 6 || i == 8 || i == 10) {


                //得到显示第一节课的那一行
                Element e1 = trs.get(i);
                //得到所有的列
                Elements tds = e1.getElementsByTag("td");
                if (i == 2 || i == 6 || i == 10) {
                    //先清除显示上午、下午、晚上的一行
                    tds.remove(0);
                }
                tds.remove(0);
                for (int j = 0; j < tds.size(); j++) {
                    String msg = tds.get(j).text();
                    //判断是否有课程
                    if (msg.length()==0) {
                        //没有课程就跳过
                        Log.i("xsx", "msg为空跳过");
                        continue;
                    }
                    //处理一个td里的字符，如：计算机组成原理 2节/周(1-17) 张凤英 田师208
                    //

                    String strings[] = msg.split(" ");

                    String stringstest[] = msg.split(strings[0]);

                    ArrayList<TimeBean> timeBeans=new ArrayList<>();

                    //多少节
                    TimeTableBean timeTableBean = new TimeTableBean();
                    timeTableBean.setcName(strings[0]);
                    timeTableBean.setTeacherName(strings[2]);
                    String jieshu[]=strings[1].substring(strings[1].indexOf("第")+1,strings[1].indexOf("节")).split(",");
                    timeTableBean.setDay(""+strings[1].charAt(0)+strings[1].charAt(1));
                    timeTableBean.setStartNum(jieshu[0]);
                    timeTableBean.setEndNum(jieshu[jieshu.length-1]);


                    for(int b=1;b<stringstest.length;b++){
                        String stringstestb[] = stringstest[b].split(" ");
                        String week=stringstestb[1].substring(stringstestb[1].indexOf("{"),stringstestb[1].indexOf("}"));
                        week=week.substring(1);
                        week=week.substring(1);
                        week=week.substring(0,week.length()-1);
                        String weeks[]=week.split("-");
                        TimeBean timeBean=new TimeBean();
                        timeBean.setStartweek(weeks[0]);
                        timeBean.setEndweek(weeks[1]);
                        timeBean.setClassroom(stringstestb[3]);
                        timeBeans.add(timeBean);
                    }

                    timeTableBean.setTime(timeBeans);
                    timeTableBeens.add(timeTableBean);

                }
            }


        }

        return timeTableBeens;
    }

    public  List<TestScheduleBean> parseTestScheduleHtml(String html) {
        List<TestScheduleBean> testScheduleBeans = new ArrayList<>();
        //先解析
        Document document = Jsoup.parse(html);

        Element element = document.getElementById("DataGrid1");
        //得到所有的行
        Elements trs = element.getElementsByTag("tr");

        for(int i=0;i<trs.size();i++){
            TestScheduleBean scheduleBean=new TestScheduleBean();
            Element e1 = trs.get(i);
            Elements tds = e1.getElementsByTag("td");

            scheduleBean.setTestClassName(tds.get(1).text());
            scheduleBean.setStuName(tds.get(2).text());
            scheduleBean.setTestTime(tds.get(3).text());
            scheduleBean.setTestClassRoom(tds.get(4).text());
            scheduleBean.setTestSitNum(tds.get(6).text());

            testScheduleBeans.add(scheduleBean);
        }


        return testScheduleBeans;
    }

    public  List<ResultBean> parseResultHtml(String html) {
        List<ResultBean> resultBeans = new ArrayList<>();
        //先解析
        Document document = Jsoup.parse(html);

        Element element = document.getElementById("Datagrid1");
        //得到所有的行
        Elements trs = element.getElementsByTag("tr");

        for(int i=1;i<trs.size();i++){
            ResultBean resultBean=new ResultBean();
            Element e1 = trs.get(i);
            Elements tds = e1.getElementsByTag("td");

            resultBean.setClassYear(tds.get(0).text());
            resultBean.setClassTerm(tds.get(1).text());
            resultBean.setClassName(tds.get(3).text());
            resultBean.setClassType(tds.get(4).text());
            resultBean.setClassCredit(tds.get(6).text());
            resultBean.setClassResult(tds.get(8).text());
            resultBean.setClassMakeUpResult(tds.get(10).text());
            resultBean.setClassRetakeResult(tds.get(11).text());

            resultBeans.add(resultBean);



        }


        for(int i=resultBeans.size()-1;i>=0;i--) {

            Pattern pattern = Pattern.compile("([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])");


            if(pattern.matcher(resultBeans.get(i).getClassResult()).matches()) {

                if(resultBeans.get(i).getClassResult()!=null&&resultBeans.get(i).getClassResult().length()!=0&&resultBeans.get(i).getClassRetakeResult()!=null&&resultBeans.get(i).getClassRetakeResult().length()!=0)
                {
                    if(Float.valueOf(resultBeans.get(i).getClassRetakeResult())<60){
                        resultBeans.add(resultBeans.size(),resultBeans.get(i));
                        resultBeans.remove(i);
                    }
                }else{

                if(resultBeans.get(i).getClassRetakeResult()!=null&&resultBeans.get(i).getClassRetakeResult().length()!=0)
                {
                    if(Float.valueOf(resultBeans.get(i).getClassRetakeResult())<60){
                        resultBeans.add(0,resultBeans.get(i));
                        resultBeans.remove(i);
                }
                }else
                if(resultBeans.get(i).getClassMakeUpResult()!=null&&resultBeans.get(i).getClassMakeUpResult().length()!=0)
                {
                    if(Float.valueOf(resultBeans.get(i).getClassMakeUpResult())<60){
                        resultBeans.add(0,resultBeans.get(i));
                        resultBeans.remove(i);
                    }
                }else
                if(resultBeans.get(i).getClassResult()!=null&&resultBeans.get(i).getClassResult().length()!=0)
                {
                    if(Float.valueOf(resultBeans.get(i).getClassResult())<60){
                        resultBeans.add(resultBeans.size(),resultBeans.get(i));
                        resultBeans.remove(i);
                    }
                }
                }
            }



            if(!pattern.matcher(resultBeans.get(i).getClassResult()).matches()) {


                if (resultBeans.get(i).getClassResult().equals("不及格")) {
                    resultBeans.add(resultBeans.size(), resultBeans.get(i));
                    resultBeans.remove(i);
                }



            }
        }
        return resultBeans;
    }




}
