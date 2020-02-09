package com.android.mjutimetable;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.mjutimetable.Bean.TimeBean;
import com.android.mjutimetable.Bean.TimeTableBean;
import com.android.mjutimetable.Util.ParseTool;
import com.android.mjutimetable.Util.ToastUtil;
import com.android.mjutimetable.Util.WatingDialog;
import com.google.android.material.navigation.NavigationView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {

    private int what = 1;
    private ParseTool parseTool;
    private static String schoolDay;

    private LinearLayout ll_login;

    private WatingDialog dialog;
    //Urls
    private String GETSCHOOLDAYURL = "http://47.101.148.255/getschoolday.html";
    private String GETVIEWSTATEURL = "http://jwgl.mju.edu.cn/default2.aspx";
    private String GETNAMEURL = "http://jwgl.mju.edu.cn/default2.aspx";
    private String REQUESTHOST = "jwgl.mju.edu.cn";
    private String REFERER = "http://jwgl.mju.edu.cn/";
    private String ORIGIN = "http://jwgl.mju.edu.cn";
    private String TIMETABLEURL = "http://jwgl.mju.edu.cn/xskbcx.aspx?xh={id}&xm={name}&gnmkdm=N121603";
    private String RESULTURL = "http://jwgl.mju.edu.cn/xscj_gc2.aspx?xh={id}&xm={name}&gnmkdm=N121616";
    private String TESTSCHEDULEURL = "http://jwgl.mju.edu.cn/xskscx.aspx?xh={id}&xm={name}&gnmkdm=N121604";
    private String cookie;
    private String VIEWSTATE;
    //Urls End

    //Login Page View
    private ProgressBar pb_loading_code;
    private ImageView ivCode;
    private EditText etStuId;
    private EditText etStuPsw;
    private EditText etCode;
    private Button btLogin;
    private TextView code_hint;

    //Login Page View End

    private String name;
    private String stuId;

    //TimeTable Page

    //TimeTable Page View
    TextView month;


    int tempWeek;
    int tempStartTime;
    int tempEndTime;


    String showString;


    Boolean notThisWeek;
    //TimeTable Page View End

    //ResultQuery
    private ListView lv_result_query;
    private ListView lv_test_schedule;

    //ResultQuery End
    int retry = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        dialog = new WatingDialog(LoginActivity.this, R.style.watingDialog, "稍等哦!");
        dialog.show();
        getSchoolDay();


    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    private void initViewClickListener() {
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etStuId.getText().length() == 0) {
                    etStuId.setHintTextColor(Color.RED);
                    return;
                }
                if (etStuPsw.getText().length() == 0) {
                    etStuPsw.setHintTextColor(Color.RED);
                    return;
                }
                if (etCode.getText().length() == 0) {
                    etCode.setHintTextColor(Color.RED);
                    return;
                }
                ll_login.requestFocus();

                dialog.show();
                stuId = etStuId.getText().toString();
                getViewState();

            }
        });

        ivCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCode();
            }
        });
    }


    private void getSchoolDay() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get()
                        //loginUrl就是你请求登录的url
                        .url(GETSCHOOLDAYURL)
                        .addHeader("Host", "47.101.148.255")
                        //下面数据抓包可以得到
                        .build()
                        .connTimeOut(5000)
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                Log.e("getSchoolDay", "onError");

                                retry = retry + 1;
                                if (retry >= 10) {
                                    setContentView(R.layout.activity_login);
                                    TextView no_net = findViewById(R.id.no_net_waring);
                                    LinearLayout ll_login = findViewById(R.id.ll_login);
                                    no_net.setVisibility(View.VISIBLE);
                                    ll_login.setVisibility(View.GONE);
                                    dialog.dismiss();
                                    return;
                                }
                                getSchoolDay();
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                Log.e("getSchoolDay", "onResponse");
                                System.out.println(s);
                                Document document = Jsoup.parse(s);
                                Element element = document.getElementById("timetable");
                                ListSaver.setShoolDay(element.text());
                                if(getIntent().getIntExtra("isRefresh",0)==1){
                                    Message msg = Message.obtain();
                                    msg.what = 2;//Message类有属性字段arg1、arg2、what...
                                    mHandler.sendMessage(msg);//sendMessage()用来传送Message类的值到mHandler

                                }else {
                                    Message msg = Message.obtain();
                                    msg.what = 1;//Message类有属性字段arg1、arg2、what...
                                    mHandler.sendMessage(msg);//sendMessage()用来传送Message类的值到mHandler
                                }
                            }
                        });
            }
        }).start();


    }

    public void getViewState() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        //loginUrl就是你请求登录的url
                        .url(GETVIEWSTATEURL)
                        //下面数据抓包可以得到
                        .addHeader("Host", REQUESTHOST)
                        .addHeader("Referer", "http://jwgl.mju.edu.cn/default2.aspx")
                        .addHeader("Cookie", cookie)
                        .build()
                        .connTimeOut(5000)
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showTextToas(getApplicationContext(), "连接失败，请重试！", "1", "6");
                                        dialog.dismiss();
                                    }
                                });

                            }

                            @Override
                            public void onResponse(String s, int i) {
                                //请求成功，s为网页源代码
                                if (s.contains("ERROR")) {
                                    //如果源代码包含“ERROR”，说明教务系统出错了
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtil.showTextToas(getApplicationContext(), "教务系统出错了！", "1", "6");
                                            dialog.dismiss();
                                            code_hint.setText("如果您发现网页端教务系统正常，但APP无法登录，请反馈给我们！");
                                        }
                                    });
                                    System.out.println("教务系统出错");

                                    initCode();//重新获取验证码

                                } else {
                                    //解析网页源码
                                    Document document = Jsoup.parse(s);
                                    Element element = document.getElementById("form1");
                                    Element viewstateTag = element.getElementsByTag("input").get(0);
                                    VIEWSTATE = viewstateTag.val();
                                    //按id=form1的input标签得到该标签value的值

                                    //拿到VIEWSTATE，继续下一步
                                    try {
                                        getName(etStuId.getText().toString(), etStuPsw.getText().toString(), etCode.getText().toString(), getApplicationContext());
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        });
            }
        }).start();
    }

    public void getName(final String stuId, final String stuPsw, final String code, final Context context) throws UnsupportedEncodingException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        //loginUrl就是你请求登录的url
                        .url(GETNAMEURL)
                        //下面数据抓包可以得到
                        .addParams("__VIEWSTATE", VIEWSTATE)
                        .addParams("txtUserName", stuId) //学号，
                        .addParams("Textbox1", "")
                        .addParams("TextBox2", stuPsw)//密码
                        .addParams("txtSecretCode", code) //验证码
                        .addParams("RadioButtonList1", "%D1%A7%C9%FA")
                        .addParams("Button1", "")
                        .addParams("lbLanguage", "")
                        .addParams("hidsc", "")
                        .addParams("hidPdrs", "")
                        .addHeader("Host", REQUESTHOST)
                        .addHeader("Referer", "http://jwgl.mju.edu.cn/default2.aspx")
                        .addHeader("Cookie", cookie)
                        .build()
                        .connTimeOut(5000)
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showTextToas(getApplicationContext(), "连接失败，请重试！", "2", "6");
                                        dialog.dismiss();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                System.out.println("onResponse");
                                //请求成功，response就是得到的html文件（网页源代码）
                                if (s.contains("验证码不正确")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtil.showTextToas(getApplicationContext(), "验证码不正确！", "2", "6");
                                            dialog.dismiss();
                                            etCode.setText("");
                                            code_hint.setText("");
                                        }
                                    });
                                    //如果源代码包含“验证码不正确”
                                    System.out.println("验证码不正确");
                                    initCode();
                                } else if (s.contains("密码错误")) {
                                    //如果源代码包含“密码错误”
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtil.showTextToas(getApplicationContext(), "密码错误，已为您显示明文", "2", "6");
                                            dialog.dismiss();
                                            etStuPsw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                                            etCode.setText("");

                                        }
                                    });
                                    //如果源代码包含“密码错误”
                                    System.out.println("密码错误,已为您显示明文");
                                    etStuPsw.clearFocus();
                                    etCode.clearFocus();
                                    etStuId.clearFocus();
                                    ll_login.requestFocus();
                                    initCode();
                                } else if (s.contains("用户名不存在")) {
                                    //如果源代码包含“密码错误”
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtil.showTextToas(getApplicationContext(), "用户名不存在！", "2", "6");
                                            dialog.dismiss();
                                            etCode.setText("");
                                        }
                                    });
                                    //如果源代码包含“密码错误”
                                    System.out.println("用户名不存在");
                                    initCode();
                                } else if (s.contains("ERROR")) {
                                    //如果源代码包含“密码错误”
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtil.showTextToas(getApplicationContext(), "教务系统崩了！", "2", "6");
                                            dialog.dismiss();
                                            etCode.setText("");
                                        }
                                    });
                                    //如果源代码包含“密码错误”
                                    System.out.println("教务系统崩了");
                                    initCode();

                                } else {
                                    //登录成功
                                    System.out.println("登录成功");
                                    //<span id="xhxm">杨建宇

                                    int b = s.indexOf("\"xhxm\">");
                                    name = "" + s.charAt(b + 7) + s.charAt(b + 8) + s.charAt(b + 9);
                                    UserInfoSaver.USERNAME = name;
                                    UserInfoSaver.USERID = stuId;
                                    saveIdPsw(stuId);
                                    saveIdPsw(stuPsw);
                                    saveIdPsw(name);
                                    if (what == 1) {
                                        try {
                                            getTimetableString();
                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                    }


                                }
                            }
                        });
            }
        }).start();


    }

    public void getTimetableString() throws UnsupportedEncodingException {
        TIMETABLEURL = TIMETABLEURL.replace("{id}", stuId).replace("{name}", URLEncoder.encode(name, "gb2312"));

        final String Referer = "http://jwgl.mju.edu.cn/xs_main.aspx?xh=" + stuId;
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        //loginUrl就是你请求登录的url
                        .url(TIMETABLEURL)
                        //下面数据抓包可以得到
                        .addHeader("Host", REQUESTHOST)
                        .addHeader("Referer", Referer)
                        .addHeader("Cookie", cookie)
                        .build()
                        .connTimeOut(5000)
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {

                            }

                            @Override
                            public void onResponse(String s, int i) {
                                System.out.println("onResponse");
                                ListSaver.setTimeTableBeanList(parseTimeTableHtml(s));
                                saveTimeTableJson();
                                dialog.dismiss();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
            }
        }).start();


    }


    private void saveTimeTableJson() {
        JSONArray jsonArray = new JSONArray();//创建JSON数组
        jsonArray.addAll(ListSaver.getTimeTableBeanList());//将集合放入JSONArray中
        List<TimeTableBean> test = ListSaver.getTimeTableBeanList();
        String json = jsonArray.toString();
        String filePath = "/data/data/com.android.mjutimetable/";
        String fileName = "timetablejson.txt";
        writeTimeTableJsonTxtToFile(json, filePath, fileName);

    }

    private void saveIdPsw(String save) {
        String filePath = "/data/data/com.android.mjutimetable/";
        String fileName = "idpsw.txt";
        writeTxtToFile(save, filePath, fileName);
    }

    private void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }
    private void writeTimeTableJsonTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if(file.exists()){
                FileWriter fileWriter =new FileWriter(file);
                fileWriter.write("");
                fileWriter.flush();
                fileWriter.close();
            }
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    private void initCode() {
        pb_loading_code.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils
                        .get()
                        .url("http://jwgl.mju.edu.cn/CheckCode.aspx")
                        .addHeader("Host", REQUESTHOST)
                        .addHeader("Origin", ORIGIN)
                        .addHeader("Referer", REFERER)
                        .build()
                        .connTimeOut(5000)
                        .execute(new Callback() {
                            @Override
                            public Object parseNetworkResponse(Response response, int i) throws Exception {

                                Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                                Headers headers = response.headers();
                                List<String> cookies = headers.values("Set-Cookie");
                                String sessiona = cookies.get(0);
                                String sessionaID = sessiona.substring(0, sessiona.indexOf(";"));
                                String sessionb = cookies.get(1);
                                String sessionbID = sessionb.substring(0, sessionb.indexOf(";"));

                                cookie = sessionaID + "; " + sessionbID;
                                ivCode.setImageBitmap(bitmap);
                                pb_loading_code.setVisibility(View.GONE);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        code_hint.setText("如果验证码获取时间过久请点击更新");
                                    }
                                });

                                if (response.code() != 200) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            code_hint.setText("验证码获取失败，正在重新获取");
                                        }
                                    });
                                }

                                return null;
                            }

                            @Override
                            public void onError(Call call, Exception e, int i) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pb_loading_code.setVisibility(View.GONE);
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Object o, int i) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pb_loading_code.setVisibility(View.GONE);
                                    }
                                });

                            }
                        });
            }
        }).start();


    }

    private void initGetSavedTimeTableJson() {
        File timetablejson = new File("/data/data/com.android.mjutimetable/timetablejson.txt");
        if (timetablejson.exists()) {
            String timeTableJson = ReadTimeTableJsonTxtFile(timetablejson.getPath());
            parseTimeTableJson(timeTableJson);

        }
    }

    public List<TimeTableBean> parseTimeTableHtml(String html) {
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
                    if (msg.length() == 0) {
                        //没有课程就跳过
                        Log.i("xsx", "msg为空跳过");
                        continue;
                    }
                    //处理一个td里的字符，如：计算机组成原理 2节/周(1-17) 张凤英 田师208
                    //

                    String strings[] = msg.split(" ");

                    String stringstest[] = msg.split(strings[0]);

                    ArrayList<TimeBean> timeBeans = new ArrayList<>();

                    //多少节
                    TimeTableBean timeTableBean = new TimeTableBean();
                    timeTableBean.setcName(strings[0]);
                    timeTableBean.setTeacherName(strings[2]);
                    String jieshu[] = strings[1].substring(strings[1].indexOf("第") + 1, strings[1].indexOf("节")).split(",");
                    timeTableBean.setDay("" + strings[1].charAt(0) + strings[1].charAt(1));
                    timeTableBean.setStartNum(jieshu[0]);
                    timeTableBean.setEndNum(jieshu[jieshu.length - 1]);


                    for (int b = 1; b < stringstest.length; b++) {
                        String stringstestb[] = stringstest[b].split(" ");
                        String week = stringstestb[1].substring(stringstestb[1].indexOf("{"), stringstestb[1].indexOf("}"));
                        week = week.substring(1);
                        week = week.substring(1);
                        week = week.substring(0, week.length() - 1);
                        String weeks[] = week.split("-");
                        TimeBean timeBean = new TimeBean();
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

    private void initGetSavedIdPsw() {

        File idpsw = new File("/data/data/com.android.mjutimetable/idpsw.txt");
        if (idpsw.exists()) {

            ReadIdPswTxtFile(idpsw.getPath());
            etStuId.setText(UserInfoSaver.USERID);
            etStuPsw.setText(UserInfoSaver.USERPSW);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            etCode.setFocusable(true);
                            etCode.setFocusableInTouchMode(true);
                            etCode.requestFocus();
                            //显示软键盘

                            //如果上面的代码没有弹出软键盘 可以使用下面另一种方式
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(etCode, 0);
                            //要执行的操作
                        }
                    });

                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 200);//2秒后执行TimeTask的run方法

        }else{
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            etStuId.setFocusable(true);
                            etStuId.setFocusableInTouchMode(true);
                            etStuId.requestFocus();
                            //显示软键盘

                            //如果上面的代码没有弹出软键盘 可以使用下面另一种方式
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(etStuId, 0);
                            //要执行的操作
                        }
                    });

                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 200);//2秒后执行TimeTask的run方法

        }

    }

    public static String ReadIdPswTxtFile(String strFilePath) {
        String path = strFilePath;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
            Log.d("TestFile", "The File doesn't not exist.");
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    UserInfoSaver.USERID = buffreader.readLine();
                    UserInfoSaver.USERPSW = buffreader.readLine();
                    UserInfoSaver.USERNAME = buffreader.readLine();
                    instream.close();
                }
            } catch (java.io.FileNotFoundException e) {
                Log.d("TestFile", "The File doesn't not exist.");
            } catch (IOException e) {
                Log.d("TestFile", e.getMessage());
            }
        }
        return content;
    }

    public static String ReadTimeTableJsonTxtFile(String strFilePath) {
        String path = strFilePath;
        String content = ""; //文件内容字符串
        String timetablejson = "";
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory()) {
            Log.d("TestFile", "The File doesn't not exist.");
        } else {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    StringBuffer sb = new StringBuffer();
                    //分行读取
                    while ((line = buffreader.readLine()) != null) {
                        sb.append(line);
                    }
                    timetablejson = sb.toString();
                    instream.close();
                }
            } catch (java.io.FileNotFoundException e) {
                Log.d("TestFile", "The File doesn't not exist.");
            } catch (IOException e) {
                Log.d("TestFile", e.getMessage());
            }
        }
        return timetablejson;
    }

    private void parseTimeTableJson(String timeTableJson) {
        ListSaver.setTimeTableBeanList(JSON.parseArray(timeTableJson, TimeTableBean.class)); //集合类型，使用parseArray进行解析
    }

    private void initFindViewById() {


        ll_login = findViewById(R.id.ll_login);

        pb_loading_code = findViewById(R.id.pb_loading_code);
        ivCode = findViewById(R.id.iv_code);
        etStuId = findViewById(R.id.et_stuid);
        etStuPsw = findViewById(R.id.et_stupsw);
        etCode = findViewById(R.id.et_code);
        btLogin = findViewById(R.id.bt_login);
        code_hint = findViewById(R.id.code_hint);

    }

    public String getWeekOfDate(Date date) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public int countTwoDayWeek(Date start, Date end) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        long time1 = cal.getTimeInMillis();
        cal.setTime(end);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        Double days = Double.parseDouble(String.valueOf(between_days));
        if ((days / 7) > 0 && (days / 7) <= 1) {
            //不满一周的按一周算
            return 1;
        } else if (days / 7 > 1) {
            int day = days.intValue();
            if (day % 7 > 0) {
                return day / 7 + 1;
            } else {
                return day / 7;
            }
        } else if ((days / 7) == 0) {
            return 0;
        } else {
            //负数返还null
            return 0;
        }

    }






    /**
     * 判定当前是否需要隐藏
     */
    protected boolean isShouldHideKeyBord(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            return !(ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom);
            //return !(ev.getY() > top && ev.getY() < bottom);
        }
        return false;
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            etStuId.clearFocus();
            etStuPsw.clearFocus();
            etCode.clearFocus();
        }
    }

    public void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        //显示软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //如果上面的代码没有弹出软键盘 可以使用下面另一种方式
        //InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        // imm.showSoftInput(editText, 0);
    }

    Handler mHandler = new Handler() {

        //handleMessage为处理消息的方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                File timetablejson = new File("/data/data/com.android.mjutimetable/timetablejson.txt");
                String oldSchoolDay=readSchoolDay();
                saveSchoolDay();
                if(oldSchoolDay.equals(ListSaver.getShoolDay())){
                if (timetablejson.exists()) {


                    initGetSavedTimeTableJson();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
                }
                if (!timetablejson.exists()||!oldSchoolDay.equals(ListSaver.getShoolDay())) {

                if(timetablejson.exists()&&!oldSchoolDay.equals(ListSaver.getShoolDay())){
                    ToastUtil.showTextToas(getApplicationContext(),"新学期开始了，请重新获取课程表！","2","6");
                }
                    setContentView(R.layout.activity_login);

                    parseTool = new ParseTool();

                    //-------!!!-------
                    initFindViewById();

                    initGetSavedIdPsw();
                    initViewClickListener();


                    ll_login.setVisibility(View.VISIBLE);
                    initCode();
                }
                dialog.dismiss();
            }
            if(msg.what==2){
                setContentView(R.layout.activity_login);

                parseTool = new ParseTool();

                //-------!!!-------
                initFindViewById();

                initGetSavedIdPsw();
                initViewClickListener();


                ll_login.setVisibility(View.VISIBLE);
                initCode();
                dialog.dismiss();
            }

        }
    };



    /**
     * 点击非编辑区域收起键盘
     * 获取点击事件
     */
    @CallSuper
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isShouldHideKeyBord(view, ev)) {
                hideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void saveSchoolDay() {
        //生成文件夹之后，再生成文件，不然会出错

        String strFilePath = "/data/data/com.android.mjutimetable/schoolday.txt";
        // 每次写入时，都换行写
        String strContent = ListSaver.getShoolDay();
        try {
            File file = new File(strFilePath);
            if(file.exists()){
                FileWriter fileWriter =new FileWriter(file);
                fileWriter.write("");
                fileWriter.flush();
                fileWriter.close();
            }
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    public static String readSchoolDay()
    {
        String strFilePath = "/data/data/com.android.mjutimetable/schoolday.txt";
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(strFilePath);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory())
        {
            Log.d("TestFile", "The File doesn't not exist.");
        }
        else
        {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null)
                {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    content=buffreader.readLine();
                    instream.close();
                }
            }
            catch (java.io.FileNotFoundException e)
            {
                Log.d("TestFile", "The File doesn't not exist.");
            }
            catch (IOException e)
            {
                Log.d("TestFile", e.getMessage());
            }
        }
        return content;
    }

}
