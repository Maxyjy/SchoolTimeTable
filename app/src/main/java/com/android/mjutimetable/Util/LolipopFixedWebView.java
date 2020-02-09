package com.android.mjutimetable.Util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.AttributeSet;

public class LolipopFixedWebView extends android.webkit.WebView {



    public LolipopFixedWebView(Context context) {
        super(getFixedContext(context));
    }

    public LolipopFixedWebView(Context context, AttributeSet attrs) {
        super(getFixedContext(context), attrs);
    }

    public LolipopFixedWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(getFixedContext(context), attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LolipopFixedWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(getFixedContext(context), attrs, defStyleAttr, defStyleRes);
    }

    public LolipopFixedWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(getFixedContext(context), attrs, defStyleAttr, privateBrowsing);
    }

    public static Context getFixedContext(Context context) {
        return context.createConfigurationContext(new Configuration());
    }
}

