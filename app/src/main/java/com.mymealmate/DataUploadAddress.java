package com.mymealmate;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class DataUploadAddress extends Activity {
    MyMealMateDatabase db_;
    Prefs prefs_;

    public DataUploadAddress() {
        this.db_ = null;
        this.prefs_ = null;
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.upload_address);
        EditText editText = (EditText) findViewById(R.id.uploadAddress);
        this.db_ = new MyMealMateDatabase(this);
        this.prefs_ = new Prefs(this.db_, this);
        if (this.prefs_.getRunStandalone().booleanValue()) {
            editText.setEnabled(false);
        } else {
            editText.setText(WSClient.getUrl());
        }
    }

    public void onSaveClick(View view) {
        WSClient.setUrl(((EditText) findViewById(R.id.uploadAddress)).getText().toString());
        finish();
    }

    public void onStop() {
        this.db_.close();
        super.onStop();
    }
}