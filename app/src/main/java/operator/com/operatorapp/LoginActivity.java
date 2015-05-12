package operator.com.operatorapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import operator.com.operatorapp.utils.DataController;


public class LoginActivity extends ActionBarActivity {


    DataController dc;

    Button enter;
    EditText loginField;

    private ImageView backButton;
    private ImageButton infoButton;

    TextView cabNumberView;
    TextView fullNameView;

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
                Intent intent = new Intent(LoginActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });

    }



}
