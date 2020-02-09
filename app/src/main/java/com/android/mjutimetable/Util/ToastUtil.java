package com.android.mjutimetable.Util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.mjutimetable.R;


public class ToastUtil {

    //显示文本+图片的Toast
//    public static void showImageToas(Context context, String message){
//        View toastview= LayoutInflater.from(context).inflate(R.layout.toast_image_layout,null);
//        TextView text = (TextView) toastview.findViewById(R.id.tv_message);
//        text.setText(message);    //要提示的文本
//        Toast toast=new Toast(context);   //上下文
//        toast.setGravity(Gravity.CENTER,0,0);   //位置居中
//        toast.setDuration(Toast.LENGTH_SHORT);  //设置短暂提示
//        toast.setView(toastview);   //把定义好的View布局设置到Toast里面
//        toast.show();
//    }
    //显示文本的Toast
    public static void showTextToas(Context context, String message, String lengthtype, String position){
        View toastview= LayoutInflater.from(context).inflate(R.layout.toast_text_layout,null);
        TextView text =toastview.findViewById(R.id.tv_message);
        text.setText(message);


        Toast toast=new Toast(context);
        if(position=="center")toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.CENTER,0,0);
        if(position=="1")toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM,0,(int) PxUtil.dpToPx(context,50));
        if(position=="2")toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM,0,(int) PxUtil.dpToPx(context,100));
        if(position=="3")toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM,0,(int) PxUtil.dpToPx(context,100));
        if(position=="4")toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP,0,(int) PxUtil.dpToPx(context,170));
        if(position=="5")toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM,0,(int) PxUtil.dpToPx(context,100));
        if(position=="6")toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP,0,(int) PxUtil.dpToPx(context,50));
        if(lengthtype=="1"){toast.setDuration(Toast.LENGTH_SHORT);}
        if(lengthtype=="2"){toast.setDuration(Toast.LENGTH_LONG);}

        toast.setView(toastview);

        toast.show();
    }
}
