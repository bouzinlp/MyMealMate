package com.mymealmate;

import android.os.Handler;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WSHelper {
    private static final String TAG = "MyMealMate";
    private IWSCallEventListener mDataUploadedListener;
    private IWSCallEventListener mFinishedListener;
    private final Handler mHandler;
    WSMethod mMethod;
    private boolean mResult;
    private final Handler mUploadHandler;
    private boolean mUploadResult;

    public interface IWSCallEventListener {
        void callFinished(boolean z);
    }

    /* renamed from: com.mymealmate.WSHelper.2 */
    class AnonymousClass2 extends Thread {
        private final /* synthetic */ MyMealMateDatabase val$db;

        AnonymousClass2(MyMealMateDatabase myMealMateDatabase) {
            this.val$db = myMealMateDatabase;
        }

        public void run() {
            WSHelper.this.mUploadResult = false;
            try {
                WSHelper.this.mUploadResult = WSHelper.this.performUploadAll(this.val$db);
                if (WSHelper.this.mDataUploadedListener != null) {
                    WSHelper.this.mUploadHandler.post(new Runnable() {
                        public void run() {
                            WSHelper.this.mDataUploadedListener.callFinished(WSHelper.this.mUploadResult);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (WSHelper.this.mDataUploadedListener != null) {
                    WSHelper.this.mUploadHandler.post(new Runnable() {
                        public void run() {
                            WSHelper.this.mDataUploadedListener.callFinished(WSHelper.this.mUploadResult);
                        }
                    });
                }
            } catch (Throwable th) {
                if (WSHelper.this.mDataUploadedListener != null) {
                    WSHelper.this.mUploadHandler.post(new Runnable() {
                        public void run() {
                            WSHelper.this.mDataUploadedListener.callFinished(WSHelper.this.mUploadResult);
                        }
                    });
                }
            }
        }
    }

    WSHelper() {
        this.mResult = false;
        this.mUploadResult = false;
        this.mHandler = new Handler();
        this.mUploadHandler = new Handler();
        this.mFinishedListener = null;
        this.mDataUploadedListener = null;
    }

    WSHelper(WSMethod wSMethod) {
        this.mResult = false;
        this.mUploadResult = false;
        this.mHandler = new Handler();
        this.mUploadHandler = new Handler();
        this.mFinishedListener = null;
        this.mDataUploadedListener = null;
        this.mMethod = wSMethod;
    }

    private String LongToSoapDateString(long j) {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date(j));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean performUploadAll(com.mymealmate.MyMealMateDatabase r11) {
        /*
        r10 = this;
        r4 = 1;
        r1 = 0;
        monitor-enter(r10);
        r0 = "MyMealMate";
        r2 = "UploadFood()";
        android.util.Log.i(r0, r2);	 Catch:{ all -> 0x02f0 }
        r3 = com.mymealmate.FoodEntryData.getNeedUpload(r11);	 Catch:{ all -> 0x02f0 }
        r2 = r1;
    L_0x000f:
        r0 = r3.getCount();	 Catch:{ all -> 0x02f0 }
        if (r2 < r0) goto L_0x0087;
    L_0x0015:
        r2 = r4;
    L_0x0016:
        r3.close();	 Catch:{ all -> 0x02f0 }
        r0 = "MyMealMate";
        r3 = "UploadLog()";
        android.util.Log.i(r0, r3);	 Catch:{ all -> 0x02f0 }
        r3 = com.mymealmate.LogData.getNeedUpload(r11);	 Catch:{ all -> 0x02f0 }
        r0 = r1;
    L_0x0025:
        r5 = r3.getCount();	 Catch:{ all -> 0x02f0 }
        if (r0 < r5) goto L_0x012a;
    L_0x002b:
        r3.close();	 Catch:{ all -> 0x02f0 }
        r0 = "MyMealMate";
        r3 = "UploadWeight()";
        android.util.Log.i(r0, r3);	 Catch:{ all -> 0x02f0 }
        r3 = com.mymealmate.WeightData.getNeedUpload(r11);	 Catch:{ all -> 0x02f0 }
        r0 = r1;
    L_0x003a:
        r5 = r3.getCount();	 Catch:{ all -> 0x02f0 }
        if (r0 < r5) goto L_0x0166;
    L_0x0040:
        r3.close();	 Catch:{ all -> 0x02f0 }
        r0 = "MyMealMate";
        r3 = "UploadExercise()";
        android.util.Log.i(r0, r3);	 Catch:{ all -> 0x02f0 }
        r5 = com.mymealmate.ExerciseEntryData.getNeedUpload(r11);	 Catch:{ all -> 0x02f0 }
        r3 = r1;
    L_0x004f:
        r0 = r5.getCount();	 Catch:{ all -> 0x02f0 }
        if (r3 < r0) goto L_0x01a6;
    L_0x0055:
        r0 = r2;
    L_0x0056:
        r5.close();	 Catch:{ all -> 0x02f0 }
        r2 = "MyMealMate";
        r3 = "UploadWeightLossTarget()";
        android.util.Log.i(r2, r3);	 Catch:{ all -> 0x02f0 }
        r3 = com.mymealmate.TargetSettingsData.getNeedUpload(r11);	 Catch:{ all -> 0x02f0 }
        r2 = r1;
    L_0x0065:
        r4 = r3.getCount();	 Catch:{ all -> 0x02f0 }
        if (r2 < r4) goto L_0x0231;
    L_0x006b:
        r2 = r0;
    L_0x006c:
        r3.close();	 Catch:{ all -> 0x02f0 }
        r0 = "MyMealMate";
        r3 = "UploadDateFlags()";
        android.util.Log.i(r0, r3);	 Catch:{ all -> 0x02f0 }
        r4 = com.mymealmate.DateFlagsData.getNeedUpload(r11);	 Catch:{ all -> 0x02f0 }
        r3 = r1;
    L_0x007b:
        r0 = r4.getCount();	 Catch:{ all -> 0x02f0 }
        if (r3 < r0) goto L_0x028d;
    L_0x0081:
        r0 = r2;
    L_0x0082:
        r4.close();	 Catch:{ all -> 0x02f0 }
        monitor-exit(r10);
        return r0;
    L_0x0087:
        r3.moveToPosition(r2);	 Catch:{ all -> 0x02f0 }
        r0 = new com.mymealmate.WSClient$UploadFood;	 Catch:{ all -> 0x02f0 }
        r0.<init>();	 Catch:{ all -> 0x02f0 }
        r5 = "Usernumber";
        r6 = com.mymealmate.WSClient.getParticipantId();	 Catch:{ all -> 0x02f0 }
        r0 = r0.addProperty(r5, r6);	 Catch:{ all -> 0x02f0 }
        r5 = "Date";
        r6 = r3.getDate();	 Catch:{ all -> 0x02f0 }
        r6 = r10.LongToSoapDateString(r6);	 Catch:{ all -> 0x02f0 }
        r0 = r0.addProperty(r5, r6);	 Catch:{ all -> 0x02f0 }
        r5 = "MealslotId";
        r6 = r3.getMealSlot();	 Catch:{ all -> 0x02f0 }
        r6 = java.lang.Integer.valueOf(r6);	 Catch:{ all -> 0x02f0 }
        r0 = r0.addProperty(r5, r6);	 Catch:{ all -> 0x02f0 }
        r5 = "FoodId";
        r6 = r3.getFoodItem();	 Catch:{ all -> 0x02f0 }
        r6 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x02f0 }
        r0 = r0.addProperty(r5, r6);	 Catch:{ all -> 0x02f0 }
        r5 = "Amount";
        r6 = r3.getAmount();	 Catch:{ all -> 0x02f0 }
        r6 = r6.toString();	 Catch:{ all -> 0x02f0 }
        r0 = r0.addProperty(r5, r6);	 Catch:{ all -> 0x02f0 }
        r5 = "EntryId";
        r6 = r3.getEntryId();	 Catch:{ all -> 0x02f0 }
        r6 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x02f0 }
        r0 = r0.addProperty(r5, r6);	 Catch:{ all -> 0x02f0 }
        r5 = "Cals";
        r6 = r3.getCals();	 Catch:{ all -> 0x02f0 }
        r6 = r6.toString();	 Catch:{ all -> 0x02f0 }
        r0 = r0.addProperty(r5, r6);	 Catch:{ all -> 0x02f0 }
        r5 = "Photo";
        r6 = r3.getPhoto();	 Catch:{ all -> 0x02f0 }
        r6 = r6.toString();	 Catch:{ all -> 0x02f0 }
        r0 = r0.addProperty(r5, r6);	 Catch:{ all -> 0x02f0 }
        r5 = "CreatedOn";
        r6 = r3.getCreatedOn();	 Catch:{ all -> 0x02f0 }
        r6 = r10.LongToSoapDateString(r6);	 Catch:{ all -> 0x02f0 }
        r5 = r0.addProperty(r5, r6);	 Catch:{ all -> 0x02f0 }
        r0 = r3.getDeleted();	 Catch:{ all -> 0x02f0 }
        if (r0 == 0) goto L_0x02f3;
    L_0x010f:
        r0 = "true";
    L_0x0111:
        r6 = "Deleted";
        r0 = r5.addProperty(r6, r0);	 Catch:{ all -> 0x02f0 }
        r0 = r0.call();	 Catch:{ all -> 0x02f0 }
        if (r0 == 0) goto L_0x02f7;
    L_0x011d:
        r6 = r3.getEntryId();	 Catch:{ all -> 0x02f0 }
        r0 = 0;
        com.mymealmate.FoodEntryData.setNeedUpload(r11, r6, r0);	 Catch:{ all -> 0x02f0 }
        r0 = r2 + 1;
        r2 = r0;
        goto L_0x000f;
    L_0x012a:
        r3.moveToPosition(r0);	 Catch:{ all -> 0x02f0 }
        r5 = new com.mymealmate.WSClient$UploadLog;	 Catch:{ all -> 0x02f0 }
        r5.<init>();	 Catch:{ all -> 0x02f0 }
        r6 = "Usernumber";
        r7 = com.mymealmate.WSClient.getParticipantId();	 Catch:{ all -> 0x02f0 }
        r5 = r5.addProperty(r6, r7);	 Catch:{ all -> 0x02f0 }
        r6 = "Desc";
        r7 = r3.getDescription();	 Catch:{ all -> 0x02f0 }
        r5 = r5.addProperty(r6, r7);	 Catch:{ all -> 0x02f0 }
        r6 = "Date";
        r8 = r3.getDate();	 Catch:{ all -> 0x02f0 }
        r7 = r10.LongToSoapDateString(r8);	 Catch:{ all -> 0x02f0 }
        r5 = r5.addProperty(r6, r7);	 Catch:{ all -> 0x02f0 }
        r5 = r5.call();	 Catch:{ all -> 0x02f0 }
        if (r5 == 0) goto L_0x02fa;
    L_0x015a:
        r6 = r3.getEntryId();	 Catch:{ all -> 0x02f0 }
        r5 = 0;
        com.mymealmate.LogData.setNeedUpload(r11, r6, r5);	 Catch:{ all -> 0x02f0 }
        r0 = r0 + 1;
        goto L_0x0025;
    L_0x0166:
        r3.moveToPosition(r0);	 Catch:{ all -> 0x02f0 }
        r5 = new com.mymealmate.WSClient$UploadWeight;	 Catch:{ all -> 0x02f0 }
        r5.<init>();	 Catch:{ all -> 0x02f0 }
        r6 = "Usernumber";
        r7 = com.mymealmate.WSClient.getParticipantId();	 Catch:{ all -> 0x02f0 }
        r5 = r5.addProperty(r6, r7);	 Catch:{ all -> 0x02f0 }
        r6 = "Weight";
        r7 = r3.getWeight();	 Catch:{ all -> 0x02f0 }
        r7 = r7.toString();	 Catch:{ all -> 0x02f0 }
        r5 = r5.addProperty(r6, r7);	 Catch:{ all -> 0x02f0 }
        r6 = "Date";
        r8 = r3.getDate();	 Catch:{ all -> 0x02f0 }
        r7 = r10.LongToSoapDateString(r8);	 Catch:{ all -> 0x02f0 }
        r5 = r5.addProperty(r6, r7);	 Catch:{ all -> 0x02f0 }
        r5 = r5.call();	 Catch:{ all -> 0x02f0 }
        if (r5 == 0) goto L_0x02fd;
    L_0x019a:
        r6 = r3.getEntryId();	 Catch:{ all -> 0x02f0 }
        r5 = 0;
        com.mymealmate.WeightData.setNeedUpload(r11, r6, r5);	 Catch:{ all -> 0x02f0 }
        r0 = r0 + 1;
        goto L_0x003a;
    L_0x01a6:
        r5.moveToPosition(r3);	 Catch:{ all -> 0x02f0 }
        r0 = new com.mymealmate.WSClient$UploadExercise;	 Catch:{ all -> 0x02f0 }
        r0.<init>();	 Catch:{ all -> 0x02f0 }
        r6 = "Usernumber";
        r7 = com.mymealmate.WSClient.getParticipantId();	 Catch:{ all -> 0x02f0 }
        r0 = r0.addProperty(r6, r7);	 Catch:{ all -> 0x02f0 }
        r6 = "Date";
        r8 = r5.getDate();	 Catch:{ all -> 0x02f0 }
        r7 = r10.LongToSoapDateString(r8);	 Catch:{ all -> 0x02f0 }
        r0 = r0.addProperty(r6, r7);	 Catch:{ all -> 0x02f0 }
        r6 = "EntryId";
        r8 = r5.getEntryId();	 Catch:{ all -> 0x02f0 }
        r7 = java.lang.Long.valueOf(r8);	 Catch:{ all -> 0x02f0 }
        r0 = r0.addProperty(r6, r7);	 Catch:{ all -> 0x02f0 }
        r6 = "ExerciseDataId";
        r8 = r5.getCode();	 Catch:{ all -> 0x02f0 }
        r7 = java.lang.Long.valueOf(r8);	 Catch:{ all -> 0x02f0 }
        r0 = r0.addProperty(r6, r7);	 Catch:{ all -> 0x02f0 }
        r6 = "Time";
        r7 = r5.getTime();	 Catch:{ all -> 0x02f0 }
        r7 = java.lang.Integer.valueOf(r7);	 Catch:{ all -> 0x02f0 }
        r0 = r0.addProperty(r6, r7);	 Catch:{ all -> 0x02f0 }
        r6 = "Cals";
        r7 = r5.getCals();	 Catch:{ all -> 0x02f0 }
        r7 = r7.toString();	 Catch:{ all -> 0x02f0 }
        r0 = r0.addProperty(r6, r7);	 Catch:{ all -> 0x02f0 }
        r6 = "CreatedOn";
        r8 = r5.getCreatedOn();	 Catch:{ all -> 0x02f0 }
        r7 = r10.LongToSoapDateString(r8);	 Catch:{ all -> 0x02f0 }
        r6 = r0.addProperty(r6, r7);	 Catch:{ all -> 0x02f0 }
        r0 = r5.getDeleted();	 Catch:{ all -> 0x02f0 }
        r0 = r0.intValue();	 Catch:{ all -> 0x02f0 }
        if (r0 != r4) goto L_0x0300;
    L_0x0216:
        r0 = "true";
    L_0x0218:
        r7 = "Deleted";
        r0 = r6.addProperty(r7, r0);	 Catch:{ all -> 0x02f0 }
        r0 = r0.call();	 Catch:{ all -> 0x02f0 }
        if (r0 == 0) goto L_0x0304;
    L_0x0224:
        r6 = r5.getEntryId();	 Catch:{ all -> 0x02f0 }
        r0 = 0;
        com.mymealmate.ExerciseEntryData.setNeedUpload(r11, r6, r0);	 Catch:{ all -> 0x02f0 }
        r0 = r3 + 1;
        r3 = r0;
        goto L_0x004f;
    L_0x0231:
        r3.moveToPosition(r2);	 Catch:{ all -> 0x02f0 }
        r4 = new com.mymealmate.WSClient$UploadWeightLossTarget;	 Catch:{ all -> 0x02f0 }
        r4.<init>();	 Catch:{ all -> 0x02f0 }
        r5 = "Usernumber";
        r6 = com.mymealmate.WSClient.getParticipantId();	 Catch:{ all -> 0x02f0 }
        r4 = r4.addProperty(r5, r6);	 Catch:{ all -> 0x02f0 }
        r5 = "EntryId";
        r6 = r3.getEntryId();	 Catch:{ all -> 0x02f0 }
        r6 = java.lang.Long.valueOf(r6);	 Catch:{ all -> 0x02f0 }
        r4 = r4.addProperty(r5, r6);	 Catch:{ all -> 0x02f0 }
        r5 = "Date";
        r6 = r3.getDate();	 Catch:{ all -> 0x02f0 }
        r6 = r10.LongToSoapDateString(r6);	 Catch:{ all -> 0x02f0 }
        r4 = r4.addProperty(r5, r6);	 Catch:{ all -> 0x02f0 }
        r5 = "WeightLoss";
        r6 = r3.getWeightloss();	 Catch:{ all -> 0x02f0 }
        r6 = r6.toString();	 Catch:{ all -> 0x02f0 }
        r4 = r4.addProperty(r5, r6);	 Catch:{ all -> 0x02f0 }
        r5 = "Time";
        r6 = r3.getTime();	 Catch:{ all -> 0x02f0 }
        r6 = r6.toString();	 Catch:{ all -> 0x02f0 }
        r4 = r4.addProperty(r5, r6);	 Catch:{ all -> 0x02f0 }
        r4 = r4.call();	 Catch:{ all -> 0x02f0 }
        if (r4 == 0) goto L_0x0307;
    L_0x0281:
        r4 = r3.getEntryId();	 Catch:{ all -> 0x02f0 }
        r6 = 0;
        com.mymealmate.TargetSettingsData.setNeedUpload(r11, r4, r6);	 Catch:{ all -> 0x02f0 }
        r2 = r2 + 1;
        goto L_0x0065;
    L_0x028d:
        r4.moveToPosition(r3);	 Catch:{ all -> 0x02f0 }
        r0 = new com.mymealmate.WSClient$UploadDateFlags;	 Catch:{ all -> 0x02f0 }
        r0.<init>();	 Catch:{ all -> 0x02f0 }
        r5 = "Usernumber";
        r6 = com.mymealmate.WSClient.getParticipantId();	 Catch:{ all -> 0x02f0 }
        r0 = r0.addProperty(r5, r6);	 Catch:{ all -> 0x02f0 }
        r5 = "Date";
        r6 = r4.getDate();	 Catch:{ all -> 0x02f0 }
        r6 = r10.LongToSoapDateString(r6);	 Catch:{ all -> 0x02f0 }
        r5 = r0.addProperty(r5, r6);	 Catch:{ all -> 0x02f0 }
        r0 = r4.getComplete();	 Catch:{ all -> 0x02f0 }
        if (r0 == 0) goto L_0x030a;
    L_0x02b3:
        r0 = "true";
    L_0x02b5:
        r6 = "Complete";
        r5 = r5.addProperty(r6, r0);	 Catch:{ all -> 0x02f0 }
        r0 = r4.getDeleted();	 Catch:{ all -> 0x02f0 }
        if (r0 == 0) goto L_0x02ea;
    L_0x02c1:
        r0 = "true";
    L_0x02c3:
        r6 = "Deleted";
        r0 = r5.addProperty(r6, r0);	 Catch:{ all -> 0x02f0 }
        r5 = "Timestamp";
        r6 = r4.getTimestamp();	 Catch:{ all -> 0x02f0 }
        r6 = r10.LongToSoapDateString(r6);	 Catch:{ all -> 0x02f0 }
        r0 = r0.addProperty(r5, r6);	 Catch:{ all -> 0x02f0 }
        r0 = r0.call();	 Catch:{ all -> 0x02f0 }
        if (r0 == 0) goto L_0x02ed;
    L_0x02dd:
        r6 = r4.getDate();	 Catch:{ all -> 0x02f0 }
        r0 = 0;
        com.mymealmate.DateFlagsData.setNeedUpload(r11, r6, r0);	 Catch:{ all -> 0x02f0 }
        r0 = r3 + 1;
        r3 = r0;
        goto L_0x007b;
    L_0x02ea:
        r0 = "false";
        goto L_0x02c3;
    L_0x02ed:
        r0 = r1;
        goto L_0x0082;
    L_0x02f0:
        r0 = move-exception;
        monitor-exit(r10);
        throw r0;
    L_0x02f3:
        r0 = "false";
        goto L_0x0111;
    L_0x02f7:
        r2 = r1;
        goto L_0x0016;
    L_0x02fa:
        r2 = r1;
        goto L_0x002b;
    L_0x02fd:
        r2 = r1;
        goto L_0x0040;
    L_0x0300:
        r0 = "false";
        goto L_0x0218;
    L_0x0304:
        r0 = r1;
        goto L_0x0056;
    L_0x0307:
        r2 = r1;
        goto L_0x006c;
    L_0x030a:
        r0 = "false";
        goto L_0x02b5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mymealmate.WSHelper.performUploadAll(com.mymealmate.MyMealMateDatabase):boolean");
    }

    public void setCallFinishedListener(IWSCallEventListener iWSCallEventListener) {
        this.mFinishedListener = iWSCallEventListener;
    }

    public void setDataUploadedListener(IWSCallEventListener iWSCallEventListener) {
        this.mDataUploadedListener = iWSCallEventListener;
    }

    public void start() {
        new Thread() {
            public void run() {
                WSHelper.this.mResult = WSHelper.this.mMethod.call();
                WSHelper.this.mHandler.post(new Runnable() {
                    public void run() {
                        WSHelper.this.mFinishedListener.callFinished(WSHelper.this.mResult);
                    }
                });
            }
        }.start();
    }

    public boolean uploadAll(MyMealMateDatabase myMealMateDatabase) {
        Log.i(TAG, "Start uploading data");
        boolean z = false;
        try {
            z = performUploadAll(myMealMateDatabase);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (z) {
            Log.i(TAG, "Uploading: SUCCEED");
        } else {
            Log.i(TAG, "Uploading: FAIL");
        }
        return z;
    }

    public void uploadAllAsync(MyMealMateDatabase myMealMateDatabase) {
        Log.i(TAG, "Start data uploading thread");
        new AnonymousClass2(myMealMateDatabase).start();
    }
}