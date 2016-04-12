package org.achartengine.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

public class DoughnutChart extends AbstractChart {
    private static final int SHAPE_WIDTH = 10;
    private MultipleCategorySeries mDataset;
    private DefaultRenderer mRenderer;
    private int mStep;

    public DoughnutChart(MultipleCategorySeries multipleCategorySeries, DefaultRenderer defaultRenderer) {
        this.mDataset = multipleCategorySeries;
        this.mRenderer = defaultRenderer;
    }

    public void draw(Canvas canvas, int i, int i2, int i3, int i4) {
        Paint paint = new Paint();
        paint.setAntiAlias(this.mRenderer.isAntialiasing());
        paint.setStyle(Style.FILL);
        paint.setTextSize(this.mRenderer.getLabelsTextSize());
        int i5 = 30;
        if (this.mRenderer.isShowLegend()) {
            i5 = i4 / 5;
        }
        int i6 = i + 15;
        int i7 = i2 + 5;
        int i8 = (i + i3) - 5;
        int i9 = (i2 + i4) - i5;
        drawBackground(this.mRenderer, canvas, i, i2, i3, i4, paint);
        this.mStep = 7;
        int categoriesCount = this.mDataset.getCategoriesCount();
        int min = Math.min(Math.abs(i8 - i6), Math.abs(i9 - i7));
        double d = 0.2d / ((double) categoriesCount);
        int i10 = (int) (0.35d * ((double) min));
        int i11 = (i6 + i8) / 2;
        int i12 = (i9 + i7) / 2;
        float f = ((float) i10) * 1.1f;
        String[] strArr = new String[categoriesCount];
        int i13 = i10;
        float f2 = ((float) i10) * 0.9f;
        for (int i14 = 0; i14 < categoriesCount; i14++) {
            int itemCount = this.mDataset.getItemCount(i14);
            String[] strArr2 = new String[itemCount];
            i10 = 0;
            double d2 = 0.0d;
            while (i10 < itemCount) {
                double d3 = this.mDataset.getValues(i14)[i10];
                strArr2[i10] = this.mDataset.getTitles(i14)[i10];
                i10++;
                d2 = d3 + d2;
            }
            float f3 = 0.0f;
            RectF rectF = new RectF((float) (i11 - i13), (float) (i12 - i13), (float) (i11 + i13), (float) (i12 + i13));
            for (int i15 = 0; i15 < itemCount; i15++) {
                paint.setColor(this.mRenderer.getSeriesRendererAt(i15).getColor());
                float f4 = (float) ((((double) ((float) this.mDataset.getValues(i14)[i15])) / d2) * 360.0d);
                canvas.drawArc(rectF, f3, f4, true, paint);
                if (this.mRenderer.isShowLabels()) {
                    paint.setColor(this.mRenderer.getLabelsColor());
                    double toRadians = Math.toRadians((double) (90.0f - ((f4 / 2.0f) + f3)));
                    double sin = Math.sin(toRadians);
                    toRadians = Math.cos(toRadians);
                    int round = Math.round(((float) i11) + ((float) (((double) f2) * sin)));
                    i10 = Math.round(((float) i12) + ((float) (((double) f2) * toRadians)));
                    int round2 = Math.round(((float) i11) + ((float) (sin * ((double) f))));
                    int round3 = Math.round(((float) (toRadians * ((double) f))) + ((float) i12));
                    canvas.drawLine((float) round, (float) i10, (float) round2, (float) round3, paint);
                    i10 = SHAPE_WIDTH;
                    paint.setTextAlign(Align.LEFT);
                    if (round > round2) {
                        i10 = -SHAPE_WIDTH;
                        paint.setTextAlign(Align.RIGHT);
                    }
                    canvas.drawLine((float) round2, (float) round3, (float) (round2 + i10), (float) round3, paint);
                    canvas.drawText(this.mDataset.getTitles(i14)[i15], (float) (i10 + round2), (float) (round3 + 5), paint);
                }
                f3 += f4;
            }
            i10 = (int) (((double) i13) - (((double) min) * d));
            float f5 = (float) (((double) f2) - ((((double) min) * d) - 2.0d));
            if (this.mRenderer.getBackgroundColor() != 0) {
                paint.setColor(this.mRenderer.getBackgroundColor());
            } else {
                paint.setColor(-1);
            }
            paint.setStyle(Style.FILL);
            canvas.drawArc(new RectF((float) (i11 - i10), (float) (i12 - i10), (float) (i11 + i10), (float) (i12 + i10)), 0.0f, 360.0f, true, paint);
            strArr[i14] = this.mDataset.getCategory(i14);
            i13 = i10 - 1;
            f2 = f5;
        }
        drawLegend(canvas, this.mRenderer, strArr, i6, i8, i2, i3, i4, i5, paint);
    }

    public void drawLegendShape(Canvas canvas, SimpleSeriesRenderer simpleSeriesRenderer, float f, float f2, Paint paint) {
        this.mStep--;
        canvas.drawCircle((10.0f + f) - ((float) this.mStep), f2, (float) this.mStep, paint);
    }

    public int getLegendShapeWidth() {
        return SHAPE_WIDTH;
    }
}