package org.ksoap2.serialization;

import java.util.Hashtable;
import java.util.Vector;
import org.xmlpull.v1.XmlPullParser;

public class SoapObject implements KvmSerializable {
    protected Vector attributes;
    protected String name;
    protected String namespace;
    protected Vector properties;

    public SoapObject(String str, String str2) {
        this.properties = new Vector();
        this.attributes = new Vector();
        this.namespace = str;
        this.name = str2;
    }

    public SoapObject addAttribute(String str, Object obj) {
        AttributeInfo attributeInfo = new AttributeInfo();
        attributeInfo.name = str;
        attributeInfo.type = obj == null ? PropertyInfo.OBJECT_CLASS : obj.getClass();
        attributeInfo.value = obj;
        return addAttribute(attributeInfo);
    }

    public SoapObject addAttribute(AttributeInfo attributeInfo) {
        this.attributes.addElement(attributeInfo);
        return this;
    }

    public SoapObject addProperty(String str, Object obj) {
        PropertyInfo propertyInfo = new PropertyInfo();
        propertyInfo.name = str;
        propertyInfo.type = obj == null ? PropertyInfo.OBJECT_CLASS : obj.getClass();
        propertyInfo.value = obj;
        return addProperty(propertyInfo);
    }

    public SoapObject addProperty(PropertyInfo propertyInfo) {
        this.properties.addElement(propertyInfo);
        return this;
    }

    public SoapObject addProperty(PropertyInfo propertyInfo, Object obj) {
        propertyInfo.setValue(obj);
        addProperty(propertyInfo);
        return this;
    }

    public boolean equals(Object obj) {
        if (obj instanceof SoapObject) {
            SoapObject soapObject = (SoapObject) obj;
            int size = this.properties.size();
            if (size == soapObject.properties.size()) {
                int size2 = this.attributes.size();
                if (size2 == soapObject.attributes.size()) {
                    int i = 0;
                    while (i < size) {
                        try {
                            PropertyInfo propertyInfo = (PropertyInfo) this.properties.elementAt(i);
                            if (!propertyInfo.getValue().equals(soapObject.getProperty(propertyInfo.getName()))) {
                                break;
                            }
                            i++;
                        } catch (Exception e) {
                            return false;
                        }
                    }
                    i = 0;
                    while (i < size2) {
                        AttributeInfo attributeInfo = (AttributeInfo) this.properties.elementAt(i);
                        if (attributeInfo.getValue().equals(soapObject.getProperty(attributeInfo.getName()))) {
                            i++;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public Object getAttribute(int i) {
        return ((AttributeInfo) this.attributes.elementAt(i)).getValue();
    }

    public Object getAttribute(String str) {
        for (int i = 0; i < this.attributes.size(); i++) {
            if (str.equals(((AttributeInfo) this.attributes.elementAt(i)).getName())) {
                return getAttribute(i);
            }
        }
        throw new RuntimeException("illegal property: " + str);
    }

    public int getAttributeCount() {
        return this.attributes.size();
    }

    public void getAttributeInfo(int i, AttributeInfo attributeInfo) {
        AttributeInfo attributeInfo2 = (AttributeInfo) this.attributes.elementAt(i);
        attributeInfo.name = attributeInfo2.name;
        attributeInfo.namespace = attributeInfo2.namespace;
        attributeInfo.flags = attributeInfo2.flags;
        attributeInfo.type = attributeInfo2.type;
        attributeInfo.elementType = attributeInfo2.elementType;
        attributeInfo.value = attributeInfo2.getValue();
    }

    public String getName() {
        return this.name;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public Object getProperty(int i) {
        return ((PropertyInfo) this.properties.elementAt(i)).getValue();
    }

    public Object getProperty(String str) {
        for (int i = 0; i < this.properties.size(); i++) {
            if (str.equals(((PropertyInfo) this.properties.elementAt(i)).getName())) {
                return getProperty(i);
            }
        }
        throw new RuntimeException("illegal property: " + str);
    }

    public int getPropertyCount() {
        return this.properties.size();
    }

    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        getPropertyInfo(i, propertyInfo);
    }

    public void getPropertyInfo(int i, PropertyInfo propertyInfo) {
        PropertyInfo propertyInfo2 = (PropertyInfo) this.properties.elementAt(i);
        propertyInfo.name = propertyInfo2.name;
        propertyInfo.namespace = propertyInfo2.namespace;
        propertyInfo.flags = propertyInfo2.flags;
        propertyInfo.type = propertyInfo2.type;
        propertyInfo.elementType = propertyInfo2.elementType;
    }

    public SoapObject newInstance() {
        int i = 0;
        SoapObject soapObject = new SoapObject(this.namespace, this.name);
        for (int i2 = 0; i2 < this.properties.size(); i2++) {
            soapObject.addProperty((PropertyInfo) this.properties.elementAt(i2));
        }
        while (i < this.attributes.size()) {
            soapObject.addAttribute((AttributeInfo) this.attributes.elementAt(i));
            i++;
        }
        return soapObject;
    }

    public void setProperty(int i, Object obj) {
        ((PropertyInfo) this.properties.elementAt(i)).setValue(obj);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(XmlPullParser.NO_NAMESPACE + this.name + "{");
        for (int i = 0; i < getPropertyCount(); i++) {
            stringBuffer.append(XmlPullParser.NO_NAMESPACE + ((PropertyInfo) this.properties.elementAt(i)).getName() + "=" + getProperty(i) + "; ");
        }
        stringBuffer.append("}");
        return stringBuffer.toString();
    }
}