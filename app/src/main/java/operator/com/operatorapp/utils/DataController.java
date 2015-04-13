package operator.com.operatorapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import operator.com.operatorapp.adapters.CabNumberItems;

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

    public ArrayList<CabNumberItems> requestItems;


    private  DataController(Context context){

        this.context = context;
        aq = new AQuery(context);
    }

    public static DataController getInstance(Context context){

        if(instance == null) instance = new DataController( context);
        if(instance.context == null) instance.context = context;

        return instance;
    }


    public void monitoring(String cab_number , final CallBack success, final CallBack failure){

        String url = String.format("http://serverdp.herokuapp.com/get_coordinates?cab_number=",cab_number);
        requestServer(url,
                new CallBack() {
                    @Override
                    public void process(String o) {
                        if(!o.equals("")) {
                            try {

                                JSONArray object = new JSONArray(o);
                                for(int i=0; i < object.length(); i++) {

                                    requestJSon = new JSONObject( object.get(i).toString());

                                    //requestStatus = cubNumberJSon.getString("status");

                                    //Log.e("Jush", requestStatus);
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
