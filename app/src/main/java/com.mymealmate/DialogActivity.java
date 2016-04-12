package com.mymealmate;

import android.app.Activity;
import android.os.Bundle;

public class DialogActivity extends Activity {
    private static int layoutId_;

    public static void setLayoutId(int i) {
        layoutId_ = i;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().setFlags(4, 4);
        setContentView(layoutId_);
    }
}