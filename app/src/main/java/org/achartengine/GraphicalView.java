package org.achartengine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.view.View;
import org.achartengine.chart.AbstractChart;

public class GraphicalView extends View {
    private AbstractChart mChart;
    private Handler mHandler;
    private Rect mRect;

    public GraphicalView(Context context, AbstractChart abstractChart) {
        super(context);
        this.mRect = new Rect();
        this.mChart = abstractChart;
        this.mHandler = new Handler();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.getClipBounds(this.mRect);
        int i = this.mRect.top;
        this.mChart.draw(canvas, this.mRect.left, i, this.mRect.width(), this.mRect.height());
    }

    public void repaint() {
        this.mHandler.post(new Runnable() {
            public void run() {
                GraphicalView.this.invalidate();
            }
        });
    }
}