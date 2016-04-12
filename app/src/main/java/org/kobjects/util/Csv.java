package org.kobjects.util;

public class Csv {
    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String[] decode(java.lang.String r11) {
        /*
        r10 = 94;
        r9 = 44;
        r8 = 32;
        r0 = 0;
        r7 = 34;
        r3 = new java.util.Vector;
        r3.<init>();
        r4 = r11.length();
        r1 = r0;
    L_0x0013:
        if (r1 >= r4) goto L_0x001e;
    L_0x0015:
        r2 = r11.charAt(r1);
        if (r2 > r8) goto L_0x001e;
    L_0x001b:
        r1 = r1 + 1;
        goto L_0x0013;
    L_0x001e:
        if (r1 < r4) goto L_0x0036;
    L_0x0020:
        r1 = r3.size();
        r2 = new java.lang.String[r1];
        r1 = r0;
    L_0x0027:
        r0 = r2.length;
        if (r1 >= r0) goto L_0x00d3;
    L_0x002a:
        r0 = r3.elementAt(r1);
        r0 = (java.lang.String) r0;
        r2[r1] = r0;
        r0 = r1 + 1;
        r1 = r0;
        goto L_0x0027;
    L_0x0036:
        r2 = r11.charAt(r1);
        if (r2 != r7) goto L_0x00ae;
    L_0x003c:
        r1 = r1 + 1;
        r5 = new java.lang.StringBuffer;
        r5.<init>();
    L_0x0043:
        r2 = r1 + 1;
        r6 = r11.charAt(r1);
        if (r6 != r10) goto L_0x005d;
    L_0x004b:
        if (r2 >= r4) goto L_0x005d;
    L_0x004d:
        r1 = r2 + 1;
        r2 = r11.charAt(r2);
        if (r2 != r10) goto L_0x0059;
    L_0x0055:
        r5.append(r2);
        goto L_0x0043;
    L_0x0059:
        r2 = r2 + -64;
        r2 = (char) r2;
        goto L_0x0055;
    L_0x005d:
        if (r6 != r7) goto L_0x00d1;
    L_0x005f:
        if (r2 == r4) goto L_0x0067;
    L_0x0061:
        r1 = r11.charAt(r2);
        if (r1 == r7) goto L_0x0079;
    L_0x0067:
        r1 = r5.toString();
        r3.addElement(r1);
    L_0x006e:
        if (r2 >= r4) goto L_0x007f;
    L_0x0070:
        r1 = r11.charAt(r2);
        if (r1 > r8) goto L_0x007f;
    L_0x0076:
        r2 = r2 + 1;
        goto L_0x006e;
    L_0x0079:
        r1 = r2 + 1;
    L_0x007b:
        r5.append(r6);
        goto L_0x0043;
    L_0x007f:
        if (r2 >= r4) goto L_0x0020;
    L_0x0081:
        r1 = r11.charAt(r2);
        if (r1 == r9) goto L_0x00aa;
    L_0x0087:
        r0 = new java.lang.RuntimeException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r3 = "Comma expected at ";
        r1 = r1.append(r3);
        r1 = r1.append(r2);
        r2 = " line: ";
        r1 = r1.append(r2);
        r1 = r1.append(r11);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x00aa:
        r1 = r2 + 1;
        goto L_0x0013;
    L_0x00ae:
        r2 = r11.indexOf(r9, r1);
        r5 = -1;
        if (r2 != r5) goto L_0x00c2;
    L_0x00b5:
        r1 = r11.substring(r1);
        r1 = r1.trim();
        r3.addElement(r1);
        goto L_0x0020;
    L_0x00c2:
        r1 = r11.substring(r1, r2);
        r1 = r1.trim();
        r3.addElement(r1);
        r1 = r2 + 1;
        goto L_0x0013;
    L_0x00d1:
        r1 = r2;
        goto L_0x007b;
    L_0x00d3:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.kobjects.util.Csv.decode(java.lang.String):java.lang.String[]");
    }

    public static String encode(String str, char c) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt == c || charAt == '^') {
                stringBuffer.append(charAt);
                stringBuffer.append(charAt);
            } else if (charAt < ' ') {
                stringBuffer.append('^');
                stringBuffer.append((char) (charAt + 64));
            } else {
                stringBuffer.append(charAt);
            }
        }
        return stringBuffer.toString();
    }

    public static String encode(Object[] objArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < objArr.length; i++) {
            if (i != 0) {
                stringBuffer.append(',');
            }
            Object obj = objArr[i];
            if ((obj instanceof Number) || (obj instanceof Boolean)) {
                stringBuffer.append(obj.toString());
            } else {
                stringBuffer.append('\"');
                stringBuffer.append(encode(obj.toString(), '\"'));
                stringBuffer.append('\"');
            }
        }
        return stringBuffer.toString();
    }
}