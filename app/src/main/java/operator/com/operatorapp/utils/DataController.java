package operator.com.operatorapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import operator.com.operatorapp.adapters.CabNumberItems;
import operator.com.operatorapp.adapters.Record;

/**
 * Created by parviz on 4/12/15.
 */
public class DataController {

    public Context context;
    private static DataController instance;
    public SharedPreferences preferences;
    private AQuery aq;

    private JSONObject requestJSon;
    private String requestStatus ;
    private JSONObject cabsJSon;
    public JSONObject saveJSon;

    public String saveStatus;

    public ArrayList<String> cabsList;
    public ArrayList<String> datelist;
    public ArrayList<Record> itemsList;

    public ArrayList<String> fullNameList;
    public ArrayList<String> carModelList;
    public ArrayList<String> carNumberList;

    public int selectedCar = 0;

    public Boolean onlineMode =false ;

    private  DataController(Context context){

        this.context = context;
        aq = new AQuery(context);
        cabsList = new ArrayList<String>();
        itemsList = new ArrayList<Record>();
        datelist = new ArrayList<String>();

        fullNameList = new ArrayList<String>();
        carModelList = new ArrayList<String>();
        carNumberList =new ArrayList<String>();
    }

    public static DataController getInstance(Context context){

        if(instance == null) instance = new DataController( context);
        if(instance.context == null) instance.context = context;

        return instance;
    }


    public void monitoring(String cab_number , final String choosedDate, final CallBack success, final CallBack failure){


        String url = String.format("http://serverdp.herokuapp.com/get_coordinates?cab_number=%s",cab_number);
        requestServer(url,
                new CallBack() {
                    @Override
                    public void process(String o) {
                        if(!o.equals("")) {
                            try {

                                JSONArray object = new JSONArray(o);
                                for(int i=0; i < object.length(); i++) {

                                    requestJSon = new JSONObject( object.get(i).toString());



                                    String date = requestJSon.getString("created_at").split("T")[0];

                                    if( choosedDate.equals(date))
                                    {
                                        //datelist.add(date);
                                        itemsList.add( new Record(
                                                requestJSon.getString("cab_number"),
                                                requestJSon.getDouble("latitude"),
                                                requestJSon.getDouble("longitude")
                                        ));
                                    }
                                }

                            } catch (JSONException e) {
                                Log.e("Jush", "Catched JSONException. result was: " + o);
                            }
                        }
                        success.process(o);
                    }
                },
                new CallBack() {
                    @Override
                    public void process(String o) {
                        failure.process(null);
                    }
                }
        );
    }


    public void getCabs(final CallBack success, final CallBack failure){

        String url = "http://serverdp.herokuapp.com/all_cabs";
        requestServer(url,
                new CallBack() {
                    @Override
                    public void process(String o) {
                        if(!o.equals("")) {
                            try {

                                JSONArray object = new JSONArray(o);
                               // cabsList.add(" "); // for first initialisation
                                
                                for(int i=0; i < object.length(); i++)
                                {
                                    cabsJSon = new JSONObject(object.get(i).toString());
                                    cabsList.add(cabsJSon.getString("cab_number"));

                                    fullNameList.add(cabsJSon.getString("full_name"));
                                    carModelList.add(cabsJSon.getString("car_model"));
                                    carNumberList.add(cabsJSon.getString("car_number"));
                                }

                            } catch (JSONException e) {
                                Log.e("Tracking", "Catched JSONException. result was: " + o);
                            }
                        }
                        success.process(o);
                    }
                },
                new CallBack() {
                    @Override
                    public void process(String o) {
                        failure.process(null);
                    }
                }
        );
    }


    public void addNewDriver(String board ,String phone,String name,String pass,String carNumber,String carModel , final CallBack success, final CallBack failure){

        String url = String.format("http://serverdp.herokuapp.com/add_new_driver?cab_number=%s&phone_number=%s&full_name=%s&password=%s&car_number=%s&car_model=%s", board ,phone,name,pass,carNumber,carModel);
        requestServer(url,
                new CallBack() {
                    @Override
                    public void process(String o) {
                        if(!o.equals("")) {
                            //Log.e("Jush", "Callback 4");
                            try {

                                saveJSon = new JSONObject(o.toString());

                                saveStatus  = saveJSon.getString("status");

                                Log.e("Tracking","saved request status :"+saveStatus);

                            } catch (JSONException e) {
                                Log.e("Tracking", "Catched JSONException. result was: " + o);
                            }
                        }
                        success.process(o);
                    }
                },
                new CallBack() {
                    @Override
                    public void process(String o) {
                        failure.process(null);
                    }
                }
        );
    }

    private void requestServer(final String url, final CallBack success, final CallBack failure) {
        AjaxCallback<String> callback = new AjaxCallback<String>() {
            @Override
            public void callback(String url, String object, AjaxStatus status) {
                int responseCode = status.getCode();
                if (responseCode == 200) success.process(object);
                else failure.process(null);
                super.callback(url, object, status);
            }
        };
        callback.url(url);
        callback.type(String.class);
        callback.timeout(30000);
        callback.encoding("UTF-8");
        callback.method(Constants.METHOD_GET);
        aq.ajax(callback);
    }

}
