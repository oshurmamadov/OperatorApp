package operator.com.operatorapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class SplashActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Intent intent = new Intent(SplashActivity.this ,MainActivity.class );

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    startActivity(intent);
                    finish();
                }
            }, 2000);
    }

}
