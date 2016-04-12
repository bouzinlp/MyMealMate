package org.kobjects.rss;

import java.io.IOException;
import java.io.Reader;
import org.achartengine.ChartFactory;
import org.kobjects.xml.XmlReader;

public class RssReader {
    public static final int AUTHOR = 4;
    public static final int DATE = 3;
    public static final int DESCRIPTION = 2;
    public static final int LINK = 1;
    public static final int TITLE = 0;
    XmlReader xr;

    public RssReader(Reader reader) throws IOException {
        this.xr = new XmlReader(reader);
    }

    public String[] next() throws IOException {
        String[] strArr = new String[5];
        while (this.xr.next() != LINK) {
            if (this.xr.getType() == DESCRIPTION) {
                String toLowerCase = this.xr.getName().toLowerCase();
                if (toLowerCase.equals("item") || toLowerCase.endsWith(":item")) {
                    while (this.xr.next() != DATE) {
                        if (this.xr.getType() == DESCRIPTION) {
                            toLowerCase = this.xr.getName().toLowerCase();
                            int indexOf = toLowerCase.indexOf(":");
                            if (indexOf != -1) {
                                toLowerCase = toLowerCase.substring(indexOf + LINK);
                            }
                            StringBuffer stringBuffer = new StringBuffer();
                            readText(stringBuffer);
                            String stringBuffer2 = stringBuffer.toString();
                            if (toLowerCase.equals(ChartFactory.TITLE)) {
                                strArr[0] = stringBuffer2;
                            } else if (toLowerCase.equals("link")) {
                                strArr[LINK] = stringBuffer2;
                            } else if (toLowerCase.equals("description")) {
                                strArr[DESCRIPTION] = stringBuffer2;
                            } else if (toLowerCase.equals("date")) {
                                strArr[DATE] = stringBuffer2;
                            } else if (toLowerCase.equals("author")) {
                                strArr[AUTHOR] = stringBuffer2;
                            }
                        }
                    }
                    return strArr;
                }
            }
        }
        return null;
    }

    void readText(StringBuffer stringBuffer) throws IOException {
        while (this.xr.next() != DATE) {
            switch (this.xr.getType()) {
                case DESCRIPTION /*2*/:
                    readText(stringBuffer);
                    break;
                case AUTHOR /*4*/:
                    stringBuffer.append(this.xr.getText());
                    break;
                default:
                    break;
            }
        }
    }
}