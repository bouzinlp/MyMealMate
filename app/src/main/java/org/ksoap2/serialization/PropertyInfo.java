package org.ksoap2.serialization;

import java.util.Vector;
import org.xmlpull.v1.XmlPullParser;

public class PropertyInfo {
    public static final Class BOOLEAN_CLASS;
    public static final Class INTEGER_CLASS;
    public static final Class LONG_CLASS;
    public static final int MULTI_REF = 2;
    public static final Class OBJECT_CLASS;
    public static final PropertyInfo OBJECT_TYPE;
    public static final int REF_ONLY = 4;
    public static final Class STRING_CLASS;
    public static final int TRANSIENT = 1;
    public static final Class VECTOR_CLASS;
    public PropertyInfo elementType;
    public int flags;
    public boolean multiRef;
    public String name;
    public String namespace;
    public Object type;
    protected Object value;

    static {
        OBJECT_CLASS = new Object().getClass();
        STRING_CLASS = XmlPullParser.NO_NAMESPACE.getClass();
        INTEGER_CLASS = new Integer(0).getClass();
        LONG_CLASS = new Long(0).getClass();
        BOOLEAN_CLASS = new Boolean(true).getClass();
        VECTOR_CLASS = new Vector().getClass();
        OBJECT_TYPE = new PropertyInfo();
    }

    public PropertyInfo() {
        this.type = OBJECT_CLASS;
    }

    public void clear() {
        this.type = OBJECT_CLASS;
        this.flags = 0;
        this.name = null;
        this.namespace = null;
    }

    public PropertyInfo getElementType() {
        return this.elementType;
    }

    public int getFlags() {
        return this.flags;
    }

    public String getName() {
        return this.name;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public Object getType() {
        return this.type;
    }

    public Object getValue() {
        return this.value;
    }

    public boolean isMultiRef() {
        return this.multiRef;
    }

    public void setElementType(PropertyInfo propertyInfo) {
        this.elementType = propertyInfo;
    }

    public void setFlags(int i) {
        this.flags = i;
    }

    public void setMultiRef(boolean z) {
        this.multiRef = z;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setNamespace(String str) {
        this.namespace = str;
    }

    public void setType(Object obj) {
        this.type = obj;
    }

    public void setValue(Object obj) {
        this.value = obj;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.name);
        stringBuffer.append(" : ");
        if (this.value != null) {
            stringBuffer.append(this.value);
        } else {
            stringBuffer.append("(not set)");
        }
        return stringBuffer.toString();
    }
}