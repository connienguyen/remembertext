package remembertext.com.remembertext;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.content.SharedPreferences;
import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.util.Log;

public class MyActivity extends SherlockFragmentActivity {
    private ViewPager mViewPager;
    private TabsAdapter mTabsAdapter;

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
    }

    public void saveTimePrefs(View v) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        EditText et = (EditText) findViewById(R.id.hours);
        Integer et_hours = Integer.parseInt(et.getText().toString());
        EditText et_m = (EditText) findViewById(R.id.minutes);
        Integer et_minutes = Integer.parseInt(et_m.getText().toString());
        /* Check so that minutes is not 60 or over */
        if(et_minutes >= 59) {
            et_minutes = 59;
        }
        editor.putInt(getString(R.string.hours_set), et_hours);
        editor.putInt(getString(R.string.minutes_set), et_minutes);
        editor.commit();

        Toast.makeText(getBaseContext(), "Snooze time saved", Toast.LENGTH_SHORT).show();
    }

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
