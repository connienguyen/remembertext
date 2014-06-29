package remembertext.com.remembertext;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.util.Log;
import android.provider.ContactsContract.PhoneLookup;

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
                        long l = msgs[i].getTimestampMillis();

                        String time = String.valueOf(l);
                        //Toast.makeText(context, msgBody, Toast.LENGTH_SHORT).show();

                        Intent chatHead = new Intent(context, MessageBubble.class);
                        chatHead.putExtra("Msg", msgBody);
                        chatHead.putExtra("From", msg_from);
                        chatHead.putExtra("Time",time);
                        Log.d("What's the time? ",time);
                        Log.d("Originating Address: ", msg_from);
                        Log.d("Message Body: ", msgBody);
                        String name = "?";

                        if (msgBody != null) {
                            String number = PhoneNumberUtils.formatNumber(msg_from);
                            Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

                            ContentResolver contentResolver = context.getContentResolver();

                            Cursor contactLookup = contentResolver.query(uri, new String[]{BaseColumns._ID,
                                    ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

                            try {
                                if (contactLookup != null && contactLookup.getCount() > 0) {
                                    contactLookup.moveToNext();
                                    name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                                    //String contactId = contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
                                }
                                else {
                                    name = "nameNotFound";
                                }
                            } finally {
                                if (contactLookup != null) {
                                    contactLookup.close();
                                }
                            }

                            Log.d("name: ", name);
                        }

                        chatHead.putExtra("Name",name);

                        context.startService(chatHead);
                        Log.d("msgBody : ", msgBody);
                    }
                }catch(Exception e){
                    Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }
}