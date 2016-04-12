package org.achartengine.renderer;

import java.util.HashMap;
import java.util.Map;
import org.achartengine.util.MathHelper;
import org.xmlpull.v1.XmlPullParser;

public class XYMultipleSeriesRenderer extends DefaultRenderer {
    private float mAxisTitleTextSize;
    private String mChartTitle;
    private float mChartTitleTextSize;
    private float mChartValuesTextSize;
    private boolean mDisplayChartValues;
    private double mMaxX;
    private double mMaxY;
    private double mMinX;
    private double mMinY;
    private Orientation mOrientation;
    private int mXLabels;
    private Map<Double, String> mXTextLabels;
    private String mXTitle;
    private int mYLabels;
    private String mYTitle;

    public enum Orientation {
        HORIZONTAL(0),
        VERTICAL(90);
        
        private int mAngle;

        private Orientation(int i) {
            this.mAngle = 0;
            this.mAngle = i;
        }

        public int getAngle() {
            return this.mAngle;
        }
    }

    public XYMultipleSeriesRenderer() {
        this.mChartTitle = XmlPullParser.NO_NAMESPACE;
        this.mChartTitleTextSize = 15.0f;
        this.mXTitle = XmlPullParser.NO_NAMESPACE;
        this.mYTitle = XmlPullParser.NO_NAMESPACE;
        this.mAxisTitleTextSize = 12.0f;
        this.mMinX = MathHelper.NULL_VALUE;
        this.mMaxX = -1.7976931348623157E308d;
        this.mMinY = MathHelper.NULL_VALUE;
        this.mMaxY = -1.7976931348623157E308d;
        this.mXLabels = 5;
        this.mYLabels = 5;
        this.mOrientation = Orientation.HORIZONTAL;
        this.mXTextLabels = new HashMap();
        this.mChartValuesTextSize = 9.0f;
    }

    public void addTextLabel(double d, String str) {
        this.mXTextLabels.put(Double.valueOf(d), str);
    }

    public float getAxisTitleTextSize() {
        return this.mAxisTitleTextSize;
    }

    public String getChartTitle() {
        return this.mChartTitle;
    }

    public float getChartTitleTextSize() {
        return this.mChartTitleTextSize;
    }

    public float getChartValuesTextSize() {
        return this.mChartValuesTextSize;
    }

    public Orientation getOrientation() {
        return this.mOrientation;
    }

    public double getXAxisMax() {
        return this.mMaxX;
    }

    public double getXAxisMin() {
        return this.mMinX;
    }

    public int getXLabels() {
        return this.mXLabels;
    }

    public String getXTextLabel(Double d) {
        return (String) this.mXTextLabels.get(d);
    }

    public Double[] getXTextLabelLocations() {
        return (Double[]) this.mXTextLabels.keySet().toArray(new Double[0]);
    }

    public String getXTitle() {
        return this.mXTitle;
    }

    public double getYAxisMax() {
        return this.mMaxY;
    }

    public double getYAxisMin() {
        return this.mMinY;
    }

    public int getYLabels() {
        return this.mYLabels;
    }

    public String getYTitle() {
        return this.mYTitle;
    }

    public boolean isDisplayChartValues() {
        return this.mDisplayChartValues;
    }

    public boolean isMaxXSet() {
        return this.mMaxX != -1.7976931348623157E308d;
    }

    public boolean isMaxYSet() {
        return this.mMaxY != -1.7976931348623157E308d;
    }

    public boolean isMinXSet() {
        return this.mMinX != MathHelper.NULL_VALUE;
    }

    public boolean isMinYSet() {
        return this.mMinY != MathHelper.NULL_VALUE;
    }

    public void setAxisTitleTextSize(float f) {
        this.mAxisTitleTextSize = f;
    }

    public void setChartTitle(String str) {
        this.mChartTitle = str;
    }

    public void setChartTitleTextSize(float f) {
        this.mChartTitleTextSize = f;
    }

    public void setChartValuesTextSize(float f) {
        this.mChartValuesTextSize = f;
    }

    public void setDisplayChartValues(boolean z) {
        this.mDisplayChartValues = z;
    }

    public void setOrientation(Orientation orientation) {
        this.mOrientation = orientation;
    }

    public void setXAxisMax(double d) {
        this.mMaxX = d;
    }

    public void setXAxisMin(double d) {
        this.mMinX = d;
    }

    public void setXLabels(int i) {
        this.mXLabels = i;
    }

    public void setXTitle(String str) {
        this.mXTitle = str;
    }

    public void setYAxisMax(double d) {
        this.mMaxY = d;
    }

    public void setYAxisMin(double d) {
        this.mMinY = d;
    }

    public void setYLabels(int i) {
        this.mYLabels = i;
    }

    public void setYTitle(String str) {
        this.mYTitle = str;
    }
}