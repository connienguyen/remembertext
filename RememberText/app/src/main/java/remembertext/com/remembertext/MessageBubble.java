package remembertext.com.remembertext;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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

        final View chatHead = inflater.inflate(R.layout.message_bubble, null);

        final TextView txt_title = (TextView) chatHead.findViewById(R.id.txt_title);
        final TextView txt_text = (TextView) chatHead.findViewById(R.id.txt_text);
        final Button btn_dismiss = (Button) chatHead.findViewById(R.id.btn_dismiss);

        if(intent.getStringExtra("Name").equals("nameNotFound")) {
            txt_title.setText(intent.getStringExtra("From"));
        }
        else {
            txt_title.setText(intent.getStringExtra("Name"));
        }
        txt_text.setText(intent.getStringExtra("Msg"));
        txt_title.setVisibility(View.GONE);
        txt_text.setVisibility(View.GONE);
        btn_dismiss.setVisibility(View.GONE);

        // Listener for Dismiss button
        chatHead.findViewById(R.id.btn_dismiss).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //windowManager.removeView(chatHead);
            String packageName = "com.android.mms";
            Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
                if (mIntent !=null){
                  startActivity(mIntent);
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
                        if(txt_text.getVisibility() == View.INVISIBLE) {
                            //TODO set timer here
                        }
                        else { //Else is not visible, set visible
                            txt_title.setVisibility(View.VISIBLE);
                            txt_text.setVisibility(View.VISIBLE);
                            btn_dismiss.setVisibility(View.VISIBLE);
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
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
}