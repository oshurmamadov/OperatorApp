package operator.com.operatorapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import operator.com.operatorapp.adapters.ListAdapter;
import operator.com.operatorapp.adapters.Record;
import operator.com.operatorapp.utils.CallBack;
import operator.com.operatorapp.utils.DataController;
import operator.com.operatorapp.utils.DataPickerFragment;
import operator.com.operatorapp.utils.OnSwipeTouchListener;

public class MapsActivity extends ActionBarActivity {

    private GoogleMap gMap; // Might be null if Google Play services APK is not available.
    DataController dc;

    private ImageView backButton;
    private ImageButton infoButton;

    public String choosedCab = null ;

    TextView cabNumberView;
    TextView fullNameView;

    ImageView calendarView;

    Switch switcher;

    CheckBox checkbox;

    ProgressDialog dialog;

    ViewFlipper flipper;

    TextView data ;
    int DIALOG_DATE = 1;
    int _year ;
    int _month ;
    int _day ;

    String choosed_date = " ";

    boolean online = false , justForOnlineState = false , ItemsCountIsNull = false;

    public boolean inLeft = true;

    Handler handler = new Handler();
    TimerTask doAsynchronousTask ;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        dc = DataController.getInstance(this);

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);

        infoButton = (ImageButton) findViewById(R.id.info);

        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setVisibility(View.GONE);

        cabNumberView = (TextView)findViewById(R.id.cab_number);
        fullNameView = (TextView) findViewById(R.id.driver_full_name);

        cabNumberView.setText("");
        fullNameView.setText("Выберите дату и борт");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backButton.setVisibility(View.GONE);
                toRight();
                inLeft = true;

                cabNumberView.setText("");
                fullNameView.setText("Выберите дату и борт");

                online = false;

                handler.removeCallbacks(mapUpdateThread);
                doAsynchronousTask.cancel();
                Log.e("TIMER", "timer canceled");

                switcher.setChecked(false);
            }
        });



        try {
            setUpMap();
        }
        catch(Exception e){
            e.printStackTrace();
        }

       getCabsList();


        flipper = (ViewFlipper) findViewById(R.id.view_flipper);
        flipper.setOnTouchListener(new OnSwipeTouchListener(MapsActivity.this) {
            public void onSwipeLeft() {
                   if(inLeft)
                   {
                     //  toLeft();
                       backButton.setVisibility(View.VISIBLE);
                       inLeft = false;
                   }

            }
        });

        data = (TextView) findViewById(R.id.date_view);
        calendarView = (ImageView)findViewById(R.id.calendar);
        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_DATE);
            }
        });


       // checkbox = (CheckBox)findViewById(R.id.online_checkbox);
       /* checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });*/

        switcher = (Switch)findViewById(R.id.online_switch);
        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    String strMonth;
                    String strDay;

                    if(month < 10)
                    {
                        month += 1;
                        strMonth = "-0" + month;
                    }
                    else
                    {
                        month += 1;
                        strMonth = "-" + month;
                    }

                    if(day < 10)
                    {
                        strDay = "-0" + day;
                    }
                    else
                    {
                        strDay = "-" + day;
                    }

                    choosed_date = year + strMonth + strDay;

                    online = true;
                    calendarView.setEnabled(false);
                    data.setText("Дата");

                    Log.e("TracksCloud", "online mode ON");
                }
                else{
                    calendarView.setEnabled(true);
                    online = false ;
                    choosed_date = " ";
                    data.setText("Дата" + choosed_date);

                    Log.e("TracksCloud", "online mode OFF");
                }
            }
        });


        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View infoView = getLayoutInflater().inflate(R.layout.info_layout, null, false);

                TextView driverName = (TextView)infoView.findViewById(R.id.driver_name);
                TextView carModel = (TextView)infoView.findViewById(R.id.car_name);
                TextView carNumber = (TextView)infoView.findViewById(R.id.car_number);
                TextView carBoard = (TextView)infoView.findViewById(R.id.car_board);

                driverName.setText(dc.fullNameList.get(dc.selectedCar));
                carModel.setText(dc.carModelList.get(dc.selectedCar));
                carNumber.setText(dc.carNumberList.get(dc.selectedCar));
                carBoard.setText(dc.cabsList.get(dc.selectedCar));

                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setIcon(R.drawable.info_32);
                builder.setTitle("Информация");
                builder.setView(infoView);
                builder.create();
                builder.show();
            }
        });

    }

    protected Dialog onCreateDialog(int id) {

        Calendar calendar = Calendar.getInstance();
        _year = calendar.get(Calendar.YEAR);
        _month = calendar.get(Calendar.MONTH);
        _day = calendar.get(Calendar.DAY_OF_MONTH);

        if (id == DIALOG_DATE) {
            DatePickerDialog tpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    _year = year;
                    _month = monthOfYear;
                    _day = dayOfMonth;

                    String strMonth;
                    String strDay;

                    if(_month < 10)
                    {
                        _month += 1;
                        strMonth = "-0" + _month;
                    }
                    else
                    {
                        _month += 1;
                        strMonth = "-" + _month;
                    }

                    if(_day < 10)
                    {
                        strDay = "-0" + _day;
                    }
                    else
                    {
                        strDay = "-" + _day;
                    }

                    choosed_date = _year + strMonth + strDay;
                    data.setText( choosed_date );

                }
            }, _year, _month  , _day);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Tracking","onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Tracking","onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Tracking","onResume");
        setUpMap();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.e("Tracking","onDestroy");
        dc.cabsList.clear();
        dc.datelist.clear();
        dc.itemsList.clear();
        dc.fullNameList.clear();
        dc.carModelList.clear();
        dc.carNumberList.clear();
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Tracking", "onStop");
        handler.removeCallbacks(mapUpdateThread);
        if(doAsynchronousTask != null) doAsynchronousTask.cancel();

    }

    private void backButtonPressed(){
        super.onBackPressed();
    }

    public void toLeft(){
        flipper.setInAnimation(AnimationUtils.loadAnimation(MapsActivity.this, R.anim.slide_in_from_right));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(MapsActivity.this, R.anim.slide_out_to_left));
        flipper.showNext();

        infoButton.setVisibility(View.VISIBLE);

        Log.e("TracksCloud", "Flip to oLeft");
    }

    public void toRight(){
        flipper.setInAnimation(AnimationUtils.loadAnimation(MapsActivity.this, R.anim.slide_in_from_left));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(MapsActivity.this, R.anim.slide_out_to_right));
        flipper.showPrevious();

        infoButton.setVisibility(View.INVISIBLE);

        Log.e("TracksCloud", "Flip to Right");
    }


    private void setUpMap(){
        if(gMap == null) {

            gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment)).getMap();
            //gMap.setMyLocationEnabled(true);
            gMap.getUiSettings().setZoomControlsEnabled(true);
            gMap.getUiSettings().setMyLocationButtonEnabled(false);
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(42.867157, 74.613262), 12.0f) );

            Log.e("Tracking","The map is load");

            gMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {

                   // gMap.clear();
                   // gMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("You are here!").snippet("Consider yourself located"));
                   // gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12.0f) );

                }
            });

        }
    }

    public void getCabsList(){

        dialog = ProgressDialog.show(this,"Подождите","Загружаются данные...",true);
        dc.getCabs( new CallBack() {
                        @Override
                        public void process(String o) {
                            dialog.dismiss();
                            fillList();
                            Log.e("Tracking","loading cabs ,car model,car number,full name");
                            //fillSpinner();
                        }
                    },
                new CallBack() {
                    @Override
                    public void process(String o) {

                        AlertDialog.Builder mErrorDialog = new AlertDialog.Builder(MapsActivity.this);
                        mErrorDialog.setTitle("Ошибка")
                                .setMessage("Не удалось получить данные")
                                .setNeutralButton("Повтор", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = getIntent();
                                        startActivity(intent);
                                    }
                                })
                                .show();

                    }
                });


       // dc.monitoring();
    }

    public void fillList(){

        final ListView listView = (ListView) findViewById(R.id.cab_list);
       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(MapsActivity.this,android.R.layout.simple_list_item_1 , dc.cabsList);
        ListAdapter adapter = new ListAdapter(MapsActivity.this,dc.fullNameList,dc.cabsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dialog = ProgressDialog.show(MapsActivity.this,"Подождите","Загружаются координаты",true);

                dc.itemsList.clear();
                choosedCab = parent.getItemAtPosition(position).toString();
                dc.selectedCar = position;
                if(choosed_date.length() > 1)
                {
                    Log.e("Tracking",choosedCab+" is choosed");
                    getCoordsFromServer();
                    if( !ItemsCountIsNull ) updateMapInfo();

                }
                else{

                    Toast dateToast = Toast.makeText(getApplicationContext(), "Выберите дату", Toast.LENGTH_SHORT);
                    dateToast.show();
                    dialog.dismiss();
                }
            }

        });

    }


    public void getCoordsFromServer(){

            dc.monitoring(choosedCab, choosed_date,
                    new CallBack() {
                        @Override
                        public void process(String o) {

                            //Toast toast11 = Toast.makeText(getApplicationContext(), " "+ dc.itemsList.get(0).latitude , Toast.LENGTH_SHORT);
                            // toast11.show();
                            dialog.dismiss();

                            if (dc.itemsList.size() == 0) {
                                Toast toast11 = Toast.makeText(getApplicationContext(), " Данных нет ", Toast.LENGTH_SHORT);
                                toast11.show();
                                ItemsCountIsNull = true;

                                Log.e("Tracking","There is no data");
                            } else {
                                ItemsCountIsNull = false;
                                toLeft();

                                Log.e("Tracking","Drawing trace");
                                addCoords();

                                addDriverInfo(choosedCab);
                                backButton.setVisibility(View.VISIBLE);

                                Log.e("Tracking","Loading coords from heroku");
                            }
                        }

                    },
                    new CallBack() {
                        @Override
                        public void process(String o) {
                            Toast toast3 = Toast.makeText(getApplicationContext(), "Не удалось получить координаты ", Toast.LENGTH_SHORT);
                            toast3.show();
                        }
                    });

    }

    public void getUpdatedCoords(){
        dc.monitoring(choosedCab, choosed_date,
                new CallBack() {
                    @Override
                    public void process(String o) {
                            addCoords();
                        }
                },
                new CallBack() {
                    @Override
                    public void process(String o) {
                        Toast toastU = Toast.makeText(getApplicationContext(), "Не удалось получить обновленные координаты ", Toast.LENGTH_SHORT);
                        toastU.show();
                    }
                });
    }

    public void addCoords() {

        ArrayList<LatLng> points = new ArrayList<LatLng>();
        PolylineOptions polyLineOptions = new PolylineOptions();

        gMap.clear();

        for(int i=0; i<dc.itemsList.size() ;i++){
            //gMap.addMarker(new MarkerOptions().position(new LatLng(dc.itemsList.get(i).latitude, dc.itemsList.get(i).longitude)));
              LatLng position = new LatLng(dc.itemsList.get(i).latitude, dc.itemsList.get(i).longitude);
              points.add(position);
        }

        polyLineOptions.addAll(points);
        polyLineOptions.width(4);
        polyLineOptions.color(Color.BLUE);

        gMap.addPolyline(polyLineOptions);

        if(points.size() > 0)gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(points.get(points.size()-1) , 13.5f));

       // Log.e("Tracking","Drawing trace");
    }

    public void addDriverInfo(String cabNumber){

        for(int i=0 ; i<dc.fullNameList.size(); i++ )
        {
            if(cabNumber == dc.cabsList.get(i))
            {
                //cabNumberView.setText(dc.cabsList.get(i));
                fullNameView.setText(dc.fullNameList.get(i));
            }
        }

        Log.e("Tracking","Add driver info");
    }

    public  void updateMapInfo()
    {
        Log.e("Tracking","Thread is started");

        doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(mapUpdateThread);
            }
        };
        //timer.schedule(doAsynchronousTask, 25000, 60000); //25 60
        timer.schedule(doAsynchronousTask, 5000, 10000);
    }

    public  Runnable mapUpdateThread = new Runnable() {
        @Override
        public void run() {
            try {

                if (online)
                {
                    dc.itemsList.clear();
                    getUpdatedCoords();

                    Toast toastUpdate = Toast.makeText(getApplicationContext(), " Данные обновлены", Toast.LENGTH_SHORT);
                    toastUpdate.show();

                    Log.e("Tracking","Coord was updated");
                }
                else {
                    Log.e("Tracking","Its not online");
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}
