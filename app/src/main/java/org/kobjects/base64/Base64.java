package org.kobjects.base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Base64 {
    static final char[] charTab;

    static {
        charTab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    }

    static int decode(char c) {
        if (c >= 'A' && c <= 'Z') {
            return c - 65;
        }
        if (c >= 'a' && c <= 'z') {
            return (c - 97) + 26;
        }
        if (c >= '0' && c <= '9') {
            return ((c - 48) + 26) + 26;
        }
        switch (c) {
            case '+':
                return 62;
            case '/':
                return 63;
            case '=':
                return 0;
            default:
                throw new RuntimeException("unexpected code: " + c);
        }
    }

    public static void decode(String str, OutputStream outputStream) throws IOException {
        int i = 0;
        int length = str.length();
        while (true) {
            if (i < length && str.charAt(i) <= ' ') {
                i++;
            } else if (i != length) {
                int decode = (((decode(str.charAt(i)) << 18) + (decode(str.charAt(i + 1)) << 12)) + (decode(str.charAt(i + 2)) << 6)) + decode(str.charAt(i + 3));
                outputStream.write((decode >> 16) & 255);
                if (str.charAt(i + 2) != '=') {
                    outputStream.write((decode >> 8) & 255);
                    if (str.charAt(i + 3) != '=') {
                        outputStream.write(decode & 255);
                        i += 4;
                    } else {
                        return;
                    }
                }
                return;
            } else {
                return;
            }
        }
    }

    public static byte[] decode(String str) {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            decode(str, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public static String encode(byte[] bArr) {
        return encode(bArr, 0, bArr.length, null).toString();
    }

    public static StringBuffer encode(byte[] bArr, int i, int i2, StringBuffer stringBuffer) {
        if (stringBuffer == null) {
            stringBuffer = new StringBuffer((bArr.length * 3) / 2);
        }
        int i3 = 0;
        int i4 = i;
        while (i4 <= i2 - 3) {
            int i5 = (((bArr[i4] & 255) << 16) | ((bArr[i4 + 1] & 255) << 8)) | (bArr[i4 + 2] & 255);
            stringBuffer.append(charTab[(i5 >> 18) & 63]);
            stringBuffer.append(charTab[(i5 >> 12) & 63]);
            stringBuffer.append(charTab[(i5 >> 6) & 63]);
            stringBuffer.append(charTab[i5 & 63]);
            i5 = i4 + 3;
            i4 = i3 + 1;
            if (i3 >= 14) {
                stringBuffer.append("\r\n");
                i4 = 0;
            }
            i3 = i4;
            i4 = i5;
        }
        if (i4 == (i + i2) - 2) {
            i4 = ((bArr[i4 + 1] & 255) << 8) | ((bArr[i4] & 255) << 16);
            stringBuffer.append(charTab[(i4 >> 18) & 63]);
            stringBuffer.append(charTab[(i4 >> 12) & 63]);
            stringBuffer.append(charTab[(i4 >> 6) & 63]);
            stringBuffer.append("=");
        } else if (i4 == (i + i2) - 1) {
            i4 = (bArr[i4] & 255) << 16;
            stringBuffer.append(charTab[(i4 >> 18) & 63]);
            stringBuffer.append(charTab[(i4 >> 12) & 63]);
            stringBuffer.append("==");
        }
        return stringBuffer;
    }
}