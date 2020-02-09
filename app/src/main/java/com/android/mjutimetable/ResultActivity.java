package com.android.mjutimetable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.mjutimetable.Bean.ResultBean;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class ResultActivity extends AppCompatActivity {
    ListView lv_result_query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        lv_result_query=findViewById(R.id.lv_result_query);

        Collections.reverse(ListSaver.getResultBeanList()); // 倒序排列

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        LvAdapter lvAdapter=new LvAdapter(getApplicationContext(),ListSaver.getResultBeanList());
        lv_result_query.setAdapter(lvAdapter);


    }

    public class LvAdapter extends BaseAdapter {

        private LayoutInflater mLayoutInflater;
        // 映射数据<泛型bean>
        private List<ResultBean> mDataList;

        public LvAdapter(Context context, List<ResultBean> list) {
            mLayoutInflater = LayoutInflater.from(context);
            mDataList = list;
        }

        // 获取数据量
        @Override
        public int getCount() {
            return mDataList.size();
        }

        // 获取对应ID项对应的Item
        @Override
        public Object getItem(int position) {
            return mDataList.get(position);
        }

        // 获取对应项ID
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // 只将XML转化为View，不涉及具体布局，第二个参数设null
            convertView = mLayoutInflater.inflate(R.layout.result_item, null);
            TextView result_class_name=convertView.findViewById(R.id.result_class_name);
            TextView result_class_type=convertView.findViewById(R.id.result_class_type);
            TextView result_class_credit=convertView.findViewById(R.id.result_class_credit);
            TextView result_class_year=convertView.findViewById(R.id.result_class_year);
            TextView result_class_term=convertView.findViewById(R.id.result_class_term);
            TextView classResult=convertView.findViewById(R.id.classResult);
            TextView classMakeUpResult=convertView.findViewById(R.id.classMakeUpResult);
            TextView classRetakeResult=convertView.findViewById(R.id.classRetakeResult);

            // 取出bean对象
            ResultBean resultBean = mDataList.get(position);
            // 设置控件的数据
            result_class_name.setText(resultBean.getClassName());
            result_class_type.setText(resultBean.getClassType());
            result_class_credit.setText("学分："+resultBean.getClassCredit());
            result_class_year.setText("学年："+resultBean.getClassYear());
            result_class_term.setText("学期："+resultBean.getClassTerm());


            Pattern pattern = Pattern.compile("([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9])");
            if(pattern.matcher(resultBean.getClassResult()).matches()){

                int type=0;

                classResult.setText("初考成绩："+resultBean.getClassResult());
                type=1;
                if(resultBean.getClassMakeUpResult()!=null&&resultBean.getClassMakeUpResult().length()!=0) {
                    classMakeUpResult.setText("补考成绩：" + resultBean.getClassMakeUpResult());
                    type=2;
                }else{
                    classMakeUpResult.setVisibility(View.GONE);
                }

                if(resultBean.getClassRetakeResult()!=null&&resultBean.getClassRetakeResult().length()!=0) {
                    classRetakeResult.setText("重修成绩："+resultBean.getClassRetakeResult());
                    type=3;
                }else{
                    classRetakeResult.setVisibility(View.GONE);
                }

                if(type==1){

                    if(Float.valueOf(resultBean.getClassResult())>=60){
                        classResult.setTextColor(getResources().getColor(R.color.result_pass));
                    }
                    if(Float.valueOf(resultBean.getClassResult())<60){
                        classResult.setTextColor(getResources().getColor(R.color.result_fail));

                    }
                }

                if(type==2){
                    if(Float.valueOf(resultBean.getClassMakeUpResult())>=60){
                        classMakeUpResult.setTextColor(getResources().getColor(R.color.result_pass));
                    }
                    if(Float.valueOf(resultBean.getClassMakeUpResult())<60){
                        classMakeUpResult.setTextColor(getResources().getColor(R.color.result_fail));

                    }
                }
                if(type==3){
                    if(Float.valueOf(resultBean.getClassRetakeResult())>=60){
                        classRetakeResult.setTextColor(getResources().getColor(R.color.result_pass));
                    }
                    if(Float.valueOf(resultBean.getClassRetakeResult())<60){
                        classRetakeResult.setTextColor(getResources().getColor(R.color.result_fail));

                    }
                }
            }


            if(!pattern.matcher(resultBean.getClassResult()).matches()){
                int type=0;

                classResult.setText("初考成绩："+resultBean.getClassResult());
                type=1;

                if(resultBean.getClassMakeUpResult()!=null&&resultBean.getClassMakeUpResult().length()!=0) {
                    classMakeUpResult.setText("补考成绩：" + resultBean.getClassMakeUpResult());
                    type=2;
                }else{
                    classMakeUpResult.setVisibility(View.GONE);
                }

                if(resultBean.getClassRetakeResult()!=null&&resultBean.getClassRetakeResult().length()!=0) {
                    classRetakeResult.setText("重修成绩："+resultBean.getClassRetakeResult());
                    type=3;
                }else{
                    classRetakeResult.setVisibility(View.GONE);
                }

                if(type==1){

                    if(resultBean.getClassResult().equals("优秀")||resultBean.getClassResult().equals("良好")||resultBean.getClassResult().equals("中等")||resultBean.getClassResult().equals("及格")){
                        classResult.setTextColor(getResources().getColor(R.color.result_pass));
                    }
                    if(resultBean.getClassResult().equals("不及格")){
                        classResult.setTextColor(getResources().getColor(R.color.result_fail));

                    }

                }

                if(type==2){
                    if(resultBean.getClassMakeUpResult().equals("优秀")||resultBean.getClassMakeUpResult().equals("良好")||resultBean.getClassMakeUpResult().equals("中等")||resultBean.getClassMakeUpResult().equals("及格")){
                        classMakeUpResult.setTextColor(getResources().getColor(R.color.result_pass));
                    }
                    if(resultBean.getClassResult().equals("不及格")){
                        classMakeUpResult.setTextColor(getResources().getColor(R.color.result_fail));

                    }
                }
                if(type==3){
                    if(resultBean.getClassRetakeResult().equals("优秀")||resultBean.getClassRetakeResult().equals("良好")||resultBean.getClassRetakeResult().equals("中等")||resultBean.getClassRetakeResult().equals("及格")){
                        classRetakeResult.setTextColor(getResources().getColor(R.color.result_pass));
                    }
                    if(resultBean.getClassResult().equals("不及格")){
                        classRetakeResult.setTextColor(getResources().getColor(R.color.result_fail));

                    }
                }


            }




            return convertView;
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
