package remembertext.com.remembertext;

import android.os.IBinder;
import android.app.Service;
import android.view.WindowManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.view.MotionEvent;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.Log;

public class MessageBubble extends Service {
    String from, msg;
    private WindowManager windowManager;
    private ImageView mBubble;
    WindowManager.LayoutParams params;

    @Override
    public IBinder onBind(Intent intent) {
        // Not used
        from = intent.getStringExtra("From");
        msg = intent.getStringExtra("Msg");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        mBubble = new ImageView(this);
        mBubble.setImageResource(R.drawable.fluffy);
        Log.d("resID", Integer.toString(R.drawable.fluffy));

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        windowManager.addView(mBubble, params);

        mBubble.setOnTouchListener(new View.OnTouchListener() {
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
                        Intent i = new Intent(getBaseContext(), MyActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        i.setClass(getBaseContext(), MyActivity.class);
                        i.putExtra("Msg", msg);
                        i.putExtra("From", from);
                        startActivity(i);
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                    case MotionEvent.ACTION_MOVE:


                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(mBubble, params);
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        from = intent.getStringExtra("From");
        msg = intent.getStringExtra("Msg");
        Log.d("In Start", "In start");
        return startId;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBubble != null) windowManager.removeView(mBubble);

    }
}