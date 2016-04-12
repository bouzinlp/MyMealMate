package org.ksoap2.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ServiceConnection {
    void connect() throws IOException;

    void disconnect() throws IOException;

    InputStream getErrorStream();

    InputStream openInputStream() throws IOException;

    OutputStream openOutputStream() throws IOException;

    void setRequestMethod(String str) throws IOException;

    void setRequestProperty(String str, String str2) throws IOException;
}