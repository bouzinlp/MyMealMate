package org.achartengine.model;

import java.util.ArrayList;
import java.util.List;
import org.achartengine.util.MathHelper;

public class XYValueSeries extends XYSeries {
    private double mMaxValue;
    private double mMinValue;
    private List<Double> mValue;

    public XYValueSeries(String str) {
        super(str);
        this.mValue = new ArrayList();
        this.mMinValue = MathHelper.NULL_VALUE;
        this.mMaxValue = -1.7976931348623157E308d;
    }

    private void initRange() {
        this.mMinValue = MathHelper.NULL_VALUE;
        this.mMaxValue = MathHelper.NULL_VALUE;
        int itemCount = getItemCount();
        for (int i = 0; i < itemCount; i++) {
            updateRange(getValue(i));
        }
    }

    private void updateRange(double d) {
        this.mMinValue = Math.min(this.mMinValue, d);
        this.mMaxValue = Math.max(this.mMaxValue, d);
    }

    public void add(double d, double d2) {
        add(d, d2, 0.0d);
    }

    public void add(double d, double d2, double d3) {
        super.add(d, d2);
        this.mValue.add(Double.valueOf(d3));
        updateRange(d3);
    }

    public void clear() {
        super.clear();
        this.mValue.clear();
        initRange();
    }

    public double getMaxValue() {
        return this.mMaxValue;
    }

    public double getMinValue() {
        return this.mMinValue;
    }

    public double getValue(int i) {
        return ((Double) this.mValue.get(i)).doubleValue();
    }

    public void remove(int i) {
        super.remove(i);
        double doubleValue = ((Double) this.mValue.remove(i)).doubleValue();
        if (doubleValue == this.mMinValue || doubleValue == this.mMaxValue) {
            initRange();
        }
    }
}