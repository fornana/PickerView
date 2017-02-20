package com.nalan.rollpicker.core;

import android.database.DataSetObservable;
import android.database.DataSetObserver;

/**
 * Author： liyi
 * Date：    2017/1/5.
 */

public abstract class PickerAdapter  {
    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    public void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    public void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }

    public boolean isEmpty() {
        return getCount() == 0;
    }

    public abstract String getText(int pos);
    public abstract int getCount();
}
