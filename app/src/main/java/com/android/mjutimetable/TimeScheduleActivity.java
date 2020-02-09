package com.android.mjutimetable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.mjutimetable.Bean.TestScheduleBean;

import java.util.ArrayList;
import java.util.List;

public class TimeScheduleActivity extends AppCompatActivity {
    ListView lv_test_schedule;
    private static List<TestScheduleBean> testScheduleBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_schedule);
        testScheduleBeanList=ListSaver.getTestScheduleBeanList();
        lv_test_schedule=findViewById(R.id.lv_test_schedule);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        Myadpater myadpater=new Myadpater();
        lv_test_schedule.setAdapter(myadpater);
    }
    private class Myadpater extends BaseAdapter {


        @Override
        public int getCount() {
            return testScheduleBeanList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View viewa;
            if(view==null){
                viewa = View.inflate(TimeScheduleActivity.this, R.layout.test_schedule_item, null);

            }else{
                viewa=view;

            }
//找到控件 显示集合里的数据

            LinearLayout item=viewa.findViewById(R.id.item);

            TextView tv_test_class_name=viewa.findViewById(R.id.tv_test_class_name);
            TextView tv_test_class_room=viewa.findViewById(R.id.tv_test_class_room);
            TextView tv_test_sit_num=viewa.findViewById(R.id.tv_test_sit_num);

            TextView tv_test_time=viewa.findViewById(R.id.tv_test_time);

            tv_test_class_name.setText(testScheduleBeanList.get(i).getTestClassName());
            tv_test_class_room.setText(testScheduleBeanList.get(i).getTestClassRoom());
            tv_test_sit_num.setText(testScheduleBeanList.get(i).getTestSitNum());
            tv_test_time.setText(testScheduleBeanList.get(i).getTestTime());

            if(i%2==0){
                item.setBackgroundColor(getResources().getColor(R.color.testschedulebg));
            }

            return viewa;
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
