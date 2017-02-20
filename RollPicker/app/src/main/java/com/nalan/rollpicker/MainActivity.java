package com.nalan.rollpicker;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nalan.rollpicker.tools.DatePicker;
import com.nalan.rollpicker.tools.NumberPicker;

public class MainActivity extends AppCompatActivity {
    //控件设置id，才会调用PickerView的onSaveInstanceState()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatePicker datePicker = (DatePicker) findViewById(R.id.date_picker);
        datePicker.setYearRange(1977,2017);
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                Log.e("DatePicker",year+"-"+month+"-"+day);
            }
        });

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/SanFranciscoRegular.ttf");
        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.number_picker);
        numberPicker.setTextTypeface(typeface);
        numberPicker.setHighlightTextTypeface(typeface);
        numberPicker.setOnValueChangeListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                Log.e("NumberPicker","新的值："+newVal);
            }
        });
    }
}
