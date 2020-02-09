package com.android.mjutimetable.Util;



import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.mjutimetable.R;

import java.util.ArrayList;


public class ClassDetailDialog extends Dialog {
    private String text;
    private String cName;
    private String teacherName;
    private String timeText;
    private  ArrayList<String> weekClassRoomText;
    public ClassDetailDialog(Context context)
    {
        super(context);
    }

    public ClassDetailDialog(Context context, int theme)
    {
        super(context, theme);
    }
    public ClassDetailDialog(Context context, int theme,String cName)
    {
        super(context, theme);
        this.cName=cName;
    }
    public ClassDetailDialog(Context context, int theme, String cName, String teacherName, String timeText, ArrayList<String> weekClassRoomText)
    {
        super(context, theme);
        this.text=text;
        this.cName=cName;
        this.teacherName=teacherName;
        this.timeText=timeText;
        this.weekClassRoomText=weekClassRoomText;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        init(getContext());
    }

    private void init(Context context)
    {
        //设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        setContentView(R.layout.class_detail_dialog);
//        TextView tv_load_dialog=findViewById(R.id.tv_load_dialog);
//        tv_load_dialog.setText(text);
        LinearLayout ll_class_detail_layout=findViewById(R.id.ll_class_detail_layout);
        ll_class_detail_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        TextView tv_class_name=findViewById(R.id.tv_class_name);
        TextView tv_teacher_name=findViewById(R.id.tv_teacher_name);
        TextView tv_day_time=findViewById(R.id.tv_day_time);
        TextView tv_class_room_by_week=findViewById(R.id.tv_class_room_by_week);

        tv_class_name.setText(cName);
        if(weekClassRoomText==null){
            RelativeLayout rl_class_room=findViewById(R.id.rl_class_room);
            rl_class_room.setVisibility(View.GONE);
        }
        if(weekClassRoomText!=null) {

        tv_teacher_name.setText("老师："+teacherName);
        tv_day_time.setText("时间："+timeText);

        StringBuffer bf=new StringBuffer();

            for (int i = 0; i < weekClassRoomText.size(); i++) {
                bf.append(weekClassRoomText.get(i) + "\n");
            }

            tv_class_room_by_week.setText(bf.toString());
        }

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void show()
    {
        super.show();
    }

}
