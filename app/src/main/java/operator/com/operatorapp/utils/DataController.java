package operator.com.operatorapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.Constants;

/**
 * Created by parviz on 4/12/15.
 */
public class DataController {

    public Context context;
    private static DataController instance;
    public SharedPreferences preferences;
    private AQuery aq;

    private  DataController(Context context){

        this.context = context;
        aq = new AQuery(context);
    }

    public static DataController getInstance(Context context){

        if(instance == null) instance = new DataController( context);
        if(instance.context == null) instance.context = context;

        return instance;
    }
}
