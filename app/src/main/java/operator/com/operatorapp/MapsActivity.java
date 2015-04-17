package operator.com.operatorapp;

import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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

import operator.com.operatorapp.adapters.Record;
import operator.com.operatorapp.utils.CallBack;
import operator.com.operatorapp.utils.DataController;
import operator.com.operatorapp.utils.OnSwipeTouchListener;

public class MapsActivity extends ActionBarActivity {

    private GoogleMap gMap; // Might be null if Google Play services APK is not available.
    DataController dc;

    private ImageView backButton;

    public String choosedCab = null ;
    public int selectionCount = 0;

    TextView cabNumberView;
    TextView fullNameView;

    ViewFlipper flipper;

    public boolean inLeft = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        dc = DataController.getInstance(this);

        getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);

        backButton = (ImageView) findViewById(R.id.back_button);
        backButton.setVisibility(View.GONE);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backButton.setVisibility(View.GONE);
                toRight();
                inLeft = true;
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
                       toLeft();
                       backButton.setVisibility(View.VISIBLE);
                       inLeft = false;
                   }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMap();
    }


    private void backButtonPressed(){
        super.onBackPressed();
    }

    public void toLeft(){
        flipper.setInAnimation(AnimationUtils.loadAnimation(MapsActivity.this, R.anim.slide_in_from_right));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(MapsActivity.this, R.anim.slide_out_to_left));
        flipper.showNext();
        Log.e("TracksCloud", "Flip to oLeft");
    }

    public void toRight(){
        flipper.setInAnimation(AnimationUtils.loadAnimation(MapsActivity.this, R.anim.slide_in_from_left));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(MapsActivity.this, R.anim.slide_out_to_right));
        flipper.showPrevious();
        Log.e("TracksCloud", "Flip to Right");
    }


    private void setUpMap(){
        if(gMap == null) {

            gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment)).getMap();
            //gMap.setMyLocationEnabled(true);
            gMap.getUiSettings().setZoomControlsEnabled(true);
            gMap.getUiSettings().setMyLocationButtonEnabled(false);
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(42.867157, 74.613262), 12.0f) );


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
        dc.getCabs( new CallBack() {
                        @Override
                        public void process(String o) {

                            fillList();
                            //fillSpinner();
                        }
                    },
                new CallBack() {
                    @Override
                    public void process(String o) {
                        Toast toast3 = Toast.makeText(getApplicationContext(), "Error while loading cabs " , Toast.LENGTH_SHORT);
                        toast3.show();
                    }
                });


       // dc.monitoring();
    }

    public void fillList(){

        final ListView listView = (ListView) findViewById(R.id.cab_list);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MapsActivity.this,android.R.layout.simple_list_item_1 , dc.cabsList);
        listView.setAdapter(adapter);

      /*  listView.setActivated(true);
        listView.setFocusable(true);
        listView.setFocusableInTouchMode(true);*/




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dc.itemsList.clear();
                choosedCab = parent.getItemAtPosition(position).toString();

//                Toast toast11 = Toast.makeText(getApplicationContext(), " "+ dc.itemsList.get(0).latitude , Toast.LENGTH_SHORT);
                 //toast11.show();

                dc.monitoring(choosedCab,
                        new CallBack() {
                            @Override
                            public void process(String o) {

                                 //Toast toast11 = Toast.makeText(getApplicationContext(), " "+ dc.itemsList.get(0).latitude , Toast.LENGTH_SHORT);
                                // toast11.show();
                                addCoords();
                                addDriverInfo(choosedCab);
                                toLeft();
                            }
                        },
                        new CallBack() {
                            @Override
                            public void process(String o) {
                                Toast toast3 = Toast.makeText(getApplicationContext(), "Error while loading coords ", Toast.LENGTH_SHORT);
                                toast3.show();
                            }
                        });
            }


        });

    }


   /* public  void fillLis1t(){

        listView = (ListView) findViewById(R.id.lisView);

        Log.e("Tracking", "success");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MapsActivity.this,android.R.layout.simple_spinner_item , dc.cabsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelected(false);


       // dc = DataController.getInstance(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                dc.itemsList.clear();
                choosedCab = parent.getItemAtPosition(position).toString();

                if(position != 0)
                {
                    dc.monitoring(choosedCab,
                            new CallBack() {
                                @Override
                                public void process(String o) {

                                    // Toast toast11 = Toast.makeText(getApplicationContext(), " "+ dc.itemsList.get(0).latitude , Toast.LENGTH_SHORT);
                                    // toast11.show();
                                    addCoords();
                                    addDriverInfo(choosedCab);
                                }
                            },
                            new CallBack() {
                                @Override
                                public void process(String o) {
                                    Toast toast3 = Toast.makeText(getApplicationContext(), "Error while loading coords ", Toast.LENGTH_SHORT);
                                    toast3.show();
                                }
                            });
                }
                selectionCount ++;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }) ;




    }*/

    public void addCoords() {

        ArrayList<LatLng> points = new ArrayList<LatLng>();
        PolylineOptions polyLineOptions = new PolylineOptions();

        gMap.clear();

        if(dc.itemsList.size() == 0) {
            Toast toast11 = Toast.makeText(getApplicationContext(), " Nothing to show ", Toast.LENGTH_SHORT);
            toast11.show();
        }

        for(int i=0; i<dc.itemsList.size() ;i++){
            //gMap.addMarker(new MarkerOptions().position(new LatLng(dc.itemsList.get(i).latitude, dc.itemsList.get(i).longitude)));
              LatLng position = new LatLng(dc.itemsList.get(i).latitude, dc.itemsList.get(i).longitude);
              points.add(position);
        }

        polyLineOptions.addAll(points);
        polyLineOptions.width(4);
        polyLineOptions.color(Color.BLUE);

        gMap.addPolyline(polyLineOptions);

        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(points.get(points.size()-1) , 13.5f));

    }

    public void addDriverInfo(String cabNumber){
        cabNumberView = (TextView)findViewById(R.id.cab_number);
        fullNameView = (TextView) findViewById(R.id.driver_full_name);

        for(int i=0 ; i<dc.driverList.size(); i++ )
        {
            if(cabNumber == dc.cabsList.get(i))
            {
                cabNumberView.setText(dc.cabsList.get(i));
                fullNameView.setText(dc.driverList.get(i));
            }
        }

    }

}
