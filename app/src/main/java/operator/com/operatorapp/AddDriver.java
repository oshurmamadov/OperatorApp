package operator.com.operatorapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import operator.com.operatorapp.utils.CallBack;
import operator.com.operatorapp.utils.DataController;


public class AddDriver extends ActionBarActivity {

    DataController dc;

    private ImageView backButton;
    private ImageButton infoButton;

    Button save;

    TextView cabNumberView;
    TextView fullNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);

        dc = DataController.getInstance(this);

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);

        infoButton = (ImageButton) findViewById(R.id.info);
        infoButton.setVisibility(View.GONE);

        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonPressed();
            }
        });

        cabNumberView = (TextView)findViewById(R.id.cab_number);
        fullNameView = (TextView) findViewById(R.id.driver_full_name);

        cabNumberView.setText("");
        fullNameView.setText("Регистрация нового водителя");

        save =(Button)findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewDriver();
            }
        });
    }

    private void backButtonPressed(){
        super.onBackPressed();
    }

    public void addNewDriver(){
        dc.addNewDriver("9090","0700908990","Maximus Augusto","test350","ST3456P","Lada Priora",
                new CallBack() {
                    @Override
                    public void process(String o) {

                        Toast toastSave = Toast.makeText(getApplicationContext(), "Новый водитель добавлен " , Toast.LENGTH_SHORT);
                        toastSave.show();
                    }
                },
                new CallBack() {
                    @Override
                    public void process(String o) {
                        Toast toast3 = Toast.makeText(getApplicationContext(), "Ошибка ", Toast.LENGTH_SHORT);
                        toast3.show();
                    }
                });
    }

}
