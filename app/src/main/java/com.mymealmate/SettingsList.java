package com.mymealmate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.kxml2.kdom.Node;
import org.kxml2.wap.Wbxml;

public class SettingsList extends Activity implements OnItemClickListener {
    private final String[] LIST;
    private final int[] LIST_ID;

    public SettingsList() {
        int[] iArr = new int[9];
        iArr[1] = 1;
        iArr[2] = 2;
        iArr[3] = 3;
        iArr[4] = 4;
        iArr[5] = 5;
        iArr[6] = 6;
        iArr[7] = 7;
        iArr[8] = 8;
        this.LIST_ID = iArr;
        this.LIST = new String[]{"My Profile", "My current exercise", "My weight loss goals", "Holiday mode", "Display as metric/imperial", "My favourites", "Data upload address", "Diagnostics", "Block text messages"};
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        setContentView(R.layout.settings_list);
        ListView listView = (ListView) findViewById(R.id.settingsListView);
        listView.setAdapter(new ArrayAdapter(this, 17367043, this.LIST));
        listView.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        switch (this.LIST_ID[i]) {
            case Node.DOCUMENT /*0*/:
                startActivity(new Intent(this, Profile.class));
            case Wbxml.END /*1*/:
                startActivity(new Intent(this, ExerciseSettings.class));
            case Wbxml.ENTITY /*2*/:
                startActivity(new Intent(this, TargetSettings.class));
            case Wbxml.STR_I /*3*/:
                startActivity(new Intent(this, HolidayMode.class));
            case Wbxml.LITERAL /*4*/:
                startActivity(new Intent(this, Preferences.class));
            case Node.CDSECT /*5*/:
                startActivity(new Intent(this, Favourites.class));
            case Node.ENTITY_REF /*6*/:
                startActivity(new Intent(this, DataUploadAddress.class));
            case Node.IGNORABLE_WHITESPACE /*7*/:
                startActivity(new Intent(this, Diagnostics.class));
            case Node.PROCESSING_INSTRUCTION /*8*/:
                startActivity(new Intent(this, BlockMessages.class));
            default:
        }
    }
}