package operator.com.operatorapp.adapters;

import android.content.Context;
import android.renderscript.Type;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.ArrayList;
import java.util.List;

import operator.com.operatorapp.R;
import operator.com.operatorapp.utils.DataController;

/**
 * Created by parviz on 4/13/15.
 */
public class RecordAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final ArrayList<String> items;

    DataController dc;
    AQuery aq;

    public RecordAdapter(Context context, ArrayList<String> items) {
        super(context,0, items);

        this.context = context;
        this.items = items;

        dc=DataController.getInstance(context);
        aq=new AQuery(getContext());
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
           // convertView = inflater.inflate(R.layout.my_row_layout, parent, false);
        }
        return convertView;
    }

}
