package com.mymealmate;

import android.util.Log;
import java.util.Hashtable;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;
import org.ksoap2.transport.Transport;

public class WSClient {
    private static final String NAMESPACE = "http://tempuri.org/";
    private static final String TAG = "MyMealMate";
    private static String URL;
    private static String _participantId;

    static abstract class WSMethod {
        protected SoapSerializationEnvelope _envelope;
        protected Hashtable<String, Object> _properties;
        protected String _soapAction;
        protected String _soapMethod;

        protected WSMethod(String str, String str2) {
            this._properties = new Hashtable();
            this._soapMethod = str;
            this._soapAction = str2;
        }

        public WSMethod addProperty(String str, Object obj) {
            this._properties.put(str, obj);
            return this;
        }

        protected boolean call() {
            try {
                init().call(this._soapAction, this._envelope);
                Object response = this._envelope.getResponse();
                return response != null && response.toString().toLowerCase().compareTo("true") == 0;
            } catch (Exception e) {
                Log.e(WSClient.TAG, e.getMessage());
                return false;
            }
        }

        protected Transport init() {
            SoapObject soapObject = new SoapObject(WSClient.NAMESPACE, this._soapMethod);
            for (String str : this._properties.keySet()) {
                soapObject.addProperty(str, this._properties.get(str));
            }
            this._envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            this._envelope.dotNet = true;
            this._envelope.setOutputSoapObject(soapObject);
            return new AndroidHttpTransport(WSClient.URL);
        }
    }

    public static class TestConnection extends WSMethod {
        TestConnection() {
            super("TestConnection", "http://tempuri.org/TestConnection");
        }

        public /* bridge */ /* synthetic */ WSMethod addProperty(String str, Object obj) {
            return super.addProperty(str, obj);
        }
    }

    public static class UploadDateFlags extends WSMethod {
        UploadDateFlags() {
            super("UploadDateFlags", "http://tempuri.org/UploadDateFlags");
        }

        public /* bridge */ /* synthetic */ WSMethod addProperty(String str, Object obj) {
            return super.addProperty(str, obj);
        }
    }

    public static class UploadExercise extends WSMethod {
        UploadExercise() {
            super("UploadExerciseEntry", "http://tempuri.org/UploadExerciseEntry");
        }

        public /* bridge */ /* synthetic */ WSMethod addProperty(String str, Object obj) {
            return super.addProperty(str, obj);
        }
    }

    public static class UploadFood extends WSMethod {
        UploadFood() {
            super("UploadFoodEntry", "http://tempuri.org/UploadFoodEntry");
        }

        public /* bridge */ /* synthetic */ WSMethod addProperty(String str, Object obj) {
            return super.addProperty(str, obj);
        }
    }

    public static class UploadLog extends WSMethod {
        UploadLog() {
            super("UploadLog", "http://tempuri.org/UploadLog");
        }

        public /* bridge */ /* synthetic */ WSMethod addProperty(String str, Object obj) {
            return super.addProperty(str, obj);
        }
    }

    public static class UploadWeight extends WSMethod {
        UploadWeight() {
            super("UploadWeight", "http://tempuri.org/UploadWeight");
        }

        public /* bridge */ /* synthetic */ WSMethod addProperty(String str, Object obj) {
            return super.addProperty(str, obj);
        }
    }

    public static class UploadWeightLossTarget extends WSMethod {
        UploadWeightLossTarget() {
            super("UploadWeightLossTarget", "http://tempuri.org/UploadWeightLossTarget");
        }

        public /* bridge */ /* synthetic */ WSMethod addProperty(String str, Object obj) {
            return super.addProperty(str, obj);
        }
    }

    public static String getParticipantId() {
        return _participantId;
    }

    public static String getUrl() {
        return URL;
    }

    public static void setParticipantId(String str) {
        _participantId = str;
    }

    public static void setUrl(String str) {
        URL = str;
    }
}