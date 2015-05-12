package operator.com.operatorapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import operator.com.operatorapp.utils.DataController;
import operator.com.operatorapp.utils.SaveSharedPrefrances;


public class SplashActivity extends ActionBarActivity {


    private DataController dc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

      /*  final Intent intent = new Intent(SplashActivity.this ,LoginActivity.class );

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    startActivity(intent);
                    finish();
                }
            }, 2000);*/

        final Intent loginIntent = new Intent(SplashActivity.this ,LoginActivity.class );
        final Intent mainIntent = new Intent(SplashActivity.this ,MapsActivity.class );

        dc = DataController.getInstance(this);

        loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if(SaveSharedPrefrances.getPassword(SplashActivity.this).length() == 0 ) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    dc.enterByLogin = true;
                    dc.enterByMain = false;

                    startActivity(loginIntent);
                    finish();
                }
            }, 2000);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    dc.enterByMain = true;
                    dc.enterByLogin = false;

                    startActivity(mainIntent);
                    finish();
                }
            }, 1);
        }

    }

}
