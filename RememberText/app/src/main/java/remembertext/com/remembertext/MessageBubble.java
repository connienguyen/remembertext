package remembertext.com.remembertext;

import android.os.IBinder;
import android.app.Service;
import android.view.WindowManager;
import android.view.Gravity;
import android.widget.ImageView;
import android.content.Intent;
import android.graphics.PixelFormat;

public class MessageBubble extends Service {

    private WindowManager windowManager;
    private ImageView mBubble;

    @Override public IBinder onBind(Intent intent) {
        // Not used
        return null;
    }

    @Override public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        mBubble = new ImageView(this);
        mBubble.setImageResource(R.drawable.ic_launcher);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        windowManager.addView(mBubble, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBubble != null) windowManager.removeView(mBubble);
    }
}
