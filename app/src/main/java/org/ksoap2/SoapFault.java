package org.ksoap2;

import java.io.IOException;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class SoapFault extends IOException {
    public Node detail;
    public String faultactor;
    public String faultcode;
    public String faultstring;

    public void parse(XmlPullParser xmlPullParser) throws IOException, XmlPullParserException {
        xmlPullParser.require(2, SoapEnvelope.ENV, "Fault");
        while (xmlPullParser.nextTag() == 2) {
            String name = xmlPullParser.getName();
            if (name.equals("detail")) {
                this.detail = new Node();
                this.detail.parse(xmlPullParser);
            } else {
                if (name.equals("faultcode")) {
                    this.faultcode = xmlPullParser.nextText();
                } else if (name.equals("faultstring")) {
                    this.faultstring = xmlPullParser.nextText();
                } else if (name.equals("faultactor")) {
                    this.faultactor = xmlPullParser.nextText();
                } else {
                    throw new RuntimeException("unexpected tag:" + name);
                }
                xmlPullParser.require(3, null, name);
            }
        }
    }

    public String toString() {
        return "SoapFault - faultcode: '" + this.faultcode + "' faultstring: '" + this.faultstring + "' faultactor: '" + this.faultactor + "' detail: " + this.detail;
    }

    public void write(XmlSerializer xmlSerializer) throws IOException {
        xmlSerializer.startTag(SoapEnvelope.ENV, "Fault");
        xmlSerializer.startTag(null, "faultcode");
        xmlSerializer.text(XmlPullParser.NO_NAMESPACE + this.faultcode);
        xmlSerializer.endTag(null, "faultcode");
        xmlSerializer.startTag(null, "faultstring");
        xmlSerializer.text(XmlPullParser.NO_NAMESPACE + this.faultstring);
        xmlSerializer.endTag(null, "faultstring");
        xmlSerializer.startTag(null, "detail");
        if (this.detail != null) {
            this.detail.write(xmlSerializer);
        }
        xmlSerializer.endTag(null, "detail");
        xmlSerializer.endTag(SoapEnvelope.ENV, "Fault");
    }
}