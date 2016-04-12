package org.kobjects.isodate;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class IsoDate {
    public static final int DATE = 1;
    public static final int DATE_TIME = 3;
    public static final int TIME = 2;

    public static String dateToString(Date date, int i) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeZone(TimeZone.getTimeZone("GMT"));
        instance.setTime(date);
        StringBuffer stringBuffer = new StringBuffer();
        if ((i & DATE) != 0) {
            int i2 = instance.get(DATE);
            dd(stringBuffer, i2 / 100);
            dd(stringBuffer, i2 % 100);
            stringBuffer.append('-');
            dd(stringBuffer, (instance.get(TIME) + 0) + DATE);
            stringBuffer.append('-');
            dd(stringBuffer, instance.get(5));
            if (i == DATE_TIME) {
                stringBuffer.append("T");
            }
        }
        if ((i & TIME) != 0) {
            dd(stringBuffer, instance.get(11));
            stringBuffer.append(':');
            dd(stringBuffer, instance.get(12));
            stringBuffer.append(':');
            dd(stringBuffer, instance.get(13));
            stringBuffer.append('.');
            int i3 = instance.get(14);
            stringBuffer.append((char) ((i3 / 100) + 48));
            dd(stringBuffer, i3 % 100);
            stringBuffer.append('Z');
        }
        return stringBuffer.toString();
    }

    static void dd(StringBuffer stringBuffer, int i) {
        stringBuffer.append((char) ((i / 10) + 48));
        stringBuffer.append((char) ((i % 10) + 48));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Date stringToDate(java.lang.String r9, int r10) {
        /*
        r8 = 14;
        r7 = 5;
        r6 = 11;
        r0 = 8;
        r1 = 0;
        r3 = java.util.Calendar.getInstance();
        r2 = r10 & 1;
        if (r2 == 0) goto L_0x00e0;
    L_0x0010:
        r2 = 1;
        r4 = 4;
        r4 = r9.substring(r1, r4);
        r4 = java.lang.Integer.parseInt(r4);
        r3.set(r2, r4);
        r2 = 2;
        r4 = 7;
        r4 = r9.substring(r7, r4);
        r4 = java.lang.Integer.parseInt(r4);
        r4 = r4 + -1;
        r4 = r4 + 0;
        r3.set(r2, r4);
        r2 = 10;
        r2 = r9.substring(r0, r2);
        r2 = java.lang.Integer.parseInt(r2);
        r3.set(r7, r2);
        r2 = 3;
        if (r10 != r2) goto L_0x0044;
    L_0x003e:
        r2 = r9.length();
        if (r2 >= r6) goto L_0x0059;
    L_0x0044:
        r3.set(r6, r1);
        r0 = 12;
        r3.set(r0, r1);
        r0 = 13;
        r3.set(r0, r1);
        r3.set(r8, r1);
        r0 = r3.getTime();
    L_0x0058:
        return r0;
    L_0x0059:
        r9 = r9.substring(r6);
    L_0x005d:
        r2 = 2;
        r2 = r9.substring(r1, r2);
        r2 = java.lang.Integer.parseInt(r2);
        r3.set(r6, r2);
        r2 = 12;
        r4 = 3;
        r4 = r9.substring(r4, r7);
        r4 = java.lang.Integer.parseInt(r4);
        r3.set(r2, r4);
        r2 = 13;
        r4 = 6;
        r4 = r9.substring(r4, r0);
        r4 = java.lang.Integer.parseInt(r4);
        r3.set(r2, r4);
        r2 = r9.length();
        if (r0 >= r2) goto L_0x00f3;
    L_0x008b:
        r2 = r9.charAt(r0);
        r4 = 46;
        if (r2 != r4) goto L_0x00f3;
    L_0x0093:
        r2 = 100;
    L_0x0095:
        r0 = r0 + 1;
        r4 = r9.charAt(r0);
        r5 = 48;
        if (r4 < r5) goto L_0x00a3;
    L_0x009f:
        r5 = 57;
        if (r4 <= r5) goto L_0x00ec;
    L_0x00a3:
        r3.set(r8, r1);
    L_0x00a6:
        r1 = r9.length();
        if (r0 >= r1) goto L_0x00da;
    L_0x00ac:
        r1 = r9.charAt(r0);
        r2 = 43;
        if (r1 == r2) goto L_0x00bc;
    L_0x00b4:
        r1 = r9.charAt(r0);
        r2 = 45;
        if (r1 != r2) goto L_0x00f7;
    L_0x00bc:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "GMT";
        r1 = r1.append(r2);
        r0 = r9.substring(r0);
        r0 = r1.append(r0);
        r0 = r0.toString();
        r0 = java.util.TimeZone.getTimeZone(r0);
        r3.setTimeZone(r0);
    L_0x00da:
        r0 = r3.getTime();
        goto L_0x0058;
    L_0x00e0:
        r2 = new java.util.Date;
        r4 = 0;
        r2.<init>(r4);
        r3.setTime(r2);
        goto L_0x005d;
    L_0x00ec:
        r4 = r4 + -48;
        r4 = r4 * r2;
        r1 = r1 + r4;
        r2 = r2 / 10;
        goto L_0x0095;
    L_0x00f3:
        r3.set(r8, r1);
        goto L_0x00a6;
    L_0x00f7:
        r0 = r9.charAt(r0);
        r1 = 90;
        if (r0 != r1) goto L_0x0109;
    L_0x00ff:
        r0 = "GMT";
        r0 = java.util.TimeZone.getTimeZone(r0);
        r3.setTimeZone(r0);
        goto L_0x00da;
    L_0x0109:
        r0 = new java.lang.RuntimeException;
        r1 = "illegal time format!";
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.kobjects.isodate.IsoDate.stringToDate(java.lang.String, int):java.util.Date");
    }
}