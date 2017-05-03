package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ui.picker.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        //onTimePicker(v);
//        startActivity(new Intent(this, ExpandableActivity.class));
        startActivity(new Intent(this, SectionActivity.class));
//        String s = com.example.test.util.Utils.getSectionIndexer("啊方式打开房间");
//        Log.i("Test", "s=" + s);
//        s = com.example.test.util.Utils.getSectionIndexer("GS");
//        Log.i("Test", "s=" + s);
//        s = com.example.test.util.Utils.getSectionIndexer("式打开房间");
//        Log.i("Test", "s=" + s);
//        s = com.example.test.util.Utils.getSectionIndexer("方式打开房间");
//        Log.i("Test", "s=" + s);
//        s = com.example.test.util.Utils.getSectionIndexer("hg");
//        Log.i("Test", "s=" + s);
    }

    public void onTimePicker(View view) {
        TimePicker picker = new TimePicker(this, TimePicker.HOUR_24);
        picker.setRangeStart(0, 0);//00:00
        picker.setRangeEnd(23, 59);//23:59
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);
        picker.setSelectedItem(currentHour, currentMinute);
        picker.setTopLineVisible(false);
        picker.setLineVisible(false);
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                Toast.makeText(MainActivity.this, (hour + ":" + minute), Toast.LENGTH_SHORT).show();
            }
        });
        picker.show();
    }
}
