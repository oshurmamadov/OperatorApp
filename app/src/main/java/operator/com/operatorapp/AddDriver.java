package operator.com.operatorapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    EditText addBoard;
    EditText addPhone;
    EditText addFIO;
    EditText addPass;
    EditText addCarNumber;
    EditText addCarModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_driver);

        dc = DataController.getInstance(this);

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);


        addBoard = (EditText)findViewById(R.id.editBoard);
        addPhone = (EditText)findViewById(R.id.editPhone);
        addFIO = (EditText)findViewById(R.id.editFIO);
        addPass = (EditText)findViewById(R.id.editPass);
        addCarNumber = (EditText)findViewById(R.id.editCarNumber);
        addCarModel = (EditText)findViewById(R.id.editCarModel);

        Typeface font = Typeface.createFromAsset(AddDriver.this.getAssets(), "fonts/roboto_bold.ttf");
        Typeface fontBold = Typeface.createFromAsset(AddDriver.this.getAssets(),"fonts/roboto_bold.ttf");

        addBoard.setTypeface(font);
        addPhone.setTypeface(font);
        addFIO.setTypeface(font);
        addPass.setTypeface(font);
        addCarNumber.setTypeface(font);
        addCarModel.setTypeface(font);

      //  addBoard.setOnTouchListener(editTouched);
        addBoard.setOnFocusChangeListener(editTextFocused1);
        addPhone.setOnFocusChangeListener(editTextFocused2);
        addFIO.setOnFocusChangeListener(editTextFocused3);
        addPass.setOnFocusChangeListener(editTextFocused4);
        addCarNumber.setOnFocusChangeListener(editTextFocused5);
        addCarModel.setOnFocusChangeListener(editTextFocused6);

      /*  addBoard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) addBoard.setTextColor(Color.WHITE);
                else  addBoard.setTextColor(getResources().getColor(R.color.customWhiteAlpha));
            }
        });*/

        TextView addBoardText = (TextView)findViewById(R.id.addBoard);
        TextView addPhoneText = (TextView)findViewById(R.id.addPhone);
        TextView addFIOText = (TextView)findViewById(R.id.addFIO);
        TextView addPassText = (TextView)findViewById(R.id.addPass);
        TextView addCarText = (TextView)findViewById(R.id.addCarNumber);
        TextView addModelText = (TextView)findViewById(R.id.addCarModel);

        addBoardText.setTypeface(fontBold);
        addPhoneText.setTypeface(fontBold);
        addFIOText.setTypeface(fontBold);
        addPassText.setTypeface(fontBold);
        addCarText.setTypeface(fontBold);
        addModelText.setTypeface(fontBold);

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


     if(checkEmptyFields(addBoard.getText().toString(), addPhone.getText().toString(),addFIO.getText().toString(), addPass.getText().toString(),addCarNumber.getText().toString(), addCarModel.getText().toString())) {
         if (!checkBoard(addBoard.getText().toString()) & !checkPhone(addPhone.getText().toString()) & !checkCarNumber(addCarNumber.getText().toString())) {
             dc.addNewDriver(addBoard.getText().toString(), addPhone.getText().toString(),
                     addFIO.getText().toString(), addPass.getText().toString(),
                     addCarNumber.getText().toString(), addCarModel.getText().toString(),
                     new CallBack() {
                         @Override
                         public void process(String o) {

                             Toast toastSave = Toast.makeText(getApplicationContext(), "Новый водитель зарегистрирован ", Toast.LENGTH_SHORT);
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
         } else {
             Toast toastError = Toast.makeText(getApplicationContext(), "Водитель с таким бортом или телефоном или гос.номерами уже зарегистрирован ", Toast.LENGTH_LONG);
             toastError.show();
         }
     } else {
         Toast toastError1 = Toast.makeText(getApplicationContext(), "Заполните все поля ", Toast.LENGTH_LONG);
         toastError1.show();
     }
    }

    public boolean checkEmptyFields(String board ,String phone,String name,String pass,String carNumber,String carModel){
        boolean flag = true;

        if(board.length() == 0) flag = false;
        if(phone.length() == 0) flag = false;
        if(name.length() == 0) flag = false;
        if(pass.length() == 0) flag = false;
        if(carModel.length() == 0) flag = false;
        if(carNumber.length() == 0) flag = false;

        return  flag;
    }

    public boolean checkBoard(String board){

        boolean flag = false;
        for(int i = 0; i < dc.cabsList.size(); i++){
            if(board.equals(dc.cabsList.get(i)) ) flag = true;
        }

        return  flag;
    }

    public boolean checkPhone(String phone ){
        boolean flag = false;
        for(int i = 0; i < dc.cabsList.size(); i++){
            if(phone.equals(dc.phoneNumberList.get(i)) ) flag = true;
        }

        return  flag;

    }

    public boolean checkCarNumber(String carNumber){

        boolean flag = false;
        for(int i = 0; i < dc.cabsList.size(); i++){
            if(carNumber.equals(dc.carNumberList.get(i)) ) flag = true;
        }

        return  flag;

    }

    View.OnTouchListener editTouched = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            addBoard.setTextColor(Color.WHITE);

            return false;
        }
    };


        View.OnFocusChangeListener editTextFocused1 = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) addBoard.setTextColor(Color.WHITE);
                else  addBoard.setTextColor(getResources().getColor(R.color.customWhiteAlpha));
            }
        };

    View.OnFocusChangeListener editTextFocused2 = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus) addPhone.setTextColor(Color.WHITE);
            else  addPhone.setTextColor(getResources().getColor(R.color.customWhiteAlpha));
        }
    };

    View.OnFocusChangeListener editTextFocused3 = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus) addFIO.setTextColor(Color.WHITE);
            else  addFIO.setTextColor(getResources().getColor(R.color.customWhiteAlpha));
        }
    };

    View.OnFocusChangeListener editTextFocused4 = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus) addPass.setTextColor(Color.WHITE);
            else  addPass.setTextColor(getResources().getColor(R.color.customWhiteAlpha));
        }
    };

    View.OnFocusChangeListener editTextFocused5 = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus) addCarNumber.setTextColor(Color.WHITE);
            else  addCarNumber.setTextColor(getResources().getColor(R.color.customWhiteAlpha));
        }
    };

    View.OnFocusChangeListener editTextFocused6 = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(hasFocus) addCarModel.setTextColor(Color.WHITE);
            else  addCarModel.setTextColor(getResources().getColor(R.color.customWhiteAlpha));
        }
    };


}
