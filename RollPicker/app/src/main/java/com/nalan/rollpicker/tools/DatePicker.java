package com.nalan.rollpicker.tools;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.nalan.rollpicker.R;

import java.util.Calendar;
import java.util.Locale;

/**
 * Author： liyi
 * Date：    2017/2/15.
 */

public class DatePicker extends LinearLayout implements NumberPicker.OnValueChangeListener{
    private Calendar mInstance;
    private NumberPicker mYearPicker;
    private NumberPicker mMonthPicker;
    private NumberPicker mDayPicker;
    private OnDateChangedListener mOnDateChangedListener;

    private int mMinYear,mMaxYear;

    private Drawable mDivider;

    public DatePicker(Context context) {
        this(context,null);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        mInstance = Calendar.getInstance(Locale.getDefault());
        LayoutInflater.from(context).inflate(R.layout.date_picker,this,true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.DatePicker);
        int year = typedArray.getInteger(R.styleable.DatePicker_year,mInstance.get(Calendar.YEAR));
        int month = typedArray.getInteger(R.styleable.DatePicker_month,mInstance.get(Calendar.MONTH));
        int day = typedArray.getInteger(R.styleable.DatePicker_day,mInstance.get(Calendar.DAY_OF_MONTH));
        mDivider = typedArray.getDrawable(R.styleable.DatePicker_divider);
        typedArray.recycle();

        mMinYear = mInstance.getMinimum(Calendar.YEAR);
        mMaxYear = mInstance.getMaximum(Calendar.YEAR);

        mYearPicker = (NumberPicker) findViewById(R.id.year);
        mMonthPicker = (NumberPicker) findViewById(R.id.month);
        mDayPicker = (NumberPicker) findViewById(R.id.day);

        setDate(year,month+1,day);

        mYearPicker.setOnValueChangeListener(this);
        mMonthPicker.setOnValueChangeListener(this);
        mDayPicker.setOnValueChangeListener(this);
    }

    public void setOnDateChangedListener(OnDateChangedListener listener){
        mOnDateChangedListener = listener;
    }

    public void setYearRange(int min,int max){
        mMinYear = min;
        mMaxYear = max;

        int oldYear = mYearPicker.getValue();
        int newYear = Math.max(oldYear,min);
        newYear = Math.min(newYear,max);

        mYearPicker.setRange(newYear,min,max);

        if(newYear!=oldYear)
            update(true,newYear);
    }

    //月份输入输出范围为[1,12]
    public void setDate(int year,int month,int day){
        //判断是否符合要求
        if(year<=0)
            throw new IllegalArgumentException("年份必须大于0");

        if(month<1 || month>12)
            throw new IllegalArgumentException("月份必须在1与12之间");

        int maxDay = getMaxDayInYearMonth(mInstance,year,month-1);
        if(day<1 || day>maxDay)
            throw new IllegalArgumentException("天数应该在1与最大之间");

        mYearPicker.setRange(year,mMinYear,mMaxYear);
        mMonthPicker.setRange(month,1,12);
        mDayPicker.setRange(day,1,maxDay);
    }

    private void update(boolean isYearChanged,int value){
        final int newYear;
        final int newMonth;

        if(isYearChanged){
            newYear = value;
            newMonth = mMonthPicker.getValue()-1;
        }else{
            newYear = mYearPicker.getValue();
            newMonth = value-1;
        }

        int maxDay = getMaxDayInYearMonth(mInstance,newYear,newMonth);
        int dayPickerMax = mDayPicker.getMaxValue();
        int dayPickerSelected = Math.min(maxDay,mDayPicker.getValue());
        if(dayPickerMax!=maxDay)
            mDayPicker.setRange(dayPickerSelected,1,maxDay);
    }

    public int getMaxDayInYearMonth(Calendar cal,int year, int month) {
        cal.set(Calendar.YEAR, year - 1);
        cal.set(Calendar.MONTH, month);
        return cal.getActualMaximum(Calendar.DATE);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if(mDivider!=null){
            int centerY = getPaddingTop()+(getHeight()-getPaddingTop()-getPaddingBottom())/2;
            int halfCenterHeight = (int) (mYearPicker.getCenterRangeHeight()+.5f)/2;
            int width = getWidth();
            int height = mDivider.getIntrinsicHeight();

            canvas.translate(0,centerY);
            mDivider.setBounds(0,-halfCenterHeight-height,width,-halfCenterHeight);
            mDivider.draw(canvas);
            mDivider.setBounds(0,halfCenterHeight,width,halfCenterHeight+height);
            mDivider.draw(canvas);
            canvas.translate(0,-centerY);
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        boolean changed = false;

        switch (picker.getId()){
            case R.id.year:
                changed = true;
                update(true,newVal);
                break;

            case R.id.month:
                changed = true;
                update(false,newVal);
                break;

            case R.id.day:
                changed = true;
                break;
        }

        if(changed && mOnDateChangedListener!=null)
            mOnDateChangedListener.onDateChanged(this,mYearPicker.getValue(),mMonthPicker.getValue(),mDayPicker.getValue());
    }

    public interface OnDateChangedListener {
        void onDateChanged(DatePicker view,int year,int month,int day);
    }

}
