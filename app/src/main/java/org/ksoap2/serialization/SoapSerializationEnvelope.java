package org.ksoap2.serialization;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class SoapSerializationEnvelope extends SoapEnvelope {
    private static final String ANY_TYPE_LABEL = "anyType";
    private static final String ARRAY_MAPPING_NAME = "Array";
    private static final String ARRAY_TYPE_LABEL = "arrayType";
    static final Marshal DEFAULT_MARSHAL;
    private static final String HREF_LABEL = "href";
    private static final String ID_LABEL = "id";
    private static final String ITEM_LABEL = "item";
    private static final String NIL_LABEL = "nil";
    private static final String NULL_LABEL = "null";
    protected static final int QNAME_MARSHAL = 3;
    protected static final int QNAME_NAMESPACE = 0;
    protected static final int QNAME_TYPE = 1;
    private static final String ROOT_LABEL = "root";
    private static final String TYPE_LABEL = "type";
    protected boolean addAdornments;
    protected Hashtable classToQName;
    public boolean dotNet;
    Hashtable idMap;
    public boolean implicitTypes;
    Vector multiRef;
    public Hashtable properties;
    protected Hashtable qNameToClass;

    static {
        DEFAULT_MARSHAL = new DM();
    }

    public SoapSerializationEnvelope(int i) {
        super(i);
        this.properties = new Hashtable();
        this.idMap = new Hashtable();
        this.qNameToClass = new Hashtable();
        this.classToQName = new Hashtable();
        this.addAdornments = true;
        addMapping(this.enc, ARRAY_MAPPING_NAME, PropertyInfo.VECTOR_CLASS);
        DEFAULT_MARSHAL.register(this);
    }

    private int getIndex(String str, int i, int i2) {
        return (str != null && str.length() - i >= QNAME_MARSHAL) ? Integer.parseInt(str.substring(i + QNAME_TYPE, str.length() - 1)) : i2;
    }

    private void writeElement(XmlSerializer xmlSerializer, Object obj, PropertyInfo propertyInfo, Object obj2) throws IOException {
        if (obj2 != null) {
            ((Marshal) obj2).writeInstance(xmlSerializer, obj);
        } else if (obj instanceof SoapObject) {
            writeObjectBody(xmlSerializer, (SoapObject) obj);
        } else if (obj instanceof KvmSerializable) {
            writeObjectBody(xmlSerializer, (KvmSerializable) obj);
        } else if (obj instanceof Vector) {
            writeVectorBody(xmlSerializer, (Vector) obj, propertyInfo.elementType);
        } else {
            throw new RuntimeException("Cannot serialize: " + obj);
        }
    }

    public void addMapping(String str, String str2, Class cls) {
        addMapping(str, str2, cls, null);
    }

    public void addMapping(String str, String str2, Class cls, Marshal marshal) {
        Object obj;
        Hashtable hashtable = this.qNameToClass;
        SoapPrimitive soapPrimitive = new SoapPrimitive(str, str2, null);
        if (marshal == null) {
            obj = cls;
        } else {
            Marshal marshal2 = marshal;
        }
        hashtable.put(soapPrimitive, obj);
        this.classToQName.put(cls.getName(), new Object[]{str, str2, QNAME_NAMESPACE, marshal});
    }

    public void addTemplate(SoapObject soapObject) {
        this.qNameToClass.put(new SoapPrimitive(soapObject.namespace, soapObject.name, null), soapObject);
    }

    public Object[] getInfo(Object obj, Object obj2) {
        Class cls = obj == null ? ((obj2 instanceof SoapObject) || (obj2 instanceof SoapPrimitive)) ? obj2 : obj2.getClass() : obj;
        if (cls instanceof SoapObject) {
            SoapObject soapObject = (SoapObject) cls;
            return new Object[]{soapObject.getNamespace(), soapObject.getName(), null, null};
        } else if (cls instanceof SoapPrimitive) {
            SoapPrimitive soapPrimitive = (SoapPrimitive) cls;
            return new Object[]{soapPrimitive.getNamespace(), soapPrimitive.getName(), null, DEFAULT_MARSHAL};
        } else {
            if ((cls instanceof Class) && cls != PropertyInfo.OBJECT_CLASS) {
                Object[] objArr = (Object[]) this.classToQName.get(cls.getName());
                if (objArr != null) {
                    return objArr;
                }
            }
            return new Object[]{this.xsd, ANY_TYPE_LABEL, null, null};
        }
    }

    public Object getResponse() throws SoapFault {
        if (this.bodyIn instanceof SoapFault) {
            throw ((SoapFault) this.bodyIn);
        }
        KvmSerializable kvmSerializable = (KvmSerializable) this.bodyIn;
        return kvmSerializable.getPropertyCount() == 0 ? null : kvmSerializable.getProperty(QNAME_NAMESPACE);
    }

    public Object getResult() {
        KvmSerializable kvmSerializable = (KvmSerializable) this.bodyIn;
        return kvmSerializable.getPropertyCount() == 0 ? null : kvmSerializable.getProperty(QNAME_NAMESPACE);
    }

    public boolean isAddAdornments() {
        return this.addAdornments;
    }

    public void parseBody(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        this.bodyIn = null;
        xmlPullParser.nextTag();
        if (xmlPullParser.getEventType() == 2 && xmlPullParser.getNamespace().equals(this.env) && xmlPullParser.getName().equals("Fault")) {
            SoapFault soapFault = new SoapFault();
            soapFault.parse(xmlPullParser);
            this.bodyIn = soapFault;
            return;
        }
        while (xmlPullParser.getEventType() == 2) {
            String attributeValue = xmlPullParser.getAttributeValue(this.enc, ROOT_LABEL);
            Object read = read(xmlPullParser, null, -1, xmlPullParser.getNamespace(), xmlPullParser.getName(), PropertyInfo.OBJECT_TYPE);
            if ("1".equals(attributeValue) || this.bodyIn == null) {
                this.bodyIn = read;
            }
            xmlPullParser.nextTag();
        }
    }

    public Object read(XmlPullParser xmlPullParser, Object obj, int i, String str, String str2, PropertyInfo propertyInfo) throws IOException, XmlPullParserException {
        Object obj2;
        String name = xmlPullParser.getName();
        String attributeValue = xmlPullParser.getAttributeValue(null, HREF_LABEL);
        FwdRef fwdRef;
        if (attributeValue == null) {
            Object readInstance;
            attributeValue = xmlPullParser.getAttributeValue(this.xsi, NIL_LABEL);
            String attributeValue2 = xmlPullParser.getAttributeValue(null, ID_LABEL);
            if (attributeValue == null) {
                attributeValue = xmlPullParser.getAttributeValue(this.xsi, NULL_LABEL);
            }
            if (attributeValue == null || !SoapEnvelope.stringToBoolean(attributeValue)) {
                String attributeValue3 = xmlPullParser.getAttributeValue(this.xsi, TYPE_LABEL);
                if (attributeValue3 != null) {
                    int indexOf = attributeValue3.indexOf(58);
                    str2 = attributeValue3.substring(indexOf + QNAME_TYPE);
                    str = xmlPullParser.getNamespace(indexOf == -1 ? XmlPullParser.NO_NAMESPACE : attributeValue3.substring(QNAME_NAMESPACE, indexOf));
                } else if (str2 == null && str == null) {
                    if (xmlPullParser.getAttributeValue(this.enc, ARRAY_TYPE_LABEL) != null) {
                        str = this.enc;
                        str2 = ARRAY_MAPPING_NAME;
                    } else {
                        Object[] info = getInfo(propertyInfo.type, null);
                        str2 = (String) info[QNAME_TYPE];
                        str = (String) info[QNAME_NAMESPACE];
                    }
                }
                if (attributeValue3 == null) {
                    this.implicitTypes = true;
                }
                readInstance = readInstance(xmlPullParser, str, str2, propertyInfo);
                if (readInstance == null) {
                    readInstance = readUnknown(xmlPullParser, str, str2);
                }
            } else {
                xmlPullParser.nextTag();
                xmlPullParser.require(QNAME_MARSHAL, null, name);
                readInstance = null;
            }
            if (attributeValue2 != null) {
                obj2 = this.idMap.get(attributeValue2);
                if (obj2 instanceof FwdRef) {
                    fwdRef = (FwdRef) obj2;
                    while (true) {
                        if (fwdRef.obj instanceof KvmSerializable) {
                            ((KvmSerializable) fwdRef.obj).setProperty(fwdRef.index, readInstance);
                        } else {
                            ((Vector) fwdRef.obj).setElementAt(readInstance, fwdRef.index);
                        }
                        FwdRef fwdRef2 = fwdRef.next;
                        if (fwdRef2 == null) {
                            break;
                        }
                        fwdRef = fwdRef2;
                    }
                } else if (obj2 != null) {
                    throw new RuntimeException("double ID");
                }
                this.idMap.put(attributeValue2, readInstance);
                obj2 = readInstance;
            } else {
                obj2 = readInstance;
            }
        } else if (obj == null) {
            throw new RuntimeException("href at root level?!?");
        } else {
            String substring = attributeValue.substring(QNAME_TYPE);
            obj2 = this.idMap.get(substring);
            if (obj2 == null || (obj2 instanceof FwdRef)) {
                fwdRef = new FwdRef();
                fwdRef.next = (FwdRef) obj2;
                fwdRef.obj = obj;
                fwdRef.index = i;
                this.idMap.put(substring, fwdRef);
                obj2 = null;
            }
            xmlPullParser.nextTag();
            xmlPullParser.require(QNAME_MARSHAL, null, name);
        }
        xmlPullParser.require(QNAME_MARSHAL, null, name);
        return obj2;
    }

    public Object readInstance(XmlPullParser xmlPullParser, String str, String str2, PropertyInfo propertyInfo) throws IOException, XmlPullParserException {
        Class cls = this.qNameToClass.get(new SoapPrimitive(str, str2, null));
        if (cls == null) {
            return null;
        }
        if (cls instanceof Marshal) {
            return ((Marshal) cls).readInstance(xmlPullParser, str, str2, propertyInfo);
        }
        Object newInstance;
        if (cls instanceof SoapObject) {
            newInstance = ((SoapObject) cls).newInstance();
        } else if (cls == SoapObject.class) {
            newInstance = new SoapObject(str, str2);
        } else {
            try {
                newInstance = cls.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e.toString());
            }
        }
        if (newInstance instanceof SoapObject) {
            readSerializable(xmlPullParser, (SoapObject) newInstance);
            return newInstance;
        } else if (newInstance instanceof KvmSerializable) {
            readSerializable(xmlPullParser, (KvmSerializable) newInstance);
            return newInstance;
        } else if (newInstance instanceof Vector) {
            readVector(xmlPullParser, (Vector) newInstance, propertyInfo.elementType);
            return newInstance;
        } else {
            throw new RuntimeException("no deserializer for " + newInstance.getClass());
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void readSerializable(org.xmlpull.v1.XmlPullParser r17, org.ksoap2.serialization.KvmSerializable r18) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        /*
        r16 = this;
        r4 = -1;
        r15 = r18.getPropertyCount();
        r7 = new org.ksoap2.serialization.PropertyInfo;
        r7.<init>();
    L_0x000a:
        r1 = r17.nextTag();
        r2 = 3;
        if (r1 == r2) goto L_0x00aa;
    L_0x0011:
        r13 = r17.getName();
        r0 = r16;
        r1 = r0.implicitTypes;
        if (r1 == 0) goto L_0x00b6;
    L_0x001b:
        r0 = r18;
        r1 = r0 instanceof org.ksoap2.serialization.SoapObject;
        if (r1 != 0) goto L_0x0085;
    L_0x0021:
        r1 = r15;
    L_0x0022:
        r2 = r1 + -1;
        if (r1 != 0) goto L_0x003f;
    L_0x0026:
        r1 = new java.lang.RuntimeException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Unknown Property: ";
        r2 = r2.append(r3);
        r2 = r2.append(r13);
        r2 = r2.toString();
        r1.<init>(r2);
        throw r1;
    L_0x003f:
        r4 = r4 + 1;
        if (r4 < r15) goto L_0x0044;
    L_0x0043:
        r4 = 0;
    L_0x0044:
        r0 = r16;
        r1 = r0.properties;
        r0 = r18;
        r0.getPropertyInfo(r4, r1, r7);
        r1 = r7.namespace;
        if (r1 != 0) goto L_0x0059;
    L_0x0051:
        r1 = r7.name;
        r1 = r13.equals(r1);
        if (r1 != 0) goto L_0x0073;
    L_0x0059:
        r1 = r7.name;
        if (r1 != 0) goto L_0x005f;
    L_0x005d:
        if (r4 == 0) goto L_0x0073;
    L_0x005f:
        r1 = r7.name;
        r1 = r13.equals(r1);
        if (r1 == 0) goto L_0x00b3;
    L_0x0067:
        r1 = r17.getNamespace();
        r3 = r7.namespace;
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x00b3;
    L_0x0073:
        r5 = 0;
        r6 = 0;
        r1 = r16;
        r2 = r17;
        r3 = r18;
        r1 = r1.read(r2, r3, r4, r5, r6, r7);
        r0 = r18;
        r0.setProperty(r4, r1);
        goto L_0x000a;
    L_0x0085:
        r1 = r18;
        r1 = (org.ksoap2.serialization.SoapObject) r1;
        r3 = r17.getName();
        r11 = r18.getPropertyCount();
        r2 = r18;
        r2 = (org.ksoap2.serialization.SoapObject) r2;
        r12 = r2.getNamespace();
        r14 = org.ksoap2.serialization.PropertyInfo.OBJECT_TYPE;
        r8 = r16;
        r9 = r17;
        r10 = r18;
        r2 = r8.read(r9, r10, r11, r12, r13, r14);
        r1.addProperty(r3, r2);
        goto L_0x000a;
    L_0x00aa:
        r1 = 3;
        r2 = 0;
        r3 = 0;
        r0 = r17;
        r0.require(r1, r2, r3);
        return;
    L_0x00b3:
        r1 = r2;
        goto L_0x0022;
    L_0x00b6:
        r1 = r15;
        goto L_0x0022;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.ksoap2.serialization.SoapSerializationEnvelope.readSerializable(org.xmlpull.v1.XmlPullParser, org.ksoap2.serialization.KvmSerializable):void");
    }

    protected void readSerializable(XmlPullParser xmlPullParser, SoapObject soapObject) throws IOException, XmlPullParserException {
        for (int i = QNAME_NAMESPACE; i < xmlPullParser.getAttributeCount(); i += QNAME_TYPE) {
            soapObject.addAttribute(xmlPullParser.getAttributeName(i), xmlPullParser.getAttributeValue(i));
        }
        readSerializable(xmlPullParser, (KvmSerializable) soapObject);
    }

    protected Object readUnknown(XmlPullParser xmlPullParser, String str, String str2) throws IOException, XmlPullParserException {
        String text;
        SoapPrimitive soapPrimitive;
        Object obj;
        String name = xmlPullParser.getName();
        String namespace = xmlPullParser.getNamespace();
        xmlPullParser.next();
        if (xmlPullParser.getEventType() == 4) {
            text = xmlPullParser.getText();
            soapPrimitive = new SoapPrimitive(str, str2, text);
            xmlPullParser.next();
        } else if (xmlPullParser.getEventType() == QNAME_MARSHAL) {
            Object soapObject = new SoapObject(str, str2);
            text = null;
        } else {
            text = null;
            soapPrimitive = null;
        }
        if (xmlPullParser.getEventType() != 2) {
            obj = soapPrimitive;
        } else if (text == null || text.trim().length() == 0) {
            obj = new SoapObject(str, str2);
            while (xmlPullParser.getEventType() != QNAME_MARSHAL) {
                obj.addProperty(xmlPullParser.getName(), read(xmlPullParser, obj, obj.getPropertyCount(), null, null, PropertyInfo.OBJECT_TYPE));
                xmlPullParser.nextTag();
            }
        } else {
            throw new RuntimeException("Malformed input: Mixed content");
        }
        xmlPullParser.require(QNAME_MARSHAL, namespace, name);
        return obj;
    }

    protected void readVector(XmlPullParser xmlPullParser, Vector vector, PropertyInfo propertyInfo) throws IOException, XmlPullParserException {
        String substring;
        String namespace;
        int i;
        int i2;
        int size = vector.size();
        String attributeValue = xmlPullParser.getAttributeValue(this.enc, ARRAY_TYPE_LABEL);
        if (attributeValue != null) {
            size = attributeValue.indexOf(58);
            int indexOf = attributeValue.indexOf("[", size);
            substring = attributeValue.substring(size + QNAME_TYPE, indexOf);
            namespace = xmlPullParser.getNamespace(size == -1 ? XmlPullParser.NO_NAMESPACE : attributeValue.substring(QNAME_NAMESPACE, size));
            size = getIndex(attributeValue, indexOf, -1);
            if (size != -1) {
                vector.setSize(size);
                i = QNAME_NAMESPACE;
                i2 = size;
            } else {
                i = QNAME_TYPE;
                i2 = size;
            }
        } else {
            substring = null;
            namespace = null;
            i = QNAME_TYPE;
            i2 = size;
        }
        PropertyInfo propertyInfo2 = propertyInfo == null ? PropertyInfo.OBJECT_TYPE : propertyInfo;
        xmlPullParser.nextTag();
        size = getIndex(xmlPullParser.getAttributeValue(this.enc, "offset"), QNAME_NAMESPACE, QNAME_NAMESPACE);
        while (xmlPullParser.getEventType() != QNAME_MARSHAL) {
            indexOf = getIndex(xmlPullParser.getAttributeValue(this.enc, "position"), QNAME_NAMESPACE, size);
            if (i != 0 && indexOf >= r1) {
                i2 = indexOf + QNAME_TYPE;
                vector.setSize(i2);
            }
            int i3 = i2;
            vector.setElementAt(read(xmlPullParser, vector, indexOf, namespace, substring, propertyInfo2), indexOf);
            size = indexOf + QNAME_TYPE;
            xmlPullParser.nextTag();
            i2 = i3;
        }
        xmlPullParser.require(QNAME_MARSHAL, null, null);
    }

    public void setAddAdornments(boolean z) {
        this.addAdornments = z;
    }

    public void writeBody(XmlSerializer xmlSerializer) throws IOException {
        this.multiRef = new Vector();
        this.multiRef.addElement(this.bodyOut);
        Object[] info = getInfo(null, this.bodyOut);
        xmlSerializer.startTag(this.dotNet ? XmlPullParser.NO_NAMESPACE : (String) info[QNAME_NAMESPACE], (String) info[QNAME_TYPE]);
        if (this.dotNet) {
            xmlSerializer.attribute(null, "xmlns", (String) info[QNAME_NAMESPACE]);
        }
        if (this.addAdornments) {
            xmlSerializer.attribute(null, ID_LABEL, info[2] == null ? "o0" : (String) info[2]);
            xmlSerializer.attribute(this.enc, ROOT_LABEL, "1");
        }
        writeElement(xmlSerializer, this.bodyOut, null, info[QNAME_MARSHAL]);
        xmlSerializer.endTag(this.dotNet ? XmlPullParser.NO_NAMESPACE : (String) info[QNAME_NAMESPACE], (String) info[QNAME_TYPE]);
    }

    public void writeObjectBody(XmlSerializer xmlSerializer, KvmSerializable kvmSerializable) throws IOException {
        PropertyInfo propertyInfo = new PropertyInfo();
        int propertyCount = kvmSerializable.getPropertyCount();
        for (int i = QNAME_NAMESPACE; i < propertyCount; i += QNAME_TYPE) {
            kvmSerializable.getPropertyInfo(i, this.properties, propertyInfo);
            if ((propertyInfo.flags & QNAME_TYPE) == 0) {
                xmlSerializer.startTag(propertyInfo.namespace, propertyInfo.name);
                writeProperty(xmlSerializer, kvmSerializable.getProperty(i), propertyInfo);
                xmlSerializer.endTag(propertyInfo.namespace, propertyInfo.name);
            }
        }
    }

    public void writeObjectBody(XmlSerializer xmlSerializer, SoapObject soapObject) throws IOException {
        for (int i = QNAME_NAMESPACE; i < soapObject.getAttributeCount(); i += QNAME_TYPE) {
            AttributeInfo attributeInfo = new AttributeInfo();
            soapObject.getAttributeInfo(i, attributeInfo);
            xmlSerializer.attribute(attributeInfo.getNamespace(), attributeInfo.getName(), attributeInfo.getValue().toString());
        }
        writeObjectBody(xmlSerializer, (KvmSerializable) soapObject);
    }

    protected void writeProperty(XmlSerializer xmlSerializer, Object obj, PropertyInfo propertyInfo) throws IOException {
        if (obj == null) {
            xmlSerializer.attribute(this.xsi, this.version >= SoapEnvelope.VER12 ? NIL_LABEL : NULL_LABEL, "true");
            return;
        }
        Object[] info = getInfo(null, obj);
        if (propertyInfo.multiRef || info[2] != null) {
            int indexOf = this.multiRef.indexOf(obj);
            if (indexOf == -1) {
                indexOf = this.multiRef.size();
                this.multiRef.addElement(obj);
            }
            xmlSerializer.attribute(null, HREF_LABEL, info[2] == null ? "#o" + indexOf : "#" + info[2]);
            return;
        }
        if (!(this.implicitTypes && obj.getClass() == propertyInfo.type)) {
            xmlSerializer.attribute(this.xsi, TYPE_LABEL, xmlSerializer.getPrefix((String) info[QNAME_NAMESPACE], true) + ":" + info[QNAME_TYPE]);
        }
        writeElement(xmlSerializer, obj, propertyInfo, info[QNAME_MARSHAL]);
    }

    protected void writeVectorBody(XmlSerializer xmlSerializer, Vector vector, PropertyInfo propertyInfo) throws IOException {
        if (propertyInfo == null) {
            propertyInfo = PropertyInfo.OBJECT_TYPE;
        }
        int size = vector.size();
        Object[] info = getInfo(propertyInfo.type, null);
        xmlSerializer.attribute(this.enc, ARRAY_TYPE_LABEL, xmlSerializer.getPrefix((String) info[QNAME_NAMESPACE], false) + ":" + info[QNAME_TYPE] + "[" + size + "]");
        boolean z = false;
        for (int i = QNAME_NAMESPACE; i < size; i += QNAME_TYPE) {
            if (vector.elementAt(i) == null) {
                z = QNAME_TYPE;
            } else {
                xmlSerializer.startTag(null, ITEM_LABEL);
                if (z) {
                    xmlSerializer.attribute(this.enc, "position", "[" + i + "]");
                    z = false;
                }
                writeProperty(xmlSerializer, vector.elementAt(i), propertyInfo);
                xmlSerializer.endTag(null, ITEM_LABEL);
            }
        }
    }
}