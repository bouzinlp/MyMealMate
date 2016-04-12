package org.ksoap2.serialization;

import java.io.IOException;
import org.ksoap2.SoapEnvelope;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

class DM implements Marshal {
    DM() {
    }

    public Object readInstance(XmlPullParser xmlPullParser, String str, String str2, PropertyInfo propertyInfo) throws IOException, XmlPullParserException {
        String nextText = xmlPullParser.nextText();
        switch (str2.charAt(0)) {
            case 'b':
                return new Boolean(SoapEnvelope.stringToBoolean(nextText));
            case 'i':
                return new Integer(Integer.parseInt(nextText));
            case 'l':
                return new Long(Long.parseLong(nextText));
            case 's':
                return nextText;
            default:
                throw new RuntimeException();
        }
    }

    public void register(SoapSerializationEnvelope soapSerializationEnvelope) {
        soapSerializationEnvelope.addMapping(soapSerializationEnvelope.xsd, "int", PropertyInfo.INTEGER_CLASS, this);
        soapSerializationEnvelope.addMapping(soapSerializationEnvelope.xsd, "long", PropertyInfo.LONG_CLASS, this);
        soapSerializationEnvelope.addMapping(soapSerializationEnvelope.xsd, "string", PropertyInfo.STRING_CLASS, this);
        soapSerializationEnvelope.addMapping(soapSerializationEnvelope.xsd, "boolean", PropertyInfo.BOOLEAN_CLASS, this);
    }

    public void writeInstance(XmlSerializer xmlSerializer, Object obj) throws IOException {
        xmlSerializer.text(obj.toString());
    }
}