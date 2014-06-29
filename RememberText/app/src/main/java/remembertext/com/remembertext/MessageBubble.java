package remembertext.com.remembertext;

import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.app.Service;
import android.app.AlarmManager;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.util.DisplayMetrics;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Display;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MessageBubble extends Service {
    private WindowManager windowManager;
    private List<View> chatHeads;
    private LayoutInflater inflater;
    String from, msg;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        inflater = LayoutInflater.from(this);
        chatHeads = new ArrayList<View>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final WindowManager w = (WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE);
        final Display d = w.getDefaultDisplay();
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);
        final View chatHead = inflater.inflate(R.layout.message_bubble, null);
        final TextView txt_title = (TextView) chatHead.findViewById(R.id.txt_title);
        final TextView txt_text = (TextView) chatHead.findViewById(R.id.txt_text);
        final Button btn_dismiss = (Button) chatHead.findViewById(R.id.btn_dismiss);
        final String phoneNumber = intent.getStringExtra("From");

        if(intent.getStringExtra("Name").equals("nameNotFound")) {
            txt_title.setText(intent.getStringExtra("From"));
        }
        else {
            txt_title.setText(intent.getStringExtra("Name"));
        }
        txt_text.setText(intent.getStringExtra("Msg"));
        final View message_box = (View) chatHead.findViewById(R.id.message_box);
        message_box.setVisibility(View.GONE);

        // Listener for dismiss button
        //TODO rename btn_dismiss to btn_snooze for cleaner code
        chatHead.findViewById(R.id.btn_dismiss).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeView(chatHead);
                snoozeMessage(txt_title.getText().toString(), txt_text.getText().toString());
            }

        });

         //Listener for text message (txt_text)
        chatHead.findViewById(R.id.message_box).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent mIntent = new Intent(Intent.ACTION_SENDTO);
                    mIntent.setData(Uri.parse("smsto:"+phoneNumber));
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(mIntent);
                }
                catch (Exception e) {
                    Log.v("Exception thrown ", e.getMessage());
                    Toast.makeText(getBaseContext(), "Messaged failed. Try again later.", Toast.LENGTH_LONG).show();
                }
                finally {
                    windowManager.removeView(chatHead);
                }
            }
        });

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE, 0, PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;

        //Listener for the icon
        chatHead.findViewById(R.id.sender_icon).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private float percentScreen;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        if(message_box.getVisibility() == View.VISIBLE) {
                            //TODO set timer here
                        }
                        else { //Else is not visible, set visible
                            message_box.setVisibility(View.VISIBLE);
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //message_box.setVisibility(View.INVISIBLE);
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        percentScreen = event.getRawY() / displayMetrics.heightPixels;
                        //Log.d("Raw Y", Integer.toString(displayMetrics.heightPixels));
                        //Log.d("Raw Y", Float.toString(percentScreen));
                        if(percentScreen >= 0.7) { // Dismiss chatheads beyond this Y
                            windowManager.removeView(chatHead);
                            return false;
                        }
                        windowManager.updateViewLayout(chatHead, params);
                        return true;
                }
                return false;
            }
        });

        addChatHead(chatHead, params);

        return super.onStartCommand(intent, flags, startId);
    }

    public void addChatHead(View chatHead, LayoutParams params) {
        chatHeads.add(chatHead);
        windowManager.addView(chatHead, params);
    }

    public void removeChatHead(View chatHead) {
        chatHeads.remove(chatHead);
        windowManager.removeView(chatHead);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (View chatHead : chatHeads) {
            removeChatHead(chatHead);
        }
    }

    //Open chathead again
    public void openMessage(View v) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.TYPE_PHONE, 0, PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        windowManager.addView(v, params);
    }

    //Set snooze
    public void snoozeMessage(String sender, String msg) {
        Intent myIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        int previewMax = 33;
        String preview;
        if(msg.length() < previewMax) {
            preview = msg;
        }
        else {
            preview = msg.substring(0, 33);
            preview = preview + "...";
        }
        myIntent.putExtra("Sender", sender);
        myIntent.putExtra("Msg", preview);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent,0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 10000, pendingIntent);
    }
}