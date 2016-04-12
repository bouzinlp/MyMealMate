package org.achartengine.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MathHelper {
    public static final int ANGLE = 360;
    public static final double[] COS;
    private static final NumberFormat FORMAT;
    public static final double NULL_VALUE = Double.MAX_VALUE;
    public static final double[] RADIANS;
    public static final double[] SIN;

    static {
        RADIANS = new double[361];
        SIN = new double[361];
        COS = new double[361];
        FORMAT = NumberFormat.getNumberInstance();
        calculateValues();
    }

    private MathHelper() {
    }

    public static void calculateValues() {
        for (int i = 0; i <= ANGLE; i++) {
            double toRadians = Math.toRadians((double) i);
            RADIANS[i] = toRadians;
            SIN[i] = Math.sin(toRadians);
            COS[i] = Math.cos(toRadians);
        }
    }

    private static double[] computeLabels(double d, double d2, int i) {
        if (Math.abs(d - d2) < 1.0000000116860974E-7d) {
            return new double[]{d, d, 0.0d};
        }
        Object obj = null;
        if (d > d2) {
            obj = 1;
        } else {
            double d3 = d;
            d = d2;
            d2 = d3;
        }
        double roundUp = roundUp(Math.abs(d2 - d) / ((double) i));
        double ceil = Math.ceil(d2 / roundUp) * roundUp;
        double floor = Math.floor(d / roundUp) * roundUp;
        if (obj != null) {
            return new double[]{floor, ceil, roundUp * -1.0d};
        }
        return new double[]{ceil, floor, roundUp};
    }

    public static int getAngle(int i) {
        return i < 0 ? i + ANGLE : i > ANGLE ? i - 360 : i;
    }

    public static double[] getDoubles(Object[] objArr) {
        int length = objArr.length;
        double[] dArr = new double[length];
        for (int i = 0; i < length; i++) {
            dArr[i] = ((Double) objArr[i]).doubleValue();
        }
        return dArr;
    }

    public static float[] getFloats(Object[] objArr) {
        int length = objArr.length;
        float[] fArr = new float[length];
        for (int i = 0; i < length; i++) {
            fArr[i] = ((Float) objArr[i]).floatValue();
        }
        return fArr;
    }

    public static List<Double> getLabels(double d, double d2, int i) {
        FORMAT.setMaximumFractionDigits(5);
        List<Double> arrayList = new ArrayList();
        double[] computeLabels = computeLabels(d, d2, i);
        int i2 = (int) ((computeLabels[1] - computeLabels[0]) / computeLabels[2]);
        for (int i3 = 0; i3 < i2 + 1; i3++) {
            double d3 = computeLabels[0] + (((double) i3) * computeLabels[2]);
            try {
                d3 = FORMAT.parse(FORMAT.format(d3)).doubleValue();
            } catch (ParseException e) {
            }
            arrayList.add(Double.valueOf(d3));
        }
        return arrayList;
    }

    public static double[] minmax(List<Double> list) {
        if (list.size() == 0) {
            return new double[2];
        }
        double doubleValue = ((Double) list.get(0)).doubleValue();
        int size = list.size();
        double d = doubleValue;
        double d2 = doubleValue;
        for (int i = 1; i < size; i++) {
            double doubleValue2 = ((Double) list.get(i)).doubleValue();
            d = Math.min(d, doubleValue2);
            d2 = Math.max(d2, doubleValue2);
        }
        return new double[]{d, d2};
    }

    private static double roundUp(double d) {
        double d2 = 2.0d;
        int floor = (int) Math.floor(Math.log10(d));
        double pow = Math.pow(10.0d, (double) (-floor)) * d;
        if (pow > 5.0d) {
            d2 = 10.0d;
        } else if (pow > 2.0d) {
            d2 = 5.0d;
        } else if (pow <= 1.0d) {
            d2 = pow;
        }
        return d2 * Math.pow(10.0d, (double) floor);
    }

    public static double sum(List<Double> list) {
        double d = 0.0d;
        for (int i = 0; i < list.size(); i++) {
            d += ((Double) list.get(i)).doubleValue();
        }
        return d;
    }
}