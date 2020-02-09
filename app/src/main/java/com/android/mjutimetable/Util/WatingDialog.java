package com.android.mjutimetable.Util;



import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.mjutimetable.R;


public class WatingDialog extends ProgressDialog{
    private String text;
    public WatingDialog(Context context)
    {
        super(context);
    }

    public WatingDialog(Context context, int theme)
    {
        super(context, theme);
    }
    public WatingDialog(Context context, int theme,String text)
    {
        super(context, theme);
        this.text=text;
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
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        setContentView(R.layout.process_dialog);
        TextView tv_load_dialog=findViewById(R.id.tv_load_dialog);
        tv_load_dialog.setText(text);
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
