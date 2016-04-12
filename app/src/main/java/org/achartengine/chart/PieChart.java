package org.achartengine.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

public class PieChart extends AbstractChart {
    private static final int SHAPE_WIDTH = 10;
    private CategorySeries mDataset;
    private DefaultRenderer mRenderer;

    public PieChart(CategorySeries categorySeries, DefaultRenderer defaultRenderer) {
        this.mDataset = categorySeries;
        this.mRenderer = defaultRenderer;
    }

    public void draw(Canvas canvas, int i, int i2, int i3, int i4) {
        int i5;
        Paint paint = new Paint();
        paint.setAntiAlias(this.mRenderer.isAntialiasing());
        paint.setStyle(Style.FILL);
        paint.setTextSize(this.mRenderer.getLabelsTextSize());
        int i6 = 30;
        if (this.mRenderer.isShowLegend()) {
            i6 = i4 / 5;
        }
        int i7 = i + 15;
        int i8 = i2 + 5;
        int i9 = (i + i3) - 5;
        int i10 = (i2 + i4) - i6;
        drawBackground(this.mRenderer, canvas, i, i2, i3, i4, paint);
        int itemCount = this.mDataset.getItemCount();
        String[] strArr = new String[itemCount];
        double d = 0.0d;
        for (i5 = 0; i5 < itemCount; i5++) {
            double value = this.mDataset.getValue(i5);
            strArr[i5] = this.mDataset.getCategory(i5);
            d = value + d;
        }
        float f = 0.0f;
        i5 = (int) (((double) Math.min(Math.abs(i9 - i7), Math.abs(i10 - i8))) * 0.35d);
        int i11 = (i7 + i9) / 2;
        int i12 = (i10 + i8) / 2;
        float f2 = ((float) i5) * 0.9f;
        float f3 = ((float) i5) * 1.1f;
        RectF rectF = new RectF((float) (i11 - i5), (float) (i12 - i5), (float) (i11 + i5), (float) (i5 + i12));
        for (int i13 = 0; i13 < itemCount; i13++) {
            paint.setColor(this.mRenderer.getSeriesRendererAt(i13).getColor());
            float value2 = (float) ((((double) ((float) this.mDataset.getValue(i13))) / d) * 360.0d);
            canvas.drawArc(rectF, f, value2, true, paint);
            if (this.mRenderer.isShowLabels()) {
                paint.setColor(this.mRenderer.getLabelsColor());
                double toRadians = Math.toRadians((double) (90.0f - ((value2 / 2.0f) + f)));
                double sin = Math.sin(toRadians);
                toRadians = Math.cos(toRadians);
                int round = Math.round(((float) i11) + ((float) (((double) f2) * sin)));
                i5 = Math.round(((float) i12) + ((float) (((double) f2) * toRadians)));
                int round2 = Math.round(((float) i11) + ((float) (sin * ((double) f3))));
                int round3 = Math.round(((float) (toRadians * ((double) f3))) + ((float) i12));
                canvas.drawLine((float) round, (float) i5, (float) round2, (float) round3, paint);
                i5 = SHAPE_WIDTH;
                paint.setTextAlign(Align.LEFT);
                if (round > round2) {
                    i5 = -SHAPE_WIDTH;
                    paint.setTextAlign(Align.RIGHT);
                }
                canvas.drawLine((float) round2, (float) round3, (float) (round2 + i5), (float) round3, paint);
                canvas.drawText(this.mDataset.getCategory(i13), (float) (i5 + round2), (float) (round3 + 5), paint);
            }
            f += value2;
        }
        drawLegend(canvas, this.mRenderer, strArr, i7, i9, i2, i3, i4, i6, paint);
    }

    public void drawLegendShape(Canvas canvas, SimpleSeriesRenderer simpleSeriesRenderer, float f, float f2, Paint paint) {
        canvas.drawRect(f, f2 - 5.0f, f + 10.0f, f2 + 5.0f, paint);
    }

    public int getLegendShapeWidth() {
        return SHAPE_WIDTH;
    }
}