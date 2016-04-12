package org.xmlpull.v1;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class XmlPullParserFactory {
    public static final String PROPERTY_NAME = "org.xmlpull.v1.XmlPullParserFactory";
    private static final String RESOURCE_NAME = "/META-INF/services/org.xmlpull.v1.XmlPullParserFactory";
    static final Class referenceContextClass;
    protected String classNamesLocation;
    protected Hashtable features;
    protected Vector parserClasses;
    protected Vector serializerClasses;

    static {
        referenceContextClass = new XmlPullParserFactory().getClass();
    }

    protected XmlPullParserFactory() {
        this.features = new Hashtable();
    }

    public static XmlPullParserFactory newInstance() throws XmlPullParserException {
        return newInstance(null, null);
    }

    public static XmlPullParserFactory newInstance(String str, Class cls) throws XmlPullParserException {
        String str2;
        Class cls2;
        Object obj = new Object();
        Object newInstance;
        Class cls3;
        Object obj2;
        if (cls == null) {
            cls = referenceContextClass;
        }
        if (str == null || str.length() == 0 || "DEFAULT".equals(str)) {
            try {
                InputStream resourceAsStream = cls.getResourceAsStream(RESOURCE_NAME);
                if (resourceAsStream == null) {
                    throw new XmlPullParserException("resource not found: /META-INF/services/org.xmlpull.v1.XmlPullParserFactory make sure that parser implementing XmlPull API is available");
                }
                StringBuffer stringBuffer = new StringBuffer();
                while (true) {
                    int read = resourceAsStream.read();
                    if (read < 0) {
                        break;
                    } else if (read > 32) {
                        stringBuffer.append((char) read);
                    }
                }
                resourceAsStream.close();
                str = stringBuffer.toString();
                str2 = "resource /META-INF/services/org.xmlpull.v1.XmlPullParserFactory that contained '" + str + "'";
            } catch (Throwable e) {
                throw new XmlPullParserException(null, null, e);
            }
        }
        str2 = "parameter classNames to newInstance() that contained '" + str + "'";
        Vector vector = new Vector();
        Vector vector2 = new Vector();
        int i = 0;
        XmlPullParserFactory xmlPullParserFactory = null;
        while (i < str.length()) {
            int indexOf = str.indexOf(44, i);
            if (indexOf == -1) {
                indexOf = str.length();
            }
            String substring = str.substring(i, indexOf);
            try {
                cls2 = Class.forName(substring);
                try {
                    obj = cls2;
                    newInstance = cls2.newInstance();
                } catch (Exception e2) {
                    cls3 = cls2;
                    newInstance = null;
                    if (obj != null) {
                        if (newInstance instanceof XmlPullParser) {
                            obj2 = null;
                        } else {
                            vector.addElement(obj);
                            obj2 = 1;
                        }
                        if (newInstance instanceof XmlSerializer) {
                            vector2.addElement(obj);
                            obj2 = 1;
                        }
                        if (newInstance instanceof XmlPullParserFactory) {
                            obj2 = 1;
                            xmlPullParserFactory = xmlPullParserFactory == null ? xmlPullParserFactory : (XmlPullParserFactory) newInstance;
                        }
                        if (obj2 != null) {
                            throw new XmlPullParserException("incompatible class: " + substring);
                        }
                    }
                    i = indexOf + 1;
                }
            } catch (Exception e3) {
                cls2 = null;
                cls3 = cls2;
                newInstance = null;
                if (obj != null) {
                    if (newInstance instanceof XmlPullParser) {
                        vector.addElement(obj);
                        obj2 = 1;
                    } else {
                        obj2 = null;
                    }
                    if (newInstance instanceof XmlSerializer) {
                        vector2.addElement(obj);
                        obj2 = 1;
                    }
                    if (newInstance instanceof XmlPullParserFactory) {
                        if (xmlPullParserFactory == null) {
                        }
                        obj2 = 1;
                        xmlPullParserFactory = xmlPullParserFactory == null ? xmlPullParserFactory : (XmlPullParserFactory) newInstance;
                    }
                    if (obj2 != null) {
                        throw new XmlPullParserException("incompatible class: " + substring);
                    }
                }
                i = indexOf + 1;
            }
            if (obj != null) {
                if (newInstance instanceof XmlPullParser) {
                    vector.addElement(obj);
                    obj2 = 1;
                } else {
                    obj2 = null;
                }
                if (newInstance instanceof XmlSerializer) {
                    vector2.addElement(obj);
                    obj2 = 1;
                }
                if (newInstance instanceof XmlPullParserFactory) {
                    if (xmlPullParserFactory == null) {
                    }
                    obj2 = 1;
                    xmlPullParserFactory = xmlPullParserFactory == null ? xmlPullParserFactory : (XmlPullParserFactory) newInstance;
                }
                if (obj2 != null) {
                    throw new XmlPullParserException("incompatible class: " + substring);
                }
            }
            i = indexOf + 1;
        }
        if (xmlPullParserFactory == null) {
            xmlPullParserFactory = new XmlPullParserFactory();
        }
        xmlPullParserFactory.parserClasses = vector;
        xmlPullParserFactory.serializerClasses = vector2;
        xmlPullParserFactory.classNamesLocation = str2;
        return xmlPullParserFactory;
    }

    public boolean getFeature(String str) {
        Boolean bool = (Boolean) this.features.get(str);
        return bool != null ? bool.booleanValue() : false;
    }

    public boolean isNamespaceAware() {
        return getFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES);
    }

    public boolean isValidating() {
        return getFeature(XmlPullParser.FEATURE_VALIDATION);
    }

    public XmlPullParser newPullParser() throws XmlPullParserException {
        if (this.parserClasses == null) {
            throw new XmlPullParserException("Factory initialization was incomplete - has not tried " + this.classNamesLocation);
        } else if (this.parserClasses.size() == 0) {
            throw new XmlPullParserException("No valid parser classes found in " + this.classNamesLocation);
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            int i = 0;
            while (i < this.parserClasses.size()) {
                Class cls = (Class) this.parserClasses.elementAt(i);
                try {
                    XmlPullParser xmlPullParser = (XmlPullParser) cls.newInstance();
                    Enumeration keys = this.features.keys();
                    while (keys.hasMoreElements()) {
                        String str = (String) keys.nextElement();
                        Boolean bool = (Boolean) this.features.get(str);
                        if (bool != null && bool.booleanValue()) {
                            xmlPullParser.setFeature(str, true);
                        }
                    }
                    return xmlPullParser;
                } catch (Exception e) {
                    stringBuffer.append(cls.getName() + ": " + e.toString() + "; ");
                    i++;
                }
            }
            throw new XmlPullParserException("could not create parser: " + stringBuffer);
        }
    }

    public XmlSerializer newSerializer() throws XmlPullParserException {
        if (this.serializerClasses == null) {
            throw new XmlPullParserException("Factory initialization incomplete - has not tried " + this.classNamesLocation);
        } else if (this.serializerClasses.size() == 0) {
            throw new XmlPullParserException("No valid serializer classes found in " + this.classNamesLocation);
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            int i = 0;
            while (i < this.serializerClasses.size()) {
                Class cls = (Class) this.serializerClasses.elementAt(i);
                try {
                    return (XmlSerializer) cls.newInstance();
                } catch (Exception e) {
                    stringBuffer.append(cls.getName() + ": " + e.toString() + "; ");
                    i++;
                }
            }
            throw new XmlPullParserException("could not create serializer: " + stringBuffer);
        }
    }

    public void setFeature(String str, boolean z) throws XmlPullParserException {
        this.features.put(str, new Boolean(z));
    }

    public void setNamespaceAware(boolean z) {
        this.features.put(XmlPullParser.FEATURE_PROCESS_NAMESPACES, new Boolean(z));
    }

    public void setValidating(boolean z) {
        this.features.put(XmlPullParser.FEATURE_VALIDATION, new Boolean(z));
    }
}