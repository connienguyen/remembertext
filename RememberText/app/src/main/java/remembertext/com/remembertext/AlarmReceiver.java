package remembertext.com.remembertext;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service1 = new Intent(context, AlarmService.class);
        context.startService(service1);
        //Toast.makeText(context, "Alarm worked.", Toast.LENGTH_LONG).show();
        Log.d("OK", "AlarmReceiver.onReceive");
    }
}
