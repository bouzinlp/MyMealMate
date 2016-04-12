package org.ksoap2.serialization;

public class SoapPrimitive {
    String name;
    String namespace;
    String value;

    public SoapPrimitive(String str, String str2, String str3) {
        this.namespace = str;
        this.name = str2;
        this.value = str3;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r3) {
        /*
        r2 = this;
        r0 = r3 instanceof org.ksoap2.serialization.SoapPrimitive;
        if (r0 != 0) goto L_0x0006;
    L_0x0004:
        r0 = 0;
    L_0x0005:
        return r0;
    L_0x0006:
        r3 = (org.ksoap2.serialization.SoapPrimitive) r3;
        r0 = r2.name;
        r1 = r3.name;
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0004;
    L_0x0012:
        r0 = r2.namespace;
        if (r0 != 0) goto L_0x0024;
    L_0x0016:
        r0 = r3.namespace;
        if (r0 != 0) goto L_0x0004;
    L_0x001a:
        r0 = r2.value;
        if (r0 != 0) goto L_0x002f;
    L_0x001e:
        r0 = r3.value;
        if (r0 != 0) goto L_0x0004;
    L_0x0022:
        r0 = 1;
        goto L_0x0005;
    L_0x0024:
        r0 = r2.namespace;
        r1 = r3.namespace;
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0004;
    L_0x002e:
        goto L_0x001a;
    L_0x002f:
        r0 = r2.value;
        r1 = r3.value;
        r0 = r0.equals(r1);
        if (r0 == 0) goto L_0x0004;
    L_0x0039:
        goto L_0x0022;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.ksoap2.serialization.SoapPrimitive.equals(java.lang.Object):boolean");
    }

    public String getName() {
        return this.name;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public int hashCode() {
        return (this.namespace == null ? 0 : this.namespace.hashCode()) ^ this.name.hashCode();
    }

    public String toString() {
        return this.value;
    }
}