package org.ksoap2.transport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.ksoap2.SoapEnvelope;
import org.kxml2.io.KXmlParser;
import org.kxml2.io.KXmlSerializer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public abstract class Transport {
    public boolean debug;
    public String requestDump;
    public String responseDump;
    protected String url;
    private String xmlVersionTag;

    public Transport() {
        this.xmlVersionTag = XmlPullParser.NO_NAMESPACE;
    }

    public Transport(String str) {
        this.xmlVersionTag = XmlPullParser.NO_NAMESPACE;
        this.url = str;
    }

    public abstract void call(String str, SoapEnvelope soapEnvelope) throws IOException, XmlPullParserException;

    protected byte[] createRequestData(SoapEnvelope soapEnvelope) throws IOException {
        OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(this.xmlVersionTag.getBytes());
        XmlSerializer kXmlSerializer = new KXmlSerializer();
        kXmlSerializer.setOutput(byteArrayOutputStream, null);
        soapEnvelope.write(kXmlSerializer);
        kXmlSerializer.flush();
        byteArrayOutputStream.write(13);
        byteArrayOutputStream.write(10);
        byteArrayOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    protected void parseResponse(SoapEnvelope soapEnvelope, InputStream inputStream) throws XmlPullParserException, IOException {
        XmlPullParser kXmlParser = new KXmlParser();
        kXmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
        kXmlParser.setInput(inputStream, null);
        soapEnvelope.parse(kXmlParser);
    }

    public void reset() {
    }

    public void setUrl(String str) {
        this.url = str;
    }

    public void setXmlVersionTag(String str) {
        this.xmlVersionTag = str;
    }
}