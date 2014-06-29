package remembertext.com.remembertext;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by Donnya on 6/28/2014.
 */
public class incomingMessage extends BroadcastReceiver{
    public void onReceive(Context paramContext, Intent paramIntent){
       Toast.makeText(paramContext, "SMS diterima", Toast.LENGTH_LONG).show();
       Bundle localBundle = paramIntent.getExtras();
        if(localBundle != null) {

            Object[] arrayOfObject = (Object[])localBundle.get("pdus");
            SmsMessage[] arrayofSmsMessage = new SmsMessage[arrayOfObject.length];

            int i = 0;

            while(i < arrayofSmsMessage.length){
                arrayofSmsMessage[i] = SmsMessage.createFromPdu((byte[])(byte[])arrayOfObject[i]);
                String str1 = arrayofSmsMessage[i].getOriginatingAddress();
                String str2 = arrayofSmsMessage[i].getMessageBody();
                Long l =  System.currentTimeMillis();
                System.out.println(str1 + '\n' + str2);
            }
        }
    }

        }
