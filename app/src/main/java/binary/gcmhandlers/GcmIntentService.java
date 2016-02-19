package binary.gcmhandlers;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import binary.datahandlers.DataProvider;
import webradic.binarymessenger.MainActivity;
import webradic.binarymessenger.R;


public class GcmIntentService extends IntentService {
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    //private Context context;
    private ContentResolver cr;

    public static final int NOTIFICATION_ID = 1;

    public GcmIntentService() {
        super("GcmIntentService");
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        cr = getApplicationContext().getContentResolver();
        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /**
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                String msg = intent.getStringExtra("message");
                String frm = intent.getStringExtra("former");

                String contactname = null;
                Cursor c = getApplicationContext().getContentResolver().query(
                        DataProvider.CONTENT_URI_PROFILE,
                        new String[]{DataProvider.COL_NAME},
                        DataProvider.COL_EMAIL + " = ?",
                        new String[]{frm},
                        null);

                if (c != null) {
                    if (c.moveToFirst()) {
                        contactname = c.getString(0);
                    }
                }
                c.close();


                ContentValues values = new ContentValues(1);
                values.put(DataProvider.COL_MSG, msg);
                values.put(DataProvider.COL_FROM, frm);
                //values.put(DataProvider.COL_TO, null);
                cr.insert(DataProvider.CONTENT_URI_MESSAGES, values);
                Cursor c1 = getApplicationContext().getContentResolver().query(
                        DataProvider.CONTENT_URI_BANNER,
                        new String[]{DataProvider.COL_COUNT},
                        DataProvider.COL_WITH + " = ?",
                        new String[]{frm},
                        null);
                int count = 0;
                if (c1 != null) {
                    if (c1.moveToFirst()) {
                        count = c1.getInt(0);
                    }
                }
                if (c1 != null && count != 0) {
                    c1.close();
                    ContentValues cv1 = new ContentValues(0);
                    cv1.put(DataProvider.COL_COUNT, ++count);
                    cr.update(DataProvider.CONTENT_URI_BANNER, cv1, DataProvider.COL_WITH + " = ?", new String[]{frm});
                } else {
                    c1.close();
                    Cursor c2 = getApplicationContext().getContentResolver().query(
                            DataProvider.CONTENT_URI_PROFILE,
                            new String[]{DataProvider.COL_NAME},
                            DataProvider.COL_EMAIL+" =?",
                            new String[]{frm},
                            null
                    );
                    String name="";
                    if(c2!=null){
                        if(c2.moveToFirst()){
                             name = c2.getString(0);
                            ContentValues v2 = new ContentValues(2);
                            v2.put(DataProvider.COL_WITH,frm);
                            v2.put(DataProvider.COL_COUNT,1);
                            v2.put(DataProvider.COL_NAME,name);
                            cr.insert(DataProvider.CONTENT_URI_BANNER,v2);
                        }
                    }
                    c2.close();
                }
                    cr.notifyChange(DataProvider.CONTENT_URI_BANNER,null);
                    cr.notifyChange(DataProvider.CONTENT_URI_MESSAGES,null);
                sendNotification("New message : " + msg);
                Log.e("GcmIntentService", "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {

        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)

                        .setContentTitle("Binary")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());


    }

}
