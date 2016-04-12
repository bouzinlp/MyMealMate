package org.ksoap2.transport;

import java.io.IOException;

public class AndroidHttpTransport extends HttpTransportSE {
    public AndroidHttpTransport(String str) {
        super(str);
    }

    protected ServiceConnection getServiceConnection() throws IOException {
        return new AndroidServiceConnection(this.url);
    }
}