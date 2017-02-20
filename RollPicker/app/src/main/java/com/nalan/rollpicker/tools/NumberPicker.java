package com.nalan.rollpicker.tools;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.nalan.rollpicker.R;
import com.nalan.rollpicker.core.PickerAdapter;
import com.nalan.rollpicker.core.PickerView;

/**
 * Author： liyi
 * Date：    2017/1/10.
 */

public class NumberPicker extends PickerView{
    private OnValueChangeListener mOnValueChangeListener;
    private PickerAdapter mAdapter;
    private int mMinValue,mMaxValue;

    public NumberPicker(Context context) {
        this(context,null);
    }

    public NumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray tValue = context.obtainStyledAttributes(attrs, R.styleable.NumberPicker);
        final int initValue = tValue.getInteger(R.styleable.NumberPicker_initValue,0);
        mMinValue = tValue.getInteger(R.styleable.NumberPicker_minValue,0);
        mMaxValue = tValue.getInteger(R.styleable.NumberPicker_maxValue,0);
        tValue.recycle();

        mAdapter = new NumberAdapter();
        setAdapter(mAdapter,initValue);

        setOnPickListener(new OnPickListener() {
            @Override
            public void onPick(PickerView picker, int oldPosition, int newPosition) {
                if(mOnValueChangeListener !=null)
                    mOnValueChangeListener.onValueChange(NumberPicker.this,mMinValue+oldPosition,mMinValue+newPosition);
            }
        });
    }

    public void setRange(int init,int min,int max){
        mMinValue = min;
        mMaxValue = max;
        mAdapter.notifyDataSetChanged();

        setValue(init);
    }

    public int getMinValue(){
        return mMinValue;
    }

    public int getMaxValue(){
        return mMaxValue;
    }

    public void setValue(int value) {
        setValueIndex(value-mMinValue);
    }

    public int getValue(){
        return mMinValue+getValueIndex();
    }

    public void setOnValueChangeListener(OnValueChangeListener listener){
        mOnValueChangeListener = listener;
    }

    public interface OnValueChangeListener{
        void onValueChange(NumberPicker picker, int oldVal, int newVal);
    }

    class NumberAdapter extends PickerAdapter{
        @Override
        public String getText(int pos) {
            return Integer.toString(mMinValue+pos);
        }

        @Override
        public int getCount() {
            return mMaxValue-mMinValue+1;
        }
    }

}
