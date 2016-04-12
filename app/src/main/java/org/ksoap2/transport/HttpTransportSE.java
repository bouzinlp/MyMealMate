package org.ksoap2.transport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.ksoap2.SoapEnvelope;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class HttpTransportSE extends Transport {
    public HttpTransportSE(String str) {
        super(str);
    }

    public void call(String str, SoapEnvelope soapEnvelope) throws IOException, XmlPullParserException {
        InputStream openInputStream;
        if (str == null) {
            str = "\"\"";
        }
        byte[] createRequestData = createRequestData(soapEnvelope);
        this.requestDump = this.debug ? new String(createRequestData) : null;
        this.responseDump = null;
        ServiceConnection serviceConnection = getServiceConnection();
        serviceConnection.setRequestProperty("User-Agent", "kSOAP/2.0");
        serviceConnection.setRequestProperty("SOAPAction", str);
        serviceConnection.setRequestProperty("Content-Type", "text/xml");
        serviceConnection.setRequestProperty("Connection", "close");
        serviceConnection.setRequestProperty("Content-Length", XmlPullParser.NO_NAMESPACE + createRequestData.length);
        serviceConnection.setRequestMethod("POST");
        serviceConnection.connect();
        OutputStream openOutputStream = serviceConnection.openOutputStream();
        openOutputStream.write(createRequestData, 0, createRequestData.length);
        openOutputStream.flush();
        openOutputStream.close();
        try {
            serviceConnection.connect();
            openInputStream = serviceConnection.openInputStream();
        } catch (IOException e) {
            IOException iOException = e;
            openInputStream = serviceConnection.getErrorStream();
            if (openInputStream == null) {
                serviceConnection.disconnect();
                throw iOException;
            }
        }
        if (this.debug) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            createRequestData = new byte[256];
            while (true) {
                int read = openInputStream.read(createRequestData, 0, 256);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream.write(createRequestData, 0, read);
            }
            byteArrayOutputStream.flush();
            byte[] toByteArray = byteArrayOutputStream.toByteArray();
            this.responseDump = new String(toByteArray);
            openInputStream.close();
            openInputStream = new ByteArrayInputStream(toByteArray);
        }
        parseResponse(soapEnvelope, openInputStream);
    }

    protected ServiceConnection getServiceConnection() throws IOException {
        return new ServiceConnectionSE(this.url);
    }
}