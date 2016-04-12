package org.achartengine.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.achartengine.util.MathHelper;

public class XYSeries implements Serializable {
    private double mMaxX;
    private double mMaxY;
    private double mMinX;
    private double mMinY;
    private String mTitle;
    private List<Double> mX;
    private List<Double> mY;

    public XYSeries(String str) {
        this.mX = new ArrayList();
        this.mY = new ArrayList();
        this.mMinX = MathHelper.NULL_VALUE;
        this.mMaxX = -1.7976931348623157E308d;
        this.mMinY = MathHelper.NULL_VALUE;
        this.mMaxY = -1.7976931348623157E308d;
        this.mTitle = str;
        initRange();
    }

    private void initRange() {
        this.mMinX = MathHelper.NULL_VALUE;
        this.mMaxX = -1.7976931348623157E308d;
        this.mMinY = MathHelper.NULL_VALUE;
        this.mMaxY = -1.7976931348623157E308d;
        int itemCount = getItemCount();
        for (int i = 0; i < itemCount; i++) {
            updateRange(getX(i), getY(i));
        }
    }

    private void updateRange(double d, double d2) {
        this.mMinX = Math.min(this.mMinX, d);
        this.mMaxX = Math.max(this.mMaxX, d);
        this.mMinY = Math.min(this.mMinY, d2);
        this.mMaxY = Math.max(this.mMaxY, d2);
    }

    public void add(double d, double d2) {
        this.mX.add(Double.valueOf(d));
        this.mY.add(Double.valueOf(d2));
        updateRange(d, d2);
    }

    public void clear() {
        this.mX.clear();
        this.mY.clear();
        initRange();
    }

    public int getItemCount() {
        return this.mX.size();
    }

    public double getMaxX() {
        return this.mMaxX;
    }

    public double getMaxY() {
        return this.mMaxY;
    }

    public double getMinX() {
        return this.mMinX;
    }

    public double getMinY() {
        return this.mMinY;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public double getX(int i) {
        return ((Double) this.mX.get(i)).doubleValue();
    }

    public double getY(int i) {
        return ((Double) this.mY.get(i)).doubleValue();
    }

    public void remove(int i) {
        double doubleValue = ((Double) this.mX.remove(i)).doubleValue();
        double doubleValue2 = ((Double) this.mY.remove(i)).doubleValue();
        if (doubleValue == this.mMinX || doubleValue == this.mMaxX || doubleValue2 == this.mMinY || doubleValue2 == this.mMaxY) {
            initRange();
        }
    }

    public void setTitle(String str) {
        this.mTitle = str;
    }
}