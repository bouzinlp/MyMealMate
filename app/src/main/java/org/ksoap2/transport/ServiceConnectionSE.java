package org.ksoap2.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServiceConnectionSE implements ServiceConnection {
    private HttpURLConnection connection;

    public ServiceConnectionSE(String str) throws IOException {
        this.connection = (HttpURLConnection) new URL(str).openConnection();
        this.connection.setUseCaches(false);
        this.connection.setDoOutput(true);
        this.connection.setDoInput(true);
    }

    public void connect() throws IOException {
        this.connection.connect();
    }

    public void disconnect() {
        this.connection.disconnect();
    }

    public InputStream getErrorStream() {
        return this.connection.getErrorStream();
    }

    public InputStream openInputStream() throws IOException {
        return this.connection.getInputStream();
    }

    public OutputStream openOutputStream() throws IOException {
        return this.connection.getOutputStream();
    }

    public void setRequestMethod(String str) throws IOException {
        this.connection.setRequestMethod(str);
    }

    public void setRequestProperty(String str, String str2) {
        this.connection.setRequestProperty(str, str2);
    }
}