package remembertext.com.remembertext;

import android.os.Bundle;
import android.telephony.SmsMessage;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Log.d("test", "came here!");
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    int length = msgs.length;
                    for(int i=0; i<length;i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        Toast.makeText(context, msgBody, Toast.LENGTH_SHORT).show();

                        Intent chatHead = new Intent(context, MessageBubble.class);
                        chatHead.putExtra("Msg", msgBody);
                        chatHead.putExtra("From", msg_from);

                        context.startService(chatHead);
                        Log.d("msgBody : ", msgBody);
                    }
                }catch(Exception e){
                    Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }
    /*
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //---get the SMS message passed in---
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String messageReceived = "";
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++)

            {
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                messageReceived += msgs[i].getMessageBody().toString();
                messageReceived += "\n";
            }

            //---display the new SMS message---
            Toast.makeText(context, messageReceived, Toast.LENGTH_SHORT).show();

            // Get the Sender Phone Number
            String senderPhoneNumber=msgs[0].getOriginatingAddress ();
        }
    }
    */
}