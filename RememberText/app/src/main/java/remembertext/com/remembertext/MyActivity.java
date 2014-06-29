package remembertext.com.remembertext;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import android.view.View;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import android.util.Log;


public class MyActivity extends SherlockFragmentActivity {
    private ViewPager mViewPager;
    private TabsAdapter mTabsAdapter;
    String from,msg;
    MessageBubble mb = new MessageBubble();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.pager);
        setContentView(mViewPager);

        final ActionBar bar = getSupportActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        mTabsAdapter = new TabsAdapter(this, mViewPager);
        mTabsAdapter.addTab(bar.newTab().setText("Settings"), Fragment_Settings.class, null);
        mTabsAdapter.addTab(bar.newTab().setText("Leaderboard"), Fragment_Leaderboard.class, null);

        Intent incomingIntent = getIntent();
        from = incomingIntent.getStringExtra("From");
        msg = incomingIntent.getStringExtra("Msg");
        //Log.d("Activity", "Came in activity");
    }

    public void saveTimePrefs(View v) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        EditText et = (EditText) findViewById(R.id.hours);
        EditText et_m = (EditText) findViewById(R.id.minutes);
        Integer et_hours;
        Integer et_minutes;
        try {
            et_hours = Integer.parseInt(et.getText().toString());
            et_minutes = Integer.parseInt(et_m.getText().toString());
            if(et_minutes >= 59) throw new NumberFormatException();
        }
        catch(IllegalStateException ise) {
            et_hours = 0;
            et_minutes = 15;
            Toast.makeText(getBaseContext(), "Invalid input. Snooze time reset.", Toast.LENGTH_SHORT).show();
            et.setText("0");
            et_m.setText("15");
        }
        catch(NumberFormatException nfe) {
            et_hours = 0;
            et_minutes = 15;
            Toast.makeText(getBaseContext(), "Invalid input. Snooze time reset.", Toast.LENGTH_SHORT).show();
            et.setText("0");
            et_m.setText("15");
        }

        editor.putInt(getString(R.string.hours_set), et_hours);
        editor.putInt(getString(R.string.minutes_set), et_minutes);
        editor.commit();

        Toast.makeText(getBaseContext(), "Snooze time saved", Toast.LENGTH_SHORT).show();

        /* Message Bubble testing */
        //mbCheck();
    }


/*
    NotificationManager NM = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

    @SuppressWarnings("deprecation")
    public void notify(Context context) {
        String Hours = ((EditText) findViewById(R.id.hours)).getText().toString();
        String Mins = ((EditText) findViewById(R.id.hours)).getText().toString();
        Log.d("HOURS: ", Hours);
        Log.d("Minutes: ", Mins);
        NM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification(android.R.drawable.stat_notify_more, "TEST", System.currentTimeMillis());
        PendingIntent pending = PendingIntent.getActivity(context.getApplicationContext(), 0, new Intent(), 0);
        notify.setLatestEventInfo(context.getApplicationContext(), Hours, Mins, pending);
        NM.notify(0, notify);
    }
*/
    /*
    private void mbCheck() {
        startService(new Intent(MyActivity.this, MessageBubble.class));
        Toast.makeText(getBaseContext(), "Start Service", Toast.LENGTH_SHORT).show();
    }
    */
    /* Debug function
    private void check() {
        //SharedPreferences sharedPref = getSharedPreferences(Context.MODE_PRIVATE);
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int defaultValue = 15;
        Integer highScore = sharedPref.getInt(getString(R.string.hours_set), defaultValue);
        Log.v("\n\nHours saved", Integer.toString(highScore));

        Toast.makeText(getBaseContext(), Integer.toString(highScore), Toast.LENGTH_SHORT).show();
    }
    */
}
