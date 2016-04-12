package org.achartengine.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.kxml2.kdom.Node;
import org.kxml2.wap.Wbxml;

public class ScatterChart extends XYChart {
    private static final int SHAPE_WIDTH = 10;
    private static final float SIZE = 3.0f;

    /* renamed from: org.achartengine.chart.ScatterChart.1 */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$org$achartengine$chart$PointStyle;

        static {
            $SwitchMap$org$achartengine$chart$PointStyle = new int[PointStyle.values().length];
            try {
                $SwitchMap$org$achartengine$chart$PointStyle[PointStyle.X.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$achartengine$chart$PointStyle[PointStyle.CIRCLE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$achartengine$chart$PointStyle[PointStyle.TRIANGLE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$achartengine$chart$PointStyle[PointStyle.SQUARE.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$achartengine$chart$PointStyle[PointStyle.DIAMOND.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$achartengine$chart$PointStyle[PointStyle.POINT.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    public ScatterChart(XYMultipleSeriesDataset xYMultipleSeriesDataset, XYMultipleSeriesRenderer xYMultipleSeriesRenderer) {
        super(xYMultipleSeriesDataset, xYMultipleSeriesRenderer);
    }

    private void drawCircle(Canvas canvas, Paint paint, float f, float f2) {
        canvas.drawCircle(f, f2, SIZE, paint);
    }

    private void drawDiamond(Canvas canvas, Paint paint, float[] fArr, float f, float f2) {
        fArr[0] = f;
        fArr[1] = f2 - SIZE;
        fArr[2] = f - SIZE;
        fArr[3] = f2;
        fArr[4] = f;
        fArr[5] = f2 + SIZE;
        fArr[6] = f + SIZE;
        fArr[7] = f2;
        drawPath(canvas, fArr, paint, true);
    }

    private void drawSquare(Canvas canvas, Paint paint, float f, float f2) {
        canvas.drawRect(f - SIZE, f2 - SIZE, f + SIZE, f2 + SIZE, paint);
    }

    private void drawTriangle(Canvas canvas, Paint paint, float[] fArr, float f, float f2) {
        fArr[0] = f;
        fArr[1] = (f2 - SIZE) - 1.5f;
        fArr[2] = f - SIZE;
        fArr[3] = f2 + SIZE;
        fArr[4] = f + SIZE;
        fArr[5] = fArr[3];
        drawPath(canvas, fArr, paint, true);
    }

    private void drawX(Canvas canvas, Paint paint, float f, float f2) {
        canvas.drawLine(f - SIZE, f2 - SIZE, f + SIZE, f2 + SIZE, paint);
        canvas.drawLine(f + SIZE, f2 - SIZE, f - SIZE, f2 + SIZE, paint);
    }

    public void drawLegendShape(Canvas canvas, SimpleSeriesRenderer simpleSeriesRenderer, float f, float f2, Paint paint) {
        if (((XYSeriesRenderer) simpleSeriesRenderer).isFillPoints()) {
            paint.setStyle(Style.FILL);
        } else {
            paint.setStyle(Style.STROKE);
        }
        switch (AnonymousClass1.$SwitchMap$org$achartengine$chart$PointStyle[((XYSeriesRenderer) simpleSeriesRenderer).getPointStyle().ordinal()]) {
            case Wbxml.END /*1*/:
                drawX(canvas, paint, f + 10.0f, f2);
            case Wbxml.ENTITY /*2*/:
                drawCircle(canvas, paint, f + 10.0f, f2);
            case Wbxml.STR_I /*3*/:
                drawTriangle(canvas, paint, new float[6], f + 10.0f, f2);
            case Wbxml.LITERAL /*4*/:
                drawSquare(canvas, paint, f + 10.0f, f2);
            case Node.CDSECT /*5*/:
                drawDiamond(canvas, paint, new float[8], f + 10.0f, f2);
            case Node.ENTITY_REF /*6*/:
                canvas.drawPoint(f + 10.0f, f2, paint);
            default:
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void drawSeries(android.graphics.Canvas r9, android.graphics.Paint r10, float[] r11, org.achartengine.renderer.SimpleSeriesRenderer r12, float r13, int r14) {
        /*
        r8 = this;
        r0 = 0;
        r12 = (org.achartengine.renderer.XYSeriesRenderer) r12;
        r1 = r12.getColor();
        r10.setColor(r1);
        r1 = r12.isFillPoints();
        if (r1 == 0) goto L_0x0026;
    L_0x0010:
        r1 = android.graphics.Paint.Style.FILL;
        r10.setStyle(r1);
    L_0x0015:
        r7 = r11.length;
        r1 = org.achartengine.chart.ScatterChart.AnonymousClass1.$SwitchMap$org$achartengine$chart$PointStyle;
        r2 = r12.getPointStyle();
        r2 = r2.ordinal();
        r1 = r1[r2];
        switch(r1) {
            case 1: goto L_0x002c;
            case 2: goto L_0x003a;
            case 3: goto L_0x0048;
            case 4: goto L_0x005e;
            case 5: goto L_0x006c;
            case 6: goto L_0x0083;
            default: goto L_0x0025;
        };
    L_0x0025:
        return;
    L_0x0026:
        r1 = android.graphics.Paint.Style.STROKE;
        r10.setStyle(r1);
        goto L_0x0015;
    L_0x002c:
        if (r0 >= r7) goto L_0x0025;
    L_0x002e:
        r1 = r11[r0];
        r2 = r0 + 1;
        r2 = r11[r2];
        r8.drawX(r9, r10, r1, r2);
        r0 = r0 + 2;
        goto L_0x002c;
    L_0x003a:
        if (r0 >= r7) goto L_0x0025;
    L_0x003c:
        r1 = r11[r0];
        r2 = r0 + 1;
        r2 = r11[r2];
        r8.drawCircle(r9, r10, r1, r2);
        r0 = r0 + 2;
        goto L_0x003a;
    L_0x0048:
        r1 = 6;
        r3 = new float[r1];
        r6 = r0;
    L_0x004c:
        if (r6 >= r7) goto L_0x0025;
    L_0x004e:
        r4 = r11[r6];
        r0 = r6 + 1;
        r5 = r11[r0];
        r0 = r8;
        r1 = r9;
        r2 = r10;
        r0.drawTriangle(r1, r2, r3, r4, r5);
        r0 = r6 + 2;
        r6 = r0;
        goto L_0x004c;
    L_0x005e:
        if (r0 >= r7) goto L_0x0025;
    L_0x0060:
        r1 = r11[r0];
        r2 = r0 + 1;
        r2 = r11[r2];
        r8.drawSquare(r9, r10, r1, r2);
        r0 = r0 + 2;
        goto L_0x005e;
    L_0x006c:
        r1 = 8;
        r3 = new float[r1];
        r6 = r0;
    L_0x0071:
        if (r6 >= r7) goto L_0x0025;
    L_0x0073:
        r4 = r11[r6];
        r0 = r6 + 1;
        r5 = r11[r0];
        r0 = r8;
        r1 = r9;
        r2 = r10;
        r0.drawDiamond(r1, r2, r3, r4, r5);
        r0 = r6 + 2;
        r6 = r0;
        goto L_0x0071;
    L_0x0083:
        r9.drawPoints(r11, r10);
        goto L_0x0025;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.achartengine.chart.ScatterChart.drawSeries(android.graphics.Canvas, android.graphics.Paint, float[], org.achartengine.renderer.SimpleSeriesRenderer, float, int):void");
    }

    public int getLegendShapeWidth() {
        return SHAPE_WIDTH;
    }
}