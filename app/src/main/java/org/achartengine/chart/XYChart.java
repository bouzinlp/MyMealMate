package org.achartengine.chart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.graphics.Typeface;
import java.util.List;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;
import org.achartengine.util.MathHelper;
import org.xmlpull.v1.XmlPullParser;

public abstract class XYChart extends AbstractChart {
    protected static final int GRID_COLOR;
    private PointF mCenter;
    protected XYMultipleSeriesDataset mDataset;
    protected XYMultipleSeriesRenderer mRenderer;
    private float mScale;
    private float mTranslate;

    static {
        GRID_COLOR = Color.argb(75, 200, 200, 200);
    }

    public XYChart(XYMultipleSeriesDataset xYMultipleSeriesDataset, XYMultipleSeriesRenderer xYMultipleSeriesRenderer) {
        this.mDataset = xYMultipleSeriesDataset;
        this.mRenderer = xYMultipleSeriesRenderer;
    }

    private void transform(Canvas canvas, float f, boolean z) {
        if (z) {
            canvas.scale(1.0f / this.mScale, this.mScale);
            canvas.translate(this.mTranslate, -this.mTranslate);
            canvas.rotate(-f, this.mCenter.x, this.mCenter.y);
            return;
        }
        canvas.rotate(f, this.mCenter.x, this.mCenter.y);
        canvas.translate(-this.mTranslate, this.mTranslate);
        canvas.scale(this.mScale, 1.0f / this.mScale);
    }

    public void draw(Canvas canvas, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        double d;
        Paint paint = new Paint();
        paint.setAntiAlias(this.mRenderer.isAntialiasing());
        int i7 = 30;
        if (this.mRenderer.isShowLegend()) {
            i7 = i4 / 5;
        }
        int i8 = i + 20;
        int i9 = i2 + 10;
        int i10 = i + i3;
        int i11 = (i2 + i4) - i7;
        drawBackground(this.mRenderer, canvas, i, i2, i3, i4, paint);
        if (!(paint.getTypeface() != null && paint.getTypeface().toString().equals(this.mRenderer.getTextTypefaceName()) && paint.getTypeface().getStyle() == this.mRenderer.getTextTypefaceStyle())) {
            paint.setTypeface(Typeface.create(this.mRenderer.getTextTypefaceName(), this.mRenderer.getTextTypefaceStyle()));
        }
        Orientation orientation = this.mRenderer.getOrientation();
        if (orientation == Orientation.VERTICAL) {
            i5 = (i7 - 20) + i11;
            i6 = i10 - i7;
        } else {
            i6 = i10;
            i5 = i11;
        }
        int angle = orientation.getAngle();
        Object obj = angle == 90 ? 1 : null;
        this.mScale = ((float) i4) / ((float) i3);
        this.mTranslate = (float) (Math.abs(i3 - i4) / 2);
        if (this.mScale < 1.0f) {
            this.mTranslate *= -1.0f;
        }
        this.mCenter = new PointF((float) ((i + i3) / 2), (float) ((i2 + i4) / 2));
        if (obj != null) {
            transform(canvas, (float) angle, false);
        }
        double xAxisMin = this.mRenderer.getXAxisMin();
        double xAxisMax = this.mRenderer.getXAxisMax();
        double yAxisMin = this.mRenderer.getYAxisMin();
        double yAxisMax = this.mRenderer.getYAxisMax();
        boolean isMinXSet = this.mRenderer.isMinXSet();
        boolean isMaxXSet = this.mRenderer.isMaxXSet();
        boolean isMinYSet = this.mRenderer.isMinYSet();
        boolean isMaxYSet = this.mRenderer.isMaxYSet();
        int seriesCount = this.mDataset.getSeriesCount();
        String[] strArr = new String[seriesCount];
        int i12 = 0;
        while (i12 < seriesCount) {
            double d2;
            XYSeries seriesAt = this.mDataset.getSeriesAt(i12);
            strArr[i12] = seriesAt.getTitle();
            if (seriesAt.getItemCount() == 0) {
                d2 = yAxisMin;
                d = xAxisMax;
            } else {
                if (!isMinXSet) {
                    xAxisMin = Math.min(xAxisMin, seriesAt.getMinX());
                }
                d = !isMaxXSet ? Math.max(xAxisMax, seriesAt.getMaxX()) : xAxisMax;
                d2 = !isMinYSet ? Math.min(yAxisMin, (double) ((float) seriesAt.getMinY())) : yAxisMin;
                if (!isMaxYSet) {
                    yAxisMax = Math.max(yAxisMax, (double) ((float) seriesAt.getMaxY()));
                }
            }
            i12++;
            xAxisMax = d;
            yAxisMin = d2;
        }
        double d3 = xAxisMax - xAxisMin != 0.0d ? ((double) (i6 - i8)) / (xAxisMax - xAxisMin) : 0.0d;
        double d4 = yAxisMax - yAxisMin != 0.0d ? (double) ((float) (((double) (i5 - i9)) / (yAxisMax - yAxisMin))) : 0.0d;
        Object obj2 = null;
        for (int i13 = 0; i13 < seriesCount; i13++) {
            XYSeries seriesAt2 = this.mDataset.getSeriesAt(i13);
            if (seriesAt2.getItemCount() != 0) {
                obj2 = 1;
                SimpleSeriesRenderer seriesRendererAt = this.mRenderer.getSeriesRendererAt(i13);
                int itemCount = seriesAt2.getItemCount() * 2;
                float[] fArr = new float[itemCount];
                for (int i14 = 0; i14 < itemCount; i14 += 2) {
                    int i15 = i14 / 2;
                    fArr[i14] = (float) (((double) i8) + ((seriesAt2.getX(i15) - xAxisMin) * d3));
                    fArr[i14 + 1] = (float) (((double) i5) - ((seriesAt2.getY(i15) - yAxisMin) * d4));
                }
                drawSeries(canvas, paint, fArr, seriesRendererAt, Math.min((float) i5, (float) (((double) i5) + (d4 * yAxisMin))), i13);
                if (isRenderPoints(seriesRendererAt)) {
                    new ScatterChart(this.mDataset, this.mRenderer).drawSeries(canvas, paint, fArr, seriesRendererAt, 0.0f, i13);
                }
                paint.setTextSize(this.mRenderer.getChartValuesTextSize());
                if (orientation == Orientation.HORIZONTAL) {
                    paint.setTextAlign(Align.CENTER);
                } else {
                    paint.setTextAlign(Align.LEFT);
                }
                if (this.mRenderer.isDisplayChartValues()) {
                    drawChartValuesText(canvas, seriesAt2, paint, fArr, i13);
                }
            }
        }
        Object obj3 = (!this.mRenderer.isShowLabels() || obj2 == null) ? null : 1;
        boolean isShowGrid = this.mRenderer.isShowGrid();
        if (obj3 != null || isShowGrid) {
            List labels = MathHelper.getLabels(xAxisMin, xAxisMax, this.mRenderer.getXLabels());
            List labels2 = MathHelper.getLabels(yAxisMin, yAxisMax, this.mRenderer.getYLabels());
            if (obj3 != null) {
                paint.setColor(this.mRenderer.getLabelsColor());
                paint.setTextSize(this.mRenderer.getLabelsTextSize());
                paint.setTextAlign(Align.CENTER);
            }
            drawXLabels(labels, this.mRenderer.getXTextLabelLocations(), canvas, paint, i8, i9, i5, d3, xAxisMin);
            int size = labels2.size();
            for (int i16 = 0; i16 < size; i16++) {
                d = ((Double) labels2.get(i16)).doubleValue();
                float f = (float) (((double) i5) - ((d - yAxisMin) * d4));
                if (orientation == Orientation.HORIZONTAL) {
                    if (obj3 != null) {
                        paint.setColor(this.mRenderer.getLabelsColor());
                        canvas.drawLine((float) (i8 - 4), f, (float) i8, f, paint);
                        drawText(canvas, getLabel(d), (float) (i8 - 2), f - 2.0f, paint, 0);
                    }
                    if (isShowGrid) {
                        paint.setColor(GRID_COLOR);
                        canvas.drawLine((float) i8, f, (float) i6, f, paint);
                    }
                } else if (orientation == Orientation.VERTICAL) {
                    if (obj3 != null) {
                        paint.setColor(this.mRenderer.getLabelsColor());
                        canvas.drawLine((float) (i6 + 4), f, (float) i6, f, paint);
                        drawText(canvas, getLabel(d), (float) (i6 + 10), f - 2.0f, paint, 0);
                    }
                    if (isShowGrid) {
                        paint.setColor(GRID_COLOR);
                        canvas.drawLine((float) i6, f, (float) i8, f, paint);
                    }
                }
            }
            if (obj3 != null) {
                paint.setColor(this.mRenderer.getLabelsColor());
                paint.setTextSize(this.mRenderer.getAxisTitleTextSize());
                paint.setTextAlign(Align.CENTER);
                if (orientation == Orientation.HORIZONTAL) {
                    drawText(canvas, this.mRenderer.getXTitle(), (float) ((i3 / 2) + i), (float) (i5 + 24), paint, 0);
                    drawText(canvas, this.mRenderer.getYTitle(), (float) (i + 10), (float) ((i4 / 2) + i2), paint, -90);
                    paint.setTextSize(this.mRenderer.getChartTitleTextSize());
                    drawText(canvas, this.mRenderer.getChartTitle(), (float) ((i3 / 2) + i), (float) (i9 + 10), paint, 0);
                } else if (orientation == Orientation.VERTICAL) {
                    drawText(canvas, this.mRenderer.getXTitle(), (float) ((i3 / 2) + i), (float) ((i2 + i4) - 10), paint, -90);
                    drawText(canvas, this.mRenderer.getYTitle(), (float) (i6 + 20), (float) ((i4 / 2) + i2), paint, 0);
                    paint.setTextSize(this.mRenderer.getChartTitleTextSize());
                    drawText(canvas, this.mRenderer.getChartTitle(), (float) (i + 14), (float) ((i4 / 2) + i9), paint, 0);
                }
            }
        }
        if (orientation == Orientation.HORIZONTAL) {
            drawLegend(canvas, this.mRenderer, strArr, i8, i6, i2, i3, i4, i7, paint);
        } else if (orientation == Orientation.VERTICAL) {
            transform(canvas, (float) angle, true);
            drawLegend(canvas, this.mRenderer, strArr, i8, i6, i2, i3, i4, i7, paint);
            transform(canvas, (float) angle, false);
        }
        if (this.mRenderer.isShowAxes()) {
            paint.setColor(this.mRenderer.getAxesColor());
            canvas.drawLine((float) i8, (float) i5, (float) i6, (float) i5, paint);
            if (orientation == Orientation.HORIZONTAL) {
                canvas.drawLine((float) i8, (float) i9, (float) i8, (float) i5, paint);
            } else if (orientation == Orientation.VERTICAL) {
                canvas.drawLine((float) i6, (float) i9, (float) i6, (float) i5, paint);
            }
        }
        if (obj != null) {
            transform(canvas, (float) angle, true);
        }
    }

    protected void drawChartValuesText(Canvas canvas, XYSeries xYSeries, Paint paint, float[] fArr, int i) {
        for (int i2 = 0; i2 < fArr.length; i2 += 2) {
            drawText(canvas, getLabel(xYSeries.getY(i2 / 2)), fArr[i2], fArr[i2 + 1] - 3.5f, paint, 0);
        }
    }

    public abstract void drawSeries(Canvas canvas, Paint paint, float[] fArr, SimpleSeriesRenderer simpleSeriesRenderer, float f, int i);

    protected void drawText(Canvas canvas, String str, float f, float f2, Paint paint, int i) {
        int i2 = (-this.mRenderer.getOrientation().getAngle()) + i;
        if (i2 != 0) {
            canvas.rotate((float) i2, f, f2);
        }
        canvas.drawText(str, f, f2, paint);
        if (i2 != 0) {
            canvas.rotate((float) (-i2), f, f2);
        }
    }

    protected void drawXLabels(List<Double> list, Double[] dArr, Canvas canvas, Paint paint, int i, int i2, int i3, double d, double d2) {
        int size = list.size();
        boolean isShowLabels = this.mRenderer.isShowLabels();
        boolean isShowGrid = this.mRenderer.isShowGrid();
        for (int i4 = 0; i4 < size; i4++) {
            double doubleValue = ((Double) list.get(i4)).doubleValue();
            float f = (float) (((double) i) + ((doubleValue - d2) * d));
            if (isShowLabels) {
                paint.setColor(this.mRenderer.getLabelsColor());
                canvas.drawLine(f, (float) i3, f, (float) (i3 + 4), paint);
                drawText(canvas, getLabel(doubleValue), f, (float) (i3 + 12), paint, 0);
            }
            if (isShowGrid) {
                paint.setColor(GRID_COLOR);
                canvas.drawLine(f, (float) i3, f, (float) i2, paint);
            }
        }
        if (isShowLabels) {
            paint.setColor(this.mRenderer.getLabelsColor());
            for (Double d3 : dArr) {
                f = (float) (((double) i) + ((d3.doubleValue() - d2) * d));
                canvas.drawLine(f, (float) i3, f, (float) (i3 + 4), paint);
                drawText(canvas, this.mRenderer.getXTextLabel(d3), f, (float) (i3 + 12), paint, 0);
            }
        }
    }

    protected String getLabel(double d) {
        return d == ((double) Math.round(d)) ? Math.round(d) + XmlPullParser.NO_NAMESPACE : d + XmlPullParser.NO_NAMESPACE;
    }

    public boolean isRenderPoints(SimpleSeriesRenderer simpleSeriesRenderer) {
        return false;
    }
}