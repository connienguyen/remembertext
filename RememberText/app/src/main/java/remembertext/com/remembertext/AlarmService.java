package remembertext.com.remembertext;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.net.Uri;
import android.view.View;
import android.content.Intent;
import android.os.IBinder;


public class AlarmService extends Service
{
    private NotificationManager mManager;
    private View chathead;

    @Override
    public IBinder onBind(Intent arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @SuppressWarnings("static-access")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);


        mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this.getApplicationContext(),MyActivity.class);

        Intent mIntent = new Intent(Intent.ACTION_SENDTO);
        mIntent.setData(Uri.parse("smsto:"+ "+16789838215"));
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mIntent);

        //PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
               // PendingIntent.FLAG_UPDATE_CURRENT);
        //PendingIntent mIntent = new Intent(Intent.ACTION_SENDTO);
        //mIntent.setData(Uri.parse("smsto:" + "+16789838215"));
        //mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(mIntent);

        Notification notification = new Notification.Builder(this.getApplicationContext())

                //.setContentTitle("Sender's name")
                //.setContentText("Incoming message preview") //.setContentIntent(pIntent)
                .setSmallIcon(R.drawable.chathead_demo)
                .setAutoCancel(true)
                .build();


        notification.defaults = Notification.DEFAULT_ALL;  //Makes Default Notification Noise/Vibrations
        //notification.flags |= Notification.FLAG_AUTO_CANCEL;
        //Notification notification = new Notification(R.drawable.ic_launcher,"This is a test message!", System.currentTimeMillis());
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;


        notification.setLatestEventInfo(this.getApplicationContext(), "AlarmManagerDemo", "This is a test message!", pendingNotificationIntent);

        mManager.notify(0, notification);
        return super.onStartCommand(intent, flags, startId);

        //Intent resultIntent = new Intent(this,MyActivity.class);
        //resultIntent.setAction(Intent.ACTION_MAIN);
        //resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);


    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}