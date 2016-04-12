package org.achartengine.renderer;

import android.graphics.Color;
import org.achartengine.chart.PointStyle;

public class XYSeriesRenderer extends SimpleSeriesRenderer {
    private boolean mFillBelowLine;
    private int mFillColor;
    private boolean mFillPoints;
    private float mLineWidth;
    private PointStyle mPointStyle;

    public XYSeriesRenderer() {
        this.mFillPoints = false;
        this.mFillBelowLine = false;
        this.mFillColor = Color.argb(125, 0, 0, 200);
        this.mPointStyle = PointStyle.POINT;
        this.mLineWidth = 1.0f;
    }

    public int getFillBelowLineColor() {
        return this.mFillColor;
    }

    public float getLineWidth() {
        return this.mLineWidth;
    }

    public PointStyle getPointStyle() {
        return this.mPointStyle;
    }

    public boolean isFillBelowLine() {
        return this.mFillBelowLine;
    }

    public boolean isFillPoints() {
        return this.mFillPoints;
    }

    public void setFillBelowLine(boolean z) {
        this.mFillBelowLine = z;
    }

    public void setFillBelowLineColor(int i) {
        this.mFillColor = i;
    }

    public void setFillPoints(boolean z) {
        this.mFillPoints = z;
    }

    public void setLineWidth(float f) {
        this.mLineWidth = f;
    }

    public void setPointStyle(PointStyle pointStyle) {
        this.mPointStyle = pointStyle;
    }
}