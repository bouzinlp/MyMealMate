package org.ksoap2.serialization;

import java.io.IOException;
import java.math.BigDecimal;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class MarshalFloat implements Marshal {
    public Object readInstance(XmlPullParser xmlPullParser, String str, String str2, PropertyInfo propertyInfo) throws IOException, XmlPullParserException {
        String nextText = xmlPullParser.nextText();
        if (str2.equals("float")) {
            return new Float(nextText);
        }
        if (str2.equals("double")) {
            return new Double(nextText);
        }
        if (str2.equals("decimal")) {
            return new BigDecimal(nextText);
        }
        throw new RuntimeException("float, double, or decimal expected");
    }

    public void register(SoapSerializationEnvelope soapSerializationEnvelope) {
        soapSerializationEnvelope.addMapping(soapSerializationEnvelope.xsd, "float", Float.class, this);
        soapSerializationEnvelope.addMapping(soapSerializationEnvelope.xsd, "double", Double.class, this);
        soapSerializationEnvelope.addMapping(soapSerializationEnvelope.xsd, "decimal", BigDecimal.class, this);
    }

    public void writeInstance(XmlSerializer xmlSerializer, Object obj) throws IOException {
        xmlSerializer.text(obj.toString());
    }
}