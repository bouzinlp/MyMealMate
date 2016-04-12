package org.achartengine.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

public class TimeChart extends LineChart {
    public static final long DAY = 86400000;
    private String mDateFormat;

    public TimeChart(XYMultipleSeriesDataset xYMultipleSeriesDataset, XYMultipleSeriesRenderer xYMultipleSeriesRenderer) {
        super(xYMultipleSeriesDataset, xYMultipleSeriesRenderer);
    }

    private DateFormat getDateFormat(double d, double d2) {
        if (this.mDateFormat != null) {
            try {
                return new SimpleDateFormat(this.mDateFormat);
            } catch (Exception e) {
            }
        }
        double d3 = d2 - d;
        return (d3 <= 8.64E7d || d3 >= 4.32E8d) ? d3 < 8.64E7d ? SimpleDateFormat.getTimeInstance(2) : SimpleDateFormat.getDateInstance(2) : SimpleDateFormat.getDateTimeInstance(3, 3);
    }

    protected void drawXLabels(List<Double> list, Double[] dArr, Canvas canvas, Paint paint, int i, int i2, int i3, double d, double d2) {
        int size = list.size();
        boolean isShowLabels = this.mRenderer.isShowLabels();
        boolean isShowGrid = this.mRenderer.isShowGrid();
        DateFormat dateFormat = getDateFormat(((Double) list.get(0)).doubleValue(), ((Double) list.get(size - 1)).doubleValue());
        for (int i4 = 0; i4 < size; i4++) {
            long round = Math.round(((Double) list.get(i4)).doubleValue());
            float f = (float) (((double) i) + ((((double) round) - d2) * d));
            if (isShowLabels) {
                paint.setColor(this.mRenderer.getLabelsColor());
                Canvas canvas2 = canvas;
                canvas2.drawLine(f, (float) i3, f, (float) (i3 + 4), paint);
                drawText(canvas, dateFormat.format(new Date(round)), f, (float) (i3 + 12), paint, 0);
            }
            if (isShowGrid) {
                paint.setColor(GRID_COLOR);
                canvas.drawLine(f, (float) i3, f, (float) i2, paint);
            }
        }
    }

    public String getDateFormat() {
        return this.mDateFormat;
    }

    public void setDateFormat(String str) {
        this.mDateFormat = str;
    }
}