package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.test.pinyin.Pinyin;
import com.ui.picker.TimePicker;

import java.util.Calendar;

import static com.github.promeg.pinyinhelper.Pinyin.init;
import static com.github.promeg.pinyinhelper.Pinyin.newConfig;
import static com.github.promeg.pinyinhelper.Pinyin.toPinyin;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                onTimePicker(v);
                break;
            case R.id.button2:
                startActivity(new Intent(this, ExpandableActivity.class));
                break;
            case R.id.button3:
                startActivity(new Intent(this, SectionActivity.class));
                break;
            case R.id.button4:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.button5:
                String s = Pinyin.toPinyin("啊方式打开房间");
                Log.i("Test", "s=" + s);
                s = Pinyin.toPinyin("GS");
                Log.i("Test", "s=" + s);
                s = Pinyin.toPinyin("式打开房间");
                Log.i("Test", "s=" + s);
                s = Pinyin.toPinyin("拗口");
                Log.i("Test", "s=" + s);
                s = Pinyin.toPinyin("执拗");
                Log.i("Test", "s=" + s);
                s = Pinyin.toPinyin("hg");
                Log.i("Test", "s=" + s);

                init(newConfig());
                s = toPinyin("啊方式打开房间", "");
                Log.i("Test", "s=" + s);
                s = toPinyin("GS", "");
                Log.i("Test", "s=" + s);
                s = toPinyin("式打开房间", "");
                Log.i("Test", "s=" + s);
                s = toPinyin("拗口", "");
                Log.i("Test", "s=" + s);
                s = toPinyin("执拗", "");
                Log.i("Test", "s=" + s);
                s = toPinyin("hg", "");
                Log.i("Test", "s=" + s);
                break;
            case R.id.button6:
                startActivity(new Intent(this, MediaPlayActivity.class));
                break;
        }
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
