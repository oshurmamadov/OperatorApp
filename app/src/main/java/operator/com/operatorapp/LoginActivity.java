package operator.com.operatorapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import operator.com.operatorapp.utils.CallBack;
import operator.com.operatorapp.utils.DataController;
import operator.com.operatorapp.utils.SaveSharedPrefrances;


public class LoginActivity extends ActionBarActivity {


    DataController dc;

    Button enter;
    EditText loginField;

    private ImageView backButton;
    private ImageButton infoButton;

    TextView cabNumberView;
    TextView fullNameView;

    ProgressDialog mProgressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dc = DataController.getInstance(this);

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);

        Typeface font = Typeface.createFromAsset(LoginActivity.this.getAssets(), "fonts/roboto_bold.ttf");
        Typeface fontBold = Typeface.createFromAsset(LoginActivity.this.getAssets(),"fonts/roboto_bold.ttf");

        loginField = (EditText)findViewById(R.id.editTextLogin);
        loginField.setTypeface(fontBold);


        infoButton = (ImageButton) findViewById(R.id.info);
        infoButton.setVisibility(View.GONE);

        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setVisibility(View.GONE);


        cabNumberView = (TextView)findViewById(R.id.cab_number);
        fullNameView = (TextView) findViewById(R.id.driver_full_name);

        cabNumberView.setText("");
        fullNameView.setText("Вход в систему");


        enter = (Button) findViewById(R.id.buttonLogin);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog = ProgressDialog.show(LoginActivity.this, "Подождите", "Выполняется вход...", true);
                saveData();

                dc.login(SaveSharedPrefrances.getPassword(LoginActivity.this),//loginField.getText().toString(),
                        new CallBack() {
                            @Override
                            public void process(String o) {
                                mProgressDialog.dismiss();
                                beginTracking();
                            }
                        },
                        new CallBack() {
                            @Override
                            public void process(String o) {

                                Toast toastError = Toast.makeText(getApplicationContext(), "Ошибка:" + dc.loginStatus, Toast.LENGTH_SHORT);
                                toastError.show();
                            }
                        });
            }
        });

    }

    public void saveData()
    {
        SaveSharedPrefrances.setPassword(this, loginField.getText().toString());
    }

    public void beginTracking(){

        if(dc.loginStatus != null)
        {
            Log.e("Tracking", dc.loginStatus);

            String flag = "success";
            if (dc.loginStatus.equals(flag))
            {
                Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                LoginActivity.this.startActivityForResult(intent, 1);
                finish();
            } else
            {
                SaveSharedPrefrances.setPassword(this, "");

                Toast toast = Toast.makeText(getApplicationContext(), "Неправильный пароль", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }



}
