package com.mymealmate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import java.util.Date;
import org.xmlpull.v1.XmlPullParser;

public class Prefs extends BaseData {
    private final String DATE_BIRTH_NAME;
    private final Unit DEFAULT_HEIGHT_UNIT;
    private final Unit DEFAULT_WEIGHT_UNIT;
    private final String FEEDBACK_A1;
    private final String FEEDBACK_A2;
    private final String FEEDBACK_A3;
    private final String FEEDBACK_A4;
    private final String FEEDBACK_B1;
    private final String FEEDBACK_B2;
    private final String FEEDBACK_B3;
    private final String FEEDBACK_DAILY_CHECK;
    private final String FEEDBACK_MONTHLY_CHECK;
    private final String FEEDBACK_WEEKLY_CHECK;
    private final String GENDER_NAME;
    private final String HEIGHT_NAME;
    private final String HEIGHT_UNIT_NAME;
    private final String HOLIDAY_MODE_NAME;
    private final String HOUR_END_MESSAGE_BLOCK;
    private final String HOUR_START_MESSAGE_BLOCK;
    private final String IS_FIRST_RUN;
    private final String LAST_UPLOAD_NAME;
    private final String MESSAGE_BLOCK_ALWAYS;
    private final String MINUTE_END_MESSAGE_BLOCK;
    private final String MINUTE_START_MESSAGE_BLOCK;
    private final String PAL_VALUE_NAME;
    private final String PARTICIPANT_ID_NAME;
    private final String RUN_STANDALONE_NAME;
    private final String WEIGHT_NAME;
    private final String WEIGHT_UNIT_NAME;

    public enum Unit {
        METRIC,
        IMPERIAL,
        IMPERIAL_ST
    }

    public Prefs(MyMealMateDatabase myMealMateDatabase, Context context) {
        super(myMealMateDatabase);
        this.DEFAULT_HEIGHT_UNIT = Unit.METRIC;
        this.DEFAULT_WEIGHT_UNIT = Unit.METRIC;
        this.HEIGHT_UNIT_NAME = "HeightUnit";
        this.WEIGHT_UNIT_NAME = "WeightUnit";
        this.PARTICIPANT_ID_NAME = "ParticipantId";
        this.DATE_BIRTH_NAME = "DateOfBirth";
        this.GENDER_NAME = "Gender";
        this.HEIGHT_NAME = "Height";
        this.WEIGHT_NAME = "Weight";
        this.PAL_VALUE_NAME = "PAL";
        this.LAST_UPLOAD_NAME = "LastUpload";
        this.HOLIDAY_MODE_NAME = "HolidayMode";
        this.RUN_STANDALONE_NAME = "RunStandalone";
        this.FEEDBACK_DAILY_CHECK = "FeedbackDaily";
        this.FEEDBACK_WEEKLY_CHECK = "FeedbackWeekly";
        this.FEEDBACK_MONTHLY_CHECK = "FeedbackMonthly";
        this.HOUR_START_MESSAGE_BLOCK = "HourStartMessageBlock";
        this.HOUR_END_MESSAGE_BLOCK = "HourEndMessageBlock";
        this.MINUTE_START_MESSAGE_BLOCK = "MinuteStartMessageBlock";
        this.MINUTE_END_MESSAGE_BLOCK = "MinuteEndMessageBlock";
        this.MESSAGE_BLOCK_ALWAYS = "MessageBlockAlways";
        this.FEEDBACK_A1 = "FeedbackA1";
        this.FEEDBACK_A2 = "FeedbackA2";
        this.FEEDBACK_A3 = "FeedbackA3";
        this.FEEDBACK_A4 = "FeedbackA4";
        this.FEEDBACK_B1 = "FeedbackB1";
        this.FEEDBACK_B2 = "FeedbackB2";
        this.FEEDBACK_B3 = "FeedbackB3";
        this.IS_FIRST_RUN = "IsFirstRun";
        this._ctx = context;
    }

    public static String cmToFtString(float f) {
        return String.valueOf(new Float(((float) new Float(0.3937f * f).intValue()) / 12.0f).intValue());
    }

    public static String cmToInchString(float f) {
        return String.format("%.1f", new Object[]{Float.valueOf(new Float(0.3937f * f).floatValue() % 12.0f)});
    }

    public static String getBmiString(float f, float f2) {
        float f3 = f / 100.0f;
        return String.format("%.1f", new Object[]{Float.valueOf(f2 / (f3 * f3))});
    }

    private String getValue(String str) {
        SQLiteDatabase readableDatabase = _DB.getReadableDatabase();
        SQLiteStatement compileStatement = readableDatabase.compileStatement("SELECT Value FROM settings WHERE Name = ?");
        compileStatement.bindString(1, str);
        String simpleQueryForString;
        try {
            simpleQueryForString = compileStatement.simpleQueryForString();
            return simpleQueryForString;
        } catch (Exception e) {
            simpleQueryForString = e;
            return null;
        } finally {
            readableDatabase.close();
        }
    }

    public static String kgToLbsString(float f) {
        return String.valueOf(new Float(((double) f) * 2.205d).intValue());
    }

    public static String kgToStLbsString(float f) {
        return String.valueOf(new Float(2.205f * f).intValue() % 14);
    }

    public static String kgToStString(float f) {
        return String.valueOf(new Float(2.205f * f).intValue() / 14);
    }

    public String getDateOfBirth() {
        String value;
        synchronized (this) {
            value = getValue("DateOfBirth");
            if (value == null) {
                value = XmlPullParser.NO_NAMESPACE;
            }
        }
        return value;
    }

    public String getFeedbackA1() {
        String feedbackMessage;
        synchronized (this) {
            feedbackMessage = getFeedbackMessage("FeedbackA1", R.array.feedback_messages_a1);
        }
        return feedbackMessage;
    }

    public String getFeedbackA2() {
        String feedbackMessage;
        synchronized (this) {
            feedbackMessage = getFeedbackMessage("FeedbackA2", R.array.feedback_messages_a2);
        }
        return feedbackMessage;
    }

    public String getFeedbackA3() {
        String feedbackMessage;
        synchronized (this) {
            feedbackMessage = getFeedbackMessage("FeedbackA3", R.array.feedback_messages_a3);
        }
        return feedbackMessage;
    }

    public String getFeedbackA4() {
        String feedbackMessage;
        synchronized (this) {
            feedbackMessage = getFeedbackMessage("FeedbackA4", R.array.feedback_messages_a4);
        }
        return feedbackMessage;
    }

    public String getFeedbackB1() {
        String feedbackMessage;
        synchronized (this) {
            feedbackMessage = getFeedbackMessage("FeedbackB1", R.array.feedback_messages_b1);
        }
        return feedbackMessage;
    }

    public String getFeedbackB2() {
        String feedbackMessage;
        synchronized (this) {
            feedbackMessage = getFeedbackMessage("FeedbackB2", R.array.feedback_messages_b2);
        }
        return feedbackMessage;
    }

    public String getFeedbackB3() {
        String feedbackMessage;
        synchronized (this) {
            feedbackMessage = getFeedbackMessage("FeedbackB3", R.array.feedback_messages_b3);
        }
        return feedbackMessage;
    }

    public String getFeedbackMessage(String str, int i) {
        String value;
        int i2 = 0;
        synchronized (this) {
            value = getValue(str);
            int parseInt = (value == null || value.length() <= 0) ? 0 : Integer.parseInt(value);
            String[] stringArray = this._ctx.getResources().getStringArray(i);
            if (parseInt < stringArray.length) {
                i2 = parseInt;
            }
            value = stringArray[i2];
            setValue(str, String.valueOf(i2 + 1));
        }
        return value;
    }

    public Boolean getFirstRun() {
        Boolean valueOf;
        synchronized (this) {
            String value = getValue("IsFirstRun");
            valueOf = value != null ? Boolean.valueOf(Boolean.parseBoolean(value)) : Boolean.valueOf(false);
        }
        return valueOf;
    }

    public String getGender() {
        String value;
        synchronized (this) {
            value = getValue("Gender");
            if (value == null) {
                value = "M";
            }
        }
        return value;
    }

    public float getHeight() {
        float parseFloat;
        synchronized (this) {
            String value = getValue("Height");
            parseFloat = value != null ? Float.parseFloat(value) : 0.0f;
        }
        return parseFloat;
    }

    public Unit getHeightUnit() {
        Unit valueOf;
        synchronized (this) {
            String value = getValue("HeightUnit");
            valueOf = value != null ? Unit.valueOf(value) : this.DEFAULT_HEIGHT_UNIT;
        }
        return valueOf;
    }

    public Boolean getHolidayMode() {
        Boolean valueOf;
        synchronized (this) {
            String value = getValue("HolidayMode");
            valueOf = value != null ? Boolean.valueOf(Boolean.parseBoolean(value)) : Boolean.valueOf(false);
        }
        return valueOf;
    }

    public int getHourEndMessageBlock() {
        int parseInt;
        synchronized (this) {
            String value = getValue("HourEndMessageBlock");
            parseInt = (value == null || value.length() <= 0) ? 0 : Integer.parseInt(value);
        }
        return parseInt;
    }

    public int getHourStartMessageBlock() {
        int parseInt;
        synchronized (this) {
            String value = getValue("HourStartMessageBlock");
            parseInt = (value == null || value.length() <= 0) ? 0 : Integer.parseInt(value);
        }
        return parseInt;
    }

    public Date getLastDailyCheck() {
        Date date;
        synchronized (this) {
            String value = getValue("FeedbackDaily");
            date = (value == null || value.length() <= 0) ? new Date(0) : new Date(Long.parseLong(value));
        }
        return date;
    }

    public Date getLastMonthlyCheck() {
        Date date;
        synchronized (this) {
            String value = getValue("FeedbackMonthly");
            date = (value == null || value.length() <= 0) ? new Date(0) : new Date(Long.parseLong(value));
        }
        return date;
    }

    public long getLastUpload() {
        long parseLong;
        synchronized (this) {
            String value = getValue("LastUpload");
            parseLong = value != null ? Long.parseLong(value) : 0;
        }
        return parseLong;
    }

    public Date getLastWeeklyCheck() {
        Date date;
        synchronized (this) {
            String value = getValue("FeedbackWeekly");
            date = (value == null || value.length() <= 0) ? new Date(0) : new Date(Long.parseLong(value));
        }
        return date;
    }

    public boolean getMessageBlockAlways() {
        boolean parseBoolean;
        synchronized (this) {
            Object value = getValue("MessageBlockAlways");
            parseBoolean = !TextUtils.isEmpty(value) ? Boolean.parseBoolean(value) : false;
        }
        return parseBoolean;
    }

    public int getMinuteEndMessageBlock() {
        int parseInt;
        synchronized (this) {
            String value = getValue("MinuteEndMessageBlock");
            parseInt = (value == null || value.length() <= 0) ? 0 : Integer.parseInt(value);
        }
        return parseInt;
    }

    public int getMinuteStartMessageBlock() {
        int parseInt;
        synchronized (this) {
            String value = getValue("MinuteStartMessageBlock");
            parseInt = (value == null || value.length() <= 0) ? 0 : Integer.parseInt(value);
        }
        return parseInt;
    }

    public float getPalValue() {
        float parseFloat;
        synchronized (this) {
            String value = getValue("PAL");
            parseFloat = value != null ? Float.parseFloat(value) : 1.45f;
        }
        return parseFloat;
    }

    public String getParticipantId() {
        String value;
        synchronized (this) {
            value = getValue("ParticipantId");
            if (value == null) {
                value = XmlPullParser.NO_NAMESPACE;
            }
        }
        return value;
    }

    public Boolean getRunStandalone() {
        Boolean valueOf;
        synchronized (this) {
            String value = getValue("RunStandalone");
            valueOf = value != null ? Boolean.valueOf(Boolean.parseBoolean(value)) : Boolean.valueOf(false);
        }
        return valueOf;
    }

    public float getWeight() {
        float parseFloat;
        synchronized (this) {
            String value = getValue("Weight");
            parseFloat = value != null ? Float.parseFloat(value) : 0.0f;
        }
        return parseFloat;
    }

    public Unit getWeightUnit() {
        Unit valueOf;
        synchronized (this) {
            String value = getValue("WeightUnit");
            valueOf = value != null ? Unit.valueOf(value) : this.DEFAULT_WEIGHT_UNIT;
        }
        return valueOf;
    }

    public void setDateOfBirth(String str) {
        synchronized (this) {
            setValue("DateOfBirth", str);
        }
    }

    public void setFirstRun(Boolean bool) {
        synchronized (this) {
            setValue("IsFirstRun", bool.toString());
        }
    }

    public void setGender(String str) {
        synchronized (this) {
            setValue("Gender", str);
        }
    }

    public void setHeight(Float f) {
        synchronized (this) {
            if (getHeight() != f.floatValue()) {
                LogData.message(getDB(), "New height value: " + f.toString() + " cm");
            }
            setValue("Height", f.toString());
        }
    }

    public void setHeightUnit(Unit unit) {
        synchronized (this) {
            setValue("HeightUnit", unit.name());
        }
    }

    public void setHolidayMode(Boolean bool) {
        synchronized (this) {
            setValue("HolidayMode", bool.toString());
        }
    }

    public void setHourEndMessageBlock(int i) {
        synchronized (this) {
            setValue("HourEndMessageBlock", String.valueOf(i));
        }
    }

    public void setHourStartMessageBlock(int i) {
        synchronized (this) {
            setValue("HourStartMessageBlock", String.valueOf(i));
        }
    }

    public void setLastDailyCheck(Date date) {
        synchronized (this) {
            setValue("FeedbackDaily", String.valueOf(date.getTime()));
        }
    }

    public void setLastMonthlyCheck(Date date) {
        synchronized (this) {
            setValue("FeedbackMonthly", String.valueOf(date.getTime()));
        }
    }

    public void setLastUpload(Long l) {
        synchronized (this) {
            setValue("LastUpload", l.toString());
        }
    }

    public void setLastWeeklyCheck(Date date) {
        synchronized (this) {
            setValue("FeedbackWeekly", String.valueOf(date.getTime()));
        }
    }

    public void setMessageBlockAlways(boolean z) {
        synchronized (this) {
            setValue("MessageBlockAlways", String.valueOf(z));
        }
    }

    public void setMinuteEndMessageBlock(int i) {
        synchronized (this) {
            setValue("MinuteEndMessageBlock", String.valueOf(i));
        }
    }

    public void setMinuteStartMessageBlock(int i) {
        synchronized (this) {
            setValue("MinuteStartMessageBlock", String.valueOf(i));
        }
    }

    public void setPalValue(Float f) {
        synchronized (this) {
            setValue("PAL", f.toString());
        }
    }

    public void setParticipantId(String str) {
        synchronized (this) {
            setValue("ParticipantId", str);
        }
    }

    public void setRunStandalone(Boolean bool) {
        synchronized (this) {
            setValue("RunStandalone", bool.toString());
        }
    }

    public void setValue(String str, String str2) {
        SQLiteDatabase writableDatabase = _DB.getWritableDatabase();
        SQLiteStatement compileStatement = writableDatabase.compileStatement("SELECT Value FROM settings WHERE Name = ?");
        compileStatement.bindString(1, str);
        try {
            compileStatement.simpleQueryForString();
            compileStatement = writableDatabase.compileStatement("UPDATE settings SET Value = ? WHERE Name = ?");
            compileStatement.bindString(1, str2);
            compileStatement.bindString(2, str);
            compileStatement.execute();
        } catch (SQLiteDoneException e) {
            compileStatement = writableDatabase.compileStatement("INSERT INTO settings (Name, Value) values (?, ?)");
            compileStatement.bindString(1, str);
            compileStatement.bindString(2, str2);
            compileStatement.executeInsert();
        } finally {
            writableDatabase.close();
        }
    }

    public void setWeight(Float f) {
        synchronized (this) {
            if (getWeight() != f.floatValue()) {
                LogData.message(getDB(), "New weight value: " + f.toString() + " kg");
            }
            setValue("Weight", f.toString());
        }
    }

    public void setWeightUnit(Unit unit) {
        synchronized (this) {
            setValue("WeightUnit", unit.name());
        }
    }
}