package org.kobjects.mime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

public class Decoder {
    String boundary;
    char[] buf;
    String characterEncoding;
    boolean consumed;
    boolean eof;
    Hashtable header;
    InputStream is;

    public Decoder(InputStream inputStream, String str) throws IOException {
        this(inputStream, str, null);
    }

    public Decoder(InputStream inputStream, String str, String str2) throws IOException {
        this.buf = new char[256];
        this.characterEncoding = str2;
        this.is = inputStream;
        this.boundary = "--" + str;
        String readLine;
        do {
            readLine = readLine();
            if (readLine == null) {
                throw new IOException("Unexpected EOF");
            }
        } while (!readLine.startsWith(this.boundary));
        if (readLine.endsWith("--")) {
            this.eof = true;
            inputStream.close();
        }
        this.consumed = true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Hashtable getHeaderElements(java.lang.String r9) {
        /*
        r8 = 59;
        r7 = 34;
        r6 = -1;
        r0 = "";
        r2 = 0;
        r1 = new java.util.Hashtable;
        r1.<init>();
        r3 = r9.length();
    L_0x0011:
        if (r2 >= r3) goto L_0x001e;
    L_0x0013:
        r4 = r9.charAt(r2);
        r5 = 32;
        if (r4 > r5) goto L_0x001e;
    L_0x001b:
        r2 = r2 + 1;
        goto L_0x0011;
    L_0x001e:
        if (r2 < r3) goto L_0x0022;
    L_0x0020:
        r0 = r1;
    L_0x0021:
        return r0;
    L_0x0022:
        r4 = r9.charAt(r2);
        if (r4 != r7) goto L_0x0075;
    L_0x0028:
        r2 = r2 + 1;
        r4 = r9.indexOf(r7, r2);
        if (r4 != r6) goto L_0x0049;
    L_0x0030:
        r0 = new java.lang.RuntimeException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "End quote expected in ";
        r1 = r1.append(r2);
        r1 = r1.append(r9);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x0049:
        r2 = r9.substring(r2, r4);
        r1.put(r0, r2);
        r0 = r4 + 2;
        if (r0 >= r3) goto L_0x0020;
    L_0x0054:
        r2 = r0 + -1;
        r2 = r9.charAt(r2);
        if (r2 == r8) goto L_0x008d;
    L_0x005c:
        r0 = new java.lang.RuntimeException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "; expected in ";
        r1 = r1.append(r2);
        r1 = r1.append(r9);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x0075:
        r4 = r9.indexOf(r8, r2);
        if (r4 != r6) goto L_0x0084;
    L_0x007b:
        r2 = r9.substring(r2);
        r1.put(r0, r2);
        r0 = r1;
        goto L_0x0021;
    L_0x0084:
        r2 = r9.substring(r2, r4);
        r1.put(r0, r2);
        r0 = r4 + 1;
    L_0x008d:
        r2 = 61;
        r2 = r9.indexOf(r2, r0);
        if (r2 == r6) goto L_0x0020;
    L_0x0095:
        r0 = r9.substring(r0, r2);
        r0 = r0.toLowerCase();
        r0 = r0.trim();
        r2 = r2 + 1;
        goto L_0x0011;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.kobjects.mime.Decoder.getHeaderElements(java.lang.String):java.util.Hashtable");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final java.lang.String readLine() throws java.io.IOException {
        /*
        r7 = this;
        r6 = -1;
        r1 = 0;
        r0 = r1;
    L_0x0003:
        r2 = r7.is;
        r2 = r2.read();
        if (r2 != r6) goto L_0x000f;
    L_0x000b:
        if (r0 != 0) goto L_0x000f;
    L_0x000d:
        r0 = 0;
    L_0x000e:
        return r0;
    L_0x000f:
        if (r2 == r6) goto L_0x0015;
    L_0x0011:
        r3 = 10;
        if (r2 != r3) goto L_0x001e;
    L_0x0015:
        r2 = new java.lang.String;
        r3 = r7.buf;
        r2.<init>(r3, r1, r0);
        r0 = r2;
        goto L_0x000e;
    L_0x001e:
        r3 = 13;
        if (r2 == r3) goto L_0x0003;
    L_0x0022:
        r3 = r7.buf;
        r3 = r3.length;
        if (r0 < r3) goto L_0x003a;
    L_0x0027:
        r3 = r7.buf;
        r3 = r3.length;
        r3 = r3 * 3;
        r3 = r3 / 2;
        r3 = new char[r3];
        r4 = r7.buf;
        r5 = r7.buf;
        r5 = r5.length;
        java.lang.System.arraycopy(r4, r1, r3, r1, r5);
        r7.buf = r3;
    L_0x003a:
        r3 = r7.buf;
        r2 = (char) r2;
        r3[r0] = r2;
        r0 = r0 + 1;
        goto L_0x0003;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.kobjects.mime.Decoder.readLine():java.lang.String");
    }

    public String getHeader(String str) {
        return (String) this.header.get(str.toLowerCase());
    }

    public Enumeration getHeaderNames() {
        return this.header.keys();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean next() throws java.io.IOException {
        /*
        r5 = this;
        r0 = 0;
        r1 = r5.consumed;
        if (r1 != 0) goto L_0x0009;
    L_0x0005:
        r1 = 0;
        r5.readContent(r1);
    L_0x0009:
        r1 = r5.eof;
        if (r1 == 0) goto L_0x000e;
    L_0x000d:
        return r0;
    L_0x000e:
        r1 = new java.util.Hashtable;
        r1.<init>();
        r5.header = r1;
    L_0x0015:
        r1 = r5.readLine();
        if (r1 == 0) goto L_0x0023;
    L_0x001b:
        r2 = "";
        r2 = r1.equals(r2);
        if (r2 == 0) goto L_0x0027;
    L_0x0023:
        r5.consumed = r0;
        r0 = 1;
        goto L_0x000d;
    L_0x0027:
        r2 = 58;
        r2 = r1.indexOf(r2);
        r3 = -1;
        if (r2 != r3) goto L_0x0049;
    L_0x0030:
        r0 = new java.io.IOException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "colon missing in multipart header line: ";
        r2 = r2.append(r3);
        r1 = r2.append(r1);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x0049:
        r3 = r5.header;
        r4 = r1.substring(r0, r2);
        r4 = r4.trim();
        r4 = r4.toLowerCase();
        r2 = r2 + 1;
        r1 = r1.substring(r2);
        r1 = r1.trim();
        r3.put(r4, r1);
        goto L_0x0015;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.kobjects.mime.Decoder.next():boolean");
    }

    public String readContent() throws IOException {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        readContent(byteArrayOutputStream);
        String str = this.characterEncoding == null ? new String(byteArrayOutputStream.toByteArray()) : new String(byteArrayOutputStream.toByteArray(), this.characterEncoding);
        System.out.println("Field content: '" + str + "'");
        return str;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void readContent(java.io.OutputStream r8) throws java.io.IOException {
        /*
        r7 = this;
        r2 = 1;
        r1 = 0;
        r0 = r7.consumed;
        if (r0 == 0) goto L_0x000e;
    L_0x0006:
        r0 = new java.lang.RuntimeException;
        r1 = "Content already consumed!";
        r0.<init>(r1);
        throw r0;
    L_0x000e:
        r0 = "Content-Type";
        r7.getHeader(r0);
        r0 = "base64";
        r3 = "Content-Transfer-Encoding";
        r3 = r7.getHeader(r3);
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x004d;
    L_0x0021:
        r0 = new java.io.ByteArrayOutputStream;
        r0.<init>();
    L_0x0026:
        r0 = r7.readLine();
        if (r0 != 0) goto L_0x0034;
    L_0x002c:
        r0 = new java.io.IOException;
        r1 = "Unexpected EOF";
        r0.<init>(r1);
        throw r0;
    L_0x0034:
        r1 = r7.boundary;
        r1 = r0.startsWith(r1);
        if (r1 == 0) goto L_0x0049;
    L_0x003c:
        r1 = "--";
        r0 = r0.endsWith(r1);
        if (r0 == 0) goto L_0x0046;
    L_0x0044:
        r7.eof = r2;
    L_0x0046:
        r7.consumed = r2;
        return;
    L_0x0049:
        org.kobjects.base64.Base64.decode(r0, r8);
        goto L_0x0026;
    L_0x004d:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r3 = "\r\n";
        r0 = r0.append(r3);
        r3 = r7.boundary;
        r0 = r0.append(r3);
        r4 = r0.toString();
        r0 = r1;
    L_0x0063:
        r3 = r7.is;
        r5 = r3.read();
        r3 = -1;
        if (r5 != r3) goto L_0x0074;
    L_0x006c:
        r0 = new java.lang.RuntimeException;
        r1 = "Unexpected EOF";
        r0.<init>(r1);
        throw r0;
    L_0x0074:
        r3 = (char) r5;
        r6 = r4.charAt(r0);
        if (r3 != r6) goto L_0x0088;
    L_0x007b:
        r0 = r0 + 1;
        r3 = r4.length();
        if (r0 != r3) goto L_0x0063;
    L_0x0083:
        r0 = r7.readLine();
        goto L_0x003c;
    L_0x0088:
        if (r0 <= 0) goto L_0x00a0;
    L_0x008a:
        r3 = r1;
    L_0x008b:
        if (r3 >= r0) goto L_0x0098;
    L_0x008d:
        r6 = r4.charAt(r3);
        r6 = (byte) r6;
        r8.write(r6);
        r3 = r3 + 1;
        goto L_0x008b;
    L_0x0098:
        r0 = (char) r5;
        r3 = r4.charAt(r1);
        if (r0 != r3) goto L_0x00a7;
    L_0x009f:
        r0 = r2;
    L_0x00a0:
        if (r0 != 0) goto L_0x0063;
    L_0x00a2:
        r3 = (byte) r5;
        r8.write(r3);
        goto L_0x0063;
    L_0x00a7:
        r0 = r1;
        goto L_0x00a0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.kobjects.mime.Decoder.readContent(java.io.OutputStream):void");
    }
}