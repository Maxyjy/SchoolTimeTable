package com.android.mjutimetable;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.InputType;
import android.util.Log;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.KeyEvent;
import android.view.MenuItem;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.mjutimetable.Bean.SpinnearBean;
import com.android.mjutimetable.Bean.TestScheduleBean;
import com.android.mjutimetable.Bean.TimeBean;
import com.android.mjutimetable.Bean.TimeTableBean;
import com.android.mjutimetable.Util.ClassDetailDialog;
import com.android.mjutimetable.Util.DownloadUtil;
import com.android.mjutimetable.Util.FTPUtils;
import com.android.mjutimetable.Util.ParseTool;
import com.android.mjutimetable.Util.PxUtil;
import com.android.mjutimetable.Util.ToastUtil;
import com.android.mjutimetable.Util.WatingDialog;
import com.android.mjutimetable.spinner.SpinnerViewPop;
import com.android.mjutimetable.spinner.listener.OnSpinnerItemClickListener;
import com.google.android.material.navigation.NavigationView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.Toast;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private int currentVersionCode;


    private ArrayList<SpinnearBean> mSpinnerList1;
    private SpinnerViewPop spinnerView_radioDialog1;
    private TextView spinnerTextView1;

    private AlertDialog.Builder builder;

    /*===========读取assets目录下的js字符串文件（js数组和js对象），然后生成List集合===========*/
    public static final String LISTROOTNODE = "spinnerList";
    public static final String KEY_LISTITEM_NAME = "paraName";
    public static final String KEY_LISTITEM_VALUE = "paraValue";
    public static final String KEY_LISTITEM_CHECKCOLOR = "checkColor";


    private int what=1;
    private ParseTool parseTool;
    private static String schoolDay;

    DrawerLayout drawer;

    private LinearLayout ll_login;
    private LinearLayout ll_timetable;

    private RelativeLayout rl_about;

    private NavigationView navigationView;
    private WatingDialog dialog;
    private ClassDetailDialog classDetailDialog;
    private ClassDetailDialog errorDialog;
    //Urls
    private String GETSCHOOLDAYURL="http://47.101.148.255/version.html";
    private String GETVIEWSTATEURL = "http://jwgl.mju.edu.cn/default2.aspx";
    private String GETNAMEURL = "http://jwgl.mju.edu.cn/default2.aspx";
    private String REQUESTHOST="jwgl.mju.edu.cn";
    private String REFERER="http://jwgl.mju.edu.cn/";
    private String ORIGIN="http://jwgl.mju.edu.cn";
    private String TIMETABLEURL="http://jwgl.mju.edu.cn/xskbcx.aspx?xh={id}&xm={name}&gnmkdm=N121603";
    private String RESULTURL ="http://jwgl.mju.edu.cn/xscj_gc2.aspx?xh={id}&xm={name}&gnmkdm=N121616";
    private String TESTSCHEDULEURL ="http://jwgl.mju.edu.cn/xskscx.aspx?xh={id}&xm={name}&gnmkdm=N121604";
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
    private TextView tv_stu_name;
    private TextView tv_stu_id;
    //Login Page View End

    private String name;
    private String stuId;

    //TimeTable Page

    //TimeTable Page View
    TextView month;

    TextView line_monday_cell_hint_day;
    TextView line_tuesday_cell_hint_day;
    TextView line_wednesday_cell_hint_day;
    TextView line_thursday_cell_hint_day;
    TextView line_friday_cell_hint_day;
    TextView line_saturday_cell_hint_day;
    TextView line_sunday_cell_hint_day;

    LinearLayout monday_bg;
    LinearLayout tuesday_bg;
    LinearLayout wednesday_bg;
    LinearLayout thursday_bg;
    LinearLayout friday_bg;
    LinearLayout saturday_bg;
    LinearLayout sunday_bg;

    TextView line_monday_cell_1;
    TextView line_monday_cell_2;
    TextView line_monday_cell_3;
    TextView line_monday_cell_4;
    TextView line_monday_cell_5;
    TextView line_monday_cell_6;
    TextView line_monday_cell_7;
    TextView line_monday_cell_8;
    TextView line_monday_cell_9;
    TextView line_monday_cell_10;
    TextView line_monday_cell_11;

    TextView line_monday_cell_1_2;
    TextView line_monday_cell_1_2_3;
    TextView line_monday_cell_3_4;
    TextView line_monday_cell_1_2_3_4;
    TextView line_monday_cell_5_6;
    TextView line_monday_cell_5_6_7;
    TextView line_monday_cell_7_8;
    TextView line_monday_cell_5_6_7_8;
    TextView line_monday_cell_9_10;
    TextView line_monday_cell_9_10_11;

    TextView line_tuesday_cell_1;
    TextView line_tuesday_cell_2;
    TextView line_tuesday_cell_3;
    TextView line_tuesday_cell_4;
    TextView line_tuesday_cell_5;
    TextView line_tuesday_cell_6;
    TextView line_tuesday_cell_7;
    TextView line_tuesday_cell_8;
    TextView line_tuesday_cell_9;
    TextView line_tuesday_cell_10;
    TextView line_tuesday_cell_11;

    TextView line_tuesday_cell_1_2;
    TextView line_tuesday_cell_1_2_3;
    TextView line_tuesday_cell_3_4;
    TextView line_tuesday_cell_1_2_3_4;
    TextView line_tuesday_cell_5_6;
    TextView line_tuesday_cell_5_6_7;
    TextView line_tuesday_cell_7_8;
    TextView line_tuesday_cell_5_6_7_8;
    TextView line_tuesday_cell_9_10;
    TextView line_tuesday_cell_9_10_11;

    TextView line_wednesday_cell_1;
    TextView line_wednesday_cell_2;
    TextView line_wednesday_cell_3;
    TextView line_wednesday_cell_4;
    TextView line_wednesday_cell_5;
    TextView line_wednesday_cell_6;
    TextView line_wednesday_cell_7;
    TextView line_wednesday_cell_8;
    TextView line_wednesday_cell_9;
    TextView line_wednesday_cell_10;
    TextView line_wednesday_cell_11;

    TextView line_wednesday_cell_1_2;
    TextView line_wednesday_cell_1_2_3;
    TextView line_wednesday_cell_3_4;
    TextView line_wednesday_cell_1_2_3_4;
    TextView line_wednesday_cell_5_6;
    TextView line_wednesday_cell_5_6_7;
    TextView line_wednesday_cell_7_8;
    TextView line_wednesday_cell_5_6_7_8;
    TextView line_wednesday_cell_9_10;
    TextView line_wednesday_cell_9_10_11;

    TextView line_thursday_cell_1;
    TextView line_thursday_cell_2;
    TextView line_thursday_cell_3;
    TextView line_thursday_cell_4;
    TextView line_thursday_cell_5;
    TextView line_thursday_cell_6;
    TextView line_thursday_cell_7;
    TextView line_thursday_cell_8;
    TextView line_thursday_cell_9;
    TextView line_thursday_cell_10;
    TextView line_thursday_cell_11;

    TextView line_thursday_cell_1_2;
    TextView line_thursday_cell_1_2_3;
    TextView line_thursday_cell_3_4;
    TextView line_thursday_cell_1_2_3_4;
    TextView line_thursday_cell_5_6;
    TextView line_thursday_cell_5_6_7;
    TextView line_thursday_cell_7_8;
    TextView line_thursday_cell_5_6_7_8;
    TextView line_thursday_cell_9_10;
    TextView line_thursday_cell_9_10_11;


    TextView line_friday_cell_1;
    TextView line_friday_cell_2;
    TextView line_friday_cell_3;
    TextView line_friday_cell_4;
    TextView line_friday_cell_5;
    TextView line_friday_cell_6;
    TextView line_friday_cell_7;
    TextView line_friday_cell_8;
    TextView line_friday_cell_9;
    TextView line_friday_cell_10;
    TextView line_friday_cell_11;

    TextView line_friday_cell_1_2;
    TextView line_friday_cell_1_2_3;
    TextView line_friday_cell_3_4;
    TextView line_friday_cell_1_2_3_4;
    TextView line_friday_cell_5_6;
    TextView line_friday_cell_5_6_7;
    TextView line_friday_cell_7_8;
    TextView line_friday_cell_5_6_7_8;
    TextView line_friday_cell_9_10;
    TextView line_friday_cell_9_10_11;


    TextView line_saturday_cell_1;
    TextView line_saturday_cell_2;
    TextView line_saturday_cell_3;
    TextView line_saturday_cell_4;
    TextView line_saturday_cell_5;
    TextView line_saturday_cell_6;
    TextView line_saturday_cell_7;
    TextView line_saturday_cell_8;
    TextView line_saturday_cell_9;
    TextView line_saturday_cell_10;
    TextView line_saturday_cell_11;

    TextView line_saturday_cell_1_2;
    TextView line_saturday_cell_1_2_3;
    TextView line_saturday_cell_3_4;
    TextView line_saturday_cell_1_2_3_4;
    TextView line_saturday_cell_5_6;
    TextView line_saturday_cell_5_6_7;
    TextView line_saturday_cell_7_8;
    TextView line_saturday_cell_5_6_7_8;
    TextView line_saturday_cell_9_10;
    TextView line_saturday_cell_9_10_11;


    TextView line_sunday_cell_1;
    TextView line_sunday_cell_2;
    TextView line_sunday_cell_3;
    TextView line_sunday_cell_4;
    TextView line_sunday_cell_5;
    TextView line_sunday_cell_6;
    TextView line_sunday_cell_7;
    TextView line_sunday_cell_8;
    TextView line_sunday_cell_9;
    TextView line_sunday_cell_10;
    TextView line_sunday_cell_11;

    TextView line_sunday_cell_1_2;
    TextView line_sunday_cell_1_2_3;
    TextView line_sunday_cell_3_4;
    TextView line_sunday_cell_1_2_3_4;
    TextView line_sunday_cell_5_6;
    TextView line_sunday_cell_5_6_7;
    TextView line_sunday_cell_7_8;
    TextView line_sunday_cell_5_6_7_8;
    TextView line_sunday_cell_9_10;
    TextView line_sunday_cell_9_10_11;

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
    int selectWeek=-1;

    int nowCurrentWeekNum =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new WatingDialog(MainActivity.this,R.style.watingDialog,"稍等哦!");
        errorDialog=new ClassDetailDialog(this,R.style.classDetailDialog,"你还没有进行教学质量评价，请先登录原网页教务系统进行评价！");
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        schoolDay=ListSaver.getShoolDay();
        getCurrentVersionCode();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        parseTool=new ParseTool();

        Date today = new Date(System.currentTimeMillis());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date schoolDayDate = null;
        try {
            schoolDayDate = df.parse(schoolDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int currentWeekNum=countTwoDayWeek(schoolDayDate,today);
        if(selectWeek==-1) {
            nowCurrentWeekNum = currentWeekNum;
        }

        //-------!!!-------
        initFindViewById();
        initSpinner();
        initGetSavedIdPsw();
        initViewClickListener();

        tv_stu_name.setText(UserInfoSaver.USERNAME);
        tv_stu_id.setText(UserInfoSaver.USERID);

        getIsUpdate();

      

        Message msg = Message.obtain();
        msg.what = 1;//Message类有属性字段arg1、arg2、what...
        mHandler.sendMessage(msg);//sendMessage()用来传送Message类的值到mHandler




    }


    private void initSpinner() {
        Drawable right=getResources().getDrawable(R.drawable.ic_expand_more_black_24dp);

        right.setBounds(0,0,PxUtil.dip2px(getApplicationContext(),20),PxUtil.dip2px(getApplicationContext(),20));//必须设置图片的大小否则没有作用
        spinnerTextView1.setCompoundDrawables(null,null ,right,null);//设置图片left这里如果是右边就放到第二个参数里面依次对应
        spinnerTextView1.setCompoundDrawablePadding(0);

        mSpinnerList1 = new ArrayList<SpinnearBean>();
        //模拟获取数据集合
        try{
            mSpinnerList1 = parseJsonArray("spinners1.txt");
        }catch (Exception e) {
            e.printStackTrace();
        }
        //设置下拉菜单显示的列表项文本
        if (mSpinnerList1 != null && mSpinnerList1.size() > 0){
            spinnerView_radioDialog1.setData(mSpinnerList1);//设置下拉菜单列表集合源
            spinnerView_radioDialog1.setSelectedIndexAndText(0);//更改下拉菜单选中的列表项下标值
        }
        //SPINNER1

        //下拉菜单列表的列表项的点击事件监听
        spinnerView_radioDialog1.setOnSpinnerItemClickListener(new OnSpinnerItemClickListener() {
            @Override
            public void OnFinished(int position) {

                StringBuffer str = new StringBuffer();

                for(int i=0;i<mSpinnerList1.size();i++){
                    str.append(mSpinnerList1.get(i).getParaName() + ":" + mSpinnerList1.get(i).isSelectedState() + "\n");

                    if(mSpinnerList1.get(i).isSelectedState()){
                        selectWeek =Integer.valueOf(mSpinnerList1.get(i).getParaValue());

                        try {
                            initTimeTable();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        try {
                            initTimeTableHint();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }


                }

            }
        });

    }

    /**
     * 解析JSON文件的简单数组
     */
    private ArrayList<SpinnearBean> parseJsonArray(String fileName) throws Exception{

        ArrayList<SpinnearBean> itemsList = new ArrayList<SpinnearBean>();

        String jsonStr = getStringFromAssert(MainActivity.this, fileName);
        if(jsonStr.equals("")){
            return null;
        }
        JSONObject allData = new JSONObject(jsonStr);  //全部内容变为一个项
        org.json.JSONArray jsonArr = allData.getJSONArray(LISTROOTNODE); //取出数组
        for(int x = 0;x<jsonArr.length();x++){
            SpinnearBean model = new SpinnearBean();
            JSONObject jsonobj = jsonArr.getJSONObject(x);

            if(Integer.valueOf(jsonobj.getString(KEY_LISTITEM_VALUE))==nowCurrentWeekNum){
                model.setParaName("["+jsonobj.getString(KEY_LISTITEM_NAME)+"]");
            }
            if(Integer.valueOf(jsonobj.getString(KEY_LISTITEM_VALUE))!=nowCurrentWeekNum){
                model.setParaName(jsonobj.getString(KEY_LISTITEM_NAME));
            }

            model.setParaValue(jsonobj.getString(KEY_LISTITEM_VALUE));
            if(jsonobj.has(KEY_LISTITEM_CHECKCOLOR)){
                model.setCheckColor(jsonobj.getString(KEY_LISTITEM_CHECKCOLOR));
            }
            model.setSelectedState(false);
            itemsList.add(model);
            model = null;
        }
        return itemsList;
    }

    /**
     * 访问assets目录下的资源文件，获取文件中的字符串
     * @param filePath - 文件的相对路径，例如："listdata.txt"或者"/www/listdata.txt"
     * @return 内容字符串
     * */
    public String getStringFromAssert(Context mContext, String filePath) {

        String content = ""; // 结果字符串
        try {
            InputStream is = mContext.getResources().getAssets().open(filePath);// 打开文件
            int ch = 0;
            ByteArrayOutputStream out = new ByteArrayOutputStream(); // 实现了一个输出流
            while ((ch = is.read()) != -1) {
                out.write(ch); // 将指定的字节写入此 byte 数组输出流
            }
            byte[] buff = out.toByteArray();// 以 byte 数组的形式返回此输出流的当前内容
            out.close(); // 关闭流
            is.close(); // 关闭流
            content = new String(buff, "UTF-8"); // 设置字符串编码
        } catch (Exception e) {
            Toast.makeText(mContext, "对不起，没有找到指定文件！", Toast.LENGTH_SHORT)
                    .show();
        }
        return content;
    }

    private void initViewClickListener() {
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etStuId.getText().length()==0){
                    etStuId.setHintTextColor(Color.RED);return;
                }
                if(etStuPsw.getText().length()==0){
                    etStuPsw.setHintTextColor(Color.RED);return;
                }
                if(etCode.getText().length()==0){
                    etCode.setHintTextColor(Color.RED);return;
                }
                ll_login.requestFocus();

                dialog.show();
                stuId=etStuId.getText().toString();
                getViewState(etStuId.getText().toString(),etStuPsw.getText().toString(),etCode.getText().toString(),getApplicationContext());

            }
        });

        ivCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCode();
            }
        });
    }
    private void showTimeTable() throws ParseException {

    getSupportActionBar().setTitle("课程表");
        initTimeTable();
    initTimeTableHint();



    }


    public void getViewState(final String stuId, final String stuPsw, final String code, final Context context) {

    new Thread(new Runnable() {
        @Override
        public void run() {
            OkHttpUtils.post()
                    //loginUrl就是你请求登录的url
                    .url(GETVIEWSTATEURL)
                    //下面数据抓包可以得到
                    .addParams("txtUserName", stuId) //学号，
                    .addParams("Textbox1","")
                    .addParams("TextBox2", stuPsw)//密码
                    .addParams("txtSecretCode", code) //验证码
                    .addParams("RadioButtonList1", "%D1%A7%C9%FA")
                    .addParams("Button1", "")
                    .addParams("lbLanguage", "")
                    .addParams("hidsc","")
                    .addParams("hidPdrs","")
                    .addHeader("Host", REQUESTHOST)
                    .addHeader("Referer", "http://jwgl.mju.edu.cn/default2.aspx")
                    .addHeader("Cookie",cookie)
                    .build()
                    .connTimeOut(5000)
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.showTextToas(getApplicationContext(),"连接失败，请重试！","1","6");
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
                                        etCode.getText().clear();
                                        //显示软键盘

                                        //要执行的操作
                                        ToastUtil.showTextToas(getApplicationContext(),"验证码不正确！","1","6");
                                        dialog.dismiss();
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
                                        ToastUtil.showTextToas(getApplicationContext(),"密码错误！","1","6");
                                        dialog.dismiss();
                                    }
                                });
                                //如果源代码包含“密码错误”
                                System.out.println("密码错误");
                                initCode();
                            } else if (s.contains("用户名不存在")) {
                                //如果源代码包含“密码错误”
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showTextToas(getApplicationContext(),"用户名不存在！","1","6");
                                        dialog.dismiss();
                                    }
                                });
                                //如果源代码包含“密码错误”
                                System.out.println("用户名不存在");
                                initCode();
                            }
                            else if (s.contains("ERROR")) {
                                //如果源代码包含“密码错误”
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showTextToas(getApplicationContext(),"教务系统崩了！","1","6");
                                        dialog.dismiss();
                                        code_hint.setText("如果您发现网页端教务系统正常，但APP无法登录，请反馈给我们！");
                                    }
                                });
                                //如果源代码包含“密码错误”

                                System.out.println("教务系统崩了");
                                initCode();

                            } else {
                                //登录成功
                                System.out.println("登录成功");
                                //<span id="xhxm">杨建宇

                                Document document = Jsoup.parse(s);
                                Element element = document.getElementById("form1");
                                Element viewstateTag = element.getElementsByTag("input").get(0);
                                VIEWSTATE = viewstateTag.val();
                                //得到所有的行

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
                    .addParams("Textbox1","")
                    .addParams("TextBox2", stuPsw)//密码
                    .addParams("txtSecretCode", code) //验证码
                    .addParams("RadioButtonList1", "%D1%A7%C9%FA")
                    .addParams("Button1", "")
                    .addParams("lbLanguage", "")
                    .addParams("hidsc","")
                    .addParams("hidPdrs","")
                    .addHeader("Host", REQUESTHOST)
                    .addHeader("Referer", "http://jwgl.mju.edu.cn/default2.aspx")
                    .addHeader("Cookie",cookie)
                    .build()
                    .connTimeOut(5000)
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.showTextToas(getApplicationContext(),"连接失败，请重试！","2","6");
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

                                        ToastUtil.showTextToas(getApplicationContext(),"验证码不正确！","2","6");
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
                                        ToastUtil.showTextToas(getApplicationContext(),"密码错误，已为您显示明文","2","6");
                                        dialog.dismiss();
                                        etStuPsw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                                        etCode.setText("");
                                    }
                                });
                                //如果源代码包含“密码错误”
                                System.out.println("密码错误,已为您显示明文");
                                initCode();
                            } else if (s.contains("用户名不存在")) {
                                //如果源代码包含“密码错误”
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showTextToas(getApplicationContext(),"用户名不存在！","2","6");
                                        dialog.dismiss();
                                        etCode.setText("");
                                    }
                                });
                                //如果源代码包含“密码错误”
                                System.out.println("用户名不存在");
                                initCode();
                            }
                            else if (s.contains("ERROR")) {
                                //如果源代码包含“密码错误”
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showTextToas(getApplicationContext(),"教务系统崩了！","2","6");
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

                                int b=s.indexOf("\"xhxm\">");
                                name=""+s.charAt(b+7)+s.charAt(b+8)+s.charAt(b+9);
                                UserInfoSaver.USERNAME=name;
                                UserInfoSaver.USERID=stuId;
                                saveIdPsw(etStuId.getText().toString());
                                saveIdPsw(etStuPsw.getText().toString());
                                saveIdPsw(name);

                                if(what==2){
                                    try {
                                        getResultViewState(getApplicationContext());
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if(what==3){
                                    try {
                                        getTestSchedule();
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


    public void getResultViewState(final Context context) throws UnsupportedEncodingException {
        RESULTURL = RESULTURL.replace("{id}",stuId).replace("{name}", URLEncoder.encode(name, "gb2312"));

        final String Referer="http://jwgl.mju.edu.cn/xs_main.aspx?xh="+stuId;
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        //loginUrl就是你请求登录的url
                        .url(RESULTURL)
                        //下面数据抓包可以得到
                        .addParams("Button2","%D4%DA%D0%A3%D1%A7%CF%B0%B3%C9%BC%A8%B2%E9%D1%AF")
                        .addParams("ddlXN","")
                        .addParams("ddlXQ","")
                        .addHeader("Host", REQUESTHOST)
                        .addHeader("Referer", Referer)
                        .addHeader("Cookie",cookie)
                        .build()
                        .connTimeOut(5000)
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {

                            }

                            @Override
                            public void onResponse(String s, int i) {
                                System.out.println("onResponse");

                                if(s.contains("你还没有进行本学期的教学质量评价")){


                                    Message msg = Message.obtain();
                                    msg.what = 4;//Message类有属性字段arg1、arg2、what...
                                    mHandler.sendMessage(msg);//sendMessage()用来传送Message类的值到mHandler

                                    return;
                                }
                                Document document = Jsoup.parse(s);
                                Element element = document.getElementById("Form1");
                                Element viewstateTag = element.getElementsByTag("input").get(0);
                                VIEWSTATE = viewstateTag.val();

                                try {
                                    getResult();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
            }
        }).start();


    }
    public void getTestSchedule() throws UnsupportedEncodingException {
        TESTSCHEDULEURL = TESTSCHEDULEURL.replace("{id}",stuId).replace("{name}", URLEncoder.encode(name, "gb2312"));

        final String Referer="http://jwgl.mju.edu.cn/xs_main.aspx?xh="+stuId;
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        //loginUrl就是你请求登录的url
                        .url(TESTSCHEDULEURL)
                        //下面数据抓包可以得到
                        .addHeader("Host", REQUESTHOST)
                        .addHeader("Referer", Referer)
                        .addHeader("Cookie",cookie)
                        .build()
                        .connTimeOut(5000)
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                Log.e("getTestSchedule","Error");
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                System.out.println("onResponse");

                                ListSaver.setTestScheduleBeanList(parseTool.parseTestScheduleHtml(s));
                                if(ListSaver.getTestScheduleBeanList().size()==1){
                                    initGetSavedTimeScheduleJson();}

                                if(ListSaver.getTestScheduleBeanList().size()>1){
                                    saveTimeScheduleJson();
                                    getSupportActionBar().setTitle("课程表");

                                    Intent intent=new Intent(MainActivity.this,TimeScheduleActivity.class);

                                startActivity(intent);
                                    TimerTask task = new TimerTask() {
                                        @Override
                                        public void run() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ll_login.setVisibility(View.GONE);
                                                    ll_timetable.setVisibility(View.VISIBLE);
                                                    dialog.dismiss();
                                                }
                                            });

                                        }
                                    };
                                    Timer timer = new Timer();
                                    timer.schedule(task, 500);//2秒后执行TimeTask的run方法

                                }

                            }
                        });

            }
        }).start();

    }


    public void getResult() throws UnsupportedEncodingException {
        RESULTURL = RESULTURL.replace("{id}",stuId).replace("{name}", URLEncoder.encode(name, "gb2312"));

        String Referer="http://jwgl.mju.edu.cn/xs_main.aspx?xh="+stuId;
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.post()
                        //loginUrl就是你请求登录的url
                        .url(RESULTURL)
                        //下面数据抓包可以得到
                        .addParams("Button2","%D4%DA%D0%A3%D1%A7%CF%B0%B3%C9%BC%A8%B2%E9%D1%AF")
                        .addParams("ddlXN","")
                        .addParams("ddlXQ","")
                        .addParams("__VIEWSTATE",VIEWSTATE)
                        .addHeader("Host", REQUESTHOST)
                        .addHeader("Cookie",cookie)
                        .build()
                        .connTimeOut(5000)
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {

                            }

                            @Override
                            public void onResponse(String s, int i) {
                                System.out.println("onResponse");
                                ListSaver.setResultBeanList(parseTool.parseResultHtml(s));



                                Intent intent=new Intent(MainActivity.this,ResultActivity.class);
                                getSupportActionBar().setTitle("课程表");

                                startActivity(intent);
                                TimerTask task = new TimerTask() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                ll_login.setVisibility(View.GONE);
                                                ll_timetable.setVisibility(View.VISIBLE);
                                                dialog.dismiss();
                                            }
                                        });

                                    }
                                };
                                Timer timer = new Timer();
                                timer.schedule(task, 500);//2秒后执行TimeTask的run方法

                            }
                        });

            }
        }).start();

    }



    private void saveTimeScheduleJson() {
        JSONArray jsonArray = new JSONArray();//创建JSON数组
        jsonArray.addAll(ListSaver.getTestScheduleBeanList());//将集合放入JSONArray中

        String json=jsonArray.toString();
        String filePath = "/data/data/com.android.mjutimetable/";
        String fileName = "timeschedule.txt";
        writeTxtToFile(json, filePath, fileName);

    }



    private void saveTimeTableJson() {
        JSONArray jsonArray = new JSONArray();//创建JSON数组
        jsonArray.addAll(ListSaver.getTimeTableBeanList());//将集合放入JSONArray中
        List<TimeTableBean> test=ListSaver.getTimeTableBeanList();
        String json=jsonArray.toString();
        String filePath = "/data/data/com.android.mjutimetable/";
        String fileName = "timetablejson.txt";
        writeTxtToFile(json, filePath, fileName);

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
                        .addHeader("Referer",REFERER)
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

                                cookie=sessionaID+"; "+sessionbID;
                                ivCode.setImageBitmap(bitmap);
                                pb_loading_code.setVisibility(View.GONE);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        code_hint.setText("点击图片更换验证码");
                                    }
                                });

                                if(response.code()!=200){
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
        File timetablejson=new File("/data/data/com.android.mjutimetable/timetablejson.txt");
        if(timetablejson.exists()){
            String timeTableJson=ReadTimeTableJsonTxtFile(timetablejson.getPath());
            parseTimeTableJson(timeTableJson);

        }
    }
    private void initGetSavedTimeScheduleJson() {
        File timeScheduleJson=new File("/data/data/com.android.mjutimetable/timeschedule.txt");
        if(timeScheduleJson.exists()){
            String timeScheduleJsonString=ReadTimeTableJsonTxtFile(timeScheduleJson.getPath());
            parseTimeScheduleJson(timeScheduleJsonString);
        }else{
            Intent intent=new Intent(MainActivity.this,TimeScheduleActivity.class);
            getSupportActionBar().setTitle("课程表");

            startActivity(intent);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ll_login.setVisibility(View.GONE);
                            ll_timetable.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        }
                    });

                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 500);//2秒后执行TimeTask的run方法

        }
    }
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
    private void initGetSavedIdPsw() {

        File idpsw=new File("/data/data/com.android.mjutimetable/idpsw.txt");
        if(idpsw.exists()){
            ReadIdPswTxtFile(idpsw.getPath());
            etStuId.setText(UserInfoSaver.USERID);
            etStuPsw.setText(UserInfoSaver.USERPSW);
            tv_stu_name.setText(UserInfoSaver.USERNAME);
            tv_stu_id.setText(UserInfoSaver.USERID);
        }

    }
    public static String ReadIdPswTxtFile(String strFilePath) {
        String path = strFilePath;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);
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
                    UserInfoSaver.USERID =buffreader.readLine();
                    UserInfoSaver.USERPSW=buffreader.readLine();
                    UserInfoSaver.USERNAME=buffreader.readLine();
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
    public static String ReadTimeTableJsonTxtFile(String strFilePath) {
        String path = strFilePath;
        String content = ""; //文件内容字符串
        String timetablejson="";
        //打开文件
        File file = new File(path);
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
                    StringBuffer sb = new StringBuffer();
                    //分行读取
                    while ((line = buffreader.readLine()) != null) {
                        sb.append(line);
                    }
                    timetablejson=sb.toString();
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
        return timetablejson;
    }
    private void parseTimeTableJson(String timeTableJson) {
        ListSaver.setTimeTableBeanList(JSON.parseArray(timeTableJson, TimeTableBean.class)); //集合类型，使用parseArray进行解析
    }
    private void parseTimeScheduleJson(String timeScheduleJson) {
        ListSaver.setTestScheduleBeanList(JSON.parseArray(timeScheduleJson, TestScheduleBean.class)); //集合类型，使用parseArray进行解析
    }
    private void initFindViewById() {

        spinnerView_radioDialog1 =findViewById(R.id.spinnerView_pop1);
        spinnerView_radioDialog1.setSpinnerType(SpinnerViewPop.TYPE_POPWINDOW);//设置对话框样式，默认为popwindow样式
        spinnerTextView1=spinnerView_radioDialog1.findViewById(R.id.titleTextView);

        drawer= findViewById(R.id.drawer_layout);

        ll_login=findViewById(R.id.ll_login);
        ll_timetable=findViewById(R.id.ll_time_table);

        rl_about=findViewById(R.id.rl_about);

        tv_stu_name=navigationView.getHeaderView(0).findViewById(R.id.tv_stu_name);
        tv_stu_id=navigationView.getHeaderView(0).findViewById(R.id.tv_stu_id);
        pb_loading_code=findViewById(R.id.pb_loading_code);
        ivCode=findViewById(R.id.iv_code);
        etStuId=findViewById(R.id.et_stuid);
        etStuPsw=findViewById(R.id.et_stupsw);
        etCode=findViewById(R.id.et_code);
        btLogin=findViewById(R.id.bt_login);
        code_hint=findViewById(R.id.code_hint);


        //timetablepage
        month=findViewById(R.id.month);

        line_monday_cell_hint_day=findViewById(R.id.line_monday_cell_hint_day);
        line_tuesday_cell_hint_day=findViewById(R.id.line_tuesday_cell_hint_day);
        line_wednesday_cell_hint_day=findViewById(R.id.line_wednesday_cell_hint_day);
        line_thursday_cell_hint_day=findViewById(R.id.line_thursday_cell_hint_day);
        line_friday_cell_hint_day=findViewById(R.id.line_friday_cell_hint_day);
        line_saturday_cell_hint_day=findViewById(R.id.line_saturday_cell_hint_day);
        line_sunday_cell_hint_day=findViewById(R.id.line_sunday_cell_hint_day);



        monday_bg=findViewById(R.id.monday_bg);
        tuesday_bg=findViewById(R.id.tuesday_bg);
        wednesday_bg=findViewById(R.id.wednesday_bg);
        thursday_bg=findViewById(R.id.thursday_bg);
        friday_bg=findViewById(R.id.friday_bg);
        saturday_bg=findViewById(R.id.saturday_bg);
        sunday_bg=findViewById(R.id.sunday_bg);



        line_monday_cell_1=findViewById(R.id.line_monday_cell_1);
        line_monday_cell_2=findViewById(R.id.line_monday_cell_2);
        line_monday_cell_3=findViewById(R.id.line_monday_cell_3);
        line_monday_cell_4=findViewById(R.id.line_monday_cell_4);
        line_monday_cell_5=findViewById(R.id.line_monday_cell_5);
        line_monday_cell_6=findViewById(R.id.line_monday_cell_6);
        line_monday_cell_7=findViewById(R.id.line_monday_cell_7);
        line_monday_cell_8=findViewById(R.id.line_monday_cell_8);
        line_monday_cell_9=findViewById(R.id.line_monday_cell_9);
        line_monday_cell_10=findViewById(R.id.line_monday_cell_10);
        line_monday_cell_11=findViewById(R.id.line_monday_cell_11);

        line_monday_cell_1_2=findViewById(R.id.line_monday_cell_1_2);
        line_monday_cell_1_2_3=findViewById(R.id.line_monday_cell_1_2_3);
        line_monday_cell_3_4=findViewById(R.id.line_monday_cell_3_4);
        line_monday_cell_1_2_3_4=findViewById(R.id.line_monday_cell_1_2_3_4);
        line_monday_cell_5_6=findViewById(R.id.line_monday_cell_5_6);
        line_monday_cell_5_6_7=findViewById(R.id.line_monday_cell_5_6_7);
        line_monday_cell_7_8=findViewById(R.id.line_monday_cell_7_8);
        line_monday_cell_5_6_7_8=findViewById(R.id.line_monday_cell_5_6_7_8);
        line_monday_cell_9_10=findViewById(R.id.line_monday_cell_9_10);
        line_monday_cell_9_10_11=findViewById(R.id.line_monday_cell_9_10_11);

        line_tuesday_cell_1=findViewById(R.id.line_tuesday_cell_1);
        line_tuesday_cell_2=findViewById(R.id.line_tuesday_cell_2);
        line_tuesday_cell_3=findViewById(R.id.line_tuesday_cell_3);
        line_tuesday_cell_4=findViewById(R.id.line_tuesday_cell_4);
        line_tuesday_cell_5=findViewById(R.id.line_tuesday_cell_5);
        line_tuesday_cell_6=findViewById(R.id.line_tuesday_cell_6);
        line_tuesday_cell_7=findViewById(R.id.line_tuesday_cell_7);
        line_tuesday_cell_8=findViewById(R.id.line_tuesday_cell_8);
        line_tuesday_cell_9=findViewById(R.id.line_tuesday_cell_9);
        line_tuesday_cell_10=findViewById(R.id.line_tuesday_cell_10);
        line_tuesday_cell_11=findViewById(R.id.line_tuesday_cell_11);

        line_tuesday_cell_1_2=findViewById(R.id.line_tuesday_cell_1_2);
        line_tuesday_cell_1_2_3=findViewById(R.id.line_tuesday_cell_1_2_3);
        line_tuesday_cell_3_4=findViewById(R.id.line_tuesday_cell_3_4);
        line_tuesday_cell_1_2_3_4=findViewById(R.id.line_tuesday_cell_1_2_3_4);
        line_tuesday_cell_5_6=findViewById(R.id.line_tuesday_cell_5_6);
        line_tuesday_cell_5_6_7=findViewById(R.id.line_tuesday_cell_5_6_7);
        line_tuesday_cell_7_8=findViewById(R.id.line_tuesday_cell_7_8);
        line_tuesday_cell_5_6_7_8=findViewById(R.id.line_tuesday_cell_5_6_7_8);
        line_tuesday_cell_9_10=findViewById(R.id.line_tuesday_cell_9_10);
        line_tuesday_cell_9_10_11=findViewById(R.id.line_tuesday_cell_9_10_11);

        line_wednesday_cell_1=findViewById(R.id.line_wednesday_cell_1);
        line_wednesday_cell_2=findViewById(R.id.line_wednesday_cell_2);
        line_wednesday_cell_3=findViewById(R.id.line_wednesday_cell_3);
        line_wednesday_cell_4=findViewById(R.id.line_wednesday_cell_4);
        line_wednesday_cell_5=findViewById(R.id.line_wednesday_cell_5);
        line_wednesday_cell_6=findViewById(R.id.line_wednesday_cell_6);
        line_wednesday_cell_7=findViewById(R.id.line_wednesday_cell_7);
        line_wednesday_cell_8=findViewById(R.id.line_wednesday_cell_8);
        line_wednesday_cell_9=findViewById(R.id.line_wednesday_cell_9);
        line_wednesday_cell_10=findViewById(R.id.line_wednesday_cell_10);
        line_wednesday_cell_11=findViewById(R.id.line_wednesday_cell_11);

        line_wednesday_cell_1_2=findViewById(R.id.line_wednesday_cell_1_2);
        line_wednesday_cell_1_2_3=findViewById(R.id.line_wednesday_cell_1_2_3);
        line_wednesday_cell_3_4=findViewById(R.id.line_wednesday_cell_3_4);
        line_wednesday_cell_1_2_3_4=findViewById(R.id.line_wednesday_cell_1_2_3_4);
        line_wednesday_cell_5_6=findViewById(R.id.line_wednesday_cell_5_6);
        line_wednesday_cell_5_6_7=findViewById(R.id.line_wednesday_cell_5_6_7);
        line_wednesday_cell_7_8=findViewById(R.id.line_wednesday_cell_7_8);
        line_wednesday_cell_5_6_7_8=findViewById(R.id.line_wednesday_cell_5_6_7_8);
        line_wednesday_cell_9_10=findViewById(R.id.line_wednesday_cell_9_10);
        line_wednesday_cell_9_10_11=findViewById(R.id.line_wednesday_cell_9_10_11);

        line_thursday_cell_1=findViewById(R.id.line_thursday_cell_1);
        line_thursday_cell_2=findViewById(R.id.line_thursday_cell_2);
        line_thursday_cell_3=findViewById(R.id.line_thursday_cell_3);
        line_thursday_cell_4=findViewById(R.id.line_thursday_cell_4);
        line_thursday_cell_5=findViewById(R.id.line_thursday_cell_5);
        line_thursday_cell_6=findViewById(R.id.line_thursday_cell_6);
        line_thursday_cell_7=findViewById(R.id.line_thursday_cell_7);
        line_thursday_cell_8=findViewById(R.id.line_thursday_cell_8);
        line_thursday_cell_9=findViewById(R.id.line_thursday_cell_9);
        line_thursday_cell_10=findViewById(R.id.line_thursday_cell_10);
        line_thursday_cell_11=findViewById(R.id.line_thursday_cell_11);

        line_thursday_cell_1_2=findViewById(R.id.line_thursday_cell_1_2);
        line_thursday_cell_1_2_3=findViewById(R.id.line_thursday_cell_1_2_3);
        line_thursday_cell_3_4=findViewById(R.id.line_thursday_cell_3_4);
        line_thursday_cell_1_2_3_4=findViewById(R.id.line_thursday_cell_1_2_3_4);
        line_thursday_cell_5_6=findViewById(R.id.line_thursday_cell_5_6);
        line_thursday_cell_5_6_7=findViewById(R.id.line_thursday_cell_5_6_7);
        line_thursday_cell_7_8=findViewById(R.id.line_thursday_cell_7_8);
        line_thursday_cell_5_6_7_8=findViewById(R.id.line_thursday_cell_5_6_7_8);
        line_thursday_cell_9_10=findViewById(R.id.line_thursday_cell_9_10);
        line_thursday_cell_9_10_11=findViewById(R.id.line_thursday_cell_9_10_11);

        line_friday_cell_1=findViewById(R.id.line_friday_cell_1);
        line_friday_cell_2=findViewById(R.id.line_friday_cell_2);
        line_friday_cell_3=findViewById(R.id.line_friday_cell_3);
        line_friday_cell_4=findViewById(R.id.line_friday_cell_4);
        line_friday_cell_5=findViewById(R.id.line_friday_cell_5);
        line_friday_cell_6=findViewById(R.id.line_friday_cell_6);
        line_friday_cell_7=findViewById(R.id.line_friday_cell_7);
        line_friday_cell_8=findViewById(R.id.line_friday_cell_8);
        line_friday_cell_9=findViewById(R.id.line_friday_cell_9);
        line_friday_cell_10=findViewById(R.id.line_friday_cell_10);
        line_friday_cell_11=findViewById(R.id.line_friday_cell_11);

        line_friday_cell_1_2=findViewById(R.id.line_friday_cell_1_2);
        line_friday_cell_1_2_3=findViewById(R.id.line_friday_cell_1_2_3);
        line_friday_cell_3_4=findViewById(R.id.line_friday_cell_3_4);
        line_friday_cell_1_2_3_4=findViewById(R.id.line_friday_cell_1_2_3_4);
        line_friday_cell_5_6=findViewById(R.id.line_friday_cell_5_6);
        line_friday_cell_5_6_7=findViewById(R.id.line_friday_cell_5_6_7);
        line_friday_cell_7_8=findViewById(R.id.line_friday_cell_7_8);
        line_friday_cell_5_6_7_8=findViewById(R.id.line_friday_cell_5_6_7_8);
        line_friday_cell_9_10=findViewById(R.id.line_friday_cell_9_10);
        line_friday_cell_9_10_11=findViewById(R.id.line_friday_cell_9_10_11);

        line_saturday_cell_1=findViewById(R.id.line_saturday_cell_1);
        line_saturday_cell_2=findViewById(R.id.line_saturday_cell_2);
        line_saturday_cell_3=findViewById(R.id.line_saturday_cell_3);
        line_saturday_cell_4=findViewById(R.id.line_saturday_cell_4);
        line_saturday_cell_5=findViewById(R.id.line_saturday_cell_5);
        line_saturday_cell_6=findViewById(R.id.line_saturday_cell_6);
        line_saturday_cell_7=findViewById(R.id.line_saturday_cell_7);
        line_saturday_cell_8=findViewById(R.id.line_saturday_cell_8);
        line_saturday_cell_9=findViewById(R.id.line_saturday_cell_9);
        line_saturday_cell_10=findViewById(R.id.line_saturday_cell_10);
        line_saturday_cell_11=findViewById(R.id.line_saturday_cell_11);

        line_saturday_cell_1_2=findViewById(R.id.line_saturday_cell_1_2);
        line_saturday_cell_1_2_3=findViewById(R.id.line_saturday_cell_1_2_3);
        line_saturday_cell_3_4=findViewById(R.id.line_saturday_cell_3_4);
        line_saturday_cell_1_2_3_4=findViewById(R.id.line_saturday_cell_1_2_3_4);
        line_saturday_cell_5_6=findViewById(R.id.line_saturday_cell_5_6);
        line_saturday_cell_5_6_7=findViewById(R.id.line_saturday_cell_5_6_7);
        line_saturday_cell_7_8=findViewById(R.id.line_saturday_cell_7_8);
        line_saturday_cell_5_6_7_8=findViewById(R.id.line_saturday_cell_5_6_7_8);
        line_saturday_cell_9_10=findViewById(R.id.line_saturday_cell_9_10);
        line_saturday_cell_9_10_11=findViewById(R.id.line_saturday_cell_9_10_11);

        line_sunday_cell_1=findViewById(R.id.line_sunday_cell_1);
        line_sunday_cell_2=findViewById(R.id.line_sunday_cell_2);
        line_sunday_cell_3=findViewById(R.id.line_sunday_cell_3);
        line_sunday_cell_4=findViewById(R.id.line_sunday_cell_4);
        line_sunday_cell_5=findViewById(R.id.line_sunday_cell_5);
        line_sunday_cell_6=findViewById(R.id.line_sunday_cell_6);
        line_sunday_cell_7=findViewById(R.id.line_sunday_cell_7);
        line_sunday_cell_8=findViewById(R.id.line_sunday_cell_8);
        line_sunday_cell_9=findViewById(R.id.line_sunday_cell_9);
        line_sunday_cell_10=findViewById(R.id.line_sunday_cell_10);
        line_sunday_cell_11=findViewById(R.id.line_sunday_cell_11);

        line_sunday_cell_1_2=findViewById(R.id.line_sunday_cell_1_2);
        line_sunday_cell_1_2_3=findViewById(R.id.line_sunday_cell_1_2_3);
        line_sunday_cell_3_4=findViewById(R.id.line_sunday_cell_3_4);
        line_sunday_cell_1_2_3_4=findViewById(R.id.line_sunday_cell_1_2_3_4);
        line_sunday_cell_5_6=findViewById(R.id.line_sunday_cell_5_6);
        line_sunday_cell_5_6_7=findViewById(R.id.line_sunday_cell_5_6_7);
        line_sunday_cell_7_8=findViewById(R.id.line_sunday_cell_7_8);
        line_sunday_cell_5_6_7_8=findViewById(R.id.line_sunday_cell_5_6_7_8);
        line_sunday_cell_9_10=findViewById(R.id.line_sunday_cell_9_10);
        line_sunday_cell_9_10_11=findViewById(R.id.line_sunday_cell_9_10_11);


        //Result Page
        lv_result_query =findViewById(R.id.lv_result_query);
        //TestQuery
        lv_test_schedule=findViewById(R.id.lv_test_schedule);

    }
    private void initTimeTableHint() throws ParseException {




        SimpleDateFormat formatter= new SimpleDateFormat("MM/dd");
        SimpleDateFormat monthFormatter= new SimpleDateFormat("MM");
        Date date = new Date(System.currentTimeMillis());
        System.out.println(formatter.format(date));

        if(selectWeek!=-1&&selectWeek!=nowCurrentWeekNum){

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date afterSelectWeekDate; //取时间
            Date schoolDayDate = df.parse(schoolDay);
            Calendar calendart = new GregorianCalendar();
            calendart.setTime(schoolDayDate);
            int temp=selectWeek-1;
            calendart.add(calendart.DATE,temp*7); //把日期往后增加一天,整数  往后推,负数往前移动
            afterSelectWeekDate=calendart.getTime(); //这个时间就是日期往后推一天的结果
            date=afterSelectWeekDate;
        }


        month.setText(monthFormatter.format(date)+"月");

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        Date after1Date; //取时间
        calendar.add(calendar.DATE,1); //把日期往后增加一天,整数  往后推,负数往前移动
        after1Date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        calendar.setTime(date);

        Date after2Date; //取时间
        calendar.add(calendar.DATE,2); //把日期往后增加一天,整数  往后推,负数往前移动
        after2Date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        calendar.setTime(date);

        Date after3Date; //取时间
        calendar.add(calendar.DATE,3); //把日期往后增加一天,整数  往后推,负数往前移动
        after3Date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        calendar.setTime(date);

        Date after4Date; //取时间
        calendar.add(calendar.DATE,4); //把日期往后增加一天,整数  往后推,负数往前移动
        after4Date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        calendar.setTime(date);

        Date after5Date; //取时间
        calendar.add(calendar.DATE,5); //把日期往后增加一天,整数  往后推,负数往前移动
        after5Date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        calendar.setTime(date);

        Date after6Date; //取时间
        calendar.add(calendar.DATE,6); //把日期往后增加一天,整数  往后推,负数往前移动
        after6Date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        calendar.setTime(date);

        Date before1Date; //取时间
        calendar.add(calendar.DATE,-1); //把日期往后增加一天,整数  往后推,负数往前移动
        before1Date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        calendar.setTime(date);

        Date before2Date; //取时间
        calendar.add(calendar.DATE,-2); //把日期往后增加一天,整数  往后推,负数往前移动
        before2Date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        calendar.setTime(date);

        Date before3Date; //取时间
        calendar.add(calendar.DATE,-3); //把日期往后增加一天,整数  往后推,负数往前移动
        before3Date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        calendar.setTime(date);

        Date before4Date; //取时间
        calendar.add(calendar.DATE,-4); //把日期往后增加一天,整数  往后推,负数往前移动
        before4Date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        calendar.setTime(date);

        Date before5Date; //取时间
        calendar.add(calendar.DATE,-5); //把日期往后增加一天,整数  往后推,负数往前移动
        before5Date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        calendar.setTime(date);

        Date before6Date; //取时间
        calendar.add(calendar.DATE,-6); //把日期往后增加一天,整数  往后推,负数往前移动
        before6Date=calendar.getTime(); //这个时间就是日期往后推一天的结果
        calendar.setTime(date);


        String currentWeek=getWeekOfDate(date);

        int currentWeekInt=0;
        if(selectWeek==-1||selectWeek==nowCurrentWeekNum) {
            if (currentWeek.equals("周一")) {
                monday_bg.setBackground(getResources().getDrawable(R.drawable.radius_bg));
                currentWeekInt = 1;
            }
            if (currentWeek.equals("周二")) {
                tuesday_bg.setBackground(getResources().getDrawable(R.drawable.radius_bg));
                currentWeekInt = 2;
            }
            if (currentWeek.equals("周三")) {
                wednesday_bg.setBackground(getResources().getDrawable(R.drawable.radius_bg));
                currentWeekInt = 3;
            }
            if (currentWeek.equals("周四")) {
                thursday_bg.setBackground(getResources().getDrawable(R.drawable.radius_bg));
                currentWeekInt = 4;
            }
            if (currentWeek.equals("周五")) {
                friday_bg.setBackground(getResources().getDrawable(R.drawable.radius_bg));
                currentWeekInt = 5;
            }
            if (currentWeek.equals("周六")) {
                saturday_bg.setBackground(getResources().getDrawable(R.drawable.radius_bg));
                currentWeekInt = 6;
            }
            if (currentWeek.equals("周日")) {
                sunday_bg.setBackground(getResources().getDrawable(R.drawable.radius_bg));
                currentWeekInt = 7;
            }
        }else{
            monday_bg.setBackgroundColor(getResources().getColor(R.color.table_bg));
            tuesday_bg.setBackgroundColor(getResources().getColor(R.color.table_bg));
            wednesday_bg.setBackgroundColor(getResources().getColor(R.color.table_bg));
            thursday_bg.setBackgroundColor(getResources().getColor(R.color.table_bg));
            friday_bg.setBackgroundColor(getResources().getColor(R.color.table_bg));
            saturday_bg.setBackgroundColor(getResources().getColor(R.color.table_bg));
            sunday_bg.setBackgroundColor(getResources().getColor(R.color.table_bg));
        }


        if (currentWeek.equals("周一")) {
            currentWeekInt = 1;
        }
        if (currentWeek.equals("周二")) {
            currentWeekInt = 2;
        }
        if (currentWeek.equals("周三")) {
            currentWeekInt = 3;
        }
        if (currentWeek.equals("周四")) {
            currentWeekInt = 4;
        }
        if (currentWeek.equals("周五")) {
            currentWeekInt = 5;
        }
        if (currentWeek.equals("周六")) {
            currentWeekInt = 6;
        }
        if (currentWeek.equals("周日")) {
            currentWeekInt = 7;
        }


        if(currentWeekInt==1){

            line_monday_cell_hint_day.setText(formatter.format(date));
            line_tuesday_cell_hint_day.setText(formatter.format(after1Date));
            line_wednesday_cell_hint_day.setText(formatter.format(after2Date));
            line_thursday_cell_hint_day.setText(formatter.format(after3Date));
            line_friday_cell_hint_day.setText(formatter.format(after4Date));
            line_saturday_cell_hint_day.setText(formatter.format(after5Date));
            line_sunday_cell_hint_day.setText(formatter.format(after6Date));

        }

        if(currentWeekInt==2){

            line_monday_cell_hint_day.setText(formatter.format(before1Date));
            line_tuesday_cell_hint_day.setText(formatter.format(date));
            line_wednesday_cell_hint_day.setText(formatter.format(after1Date));
            line_thursday_cell_hint_day.setText(formatter.format(after2Date));
            line_friday_cell_hint_day.setText(formatter.format(after3Date));
            line_saturday_cell_hint_day.setText(formatter.format(after4Date));
            line_sunday_cell_hint_day.setText(formatter.format(after5Date));

        }

        if(currentWeekInt==3){

            line_monday_cell_hint_day.setText(formatter.format(before2Date));
            line_tuesday_cell_hint_day.setText(formatter.format(before1Date));
            line_wednesday_cell_hint_day.setText(formatter.format(date));
            line_thursday_cell_hint_day.setText(formatter.format(after1Date));
            line_friday_cell_hint_day.setText(formatter.format(after2Date));
            line_saturday_cell_hint_day.setText(formatter.format(after3Date));
            line_sunday_cell_hint_day.setText(formatter.format(after4Date));

        }

        if(currentWeekInt==4){

            line_monday_cell_hint_day.setText(formatter.format(before3Date));
            line_tuesday_cell_hint_day.setText(formatter.format(before2Date));
            line_wednesday_cell_hint_day.setText(formatter.format(before1Date));
            line_thursday_cell_hint_day.setText(formatter.format(date));
            line_friday_cell_hint_day.setText(formatter.format(after1Date));
            line_saturday_cell_hint_day.setText(formatter.format(after2Date));
            line_sunday_cell_hint_day.setText(formatter.format(after3Date));

        }

        if(currentWeekInt==5){

            line_monday_cell_hint_day.setText(formatter.format(before4Date));
            line_tuesday_cell_hint_day.setText(formatter.format(before3Date));
            line_wednesday_cell_hint_day.setText(formatter.format(before2Date));
            line_thursday_cell_hint_day.setText(formatter.format(before1Date));
            line_friday_cell_hint_day.setText(formatter.format(date));
            line_saturday_cell_hint_day.setText(formatter.format(after1Date));
            line_sunday_cell_hint_day.setText(formatter.format(after2Date));

        }
        if(currentWeekInt==6){

            line_monday_cell_hint_day.setText(formatter.format(before5Date));
            line_tuesday_cell_hint_day.setText(formatter.format(before4Date));
            line_wednesday_cell_hint_day.setText(formatter.format(before3Date));
            line_thursday_cell_hint_day.setText(formatter.format(before2Date));
            line_friday_cell_hint_day.setText(formatter.format(before1Date));
            line_saturday_cell_hint_day.setText(formatter.format(date));
            line_sunday_cell_hint_day.setText(formatter.format(after1Date));

        }

        if(currentWeekInt==7){

            line_monday_cell_hint_day.setText(formatter.format(before6Date));
            line_tuesday_cell_hint_day.setText(formatter.format(before5Date));
            line_wednesday_cell_hint_day.setText(formatter.format(before4Date));
            line_thursday_cell_hint_day.setText(formatter.format(before3Date));
            line_friday_cell_hint_day.setText(formatter.format(before2Date));
            line_saturday_cell_hint_day.setText(formatter.format(before1Date));
            line_sunday_cell_hint_day.setText(formatter.format(date));

        }
    }
    private void initTimeTable() throws ParseException {
        List<TimeBean> timeBeans=new ArrayList<>();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Date today = new Date(System.currentTimeMillis());
        Date schoolDayDate = df.parse(schoolDay);

        int currentWeekNum=countTwoDayWeek(schoolDayDate,today);
        if(selectWeek==-1) {
            nowCurrentWeekNum = currentWeekNum;
        }
        if(selectWeek!=-1&&selectWeek!=nowCurrentWeekNum){
            currentWeekNum=selectWeek;
        }

        spinnerView_radioDialog1.setSelectedIndexAndText(currentWeekNum-1);//更改下拉菜单选中的列表项下标值


        for (int i=0;i<ListSaver.getTimeTableBeanList().size();i++){

            ListSaver.getTimeTableBeanList().get(i).getDay();
            switch (ListSaver.getTimeTableBeanList().get(i).getDay()){
                case "周一":tempWeek=1;
                    break;
                case "周二":tempWeek=2;
                    break;
                case "周三":tempWeek=3;
                    break;
                case "周四":tempWeek=4;
                    break;
                case "周五":tempWeek=5;
                    break;
                case "周六":tempWeek=6;
                    break;
                case "周日":tempWeek=7;
                    break;
            }

            final TimeTableBean clickPassBean=ListSaver.getTimeTableBeanList().get(i);

            showString=ListSaver.getTimeTableBeanList().get(i).getcName();
            ListSaver.getTimeTableBeanList().get(i).getTeacherName();

            tempStartTime=Integer.valueOf(ListSaver.getTimeTableBeanList().get(i).getStartNum());
            tempEndTime=Integer.valueOf(ListSaver.getTimeTableBeanList().get(i).getEndNum());




            timeBeans=ListSaver.getTimeTableBeanList().get(i).getTime();
            for(int a=0;a<timeBeans.size();a++){

                if(Integer.valueOf(timeBeans.get(a).getStartweek())<=currentWeekNum&&Integer.valueOf(timeBeans.get(a).getEndweek())>=currentWeekNum){
                    showString="@"+timeBeans.get(a).getClassroom()+"\n"+showString;
                    notThisWeek=false;
                    break;
                }else {

                    notThisWeek=true;

                }

            }
            if(notThisWeek){
                showString = "[非本周]" + showString;
            }
//monday
            if(tempWeek==1&&tempStartTime==1){
                if(tempEndTime==1) {
                    line_monday_cell_1.setText(showString);
                    if(notThisWeek) {
                        line_monday_cell_1.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_monday_cell_1.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_monday_cell_1.setTextColor(getResources().getColor(R.color.white));
                        line_monday_cell_1.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_monday_cell_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==2){
                    line_monday_cell_1.setVisibility(View.GONE);
                    line_monday_cell_2.setVisibility(View.GONE);
                    line_monday_cell_1_2.setVisibility(View.VISIBLE);
                    line_monday_cell_1_2.setText(showString);
                    if(notThisWeek) {
                        line_monday_cell_1_2.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_monday_cell_1_2.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_monday_cell_1_2.setTextColor(getResources().getColor(R.color.white));
                        line_monday_cell_1_2.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_monday_cell_1_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==3){
                    line_monday_cell_1.setVisibility(View.GONE);
                    line_monday_cell_2.setVisibility(View.GONE);
                    line_monday_cell_3.setVisibility(View.GONE);
                    line_monday_cell_1_2_3.setVisibility(View.VISIBLE);
                    line_monday_cell_1_2_3.setText(showString);
                    if(notThisWeek) {
                        line_monday_cell_1_2_3.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_monday_cell_1_2_3.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_monday_cell_1_2_3.setTextColor(getResources().getColor(R.color.white));
                        line_monday_cell_1_2_3.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_monday_cell_1_2_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
                if(tempEndTime==4){
                    line_monday_cell_1.setVisibility(View.GONE);
                    line_monday_cell_2.setVisibility(View.GONE);
                    line_monday_cell_3.setVisibility(View.GONE);
                    line_monday_cell_4.setVisibility(View.GONE);
                    if(notThisWeek) {
                        line_monday_cell_1_2_3_4.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_monday_cell_1_2_3_4.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_monday_cell_1_2_3_4.setTextColor(getResources().getColor(R.color.white));
                        line_monday_cell_1_2_3_4.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_monday_cell_1_2_3_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
            }
            if(tempWeek==1&&tempStartTime==3){
                if(tempEndTime==3){
                    line_monday_cell_3.setText(showString);
                    if(notThisWeek) {
                        line_monday_cell_3.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_monday_cell_3.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_monday_cell_3.setTextColor(getResources().getColor(R.color.white));
                        line_monday_cell_3.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_monday_cell_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==4){
                    line_monday_cell_3.setVisibility(View.GONE);
                    line_monday_cell_4.setVisibility(View.GONE);

                    line_monday_cell_3_4.setText(showString);
                    line_monday_cell_3_4.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_monday_cell_3_4.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_monday_cell_3_4.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_monday_cell_3_4.setTextColor(getResources().getColor(R.color.white));
                        line_monday_cell_3_4.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_monday_cell_3_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
            }
            if(tempWeek==1&&tempStartTime==5){
                if(tempEndTime==5){
                    line_monday_cell_5.setText(showString);
                    if(notThisWeek) {
                        line_monday_cell_5.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_monday_cell_5.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_monday_cell_5.setTextColor(getResources().getColor(R.color.white));
                        line_monday_cell_5.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_monday_cell_5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }


                if(tempEndTime==6){
                    line_monday_cell_5.setVisibility(View.GONE);
                    line_monday_cell_6.setVisibility(View.GONE);
                    line_monday_cell_5_6.setText(showString);
                    line_monday_cell_5_6.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_monday_cell_5_6.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_monday_cell_5_6.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_monday_cell_5_6.setTextColor(getResources().getColor(R.color.white));
                        line_monday_cell_5_6.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_monday_cell_5_6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
                if(tempEndTime==7){
                    line_monday_cell_5.setVisibility(View.GONE);
                    line_monday_cell_6.setVisibility(View.GONE);
                    line_monday_cell_7.setVisibility(View.GONE);
                    line_monday_cell_5_6_7.setText(showString);
                    line_monday_cell_5_6_7.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_monday_cell_5_6_7.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_monday_cell_5_6_7.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_monday_cell_5_6_7.setTextColor(getResources().getColor(R.color.white));
                        line_monday_cell_5_6_7.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_monday_cell_5_6_7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==8){
                    line_monday_cell_5.setVisibility(View.GONE);
                    line_monday_cell_6.setVisibility(View.GONE);
                    line_monday_cell_7.setVisibility(View.GONE);
                    line_monday_cell_8.setVisibility(View.GONE);
                    line_monday_cell_5_6_7_8.setText(showString);
                    line_monday_cell_5_6_7_8.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_monday_cell_5_6_7_8.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_monday_cell_5_6_7_8.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_monday_cell_5_6_7_8.setTextColor(getResources().getColor(R.color.white));
                        line_monday_cell_5_6_7_8.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_monday_cell_5_6_7_8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
            }
            if(tempWeek==1&&tempStartTime==7){
                if(tempEndTime==7){
                    line_monday_cell_7.setText(showString);
                    if(notThisWeek) {
                        line_monday_cell_7.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_monday_cell_7.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_monday_cell_7.setTextColor(getResources().getColor(R.color.white));
                        line_monday_cell_7.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_monday_cell_7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==8){
                    line_monday_cell_7.setVisibility(View.GONE);
                    line_monday_cell_8.setVisibility(View.GONE);
                    line_monday_cell_7_8.setText(showString);
                    line_monday_cell_7_8.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_monday_cell_7_8.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_monday_cell_7_8.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_monday_cell_7_8.setTextColor(getResources().getColor(R.color.white));
                        line_monday_cell_7_8.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_monday_cell_7_8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }

            }
            if(tempWeek==1&&tempStartTime==9){
                if(tempEndTime==9){
                    line_monday_cell_9.setText(showString);
                    if(notThisWeek) {
                        line_monday_cell_9.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_monday_cell_9.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_monday_cell_9.setTextColor(getResources().getColor(R.color.white));
                        line_monday_cell_9.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_monday_cell_9.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==10){
                    line_monday_cell_9.setVisibility(View.GONE);
                    line_monday_cell_10.setVisibility(View.GONE);
                    line_monday_cell_9_10.setVisibility(View.VISIBLE);
                    line_monday_cell_9_10.setText(showString);
                    if(notThisWeek) {
                        line_monday_cell_9_10.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_monday_cell_9_10.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_monday_cell_9_10.setTextColor(getResources().getColor(R.color.white));
                        line_monday_cell_9_10.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_monday_cell_9_10.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==11){
                    line_monday_cell_9.setVisibility(View.GONE);
                    line_monday_cell_10.setVisibility(View.GONE);
                    line_monday_cell_11.setVisibility(View.GONE);
                    line_monday_cell_9_10_11.setVisibility(View.VISIBLE);
                    line_monday_cell_9_10_11.setText(showString);
                    if(notThisWeek) {
                        line_monday_cell_9_10_11.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_monday_cell_9_10_11.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_monday_cell_9_10_11.setTextColor(getResources().getColor(R.color.white));
                        line_monday_cell_9_10_11.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_monday_cell_9_10_11.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
            }
//tuesday
            if(tempWeek==2&&tempStartTime==1){
                if(tempEndTime==1) {
                    line_tuesday_cell_1.setText(showString);
                    if(notThisWeek) {
                        line_tuesday_cell_1.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_tuesday_cell_1.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_tuesday_cell_1.setTextColor(getResources().getColor(R.color.white));
                        line_tuesday_cell_1.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_tuesday_cell_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==2){
                    line_tuesday_cell_1.setVisibility(View.GONE);
                    line_tuesday_cell_2.setVisibility(View.GONE);
                    line_tuesday_cell_1_2.setVisibility(View.VISIBLE);
                    line_tuesday_cell_1_2.setText(showString);
                    if(notThisWeek) {
                        line_tuesday_cell_1_2.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_tuesday_cell_1_2.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_tuesday_cell_1_2.setTextColor(getResources().getColor(R.color.white));
                        line_tuesday_cell_1_2.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_tuesday_cell_1_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==3){
                    line_tuesday_cell_1.setVisibility(View.GONE);
                    line_tuesday_cell_2.setVisibility(View.GONE);
                    line_tuesday_cell_3.setVisibility(View.GONE);
                    line_tuesday_cell_1_2_3.setVisibility(View.VISIBLE);
                    line_tuesday_cell_1_2_3.setText(showString);
                    if(notThisWeek) {
                        line_tuesday_cell_1_2_3.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_tuesday_cell_1_2_3.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_tuesday_cell_1_2_3.setTextColor(getResources().getColor(R.color.white));
                        line_tuesday_cell_1_2_3.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_tuesday_cell_1_2_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
                if(tempEndTime==4){
                    line_tuesday_cell_1.setVisibility(View.GONE);
                    line_tuesday_cell_2.setVisibility(View.GONE);
                    line_tuesday_cell_3.setVisibility(View.GONE);
                    line_tuesday_cell_4.setVisibility(View.GONE);
                    if(notThisWeek) {
                        line_tuesday_cell_1_2_3_4.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_tuesday_cell_1_2_3_4.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_tuesday_cell_1_2_3_4.setTextColor(getResources().getColor(R.color.white));
                        line_tuesday_cell_1_2_3_4.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_tuesday_cell_1_2_3_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
            }
            if(tempWeek==2&&tempStartTime==3){
                if(tempEndTime==3){
                    line_tuesday_cell_3.setText(showString);
                    if(notThisWeek) {
                        line_tuesday_cell_3.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_tuesday_cell_3.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_tuesday_cell_3.setTextColor(getResources().getColor(R.color.white));
                        line_tuesday_cell_3.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_tuesday_cell_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==4){
                    line_tuesday_cell_3.setVisibility(View.GONE);
                    line_tuesday_cell_4.setVisibility(View.GONE);

                    line_tuesday_cell_3_4.setText(showString);
                    line_tuesday_cell_3_4.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_tuesday_cell_3_4.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_tuesday_cell_3_4.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_tuesday_cell_3_4.setTextColor(getResources().getColor(R.color.white));
                        line_tuesday_cell_3_4.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_tuesday_cell_3_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
            }
            if(tempWeek==2&&tempStartTime==5){
                if(tempEndTime==5){
                    line_tuesday_cell_5.setText(showString);
                    if(notThisWeek) {
                        line_tuesday_cell_5.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_tuesday_cell_5.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_tuesday_cell_5.setTextColor(getResources().getColor(R.color.white));
                        line_tuesday_cell_5.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_tuesday_cell_5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }


                if(tempEndTime==6){
                    line_tuesday_cell_5.setVisibility(View.GONE);
                    line_tuesday_cell_6.setVisibility(View.GONE);
                    line_tuesday_cell_5_6.setText(showString);
                    line_tuesday_cell_5_6.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_tuesday_cell_5_6.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_tuesday_cell_5_6.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_tuesday_cell_5_6.setTextColor(getResources().getColor(R.color.white));
                        line_tuesday_cell_5_6.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_tuesday_cell_5_6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
                if(tempEndTime==7){
                    line_tuesday_cell_5.setVisibility(View.GONE);
                    line_tuesday_cell_6.setVisibility(View.GONE);
                    line_tuesday_cell_7.setVisibility(View.GONE);
                    line_tuesday_cell_5_6_7.setText(showString);
                    line_tuesday_cell_5_6_7.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_tuesday_cell_5_6_7.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_tuesday_cell_5_6_7.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_tuesday_cell_5_6_7.setTextColor(getResources().getColor(R.color.white));
                        line_tuesday_cell_5_6_7.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_tuesday_cell_5_6_7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
                if(tempEndTime==8){
                    line_tuesday_cell_5.setVisibility(View.GONE);
                    line_tuesday_cell_6.setVisibility(View.GONE);
                    line_tuesday_cell_7.setVisibility(View.GONE);
                    line_tuesday_cell_8.setVisibility(View.GONE);
                    line_tuesday_cell_5_6_7_8.setText(showString);
                    line_tuesday_cell_5_6_7_8.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_tuesday_cell_5_6_7_8.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_tuesday_cell_5_6_7_8.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_tuesday_cell_5_6_7_8.setTextColor(getResources().getColor(R.color.white));
                        line_tuesday_cell_5_6_7_8.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_tuesday_cell_5_6_7_8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
            }
            if(tempWeek==2&&tempStartTime==7){
                if(tempEndTime==7){
                    line_tuesday_cell_7.setText(showString);
                    if(notThisWeek) {
                        line_tuesday_cell_7.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_tuesday_cell_7.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_tuesday_cell_7.setTextColor(getResources().getColor(R.color.white));
                        line_tuesday_cell_7.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_tuesday_cell_7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==8){
                    line_tuesday_cell_7.setVisibility(View.GONE);
                    line_tuesday_cell_8.setVisibility(View.GONE);
                    line_tuesday_cell_7_8.setText(showString);
                    line_tuesday_cell_7_8.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_tuesday_cell_7_8.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_tuesday_cell_7_8.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_tuesday_cell_7_8.setTextColor(getResources().getColor(R.color.white));
                        line_tuesday_cell_7_8.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_tuesday_cell_7_8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }

            }
            if(tempWeek==2&&tempStartTime==9){
                if(tempEndTime==9){
                    line_tuesday_cell_9.setText(showString);
                    if(notThisWeek) {
                        line_tuesday_cell_9.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_tuesday_cell_9.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_tuesday_cell_9.setTextColor(getResources().getColor(R.color.white));
                        line_tuesday_cell_9.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_tuesday_cell_9.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==10){
                    line_tuesday_cell_9.setVisibility(View.GONE);
                    line_tuesday_cell_10.setVisibility(View.GONE);
                    line_tuesday_cell_9_10.setVisibility(View.VISIBLE);
                    line_tuesday_cell_9_10.setText(showString);
                    if(notThisWeek) {
                        line_tuesday_cell_9_10.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_tuesday_cell_9_10.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_tuesday_cell_9_10.setTextColor(getResources().getColor(R.color.white));
                        line_tuesday_cell_9_10.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_tuesday_cell_9_10.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==11){
                    line_tuesday_cell_9.setVisibility(View.GONE);
                    line_tuesday_cell_10.setVisibility(View.GONE);
                    line_tuesday_cell_11.setVisibility(View.GONE);
                    line_tuesday_cell_9_10_11.setVisibility(View.VISIBLE);
                    line_tuesday_cell_9_10_11.setText(showString);
                    if(notThisWeek) {
                        line_tuesday_cell_9_10_11.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_tuesday_cell_9_10_11.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_tuesday_cell_9_10_11.setTextColor(getResources().getColor(R.color.white));
                        line_tuesday_cell_9_10_11.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_tuesday_cell_9_10_11.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
            }
//wednesday
            if(tempWeek==3&&tempStartTime==1){
                if(tempEndTime==1) {
                    line_wednesday_cell_1.setText(showString);
                    if(notThisWeek) {
                        line_wednesday_cell_1.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_wednesday_cell_1.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_wednesday_cell_1.setTextColor(getResources().getColor(R.color.white));
                        line_wednesday_cell_1.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_wednesday_cell_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==2){
                    line_wednesday_cell_1.setVisibility(View.GONE);
                    line_wednesday_cell_2.setVisibility(View.GONE);
                    line_wednesday_cell_1_2.setVisibility(View.VISIBLE);
                    line_wednesday_cell_1_2.setText(showString);
                    if(notThisWeek) {
                        line_wednesday_cell_1_2.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_wednesday_cell_1_2.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_wednesday_cell_1_2.setTextColor(getResources().getColor(R.color.white));
                        line_wednesday_cell_1_2.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_wednesday_cell_1_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }

                if(tempEndTime==3){
                    line_wednesday_cell_1.setVisibility(View.GONE);
                    line_wednesday_cell_2.setVisibility(View.GONE);
                    line_wednesday_cell_3.setVisibility(View.GONE);
                    line_wednesday_cell_1_2_3.setVisibility(View.VISIBLE);
                    line_wednesday_cell_1_2_3.setText(showString);
                    if(notThisWeek) {
                        line_wednesday_cell_1_2_3.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_wednesday_cell_1_2_3.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_wednesday_cell_1_2_3.setTextColor(getResources().getColor(R.color.white));
                        line_wednesday_cell_1_2_3.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_wednesday_cell_1_2_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
                if(tempEndTime==4){
                    line_wednesday_cell_1.setVisibility(View.GONE);
                    line_wednesday_cell_2.setVisibility(View.GONE);
                    line_wednesday_cell_3.setVisibility(View.GONE);
                    line_wednesday_cell_4.setVisibility(View.GONE);
                    if(notThisWeek) {
                        line_wednesday_cell_1_2_3_4.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_wednesday_cell_1_2_3_4.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_wednesday_cell_1_2_3_4.setTextColor(getResources().getColor(R.color.white));
                        line_wednesday_cell_1_2_3_4.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_wednesday_cell_1_2_3_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
            }
            if(tempWeek==3&&tempStartTime==3){
                if(tempEndTime==3){
                    line_wednesday_cell_3.setText(showString);
                    if(notThisWeek) {
                        line_wednesday_cell_3.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_wednesday_cell_3.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_wednesday_cell_3.setTextColor(getResources().getColor(R.color.white));
                        line_wednesday_cell_3.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_wednesday_cell_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==4){
                    line_wednesday_cell_3.setVisibility(View.GONE);
                    line_wednesday_cell_4.setVisibility(View.GONE);

                    line_wednesday_cell_3_4.setText(showString);
                    line_wednesday_cell_3_4.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_wednesday_cell_3_4.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_wednesday_cell_3_4.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_wednesday_cell_3_4.setTextColor(getResources().getColor(R.color.white));
                        line_wednesday_cell_3_4.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_wednesday_cell_3_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
            }
            if(tempWeek==3&&tempStartTime==5){
                if(tempEndTime==5){
                    line_wednesday_cell_5.setText(showString);
                    if(notThisWeek) {
                        line_wednesday_cell_5.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_wednesday_cell_5.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_wednesday_cell_5.setTextColor(getResources().getColor(R.color.white));
                        line_wednesday_cell_5.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_wednesday_cell_5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }


                if(tempEndTime==6){
                    line_wednesday_cell_5.setVisibility(View.GONE);
                    line_wednesday_cell_6.setVisibility(View.GONE);
                    line_wednesday_cell_5_6.setText(showString);
                    line_wednesday_cell_5_6.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_wednesday_cell_5_6.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_wednesday_cell_5_6.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_wednesday_cell_5_6.setTextColor(getResources().getColor(R.color.white));
                        line_wednesday_cell_5_6.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_wednesday_cell_5_6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
                if(tempEndTime==7){
                    line_wednesday_cell_5.setVisibility(View.GONE);
                    line_wednesday_cell_6.setVisibility(View.GONE);
                    line_wednesday_cell_7.setVisibility(View.GONE);
                    line_wednesday_cell_5_6_7.setText(showString);
                    line_wednesday_cell_5_6_7.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_wednesday_cell_5_6_7.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_wednesday_cell_5_6_7.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_wednesday_cell_5_6_7.setTextColor(getResources().getColor(R.color.white));
                        line_wednesday_cell_5_6_7.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_wednesday_cell_5_6_7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==8){
                    line_wednesday_cell_5.setVisibility(View.GONE);
                    line_wednesday_cell_6.setVisibility(View.GONE);
                    line_wednesday_cell_7.setVisibility(View.GONE);
                    line_wednesday_cell_8.setVisibility(View.GONE);
                    line_wednesday_cell_5_6_7_8.setText(showString);
                    line_wednesday_cell_5_6_7_8.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_wednesday_cell_5_6_7_8.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_wednesday_cell_5_6_7_8.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_wednesday_cell_5_6_7_8.setTextColor(getResources().getColor(R.color.white));
                        line_wednesday_cell_5_6_7_8.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_wednesday_cell_5_6_7_8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
            }
            if(tempWeek==3&&tempStartTime==7){
                if(tempEndTime==7){
                    line_wednesday_cell_7.setText(showString);
                    if(notThisWeek) {
                        line_wednesday_cell_7.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_wednesday_cell_7.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_wednesday_cell_7.setTextColor(getResources().getColor(R.color.white));
                        line_wednesday_cell_7.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_wednesday_cell_7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==8){
                    line_wednesday_cell_7.setVisibility(View.GONE);
                    line_wednesday_cell_8.setVisibility(View.GONE);
                    line_wednesday_cell_7_8.setText(showString);
                    line_wednesday_cell_7_8.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_wednesday_cell_7_8.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_wednesday_cell_7_8.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_wednesday_cell_7_8.setTextColor(getResources().getColor(R.color.white));
                        line_wednesday_cell_7_8.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_wednesday_cell_7_8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }

            }
            if(tempWeek==3&&tempStartTime==9){
                if(tempEndTime==9){
                    line_wednesday_cell_9.setText(showString);
                    if(notThisWeek) {
                        line_wednesday_cell_9.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_wednesday_cell_9.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_wednesday_cell_9.setTextColor(getResources().getColor(R.color.white));
                        line_wednesday_cell_9.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_wednesday_cell_9.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==10){
                    line_wednesday_cell_9.setVisibility(View.GONE);
                    line_wednesday_cell_10.setVisibility(View.GONE);
                    line_wednesday_cell_9_10.setVisibility(View.VISIBLE);
                    line_wednesday_cell_9_10.setText(showString);
                    if(notThisWeek) {
                        line_wednesday_cell_9_10.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_wednesday_cell_9_10.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_wednesday_cell_9_10.setTextColor(getResources().getColor(R.color.white));
                        line_wednesday_cell_9_10.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_wednesday_cell_9_10.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==11){
                    line_wednesday_cell_9.setVisibility(View.GONE);
                    line_wednesday_cell_10.setVisibility(View.GONE);
                    line_wednesday_cell_11.setVisibility(View.GONE);
                    line_wednesday_cell_9_10_11.setVisibility(View.VISIBLE);
                    line_wednesday_cell_9_10_11.setText(showString);
                    if(notThisWeek) {
                        line_wednesday_cell_9_10_11.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_wednesday_cell_9_10_11.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_wednesday_cell_9_10_11.setTextColor(getResources().getColor(R.color.white));
                        line_wednesday_cell_9_10_11.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_wednesday_cell_9_10_11.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
            }
//thursday
            if(tempWeek==4&&tempStartTime==1){
                if(tempEndTime==1) {
                    line_thursday_cell_1.setText(showString);
                    if(notThisWeek) {
                        line_thursday_cell_1.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_thursday_cell_1.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_thursday_cell_1.setTextColor(getResources().getColor(R.color.white));
                        line_thursday_cell_1.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_thursday_cell_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==2){
                    line_thursday_cell_1.setVisibility(View.GONE);
                    line_thursday_cell_2.setVisibility(View.GONE);
                    line_thursday_cell_1_2.setVisibility(View.VISIBLE);
                    line_thursday_cell_1_2.setText(showString);
                    if(notThisWeek) {
                        line_thursday_cell_1_2.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_thursday_cell_1_2.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_thursday_cell_1_2.setTextColor(getResources().getColor(R.color.white));
                        line_thursday_cell_1_2.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_thursday_cell_1_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==3){
                    line_thursday_cell_1.setVisibility(View.GONE);
                    line_thursday_cell_2.setVisibility(View.GONE);
                    line_thursday_cell_3.setVisibility(View.GONE);
                    line_thursday_cell_1_2_3.setVisibility(View.VISIBLE);
                    line_thursday_cell_1_2_3.setText(showString);
                    if(notThisWeek) {
                        line_thursday_cell_1_2_3.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_thursday_cell_1_2_3.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_thursday_cell_1_2_3.setTextColor(getResources().getColor(R.color.white));
                        line_thursday_cell_1_2_3.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_thursday_cell_1_2_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
                if(tempEndTime==4){
                    line_thursday_cell_1.setVisibility(View.GONE);
                    line_thursday_cell_2.setVisibility(View.GONE);
                    line_thursday_cell_3.setVisibility(View.GONE);
                    line_thursday_cell_4.setVisibility(View.GONE);
                    if(notThisWeek) {
                        line_thursday_cell_1_2_3_4.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_thursday_cell_1_2_3_4.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_thursday_cell_1_2_3_4.setTextColor(getResources().getColor(R.color.white));
                        line_thursday_cell_1_2_3_4.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_thursday_cell_1_2_3_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
            }
            if(tempWeek==4&&tempStartTime==3){
                if(tempEndTime==3){
                    line_thursday_cell_3.setText(showString);
                    if(notThisWeek) {
                        line_thursday_cell_3.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_thursday_cell_3.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_thursday_cell_3.setTextColor(getResources().getColor(R.color.white));
                        line_thursday_cell_3.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_thursday_cell_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==4){
                    line_thursday_cell_3.setVisibility(View.GONE);
                    line_thursday_cell_4.setVisibility(View.GONE);

                    line_thursday_cell_3_4.setText(showString);
                    line_thursday_cell_3_4.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_thursday_cell_3_4.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_thursday_cell_3_4.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_thursday_cell_3_4.setTextColor(getResources().getColor(R.color.white));
                        line_thursday_cell_3_4.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_thursday_cell_3_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
            }
            if(tempWeek==4&&tempStartTime==5){
                if(tempEndTime==5){
                    line_thursday_cell_5.setText(showString);
                    if(notThisWeek) {
                        line_thursday_cell_5.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_thursday_cell_5.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_thursday_cell_5.setTextColor(getResources().getColor(R.color.white));
                        line_thursday_cell_5.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_thursday_cell_5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }


                if(tempEndTime==6){
                    line_thursday_cell_5.setVisibility(View.GONE);
                    line_thursday_cell_6.setVisibility(View.GONE);
                    line_thursday_cell_5_6.setText(showString);
                    line_thursday_cell_5_6.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_thursday_cell_5_6.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_thursday_cell_5_6.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_thursday_cell_5_6.setTextColor(getResources().getColor(R.color.white));
                        line_thursday_cell_5_6.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_thursday_cell_5_6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
                if(tempEndTime==7){
                    line_thursday_cell_5.setVisibility(View.GONE);
                    line_thursday_cell_6.setVisibility(View.GONE);
                    line_thursday_cell_7.setVisibility(View.GONE);
                    line_thursday_cell_5_6_7.setText(showString);
                    line_thursday_cell_5_6_7.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_thursday_cell_5_6_7.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_thursday_cell_5_6_7.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_thursday_cell_5_6_7.setTextColor(getResources().getColor(R.color.white));
                        line_thursday_cell_5_6_7.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_thursday_cell_5_6_7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==8){
                    line_thursday_cell_5.setVisibility(View.GONE);
                    line_thursday_cell_6.setVisibility(View.GONE);
                    line_thursday_cell_7.setVisibility(View.GONE);
                    line_thursday_cell_8.setVisibility(View.GONE);
                    line_thursday_cell_5_6_7_8.setText(showString);
                    line_thursday_cell_5_6_7_8.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_thursday_cell_5_6_7_8.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_thursday_cell_5_6_7_8.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_thursday_cell_5_6_7_8.setTextColor(getResources().getColor(R.color.white));
                        line_thursday_cell_5_6_7_8.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_thursday_cell_5_6_7_8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });


                }

            }
            if(tempWeek==4&&tempStartTime==7){
                if(tempEndTime==7){
                    line_thursday_cell_7.setText(showString);
                    if(notThisWeek) {
                        line_thursday_cell_7.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_thursday_cell_7.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_thursday_cell_7.setTextColor(getResources().getColor(R.color.white));
                        line_thursday_cell_7.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_thursday_cell_7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==8){
                    line_thursday_cell_7.setVisibility(View.GONE);
                    line_thursday_cell_8.setVisibility(View.GONE);
                    line_thursday_cell_7_8.setText(showString);
                    line_thursday_cell_7_8.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_thursday_cell_7_8.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_thursday_cell_7_8.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_thursday_cell_7_8.setTextColor(getResources().getColor(R.color.white));
                        line_thursday_cell_7_8.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_thursday_cell_7_8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }

            }
            if(tempWeek==4&&tempStartTime==9){
                if(tempEndTime==9){
                    line_thursday_cell_9.setText(showString);
                    if(notThisWeek) {
                        line_thursday_cell_9.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_thursday_cell_9.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_thursday_cell_9.setTextColor(getResources().getColor(R.color.white));
                        line_thursday_cell_9.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_thursday_cell_9.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==10){
                    line_thursday_cell_9.setVisibility(View.GONE);
                    line_thursday_cell_10.setVisibility(View.GONE);
                    line_thursday_cell_9_10.setVisibility(View.VISIBLE);
                    line_thursday_cell_9_10.setText(showString);
                    if(notThisWeek) {
                        line_thursday_cell_9_10.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_thursday_cell_9_10.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_thursday_cell_9_10.setTextColor(getResources().getColor(R.color.white));
                        line_thursday_cell_9_10.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_thursday_cell_9_10.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==11){
                    line_thursday_cell_9.setVisibility(View.GONE);
                    line_thursday_cell_10.setVisibility(View.GONE);
                    line_thursday_cell_11.setVisibility(View.GONE);
                    line_thursday_cell_9_10_11.setVisibility(View.VISIBLE);
                    line_thursday_cell_9_10_11.setText(showString);
                    if(notThisWeek) {
                        line_thursday_cell_9_10_11.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_thursday_cell_9_10_11.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_thursday_cell_9_10_11.setTextColor(getResources().getColor(R.color.white));
                        line_thursday_cell_9_10_11.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_thursday_cell_9_10_11.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
            }
//friday
            if(tempWeek==5&&tempStartTime==1){
                if(tempEndTime==1) {
                    line_friday_cell_1.setText(showString);
                    if(notThisWeek) {
                        line_friday_cell_1.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_friday_cell_1.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_friday_cell_1.setTextColor(getResources().getColor(R.color.white));
                        line_friday_cell_1.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_friday_cell_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==2){
                    line_friday_cell_1.setVisibility(View.GONE);
                    line_friday_cell_2.setVisibility(View.GONE);
                    line_friday_cell_1_2.setVisibility(View.VISIBLE);
                    line_friday_cell_1_2.setText(showString);
                    if(notThisWeek) {
                        line_friday_cell_1_2.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_friday_cell_1_2.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_friday_cell_1_2.setTextColor(getResources().getColor(R.color.white));
                        line_friday_cell_1_2.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_friday_cell_1_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==3){
                    line_friday_cell_1.setVisibility(View.GONE);
                    line_friday_cell_2.setVisibility(View.GONE);
                    line_friday_cell_3.setVisibility(View.GONE);
                    line_friday_cell_1_2_3.setVisibility(View.VISIBLE);
                    line_friday_cell_1_2_3.setText(showString);
                    if(notThisWeek) {
                        line_friday_cell_1_2_3.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_friday_cell_1_2_3.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_friday_cell_1_2_3.setTextColor(getResources().getColor(R.color.white));
                        line_friday_cell_1_2_3.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_friday_cell_1_2_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==4){
                    line_friday_cell_1.setVisibility(View.GONE);
                    line_friday_cell_2.setVisibility(View.GONE);
                    line_friday_cell_3.setVisibility(View.GONE);
                    line_friday_cell_4.setVisibility(View.GONE);
                    if(notThisWeek) {
                        line_friday_cell_1_2_3_4.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_friday_cell_1_2_3_4.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_friday_cell_1_2_3_4.setTextColor(getResources().getColor(R.color.white));
                        line_friday_cell_1_2_3_4.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_friday_cell_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
            }
            if(tempWeek==5&&tempStartTime==3){
                if(tempEndTime==3){
                    line_friday_cell_3.setText(showString);
                    if(notThisWeek) {
                        line_friday_cell_3.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_friday_cell_3.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_friday_cell_3.setTextColor(getResources().getColor(R.color.white));
                        line_friday_cell_3.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_friday_cell_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==4){
                    line_friday_cell_3.setVisibility(View.GONE);
                    line_friday_cell_4.setVisibility(View.GONE);

                    line_friday_cell_3_4.setText(showString);
                    line_friday_cell_3_4.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_friday_cell_3_4.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_friday_cell_3_4.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_friday_cell_3_4.setTextColor(getResources().getColor(R.color.white));
                        line_friday_cell_3_4.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_friday_cell_3_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
            }
            if(tempWeek==5&&tempStartTime==5){
                if(tempEndTime==5){
                    line_friday_cell_5.setText(showString);
                    if(notThisWeek) {
                        line_friday_cell_5.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_friday_cell_5.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_friday_cell_5.setTextColor(getResources().getColor(R.color.white));
                        line_friday_cell_5.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_friday_cell_5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }


                if(tempEndTime==6){
                    line_friday_cell_5.setVisibility(View.GONE);
                    line_friday_cell_6.setVisibility(View.GONE);
                    line_friday_cell_5_6.setText(showString);
                    line_friday_cell_5_6.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_friday_cell_5_6.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_friday_cell_5_6.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_friday_cell_5_6.setTextColor(getResources().getColor(R.color.white));
                        line_friday_cell_5_6.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_friday_cell_5_6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
                if(tempEndTime==7){
                    line_friday_cell_5.setVisibility(View.GONE);
                    line_friday_cell_6.setVisibility(View.GONE);
                    line_friday_cell_7.setVisibility(View.GONE);
                    line_friday_cell_5_6_7.setText(showString);
                    line_friday_cell_5_6_7.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_friday_cell_5_6_7.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_friday_cell_5_6_7.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_friday_cell_5_6_7.setTextColor(getResources().getColor(R.color.white));
                        line_friday_cell_5_6_7.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_friday_cell_5_6_7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==8){
                    line_friday_cell_5.setVisibility(View.GONE);
                    line_friday_cell_6.setVisibility(View.GONE);
                    line_friday_cell_7.setVisibility(View.GONE);
                    line_friday_cell_8.setVisibility(View.GONE);
                    line_friday_cell_5_6_7_8.setText(showString);
                    line_friday_cell_5_6_7_8.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_friday_cell_5_6_7_8.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_friday_cell_5_6_7_8.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_friday_cell_5_6_7_8.setTextColor(getResources().getColor(R.color.white));
                        line_friday_cell_5_6_7_8.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_friday_cell_5_6_7_8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
            }
            if(tempWeek==5&&tempStartTime==7){
                if(tempEndTime==7){
                    line_friday_cell_7.setText(showString);
                    if(notThisWeek) {
                        line_friday_cell_7.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_friday_cell_7.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_friday_cell_7.setTextColor(getResources().getColor(R.color.white));
                        line_friday_cell_7.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_friday_cell_7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==8){
                    line_friday_cell_7.setVisibility(View.GONE);
                    line_friday_cell_8.setVisibility(View.GONE);
                    line_friday_cell_7_8.setText(showString);
                    line_friday_cell_7_8.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_friday_cell_7_8.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_friday_cell_7_8.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_friday_cell_7_8.setTextColor(getResources().getColor(R.color.white));
                        line_friday_cell_7_8.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_friday_cell_7_8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }

            }
            if(tempWeek==5&&tempStartTime==9){
                if(tempEndTime==9){
                    line_friday_cell_9.setText(showString);
                    if(notThisWeek) {
                        line_friday_cell_9.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_friday_cell_9.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_friday_cell_9.setTextColor(getResources().getColor(R.color.white));
                        line_friday_cell_9.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_friday_cell_9.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==10){
                    line_friday_cell_9.setVisibility(View.GONE);
                    line_friday_cell_10.setVisibility(View.GONE);
                    line_friday_cell_9_10.setVisibility(View.VISIBLE);
                    line_friday_cell_9_10.setText(showString);
                    if(notThisWeek) {
                        line_friday_cell_9_10.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_friday_cell_9_10.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_friday_cell_9_10.setTextColor(getResources().getColor(R.color.white));
                        line_friday_cell_9_10.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_friday_cell_9_10.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==11){
                    line_friday_cell_9.setVisibility(View.GONE);
                    line_friday_cell_10.setVisibility(View.GONE);
                    line_friday_cell_11.setVisibility(View.GONE);
                    line_friday_cell_9_10_11.setVisibility(View.VISIBLE);
                    line_friday_cell_9_10_11.setText(showString);
                    if(notThisWeek) {
                        line_friday_cell_9_10_11.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_friday_cell_9_10_11.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_friday_cell_9_10_11.setTextColor(getResources().getColor(R.color.white));
                        line_friday_cell_9_10_11.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_friday_cell_9_10_11.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
            }
//saturday
            if(tempWeek==6&&tempStartTime==1){
                if(tempEndTime==1) {
                    line_saturday_cell_1.setText(showString);
                    if(notThisWeek) {
                        line_saturday_cell_1.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_saturday_cell_1.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_saturday_cell_1.setTextColor(getResources().getColor(R.color.white));
                        line_saturday_cell_1.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_saturday_cell_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==2){
                    line_saturday_cell_1.setVisibility(View.GONE);
                    line_saturday_cell_2.setVisibility(View.GONE);
                    line_saturday_cell_1_2.setVisibility(View.VISIBLE);
                    line_saturday_cell_1_2.setText(showString);
                    if(notThisWeek) {
                        line_saturday_cell_1_2.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_saturday_cell_1_2.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_saturday_cell_1_2.setTextColor(getResources().getColor(R.color.white));
                        line_saturday_cell_1_2.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_saturday_cell_1_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==3){
                    line_saturday_cell_1.setVisibility(View.GONE);
                    line_saturday_cell_2.setVisibility(View.GONE);
                    line_saturday_cell_3.setVisibility(View.GONE);
                    line_saturday_cell_1_2_3.setVisibility(View.VISIBLE);
                    line_saturday_cell_1_2_3.setText(showString);
                    if(notThisWeek) {
                        line_saturday_cell_1_2_3.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_saturday_cell_1_2_3.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_saturday_cell_1_2_3.setTextColor(getResources().getColor(R.color.white));
                        line_saturday_cell_1_2_3.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_saturday_cell_1_2_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
                if(tempEndTime==4){
                    line_saturday_cell_1.setVisibility(View.GONE);
                    line_saturday_cell_2.setVisibility(View.GONE);
                    line_saturday_cell_3.setVisibility(View.GONE);
                    line_saturday_cell_4.setVisibility(View.GONE);
                    if(notThisWeek) {
                        line_saturday_cell_1_2_3_4.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_saturday_cell_1_2_3_4.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_saturday_cell_1_2_3_4.setTextColor(getResources().getColor(R.color.white));
                        line_saturday_cell_1_2_3_4.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_saturday_cell_1_2_3_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
            }
            if(tempWeek==6&&tempStartTime==3){
                if(tempEndTime==3){
                    line_saturday_cell_3.setText(showString);
                    if(notThisWeek) {
                        line_saturday_cell_3.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_saturday_cell_3.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_saturday_cell_3.setTextColor(getResources().getColor(R.color.white));
                        line_saturday_cell_3.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_saturday_cell_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==4){
                    line_saturday_cell_3.setVisibility(View.GONE);
                    line_saturday_cell_4.setVisibility(View.GONE);

                    line_saturday_cell_3_4.setText(showString);
                    line_saturday_cell_3_4.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_saturday_cell_3_4.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_saturday_cell_3_4.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_saturday_cell_3_4.setTextColor(getResources().getColor(R.color.white));
                        line_saturday_cell_3_4.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_saturday_cell_3_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
            }
            if(tempWeek==6&&tempStartTime==5){
                if(tempEndTime==5){
                    line_saturday_cell_5.setText(showString);
                    if(notThisWeek) {
                        line_saturday_cell_5.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_saturday_cell_5.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_saturday_cell_5.setTextColor(getResources().getColor(R.color.white));
                        line_saturday_cell_5.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_saturday_cell_5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }


                if(tempEndTime==6){
                    line_saturday_cell_5.setVisibility(View.GONE);
                    line_saturday_cell_6.setVisibility(View.GONE);
                    line_saturday_cell_5_6.setText(showString);
                    line_saturday_cell_5_6.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_saturday_cell_5_6.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_saturday_cell_5_6.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_saturday_cell_5_6.setTextColor(getResources().getColor(R.color.white));
                        line_saturday_cell_5_6.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_saturday_cell_5_6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
                if(tempEndTime==7){
                    line_saturday_cell_5.setVisibility(View.GONE);
                    line_saturday_cell_6.setVisibility(View.GONE);
                    line_saturday_cell_7.setVisibility(View.GONE);
                    line_saturday_cell_5_6_7.setText(showString);
                    line_saturday_cell_5_6_7.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_saturday_cell_5_6_7.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_saturday_cell_5_6_7.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_saturday_cell_5_6_7.setTextColor(getResources().getColor(R.color.white));
                        line_saturday_cell_5_6_7.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_saturday_cell_5_6_7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
                if(tempEndTime==8){
                    line_saturday_cell_5.setVisibility(View.GONE);
                    line_saturday_cell_6.setVisibility(View.GONE);
                    line_saturday_cell_7.setVisibility(View.GONE);
                    line_saturday_cell_8.setVisibility(View.GONE);
                    line_saturday_cell_5_6_7_8.setText(showString);
                    line_saturday_cell_5_6_7_8.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_saturday_cell_5_6_7_8.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_saturday_cell_5_6_7_8.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_saturday_cell_5_6_7_8.setTextColor(getResources().getColor(R.color.white));
                        line_saturday_cell_5_6_7_8.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_saturday_cell_5_6_7_8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
            }
            if(tempWeek==6&&tempStartTime==7){
                if(tempEndTime==7){
                    line_saturday_cell_7.setText(showString);
                    if(notThisWeek) {
                        line_saturday_cell_7.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_saturday_cell_7.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_saturday_cell_7.setTextColor(getResources().getColor(R.color.white));
                        line_saturday_cell_7.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_saturday_cell_7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==8){
                    line_saturday_cell_7.setVisibility(View.GONE);
                    line_saturday_cell_8.setVisibility(View.GONE);
                    line_saturday_cell_7_8.setText(showString);
                    line_saturday_cell_7_8.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_saturday_cell_7_8.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_saturday_cell_7_8.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_saturday_cell_7_8.setTextColor(getResources().getColor(R.color.white));
                        line_saturday_cell_7_8.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_saturday_cell_7_8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }

            }
            if(tempWeek==6&&tempStartTime==9){
                if(tempEndTime==9){
                    line_saturday_cell_9.setText(showString);
                    if(notThisWeek) {
                        line_saturday_cell_9.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_saturday_cell_9.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_saturday_cell_9.setTextColor(getResources().getColor(R.color.white));
                        line_saturday_cell_9.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_saturday_cell_9.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==10){
                    line_saturday_cell_9.setVisibility(View.GONE);
                    line_saturday_cell_10.setVisibility(View.GONE);
                    line_saturday_cell_9_10.setVisibility(View.VISIBLE);
                    line_saturday_cell_9_10.setText(showString);
                    if(notThisWeek) {
                        line_saturday_cell_9_10.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_saturday_cell_9_10.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_saturday_cell_9_10.setTextColor(getResources().getColor(R.color.white));
                        line_saturday_cell_9_10.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_saturday_cell_9_10.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==11){
                    line_saturday_cell_9.setVisibility(View.GONE);
                    line_saturday_cell_10.setVisibility(View.GONE);
                    line_saturday_cell_11.setVisibility(View.GONE);
                    line_saturday_cell_9_10_11.setVisibility(View.VISIBLE);
                    line_saturday_cell_9_10_11.setText(showString);
                    if(notThisWeek) {
                        line_saturday_cell_9_10_11.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_saturday_cell_9_10_11.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_saturday_cell_9_10_11.setTextColor(getResources().getColor(R.color.white));
                        line_saturday_cell_9_10_11.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_saturday_cell_9_10_11.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }

            }
//sunday
            if(tempWeek==7&&tempStartTime==1){
                if(tempEndTime==1) {
                    line_sunday_cell_1.setText(showString);
                    if(notThisWeek) {
                        line_sunday_cell_1.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_sunday_cell_1.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_sunday_cell_1.setTextColor(getResources().getColor(R.color.white));
                        line_sunday_cell_1.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_sunday_cell_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==2){
                    line_sunday_cell_1.setVisibility(View.GONE);
                    line_sunday_cell_2.setVisibility(View.GONE);
                    line_sunday_cell_1_2.setVisibility(View.VISIBLE);
                    line_sunday_cell_1_2.setText(showString);
                    if(notThisWeek) {
                        line_sunday_cell_1_2.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_sunday_cell_1_2.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_sunday_cell_1_2.setTextColor(getResources().getColor(R.color.white));
                        line_sunday_cell_1_2.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_sunday_cell_1_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==3){
                    line_sunday_cell_1.setVisibility(View.GONE);
                    line_sunday_cell_2.setVisibility(View.GONE);
                    line_sunday_cell_3.setVisibility(View.GONE);
                    line_sunday_cell_1_2_3.setVisibility(View.VISIBLE);
                    line_sunday_cell_1_2_3.setText(showString);
                    if(notThisWeek) {
                        line_sunday_cell_1_2_3.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_sunday_cell_1_2_3.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_sunday_cell_1_2_3.setTextColor(getResources().getColor(R.color.white));
                        line_sunday_cell_1_2_3.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_sunday_cell_1_2_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
                if(tempEndTime==4){
                    line_sunday_cell_1.setVisibility(View.GONE);
                    line_sunday_cell_2.setVisibility(View.GONE);
                    line_sunday_cell_3.setVisibility(View.GONE);
                    line_sunday_cell_4.setVisibility(View.GONE);
                    if(notThisWeek) {
                        line_sunday_cell_1_2_3_4.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_sunday_cell_1_2_3_4.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_sunday_cell_1_2_3_4.setTextColor(getResources().getColor(R.color.white));
                        line_sunday_cell_1_2_3_4.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_sunday_cell_1_2_3_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
            }
            if(tempWeek==7&&tempStartTime==3){
                if(tempEndTime==3){
                    line_sunday_cell_3.setText(showString);
                    if(notThisWeek) {
                        line_sunday_cell_3.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_sunday_cell_3.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_sunday_cell_3.setTextColor(getResources().getColor(R.color.white));
                        line_sunday_cell_3.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_sunday_cell_3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }

                if(tempEndTime==4){
                    line_sunday_cell_3.setVisibility(View.GONE);
                    line_sunday_cell_4.setVisibility(View.GONE);

                    line_sunday_cell_3_4.setText(showString);
                    line_sunday_cell_3_4.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_sunday_cell_3_4.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_sunday_cell_3_4.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_sunday_cell_3_4.setTextColor(getResources().getColor(R.color.white));
                        line_sunday_cell_3_4.setBackground(getResources().getDrawable(R.drawable.color4_timetable_radius));
                    }
                    line_sunday_cell_3_4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
            }
            if(tempWeek==7&&tempStartTime==5){
                if(tempEndTime==5){
                    line_sunday_cell_5.setText(showString);
                    if(notThisWeek) {
                        line_sunday_cell_5.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_sunday_cell_5.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_sunday_cell_5.setTextColor(getResources().getColor(R.color.white));
                        line_sunday_cell_5.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_sunday_cell_5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }


                if(tempEndTime==6){
                    line_sunday_cell_5.setVisibility(View.GONE);
                    line_sunday_cell_6.setVisibility(View.GONE);
                    line_sunday_cell_5_6.setText(showString);
                    line_sunday_cell_5_6.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_sunday_cell_5_6.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_sunday_cell_5_6.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_sunday_cell_5_6.setTextColor(getResources().getColor(R.color.white));
                        line_sunday_cell_5_6.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_sunday_cell_5_6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });

                }
                if(tempEndTime==7){
                    line_sunday_cell_5.setVisibility(View.GONE);
                    line_sunday_cell_6.setVisibility(View.GONE);
                    line_sunday_cell_7.setVisibility(View.GONE);
                    line_sunday_cell_5_6_7.setText(showString);
                    line_sunday_cell_5_6_7.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_sunday_cell_5_6_7.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_sunday_cell_5_6_7.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_sunday_cell_5_6_7.setTextColor(getResources().getColor(R.color.white));
                        line_sunday_cell_5_6_7.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_sunday_cell_5_6_7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==8){
                    line_sunday_cell_5.setVisibility(View.GONE);
                    line_sunday_cell_6.setVisibility(View.GONE);
                    line_sunday_cell_7.setVisibility(View.GONE);
                    line_sunday_cell_8.setVisibility(View.GONE);
                    line_sunday_cell_5_6_7_8.setText(showString);
                    line_sunday_cell_5_6_7_8.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_sunday_cell_5_6_7_8.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_sunday_cell_5_6_7_8.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_sunday_cell_5_6_7_8.setTextColor(getResources().getColor(R.color.white));
                        line_sunday_cell_5_6_7_8.setBackground(getResources().getDrawable(R.drawable.color1_timetable_radius));
                    }
                    line_sunday_cell_5_6_7_8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
            }
            if(tempWeek==7&&tempStartTime==7){
                if(tempEndTime==7){
                    line_sunday_cell_7.setText(showString);
                    if(notThisWeek) {
                        line_sunday_cell_7.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_sunday_cell_7.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_sunday_cell_7.setTextColor(getResources().getColor(R.color.white));
                        line_sunday_cell_7.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_sunday_cell_7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }

                if(tempEndTime==8){
                    line_sunday_cell_7.setVisibility(View.GONE);
                    line_sunday_cell_8.setVisibility(View.GONE);
                    line_sunday_cell_7_8.setText(showString);
                    line_sunday_cell_7_8.setVisibility(View.VISIBLE);
                    if(notThisWeek) {
                        line_sunday_cell_7_8.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_sunday_cell_7_8.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_sunday_cell_7_8.setTextColor(getResources().getColor(R.color.white));
                        line_sunday_cell_7_8.setBackground(getResources().getDrawable(R.drawable.color2_timetable_radius));
                    }
                    line_sunday_cell_7_8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }

            }
            if(tempWeek==7&&tempStartTime==9){
                if(tempEndTime==9){
                    line_sunday_cell_9.setText(showString);
                    if(notThisWeek) {
                        line_sunday_cell_9.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_sunday_cell_9.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_sunday_cell_9.setTextColor(getResources().getColor(R.color.white));
                        line_sunday_cell_9.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_sunday_cell_9.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==10){
                    line_sunday_cell_9.setVisibility(View.GONE);
                    line_sunday_cell_10.setVisibility(View.GONE);
                    line_sunday_cell_9_10.setVisibility(View.VISIBLE);
                    line_sunday_cell_9_10.setText(showString);
                    if(notThisWeek) {
                        line_sunday_cell_9_10.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_sunday_cell_9_10.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_sunday_cell_9_10.setTextColor(getResources().getColor(R.color.white));
                        line_sunday_cell_9_10.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_sunday_cell_9_10.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
                if(tempEndTime==11){
                    line_sunday_cell_9.setVisibility(View.GONE);
                    line_sunday_cell_10.setVisibility(View.GONE);
                    line_sunday_cell_11.setVisibility(View.GONE);
                    line_sunday_cell_9_10_11.setVisibility(View.VISIBLE);
                    line_sunday_cell_9_10_11.setText(showString);
                    if(notThisWeek) {
                        line_sunday_cell_9_10_11.setTextColor(getResources().getColor(R.color.notthisweektext));
                        line_sunday_cell_9_10_11.setBackground(getResources().getDrawable(R.drawable.grey_table_radius));
                    }
                    if(!notThisWeek) {
                        line_sunday_cell_9_10_11.setTextColor(getResources().getColor(R.color.white));
                        line_sunday_cell_9_10_11.setBackground(getResources().getDrawable(R.drawable.color3_timetable_radius));
                    }
                    line_sunday_cell_9_10_11.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showClassDetailDialog(clickPassBean);
                        }
                    });
                }
            }
        }



    }

    private void initTimeTableClickListener() {






    }





    private void showClassDetailDialog(TimeTableBean classDetailBean) {
        String cName=classDetailBean.getcName();
        String teacherName=classDetailBean.getTeacherName();
        String dayText=classDetailBean.getDay()+getString(R.string.space)+classDetailBean.getStartNum()+"-"+classDetailBean.getEndNum()+"节";

        ArrayList<String> weekClassRoom=new ArrayList<>();

        for(int a=0;a<classDetailBean.getTime().size();a++){

            String temp=
            classDetailBean.getTime().get(a).getClassroom()+getString(R.string.space)+
            classDetailBean.getTime().get(a).getStartweek()+"-"+
            classDetailBean.getTime().get(a).getEndweek()+"周";
            weekClassRoom.add(temp);
        }

        classDetailDialog=new ClassDetailDialog(this,R.style.classDetailDialog,cName,teacherName,dayText,weekClassRoom);
        classDetailDialog.show();

    }

    public String getWeekOfDate( Date date) {
        String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public int countTwoDayWeek(Date start, Date end) {

        Calendar cal=Calendar.getInstance();
        cal.setTime(start);
        long time1=cal.getTimeInMillis();
        cal.setTime(end);
        long time2=cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);
        Double days=Double.parseDouble(String.valueOf(between_days));
        if((days/7)>0 && (days/7)<=1){
            //不满一周的按一周算
            return 1;
        }else if(days/7>1){
            int day=days.intValue();
            if(day%7>0){
                return day/7+1;
            }else{
                return day/7;
            }
        }else if((days/7)==0){
            return 0;
        }else{
            //负数返还null
            return 0;
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            rl_about.setVisibility(View.GONE);
            ll_timetable.setVisibility(View.VISIBLE);
            spinnerView_radioDialog1.setVisibility(View.VISIBLE);
            ll_login.setVisibility(View.GONE);
            getSupportActionBar().setTitle("课程表");


        } else if (id == R.id.nav_gallery) {
            what=2;
            etCode.getText().clear();
            rl_about.setVisibility(View.GONE);
            ll_timetable.setVisibility(View.GONE);
            spinnerView_radioDialog1.setVisibility(View.GONE);
            ll_login.setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle("成绩查询");

        if(UserInfoSaver.USERID!=null&&UserInfoSaver.USERPSW!=null){
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
        initCode();
        btLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(etStuId.getText().length()==0){
                        etStuId.setHintTextColor(Color.RED);return;
                    }
                    if(etStuPsw.getText().length()==0){
                        etStuPsw.setHintTextColor(Color.RED);return;
                    }
                    if(etCode.getText().length()==0){
                        etCode.setHintTextColor(Color.RED);return;
                    }
                    ll_login.requestFocus();
                    dialog.show();
                    stuId=etStuId.getText().toString();
                    getViewState(etStuId.getText().toString(),etStuPsw.getText().toString(),etCode.getText().toString(),getApplicationContext());

                }
            });

        } else if (id == R.id.nav_slideshow) {
            what=3;
            etCode.getText().clear();
            ll_timetable.setVisibility(View.GONE);
            spinnerView_radioDialog1.setVisibility(View.GONE);
            rl_about.setVisibility(View.GONE);
            ll_login.setVisibility(View.VISIBLE);

            getSupportActionBar().setTitle("考试安排查询");
            if(UserInfoSaver.USERID!=null&&UserInfoSaver.USERPSW!=null){
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
            initCode();
            btLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(etStuId.getText().length()==0){
                        etStuId.setHintTextColor(Color.RED);return;
                    }
                    if(etStuPsw.getText().length()==0){
                        etStuPsw.setHintTextColor(Color.RED);return;
                    }
                    if(etCode.getText().length()==0){
                        etCode.setHintTextColor(Color.RED);return;
                    }
                    ll_login.requestFocus();
                    dialog.show();
                    stuId=etStuId.getText().toString();
                    getViewState(etStuId.getText().toString(),etStuPsw.getText().toString(),etCode.getText().toString(),getApplicationContext());

                }
            });

        } else if(id==R.id.nav_refresh_timetable){

            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            getSupportActionBar().setTitle("课程表");

            intent.putExtra("isRefresh",1);
            startActivity(intent);
            finish();

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    Handler mHandler = new Handler(){

        //handleMessage为处理消息的方法
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){

                    getSupportActionBar().setTitle("课程表");
                    ll_timetable.setVisibility(View.VISIBLE);
                    initGetSavedTimeTableJson();
                    try {
                        showTimeTable();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }



                dialog.dismiss();
            }
            if(msg.what==2){
                showSoftInputFromWindow(etCode);
            }
            if(msg.what==3){
                dialog.dismiss();
                ll_login.setVisibility(View.GONE);
                ll_timetable.setVisibility(View.VISIBLE);
                try {
                    showTimeTable();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if(msg.what==4){
                dialog.dismiss();

                errorDialog.show();
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

    @Override
    protected void onResume() {
        super.onResume();
//        if(ListSaver.getTimeTableBeanList().size()!=0) {
//            getSupportActionBar().setTitle("课程表");
//            spinnerView_radioDialog1.setVisibility(View.VISIBLE);
//        }else{
//            getSupportActionBar().setTitle("查询课表");
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("1","destory");
    }

    private void showTwo() {
        builder = new AlertDialog.Builder(MainActivity.this).setTitle("请更新到最新版本！")//.setIcon(R.mipmap.ic_launcher).
                .setMessage("没有重大BUG不更新，所以每次更新都很重要！=_=\n安装包大小:4.82M 预计下载时间:7秒").setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
//                        Intent intent = new Intent();
//                        intent.setAction("android.intent.action.VIEW");
//                        Uri content_url=Uri.parse("http://47.101.148.255/mjutimetable.apk");
//                        intent.setData(content_url);
//                        intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
//                        startActivity(intent);
                        downFile("http://47.101.148.255/mjutimetable.apk");

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情

                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    private void getIsUpdate() {
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

                            }

                            @Override
                            public void onResponse(String s, int i) {

                             Document document = Jsoup.parse(s);
                             Element element = document.getElementById("version");
                             if(Integer.valueOf(element.text())!=currentVersionCode){

                                 TimerTask task = new TimerTask() {
                                     @Override
                                     public void run() {
                                         runOnUiThread(new Runnable() {
                                             @Override
                                             public void run() {
                                              showTwo();
                                             }
                                         });

                                     }
                                 };
                                 Timer timer = new Timer();
                                 timer.schedule(task, 2000);//2秒后执行TimeTask的run方法

                             }

                            }



                        });
            }
        }).start();


    }


    public void getCurrentVersionCode()
    {
        PackageManager manager = getApplicationContext().getPackageManager();

        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        currentVersionCode = info.versionCode;

    }

    /**
     * 文件下载
     *
     * @param url
     */
    public void downFile(String url) {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在下载");
        progressDialog.setMessage("请稍后...");
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.show();
        progressDialog.setCancelable(false);
        DownloadUtil.get().download(url, Environment.getExternalStorageDirectory().getAbsolutePath(), "mjutimetable.apk", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                //该行代码的作用是APK安装完毕后打开
                startActivity(i);
                //下载完成进行相关逻辑操作

            }

            @Override
            public void onDownloading(int progress) {
                progressDialog.setProgress(progress);
            }

            @Override
            public void onDownloadFailed(Exception e) {
                //下载异常进行相关提示操作
            }
        });
    }

}
