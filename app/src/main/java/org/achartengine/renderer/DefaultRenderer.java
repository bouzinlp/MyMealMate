package org.achartengine.renderer;

import android.graphics.Typeface;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultRenderer implements Serializable {
    public static final int BACKGROUND_COLOR = -16777216;
    private static final Typeface REGULAR_TEXT_FONT;
    public static final int TEXT_COLOR = -3355444;
    private boolean antialiasing;
    private boolean mApplyBackgroundColor;
    private int mAxesColor;
    private int mBackgroundColor;
    private int mLabelsColor;
    private float mLabelsTextSize;
    private float mLegendTextSize;
    private List<SimpleSeriesRenderer> mRenderers;
    private boolean mShowAxes;
    private boolean mShowGrid;
    private boolean mShowLabels;
    private boolean mShowLegend;
    private String textTypefaceName;
    private int textTypefaceStyle;

    static {
        REGULAR_TEXT_FONT = Typeface.create(Typeface.SERIF, 0);
    }

    public DefaultRenderer() {
        this.textTypefaceName = REGULAR_TEXT_FONT.toString();
        this.textTypefaceStyle = 0;
        this.mShowAxes = true;
        this.mAxesColor = TEXT_COLOR;
        this.mShowLabels = true;
        this.mLabelsColor = TEXT_COLOR;
        this.mLabelsTextSize = 9.0f;
        this.mShowLegend = true;
        this.mLegendTextSize = 12.0f;
        this.mShowGrid = false;
        this.mRenderers = new ArrayList();
        this.antialiasing = true;
    }

    public void addSeriesRenderer(SimpleSeriesRenderer simpleSeriesRenderer) {
        this.mRenderers.add(simpleSeriesRenderer);
    }

    public int getAxesColor() {
        return this.mAxesColor;
    }

    public int getBackgroundColor() {
        return this.mBackgroundColor;
    }

    public int getLabelsColor() {
        return this.mLabelsColor;
    }

    public float getLabelsTextSize() {
        return this.mLabelsTextSize;
    }

    public float getLegendTextSize() {
        return this.mLegendTextSize;
    }

    public SimpleSeriesRenderer getSeriesRendererAt(int i) {
        return (SimpleSeriesRenderer) this.mRenderers.get(i);
    }

    public int getSeriesRendererCount() {
        return this.mRenderers.size();
    }

    public SimpleSeriesRenderer[] getSeriesRenderers() {
        return (SimpleSeriesRenderer[]) this.mRenderers.toArray(new SimpleSeriesRenderer[0]);
    }

    public String getTextTypefaceName() {
        return this.textTypefaceName;
    }

    public int getTextTypefaceStyle() {
        return this.textTypefaceStyle;
    }

    public boolean isAntialiasing() {
        return this.antialiasing;
    }

    public boolean isApplyBackgroundColor() {
        return this.mApplyBackgroundColor;
    }

    public boolean isShowAxes() {
        return this.mShowAxes;
    }

    public boolean isShowGrid() {
        return this.mShowGrid;
    }

    public boolean isShowLabels() {
        return this.mShowLabels;
    }

    public boolean isShowLegend() {
        return this.mShowLegend;
    }

    public void removeSeriesRenderer(SimpleSeriesRenderer simpleSeriesRenderer) {
        this.mRenderers.remove(simpleSeriesRenderer);
    }

    public void setAntialiasing(boolean z) {
        this.antialiasing = z;
    }

    public void setApplyBackgroundColor(boolean z) {
        this.mApplyBackgroundColor = z;
    }

    public void setAxesColor(int i) {
        this.mAxesColor = i;
    }

    public void setBackgroundColor(int i) {
        this.mBackgroundColor = i;
    }

    public void setLabelsColor(int i) {
        this.mLabelsColor = i;
    }

    public void setLabelsTextSize(float f) {
        this.mLabelsTextSize = f;
    }

    public void setLegendTextSize(float f) {
        this.mLegendTextSize = f;
    }

    public void setShowAxes(boolean z) {
        this.mShowAxes = z;
    }

    public void setShowGrid(boolean z) {
        this.mShowGrid = z;
    }

    public void setShowLabels(boolean z) {
        this.mShowLabels = z;
    }

    public void setShowLegend(boolean z) {
        this.mShowLegend = z;
    }

    public void setTextTypeface(String str, int i) {
        this.textTypefaceName = str;
        this.textTypefaceStyle = i;
    }
}